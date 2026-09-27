// Package core contains OS-agnostic, pure logic shared by the agent: wire
// message types, critical-process protection, allow-list validation,
// pairing/auth primitives, growth analysis and alert evaluation, and file
// event noise filtering. Nothing in this package imports gopsutil, fsnotify,
// sqlite or any Windows-specific API, so all of it is unit-testable on any
// platform, including this Linux development box.
package core

import "encoding/json"

// Protocol version this build implements (see PROTOCOL.md §9).
const ProtoVersion = 1

// Message type constants (PROTOCOL.md §4).
const (
	TypeHello     = "hello"
	TypeHelloAck  = "hello_ack"

	TypePairRequest = "pair_request"
	TypePairSuccess = "pair_success"
	TypePairFailed  = "pair_failed"

	TypeAuthRequest   = "auth_request"
	TypeAuthChallenge = "auth_challenge"
	TypeAuthResponse  = "auth_response"
	TypeAuthSuccess   = "auth_success"
	TypeAuthFailed    = "auth_failed"

	TypeSubscribe = "subscribe"

	TypeMetrics   = "metrics"
	TypeFileEvent = "file_event"
	TypeAlert     = "alert"

	TypeListProcesses = "list_processes"
	TypeProcessList   = "process_list"
	TypeKillProcess   = "kill_process"
	TypeActionResult  = "action_result"

	TypeListAllowedApps = "list_allowed_apps"
	TypeAllowedApps     = "allowed_apps"
	TypeLaunchApp       = "launch_app"

	TypeListFileEvents = "list_file_events"
	TypeFileEvents     = "file_events"
	TypeTopGrowth      = "top_growth"
	TypeTopGrowthResult = "top_growth_result"

	TypeListAlerts = "list_alerts"
	TypeAlerts     = "alerts"
	TypeListHistory = "list_history"
	TypeHistory     = "history"

	TypeError = "error"
)

// Error reason constants (PROTOCOL.md §4.9 and scattered failure reasons).
const (
	ReasonUnauthenticated = "unauthenticated"
	ReasonBadRequest      = "bad_request"
	ReasonInternalError   = "internal_error"

	ReasonInvalidCode  = "invalid_code"
	ReasonExpiredCode  = "expired_code"
	ReasonLockedOut    = "locked_out"

	ReasonBadHMAC      = "bad_hmac"
	ReasonUnknownDevice = "unknown_device"
	ReasonRevoked       = "revoked"

	ReasonCriticalProcessProtected = "critical_process_protected"
	ReasonNotFound                 = "not_found"
	ReasonAccessDenied              = "access_denied"

	ReasonNotAllowListed = "not_allow_listed"
	ReasonLaunchFailed   = "launch_failed"
)

// File event operation kinds.
const (
	OpCreated  = "created"
	OpModified = "modified"
	OpDeleted  = "deleted"
	OpRenamed  = "renamed"
	OpMoved    = "moved"
)

// Alert kinds and severities (PROTOCOL.md §8).
const (
	AlertLargeFile     = "large_file"
	AlertFastGrowth    = "fast_growth"
	AlertDiskFillRate  = "disk_fill_rate"
	AlertLowFreeSpace  = "low_free_space"

	SeverityWarning  = "warning"
	SeverityCritical = "critical"
)

// History action kinds (PROTOCOL.md §4.8).
const (
	ActionKillProcess = "kill_process"
	ActionLaunchApp   = "launch_app"
	ActionPair        = "pair"
	ActionUnpair      = "unpair"
)

// Subscription topics (PROTOCOL.md §4.4).
const (
	TopicMetrics    = "metrics"
	TopicFileEvents = "file_events"
	TopicAlerts     = "alerts"
	TopicProcesses  = "processes"
)

// Envelope is used to peek at the "type" and "request_id" of an inbound
// message before dispatching to a type-specific struct.
type Envelope struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id,omitempty"`
}

// PeekType reads only the "type" field out of a raw JSON message.
func PeekType(raw []byte) (string, error) {
	var e Envelope
	if err := json.Unmarshal(raw, &e); err != nil {
		return "", err
	}
	return e.Type, nil
}

// ---- 4.1 Handshake ----

type Hello struct {
	Type        string `json:"type"`
	ProtoVersion int   `json:"proto_version"`
	Client      string `json:"client"`
	AppVersion  string `json:"app_version"`
}

type HelloAck struct {
	Type         string `json:"type"`
	ProtoVersion int    `json:"proto_version"`
	AgentVersion string `json:"agent_version"`
	Hostname     string `json:"hostname"`
}

// ---- 4.2 Pairing ----

type PairRequest struct {
	Type        string `json:"type"`
	RequestID   string `json:"request_id"`
	PairingCode string `json:"pairing_code"`
	DeviceName  string `json:"device_name"`
}

type PairSuccess struct {
	Type             string `json:"type"`
	RequestID        string `json:"request_id"`
	DeviceID         string `json:"device_id"`
	DeviceSecretB64  string `json:"device_secret_b64"`
}

type PairFailed struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
	Reason    string `json:"reason"`
}

// ---- 4.3 Auth ----

type AuthRequest struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
	DeviceID  string `json:"device_id"`
}

type AuthChallenge struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
	Nonce     string `json:"nonce"` // base64
}

type AuthResponse struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
	HMACB64   string `json:"hmac_b64"`
}

type AuthSuccess struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
}

type AuthFailed struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
	Reason    string `json:"reason"`
}

// ---- 4.4 Subscriptions ----

type Subscribe struct {
	Type   string   `json:"type"`
	Topics []string `json:"topics"`
}

type RAMInfo struct {
	TotalBytes uint64 `json:"total_bytes"`
	UsedBytes  uint64 `json:"used_bytes"`
}

