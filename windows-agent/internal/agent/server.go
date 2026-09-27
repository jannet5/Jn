package agent

import (
	"context"
	"crypto/tls"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"log"
	"net"
	"net/http"
	"os"
	"strings"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/websocket"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// AuthLockoutBase/Max/Threshold configure the per-device/per-IP
// exponential backoff required by PROTOCOL.md §2 ("Auth failures are
// rate-limited per device_id and per source IP (exponential lockout,
// capped)"). PROTOCOL.md does not pin exact numbers, so these are this
// implementation's chosen (documented) defaults.
const (
	AuthLockoutBase      = 2 * time.Second
	AuthLockoutMax        = 5 * time.Minute
	AuthLockoutThreshold  = 3
	authChallengeTTL      = 30 * time.Second
)

// Server is the WS/TLS agent server implementing PROTOCOL.md §3-§4.
type Server struct {
	Config      core.Config
	Store       *Store
	AllowList   *core.AllowList
	ServerKey   []byte // protects device secrets at rest, see core.EncryptDeviceSecret
	ProcessMgr  *ProcessManager
	Launcher    *Launcher
	Metrics     *MetricsSampler
	AgentVersion string
	Hostname     string

	PairingMgr  *core.PairingManager
	AuthLockout *core.BackoffLockout

	upgrader websocket.Upgrader

	mu       sync.Mutex
	sessions map[*Session]struct{}
}

// NewServer wires a Server from its dependencies. Callers (cmd/agent) are
// responsible for constructing Store/AllowList/ServerKey/etc from real
// config and real OS state; Server itself never fabricates data.
func NewServer(cfg core.Config, store *Store, allowList *core.AllowList, serverKey []byte) *Server {
	hostname, _ := os.Hostname()
	return &Server{
		Config:       cfg,
		Store:        store,
		AllowList:    allowList,
		ServerKey:    serverKey,
		ProcessMgr:   NewProcessManager(),
		Launcher:     &Launcher{AllowList: allowList},
		Metrics:      &MetricsSampler{Volumes: defaultVolumes()},
		AgentVersion: "1.0.0",
		Hostname:     hostname,
		PairingMgr:   core.NewPairingManager(),
		AuthLockout:  core.NewBackoffLockout(AuthLockoutBase, AuthLockoutMax, AuthLockoutThreshold),
		upgrader:     websocket.Upgrader{ReadBufferSize: 4096, WriteBufferSize: 4096},
		sessions:     make(map[*Session]struct{}),
	}
}

// Session is one WS connection's mutable state machine.
type Session struct {
	server *Server
	ws     *websocket.Conn
	connID string // per-connection key for pairing lockout
	remoteIP string

	writeMu sync.Mutex

	authenticated bool
	deviceID      string
	deviceName    string

	topicsMu sync.Mutex
	topics   map[string]bool

	pendingChallenge *pendingChallenge
}

type pendingChallenge struct {
	deviceID  string
	nonce     []byte
	expiresAt time.Time
}

// unauthAllowed is the exact allow-list from PROTOCOL.md §2: "the server
// only accepts hello, pair_request, auth_request, auth_response before
// auth_success."
var unauthAllowed = map[string]bool{
	core.TypeHello:        true,
	core.TypePairRequest:  true,
	core.TypeAuthRequest:  true,
	core.TypeAuthResponse: true,
}

// ServeHTTP upgrades to a WS connection and runs its lifecycle. Mount at
// "/ws" per PROTOCOL.md §1.
func (s *Server) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	ws, err := s.upgrader.Upgrade(w, r, nil)
	if err != nil {
		return
	}
	remoteIP := clientIP(r)
	sess := &Session{
		server:   s,
		ws:       ws,
		connID:   uuid.NewString(),
		remoteIP: remoteIP,
		topics:   make(map[string]bool),
	}
	s.mu.Lock()
	s.sessions[sess] = struct{}{}
	s.mu.Unlock()
	defer func() {
		s.mu.Lock()
		delete(s.sessions, sess)
		s.mu.Unlock()
		ws.Close()
	}()

	s.runPingLoop(sess)
	s.readLoop(sess)
}

