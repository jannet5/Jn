package agent

import (
	"crypto/tls"
	"crypto/x509"
	"encoding/base64"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"path/filepath"
	"strings"
	"testing"
	"time"

	"github.com/gorilla/websocket"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// testHarness spins up the real Server (with a real sqlite-backed Store)
// behind a real TLS WebSocket listener (httptest.NewTLSServer), and
// connects to it with the real gorilla/websocket client. This exercises
// the actual dispatch/auth/pairing code path end-to-end, not a mock of it.
type testHarness struct {
	t      *testing.T
	server *Server
	ts     *httptest.Server
	dialer websocket.Dialer
	wsURL  string
}

func newTestHarness(t *testing.T) *testHarness {
	t.Helper()
	dir := t.TempDir()
	store, err := OpenStore(filepath.Join(dir, "agent.db"))
	if err != nil {
		t.Fatalf("opening store: %v", err)
	}
	t.Cleanup(func() { store.Close() })

	allowList, err := core.NewAllowList([]core.AllowedApp{
		{AppID: "truthy", Label: "True", Path: "/bin/true"},
	})
	if err != nil {
		t.Fatalf("building allow-list: %v", err)
	}

	serverKey, err := core.GenerateServerKey()
	if err != nil {
		t.Fatalf("generating server key: %v", err)
	}

	srv := NewServer(core.DefaultConfig(), store, allowList, serverKey)

	ts := httptest.NewTLSServer(http.HandlerFunc(srv.ServeHTTP))
	t.Cleanup(ts.Close)

	certPool := x509.NewCertPool()
	certPool.AddCert(ts.Certificate())
	dialer := websocket.Dialer{
		TLSClientConfig: &tls.Config{RootCAs: certPool},
	}

	wsURL := "wss" + strings.TrimPrefix(ts.URL, "https")

	return &testHarness{t: t, server: srv, ts: ts, dialer: dialer, wsURL: wsURL + "/ws"}
}

func (h *testHarness) dial() *websocket.Conn {
	h.t.Helper()
	conn, _, err := h.dialer.Dial(h.wsURL, nil)
	if err != nil {
		h.t.Fatalf("dial: %v", err)
	}
	h.t.Cleanup(func() { conn.Close() })
	return conn
}

func sendJSON(t *testing.T, conn *websocket.Conn, v interface{}) {
	t.Helper()
	b, err := json.Marshal(v)
	if err != nil {
		t.Fatalf("marshal: %v", err)
	}
	if err := conn.WriteMessage(websocket.TextMessage, b); err != nil {
		t.Fatalf("write: %v", err)
	}
}

func recvJSON(t *testing.T, conn *websocket.Conn) map[string]interface{} {
	t.Helper()
	conn.SetReadDeadline(time.Now().Add(5 * time.Second))
	_, raw, err := conn.ReadMessage()
	if err != nil {
		t.Fatalf("read: %v", err)
	}
	var m map[string]interface{}
	if err := json.Unmarshal(raw, &m); err != nil {
		t.Fatalf("unmarshal %s: %v", raw, err)
	}
	return m
}

func TestServer_HelloHandshake(t *testing.T) {
	h := newTestHarness(t)
	conn := h.dial()
	sendJSON(t, conn, core.Hello{Type: core.TypeHello, ProtoVersion: 1, Client: "android", AppVersion: "1.0.0"})
	resp := recvJSON(t, conn)
	if resp["type"] != core.TypeHelloAck {
		t.Fatalf("expected hello_ack, got %v", resp)
	}
	if resp["hostname"] == nil || resp["hostname"] == "" {
		t.Error("expected a non-empty hostname in hello_ack")
	}
}

func TestServer_UnauthenticatedConnectionRejectedForProtectedTypes(t *testing.T) {
	h := newTestHarness(t)
	conn := h.dial()

	protectedRequests := []interface{}{
		core.ListProcesses{Type: core.TypeListProcesses, RequestID: "r1"},
		core.ListAllowedApps{Type: core.TypeListAllowedApps, RequestID: "r2"},
		core.ListAlerts{Type: core.TypeListAlerts, RequestID: "r3"},
		core.ListHistory{Type: core.TypeListHistory, RequestID: "r4"},
		core.ListFileEvents{Type: core.TypeListFileEvents, RequestID: "r5"},
		core.Subscribe{Type: core.TypeSubscribe, Topics: []string{"metrics"}},
	}
	for _, req := range protectedRequests {
		sendJSON(t, conn, req)
		resp := recvJSON(t, conn)
		if resp["type"] != core.TypeError {
			t.Fatalf("request %+v: expected error response, got %v", req, resp)
		}
		if resp["reason"] != core.ReasonUnauthenticated {
			t.Fatalf("request %+v: expected reason=unauthenticated, got %v", req, resp)
		}
	}
}

func TestServer_KillProcessRejectedWhenUnauthenticated(t *testing.T) {
	h := newTestHarness(t)
	conn := h.dial()
	sendJSON(t, conn, core.KillProcess{Type: core.TypeKillProcess, RequestID: "kp1", PID: 1})
	resp := recvJSON(t, conn)
	if resp["type"] != core.TypeError || resp["reason"] != core.ReasonUnauthenticated {
		t.Fatalf("expected unauthenticated error, got %v", resp)
	}
}

// pairAndAuth performs a full real pair_request -> pair_success ->
// auth_request -> auth_challenge -> auth_response -> auth_success cycle
// over the live connection and returns the device_id/secret for
// reference.
func (h *testHarness) pairAndAuth(conn *websocket.Conn) (deviceID string, secret []byte) {
	t := h.t
	code, err := h.server.PairingMgr.IssueCode(time.Now())
	if err != nil {
		t.Fatalf("issuing pairing code: %v", err)
	}
	sendJSON(t, conn, core.PairRequest{Type: core.TypePairRequest, RequestID: "p1", PairingCode: code, DeviceName: "Test Device"})
	resp := recvJSON(t, conn)
	if resp["type"] != core.TypePairSuccess {
		t.Fatalf("expected pair_success, got %v", resp)
	}
	deviceID = resp["device_id"].(string)
	secretB64 := resp["device_secret_b64"].(string)
	secret, err = base64.StdEncoding.DecodeString(secretB64)
	if err != nil {
		t.Fatalf("decoding device secret: %v", err)
	}

	sendJSON(t, conn, core.AuthRequest{Type: core.TypeAuthRequest, RequestID: "a1", DeviceID: deviceID})
	challengeResp := recvJSON(t, conn)
	if challengeResp["type"] != core.TypeAuthChallenge {
		t.Fatalf("expected auth_challenge, got %v", challengeResp)
	}
	nonceB64 := challengeResp["nonce"].(string)
	nonce, err := base64.StdEncoding.DecodeString(nonceB64)
	if err != nil {
		t.Fatalf("decoding nonce: %v", err)
	}
	mac := core.ComputeAuthHMAC(secret, nonce, deviceID)
	sendJSON(t, conn, core.AuthResponse{Type: core.TypeAuthResponse, RequestID: "a2", HMACB64: base64.StdEncoding.EncodeToString(mac)})
	successResp := recvJSON(t, conn)
	if successResp["type"] != core.TypeAuthSuccess {
		t.Fatalf("expected auth_success, got %v", successResp)
	}
	return deviceID, secret
}

func TestServer_FullPairAuthCycle(t *testing.T) {
	h := newTestHarness(t)
	conn := h.dial()
	deviceID, _ := h.pairAndAuth(conn)
	if deviceID == "" {
		t.Fatal("expected a non-empty device_id")
	}

	// History must record the successful pairing.
	items, _, err := h.server.Store.QueryHistory(0, time.Now().Unix()+10, 50, 0)
	if err != nil {
		t.Fatalf("querying history: %v", err)
	}
	foundPair := false
	for _, it := range items {
		if it.Action == core.ActionPair && it.Success && it.DeviceID == deviceID {
			foundPair = true
		}
	}
	if !foundPair {
		t.Errorf("expected a successful pair history entry for device %s, got %+v", deviceID, items)
	}
}

func TestServer_AuthWrongSecretFails(t *testing.T) {
	h := newTestHarness(t)
	conn := h.dial()
	code, _ := h.server.PairingMgr.IssueCode(time.Now())
	sendJSON(t, conn, core.PairRequest{Type: core.TypePairRequest, RequestID: "p1", PairingCode: code, DeviceName: "D"})
	resp := recvJSON(t, conn)
	deviceID := resp["device_id"].(string)

	sendJSON(t, conn, core.AuthRequest{Type: core.TypeAuthRequest, RequestID: "a1", DeviceID: deviceID})
	challengeResp := recvJSON(t, conn)
	nonceB64 := challengeResp["nonce"].(string)
	nonce, _ := base64.StdEncoding.DecodeString(nonceB64)

	wrongSecret := make([]byte, 32)
	mac := core.ComputeAuthHMAC(wrongSecret, nonce, deviceID)
	sendJSON(t, conn, core.AuthResponse{Type: core.TypeAuthResponse, RequestID: "a2", HMACB64: base64.StdEncoding.EncodeToString(mac)})
	failResp := recvJSON(t, conn)
	if failResp["type"] != core.TypeAuthFailed {
		t.Fatalf("expected auth_failed, got %v", failResp)
	}
	if failResp["reason"] != core.ReasonBadHMAC {
		t.Errorf("reason = %v, want bad_hmac", failResp["reason"])
	}
}

func TestServer_ListProcessesAfterAuth(t *testing.T) {
	h := newTestHarness(t)
	conn := h.dial()
	h.pairAndAuth(conn)

	sendJSON(t, conn, core.ListProcesses{Type: core.TypeListProcesses, RequestID: "lp1"})
	resp := recvJSON(t, conn)
	if resp["type"] != core.TypeProcessList {
		t.Fatalf("expected process_list, got %v", resp)
	}
	items, ok := resp["items"].([]interface{})
	if !ok || len(items) == 0 {
		t.Fatalf("expected at least one real process in the list, got %v", resp["items"])
	}
}

func TestServer_KillProcess_RefusesOwnPIDServerSide(t *testing.T) {
	h := newTestHarness(t)
	conn := h.dial()
	h.pairAndAuth(conn)

	ownPID := h.server.ProcessMgr.OwnPID
	sendJSON(t, conn, core.KillProcess{Type: core.TypeKillProcess, RequestID: "kp1", PID: ownPID})
	resp := recvJSON(t, conn)
	if resp["type"] != core.TypeActionResult {
		t.Fatalf("expected action_result, got %v", resp)
	}
	if resp["success"] != false {
		t.Fatalf("expected success=false for own pid, got %v", resp)
	}
	if resp["reason"] != core.ReasonCriticalProcessProtected {
		t.Errorf("reason = %v, want critical_process_protected", resp["reason"])
	}

	items, _, err := h.server.Store.QueryHistory(0, time.Now().Unix()+10, 50, 0)
	if err != nil {
		t.Fatalf("querying history: %v", err)
	}
	found := false
	for _, it := range items {
		if it.Action == core.ActionKillProcess && !it.Success {
			found = true
		}
	}
	if !found {
		t.Error("expected a failed kill_process history entry")
	}
}

func TestServer_LaunchApp_AllowedAndDisallowed(t *testing.T) {
	h := newTestHarness(t)
	conn := h.dial()
	h.pairAndAuth(conn)

	sendJSON(t, conn, core.LaunchApp{Type: core.TypeLaunchApp, RequestID: "la1", AppID: "truthy"})
	resp := recvJSON(t, conn)
	if resp["type"] != core.TypeActionResult || resp["success"] != true {
		t.Fatalf("expected successful launch, got %v", resp)
	}

	sendJSON(t, conn, core.LaunchApp{Type: core.TypeLaunchApp, RequestID: "la2", AppID: "not-allowed"})
	resp2 := recvJSON(t, conn)
	if resp2["success"] != false || resp2["reason"] != core.ReasonNotAllowListed {
		t.Fatalf("expected not_allow_listed failure, got %v", resp2)
	}
}

func TestServer_PairingLockoutClosesSocketAfter5FailedAttempts(t *testing.T) {
	h := newTestHarness(t)
	conn := h.dial()

	for i := 0; i < 4; i++ {
		sendJSON(t, conn, core.PairRequest{Type: core.TypePairRequest, RequestID: "bad", PairingCode: "0000-0000", DeviceName: "X"})
		resp := recvJSON(t, conn)
		if resp["type"] != core.TypePairFailed {
			t.Fatalf("attempt %d: expected pair_failed, got %v", i+1, resp)
		}
	}
	// 5th attempt triggers lockout and socket close.
	sendJSON(t, conn, core.PairRequest{Type: core.TypePairRequest, RequestID: "bad5", PairingCode: "0000-0000", DeviceName: "X"})
	resp := recvJSON(t, conn)
	if resp["reason"] != core.ReasonLockedOut {
		t.Fatalf("expected locked_out on 5th attempt, got %v", resp)
	}
	conn.SetReadDeadline(time.Now().Add(2 * time.Second))
	if _, _, err := conn.ReadMessage(); err == nil {
		t.Error("expected the socket to be closed after pairing lockout")
	}
}

func TestServer_BadHelloVersionClosesConnection(t *testing.T) {
	h := newTestHarness(t)
	conn := h.dial()
	sendJSON(t, conn, core.Hello{Type: core.TypeHello, ProtoVersion: 999, Client: "android", AppVersion: "1.0.0"})
	resp := recvJSON(t, conn)
	if resp["type"] != core.TypeError || resp["reason"] != core.ReasonBadRequest {
		t.Fatalf("expected bad_request error for unsupported proto_version, got %v", resp)
	}
	conn.SetReadDeadline(time.Now().Add(2 * time.Second))
	if _, _, err := conn.ReadMessage(); err == nil {
		t.Error("expected connection to be closed after bad proto_version")
	}
}