type DiskInfo struct {
	Volume     string `json:"volume"`
	TotalBytes uint64 `json:"total_bytes"`
	UsedBytes  uint64 `json:"used_bytes"`
	FreeBytes  uint64 `json:"free_bytes"`
}

type MetricsPush struct {
	Type       string     `json:"type"`
	TS         int64      `json:"ts"`
	CPUPercent float64    `json:"cpu_percent"`
	RAM        RAMInfo    `json:"ram"`
	Disks      []DiskInfo `json:"disks"`
}

type FileEventPush struct {
	Type      string `json:"type"`
	TS        int64  `json:"ts"`
	Op        string `json:"op"`
	Path      string `json:"path"`
	OldPath   *string `json:"old_path"`
	SizeBytes int64  `json:"size_bytes"`
	IsDir     bool   `json:"is_dir"`
}

type AlertPush struct {
	Type     string                 `json:"type"`
	TS       int64                  `json:"ts"`
	ID       string                 `json:"id"`
	Kind     string                 `json:"kind"`
	Severity string                 `json:"severity"`
	Message  string                 `json:"message"`
	Context  map[string]interface{} `json:"context"`
}

// ---- 4.5 Processes ----

type ListProcesses struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
	Query     string `json:"query"`
}

type ProcessItem struct {
	PID        int32   `json:"pid"`
	Name       string  `json:"name"`
	Exe        string  `json:"exe"`
	User       string  `json:"user"`
	CPUPercent float64 `json:"cpu_percent"`
	RAMBytes   uint64  `json:"ram_bytes"`
	Protected  bool    `json:"protected"`
}

type ProcessList struct {
	Type      string        `json:"type"`
	RequestID string        `json:"request_id"`
	Items     []ProcessItem `json:"items"`
}

type KillProcess struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
	PID       int32  `json:"pid"`
}

type ActionResult struct {
	Type      string  `json:"type"`
	RequestID string  `json:"request_id"`
	Action    string  `json:"action"`
	Success   bool    `json:"success"`
	Reason    string  `json:"reason,omitempty"`
	PID       *int32  `json:"pid,omitempty"`
}

// ---- 4.6 App launch ----

type ListAllowedApps struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
}

type AllowedAppItem struct {
	AppID string `json:"app_id"`
	Label string `json:"label"`
}

type AllowedApps struct {
	Type      string           `json:"type"`
	RequestID string           `json:"request_id"`
	Items     []AllowedAppItem `json:"items"`
}

type LaunchApp struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
	AppID     string `json:"app_id"`
}

// ---- 4.7 File events / growth queries ----

type ListFileEvents struct {
	Type         string  `json:"type"`
	RequestID    string  `json:"request_id"`
	FromTS       int64   `json:"from_ts"`
	ToTS         int64   `json:"to_ts"`
	Op           *string `json:"op"`
	MinSizeBytes int64   `json:"min_size_bytes"`
	PathPrefix   string  `json:"path_prefix"`
	Limit        int     `json:"limit"`
	Offset       int     `json:"offset"`
}

type FileEventRecord struct {
	TS        int64   `json:"ts"`
	Op        string  `json:"op"`
	Path      string  `json:"path"`
	OldPath   *string `json:"old_path"`
	SizeBytes int64   `json:"size_bytes"`
	IsDir     bool    `json:"is_dir"`
}

type FileEvents struct {
	Type      string             `json:"type"`
	RequestID string             `json:"request_id"`
	Total     int                `json:"total"`
	Items     []FileEventRecord  `json:"items"`
}

type TopGrowthRequest struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
	Window    string `json:"window"` // "1h"|"24h"|"7d"
	Limit     int    `json:"limit"`
}

type TopGrowthItem struct {
	Path       string `json:"path"`
	IsDir      bool   `json:"is_dir"`
	DeltaBytes int64  `json:"delta_bytes"`
	SizeBefore int64  `json:"size_before"`
	SizeAfter  int64  `json:"size_after"`
}

type TopGrowthResult struct {
	Type      string          `json:"type"`
	RequestID string          `json:"request_id"`
	Window    string          `json:"window"`
	Items     []TopGrowthItem `json:"items"`
}

// ---- 4.8 Alerts & history ----

type ListAlerts struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
	Limit     int    `json:"limit"`
}

type AlertRecord struct {
	TS       int64                  `json:"ts"`
	ID       string                 `json:"id"`
	Kind     string                 `json:"kind"`
	Severity string                 `json:"severity"`
	Message  string                 `json:"message"`
	Context  map[string]interface{} `json:"context"`
}

type Alerts struct {
	Type      string        `json:"type"`
	RequestID string        `json:"request_id"`
	Items     []AlertRecord `json:"items"`
}

type ListHistory struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id"`
	FromTS    int64  `json:"from_ts"`
	ToTS      int64  `json:"to_ts"`
	Limit     int    `json:"limit"`
	Offset    int    `json:"offset"`
}

type HistoryItem struct {
	TS         int64  `json:"ts"`
	DeviceID   string `json:"device_id"`
	DeviceName string `json:"device_name"`
	Action     string `json:"action"`
	Success    bool   `json:"success"`
	Detail     string `json:"detail"`
	Reason     *string `json:"reason"`
}

type History struct {
	Type      string        `json:"type"`
	RequestID string        `json:"request_id"`
	Total     int           `json:"total"`
	Items     []HistoryItem `json:"items"`
}

// ---- 4.9 Errors ----

type ErrorMsg struct {
	Type      string `json:"type"`
	RequestID string `json:"request_id,omitempty"`
	Reason    string `json:"reason"`
	Message   string `json:"message"`
}

func NewError(requestID, reason, message string) ErrorMsg {
	return ErrorMsg{Type: TypeError, RequestID: requestID, Reason: reason, Message: message}
}