func (s *Server) readLoop(sess *Session) {
	for {
		_, raw, err := sess.ws.ReadMessage()
		if err != nil {
			return
		}
		s.handleMessage(sess, raw)
	}
}

func (s *Server) runPingLoop(sess *Session) {
	sess.ws.SetReadDeadline(time.Now().Add(30 * time.Second))
	sess.ws.SetPongHandler(func(string) error {
		sess.ws.SetReadDeadline(time.Now().Add(30 * time.Second))
		return nil
	})
	go func() {
		ticker := time.NewTicker(20 * time.Second) // PROTOCOL.md §1: ping every 20s
		defer ticker.Stop()
		for range ticker.C {
			sess.writeMu.Lock()
			err := sess.ws.WriteControl(websocket.PingMessage, nil, time.Now().Add(10*time.Second))
			sess.writeMu.Unlock()
			if err != nil {
				return
			}
		}
	}()
}

func (sess *Session) send(v interface{}) error {
	b, err := json.Marshal(v)
	if err != nil {
		return err
	}
	sess.writeMu.Lock()
	defer sess.writeMu.Unlock()
	return sess.ws.WriteMessage(websocket.TextMessage, b)
}

// handleMessage is the single message-dispatch gate. Per the non-negotiable
// requirement: unauthenticated connections must not be able to reach
// metrics/processes/file_events/alerts/history — this is enforced here,
// before any type-specific handler runs.
func (s *Server) handleMessage(sess *Session, raw []byte) {
	typ, err := core.PeekType(raw)
	if err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "invalid JSON"))
		return
	}

	if !sess.authenticated && !unauthAllowed[typ] {
		var env core.Envelope
		_ = json.Unmarshal(raw, &env)
		sess.send(core.NewError(env.RequestID, core.ReasonUnauthenticated, "authenticate before using this message type"))
		return
	}

	switch typ {
	case core.TypeHello:
		s.handleHello(sess, raw)
	case core.TypePairRequest:
		s.handlePairRequest(sess, raw)
	case core.TypeAuthRequest:
		s.handleAuthRequest(sess, raw)
	case core.TypeAuthResponse:
		s.handleAuthResponse(sess, raw)
	case core.TypeSubscribe:
		s.handleSubscribe(sess, raw)
	case core.TypeListProcesses:
		s.handleListProcesses(sess, raw)
	case core.TypeKillProcess:
		s.handleKillProcess(sess, raw)
	case core.TypeListAllowedApps:
		s.handleListAllowedApps(sess, raw)
	case core.TypeLaunchApp:
		s.handleLaunchApp(sess, raw)
	case core.TypeListFileEvents:
		s.handleListFileEvents(sess, raw)
	case core.TypeTopGrowth:
		s.handleTopGrowth(sess, raw)
	case core.TypeListAlerts:
		s.handleListAlerts(sess, raw)
	case core.TypeListHistory:
		s.handleListHistory(sess, raw)
	default:
		var env core.Envelope
		_ = json.Unmarshal(raw, &env)
		sess.send(core.NewError(env.RequestID, core.ReasonBadRequest, fmt.Sprintf("unknown message type %q", typ)))
	}
}

// ---- 4.1 Handshake ----

func (s *Server) handleHello(sess *Session, raw []byte) {
	var h core.Hello
	if err := json.Unmarshal(raw, &h); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed hello"))
		return
	}
	if h.ProtoVersion != core.ProtoVersion {
		sess.send(core.NewError("", core.ReasonBadRequest, "unsupported proto_version"))
		sess.ws.Close()
		return
	}
	sess.send(core.HelloAck{
		Type:         core.TypeHelloAck,
		ProtoVersion: core.ProtoVersion,
		AgentVersion: s.AgentVersion,
		Hostname:     s.Hostname,
	})
}

// ---- 4.2 Pairing ----

func (s *Server) handlePairRequest(sess *Session, raw []byte) {
	var req core.PairRequest
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed pair_request"))
		return
	}
	now := time.Now()
	outcome := s.PairingMgr.Attempt(sess.connID, req.PairingCode, now)

	if outcome != core.PairOK {
		s.logHistory(HistoryEntry{
			TS: now.Unix(), DeviceID: "", DeviceName: req.DeviceName,
			Action: core.ActionPair, Success: false, Detail: "pairing attempt",
			Reason: outcome.Reason(),
		})
		sess.send(core.PairFailed{Type: core.TypePairFailed, RequestID: req.RequestID, Reason: outcome.Reason()})
		if outcome == core.PairLockedOut {
			sess.ws.Close() // PROTOCOL.md §2: "closes the socket"
		}
		return
	}

	deviceID := uuid.NewString()
	secret, err := core.GenerateDeviceSecret()
	if err != nil {
		sess.send(core.NewError(req.RequestID, core.ReasonInternalError, "failed to generate device secret"))
		return
	}
	encSecret, err := core.EncryptDeviceSecret(s.ServerKey, secret)
	if err != nil {
		sess.send(core.NewError(req.RequestID, core.ReasonInternalError, "failed to protect device secret"))
		return
	}
	name := strings.TrimSpace(req.DeviceName)
	if name == "" {
		name = "Unnamed device"
	}
	if err := s.Store.InsertDevice(DeviceRecord{
		ID: deviceID, Name: name, SecretEnc: encSecret, CreatedAt: now.Unix(),
	}); err != nil {
		sess.send(core.NewError(req.RequestID, core.ReasonInternalError, "failed to store device"))
		return
	}

	s.logHistory(HistoryEntry{
		TS: now.Unix(), DeviceID: deviceID, DeviceName: name,
		Action: core.ActionPair, Success: true, Detail: name,
	})
	sess.send(core.PairSuccess{
		Type: core.TypePairSuccess, RequestID: req.RequestID,
		DeviceID: deviceID, DeviceSecretB64: base64.StdEncoding.EncodeToString(secret),
	})
}

// ---- 4.3 Auth ----

func (s *Server) handleAuthRequest(sess *Session, raw []byte) {
	var req core.AuthRequest
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed auth_request"))
		return
	}
	now := time.Now()

	if locked, _ := s.AuthLockout.IsLockedOut(req.DeviceID, now); locked {
		sess.send(core.AuthFailed{Type: core.TypeAuthFailed, RequestID: req.RequestID, Reason: core.ReasonLockedOut})
		return
	}
	if locked, _ := s.AuthLockout.IsLockedOut(sess.remoteIP, now); locked {
		sess.send(core.AuthFailed{Type: core.TypeAuthFailed, RequestID: req.RequestID, Reason: core.ReasonLockedOut})
		return
	}

	dev, err := s.Store.GetDevice(req.DeviceID)
	if err != nil {
		sess.send(core.AuthFailed{Type: core.TypeAuthFailed, RequestID: req.RequestID, Reason: core.ReasonUnknownDevice})
		return
	}
	if dev.Revoked {
		sess.send(core.AuthFailed{Type: core.TypeAuthFailed, RequestID: req.RequestID, Reason: core.ReasonRevoked})
		return
	}

	nonce, err := core.GenerateNonce()
	if err != nil {
		sess.send(core.NewError(req.RequestID, core.ReasonInternalError, "failed to generate nonce"))
		return
	}
	sess.pendingChallenge = &pendingChallenge{
		deviceID:  req.DeviceID,
		nonce:     nonce,
		expiresAt: now.Add(authChallengeTTL),
	}
	sess.send(core.AuthChallenge{
		Type: core.TypeAuthChallenge, RequestID: req.RequestID,
		Nonce: base64.StdEncoding.EncodeToString(nonce),
	})
}

func (s *Server) handleAuthResponse(sess *Session, raw []byte) {
	var req core.AuthResponse
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed auth_response"))
		return
	}
	now := time.Now()
	pending := sess.pendingChallenge
	sess.pendingChallenge = nil

	fail := func(deviceID, reason string) {
		s.AuthLockout.RecordFailure(deviceID, now)
		s.AuthLockout.RecordFailure(sess.remoteIP, now)
		sess.send(core.AuthFailed{Type: core.TypeAuthFailed, RequestID: req.RequestID, Reason: reason})
	}

	if pending == nil || now.After(pending.expiresAt) {
		// PROTOCOL.md §4.3 lists no "expired_challenge" reason; treat a
		// missing/expired challenge as a bad HMAC (flagged best-effort
		// mapping, see README).
		fail("", core.ReasonBadHMAC)
		return
	}

	dev, err := s.Store.GetDevice(pending.deviceID)
	if err != nil {
		fail(pending.deviceID, core.ReasonUnknownDevice)
		return
	}
	if dev.Revoked {
		fail(pending.deviceID, core.ReasonRevoked)
		return
	}
	secret, err := core.DecryptDeviceSecret(s.ServerKey, dev.SecretEnc)
	if err != nil {
		fail(pending.deviceID, core.ReasonInternalError)
		return
	}
	candidate, err := base64.StdEncoding.DecodeString(req.HMACB64)
	if err != nil {
		fail(pending.deviceID, core.ReasonBadHMAC)
		return
	}
	if !core.VerifyAuthHMAC(secret, pending.nonce, pending.deviceID, candidate) {
		fail(pending.deviceID, core.ReasonBadHMAC)
		return
	}

	s.AuthLockout.RecordSuccess(pending.deviceID)
	s.AuthLockout.RecordSuccess(sess.remoteIP)
	sess.authenticated = true
	sess.deviceID = pending.deviceID
	sess.deviceName = dev.Name
	sess.send(core.AuthSuccess{Type: core.TypeAuthSuccess, RequestID: req.RequestID})
}

// ---- 4.4 Subscriptions ----

func (s *Server) handleSubscribe(sess *Session, raw []byte) {
	var req core.Subscribe
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed subscribe"))
		return
	}
	sess.topicsMu.Lock()
	for _, t := range req.Topics {
		sess.topics[t] = true
	}
	sess.topicsMu.Unlock()
}

func (sess *Session) subscribedTo(topic string) bool {
	sess.topicsMu.Lock()
	defer sess.topicsMu.Unlock()
	return sess.topics[topic]
}

// ---- 4.5 Processes ----

func (s *Server) handleListProcesses(sess *Session, raw []byte) {
	var req core.ListProcesses
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed list_processes"))
		return
	}
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	items, err := s.ProcessMgr.List(ctx, req.Query)
	if err != nil {
		sess.send(core.NewError(req.RequestID, core.ReasonInternalError, "failed to list processes"))
		return
	}
	sess.send(core.ProcessList{Type: core.TypeProcessList, RequestID: req.RequestID, Items: items})
}

func (s *Server) handleKillProcess(sess *Session, raw []byte) {
	var req core.KillProcess
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed kill_process"))
		return
	}
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	// The core protection predicate is re-checked unconditionally inside
	// ProcessMgr.Kill from live OS process state — never from anything the
	// client sent.
	result := s.ProcessMgr.Kill(ctx, req.PID)

	s.logHistory(HistoryEntry{
		TS: time.Now().Unix(), DeviceID: sess.deviceID, DeviceName: sess.deviceName,
		Action: core.ActionKillProcess, Success: result.Success,
		Detail: fmt.Sprintf("pid %d", req.PID), Reason: result.Reason,
	})

	resp := core.ActionResult{Type: core.TypeActionResult, RequestID: req.RequestID, Action: core.ActionKillProcess, Success: result.Success, Reason: result.Reason}
	sess.send(resp)
}

// ---- 4.6 App launch ----

func (s *Server) handleListAllowedApps(sess *Session, raw []byte) {
	var req core.ListAllowedApps
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed list_allowed_apps"))
		return
	}
	apps := s.AllowList.List()
	items := make([]core.AllowedAppItem, 0, len(apps))
	for _, a := range apps {
		items = append(items, core.AllowedAppItem{AppID: a.AppID, Label: a.Label})
	}
	sess.send(core.AllowedApps{Type: core.TypeAllowedApps, RequestID: req.RequestID, Items: items})
}

func (s *Server) handleLaunchApp(sess *Session, raw []byte) {
	var req core.LaunchApp
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed launch_app"))
		return
	}
	result := s.Launcher.Launch(req.AppID)

	s.logHistory(HistoryEntry{
		TS: time.Now().Unix(), DeviceID: sess.deviceID, DeviceName: sess.deviceName,
		Action: core.ActionLaunchApp, Success: result.Success,
		Detail: req.AppID, Reason: result.Reason,
	})

	resp := core.ActionResult{Type: core.TypeActionResult, RequestID: req.RequestID, Action: core.ActionLaunchApp, Success: result.Success, Reason: result.Reason}
	if result.Success {
		pid := result.PID
		resp.PID = &pid
	}
	sess.send(resp)
}

// ---- 4.7 File events / growth ----

func (s *Server) handleListFileEvents(sess *Session, raw []byte) {
	var req core.ListFileEvents
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed list_file_events"))
		return
	}
	items, total, err := s.Store.QueryFileEvents(FileEventQuery{
		FromTS: req.FromTS, ToTS: req.ToTS, Op: req.Op,
		MinSizeBytes: req.MinSizeBytes, PathPrefix: req.PathPrefix,
		Limit: req.Limit, Offset: req.Offset,
	})
	if err != nil {
		sess.send(core.NewError(req.RequestID, core.ReasonInternalError, "failed to query file events"))
		return
	}
	sess.send(core.FileEvents{Type: core.TypeFileEvents, RequestID: req.RequestID, Total: total, Items: items})
}

func (s *Server) handleTopGrowth(sess *Session, raw []byte) {
	var req core.TopGrowthRequest
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed top_growth"))
		return
	}
	windowSec, ok := parseWindow(req.Window)
	if !ok {
		sess.send(core.NewError(req.RequestID, core.ReasonBadRequest, "window must be one of 1h|24h|7d"))
		return
	}
	now := time.Now().Unix()
	afterTS, err := s.Store.LatestSnapshotTS()
	if err != nil {
		sess.send(core.NewError(req.RequestID, core.ReasonInternalError, "failed to read snapshots"))
		return
	}
	var after, before []core.SizeSnapshot
	if afterTS != 0 {
		after, _ = s.Store.SnapshotAt(afterTS)
		beforeTS, _ := s.Store.LatestSnapshotTSBefore(now - windowSec)
		if beforeTS != 0 {
			before, _ = s.Store.SnapshotAt(beforeTS)
		}
	}
	items := core.TopGrowth(before, after, req.Limit)
	wire := make([]core.TopGrowthItem, 0, len(items))
	for _, it := range items {
		wire = append(wire, core.TopGrowthItem{Path: it.Path, IsDir: it.IsDir, DeltaBytes: it.DeltaBytes, SizeBefore: it.SizeBefore, SizeAfter: it.SizeAfter})
	}
	sess.send(core.TopGrowthResult{Type: core.TypeTopGrowthResult, RequestID: req.RequestID, Window: req.Window, Items: wire})
}

func parseWindow(w string) (int64, bool) {
	switch w {
	case "1h":
		return 3600, true
	case "24h":
		return 86400, true
	case "7d":
		return 604800, true
	default:
		return 0, false
	}
}

// ---- 4.8 Alerts & history ----

func (s *Server) handleListAlerts(sess *Session, raw []byte) {
	var req core.ListAlerts
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed list_alerts"))
		return
	}
	items, err := s.Store.ListAlerts(req.Limit)
	if err != nil {
		sess.send(core.NewError(req.RequestID, core.ReasonInternalError, "failed to list alerts"))
		return
	}
	sess.send(core.Alerts{Type: core.TypeAlerts, RequestID: req.RequestID, Items: items})
}

func (s *Server) handleListHistory(sess *Session, raw []byte) {
	var req core.ListHistory
	if err := json.Unmarshal(raw, &req); err != nil {
		sess.send(core.NewError("", core.ReasonBadRequest, "malformed list_history"))
		return
	}
	toTS := req.ToTS
	if toTS == 0 {
		toTS = time.Now().Unix()
	}
	items, total, err := s.Store.QueryHistory(req.FromTS, toTS, req.Limit, req.Offset)
	if err != nil {
		sess.send(core.NewError(req.RequestID, core.ReasonInternalError, "failed to query history"))
		return
	}
	sess.send(core.History{Type: core.TypeHistory, RequestID: req.RequestID, Total: total, Items: items})
}

// ---- Broadcasting (push topics) ----

// BroadcastMetrics pushes a metrics snapshot to every authenticated
// session subscribed to the "metrics" topic.
func (s *Server) BroadcastMetrics(m core.MetricsPush) {
	s.forEachSubscribed(core.TopicMetrics, func(sess *Session) { sess.send(m) })
}

// BroadcastFileEvent pushes a file_event to every authenticated session
// subscribed to the "file_events" topic.
func (s *Server) BroadcastFileEvent(e core.FileEventRecord) {
	push := core.FileEventPush{Type: core.TypeFileEvent, TS: e.TS, Op: e.Op, Path: e.Path, OldPath: e.OldPath, SizeBytes: e.SizeBytes, IsDir: e.IsDir}
	s.forEachSubscribed(core.TopicFileEvents, func(sess *Session) { sess.send(push) })
}

// BroadcastAlert pushes an alert to every authenticated session
// subscribed to the "alerts" topic.
func (s *Server) BroadcastAlert(a core.AlertRecord) {
	push := core.AlertPush{Type: core.TypeAlert, TS: a.TS, ID: a.ID, Kind: a.Kind, Severity: a.Severity, Message: a.Message, Context: a.Context}
	s.forEachSubscribed(core.TopicAlerts, func(sess *Session) { sess.send(push) })
}

// BroadcastProcessSnapshot pushes a lighter process_list to sessions
// subscribed to the "processes" topic (PROTOCOL.md §4.4: every 5s if a
// client opts in).
func (s *Server) BroadcastProcessSnapshot(items []core.ProcessItem) {
	push := core.ProcessList{Type: core.TypeProcessList, Items: items}
	s.forEachSubscribed(core.TopicProcesses, func(sess *Session) { sess.send(push) })
}

func (s *Server) forEachSubscribed(topic string, fn func(*Session)) {
	s.mu.Lock()
	sessions := make([]*Session, 0, len(s.sessions))
	for sess := range s.sessions {
		sessions = append(sessions, sess)
	}
	s.mu.Unlock()
	for _, sess := range sessions {
		if sess.authenticated && sess.subscribedTo(topic) {
			fn(sess)
		}
	}
}

// HasActiveMetricsSubscribers reports whether at least one authenticated
// client currently subscribes to metrics, so the caller can gate the 2s
// sampling loop per PROTOCOL.md §4.4 ("push interval: 2s while at least
// one client is subscribed").
func (s *Server) HasActiveSubscribers(topic string) bool {
	s.mu.Lock()
	defer s.mu.Unlock()
	for sess := range s.sessions {
		if sess.authenticated && sess.subscribedTo(topic) {
			return true
		}
	}
	return false
}

func (s *Server) logHistory(h HistoryEntry) {
	if err := s.Store.InsertHistory(h); err != nil {
		log.Printf("failed to write history entry: %v", err)
	}
}

// ---- TLS/HTTP server bootstrap ----

// Listen starts the TLS listener for the WS server on addr (e.g.
// "0.0.0.0:8787") using the given certificate.
func (s *Server) Listen(addr string, cert tls.Certificate) (net.Listener, error) {
	tlsCfg := &tls.Config{
		Certificates: []tls.Certificate{cert},
		MinVersion:   tls.VersionTLS12, // PROTOCOL.md §1: "TLS 1.2+"
	}
	ln, err := tls.Listen("tcp", addr, tlsCfg)
	if err != nil {
		return nil, fmt.Errorf("listening on %s: %w", addr, err)
	}
	return ln, nil
}

func clientIP(r *http.Request) string {
	host, _, err := net.SplitHostPort(r.RemoteAddr)
	if err != nil {
		return r.RemoteAddr
	}
	return host
}

func defaultVolumes() []string {
	// Overridden at construction time by cmd/agent on real Windows drive
	// letters; "/" is only a sane fallback for this Linux dev environment.
	return []string{"/"}
}
