package core

import (
	"encoding/json"
	"testing"
)

func TestPeekType(t *testing.T) {
	raw := []byte(`{"type":"hello","proto_version":1,"client":"android","app_version":"1.0.0"}`)
	typ, err := PeekType(raw)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if typ != TypeHello {
		t.Errorf("got %q, want %q", typ, TypeHello)
	}
}

func TestPeekTypeInvalidJSON(t *testing.T) {
	_, err := PeekType([]byte(`not json`))
	if err == nil {
		t.Fatal("expected error for invalid JSON")
	}
}

func TestHelloRoundTrip(t *testing.T) {
	h := Hello{Type: TypeHello, ProtoVersion: 1, Client: "android", AppVersion: "1.0.0"}
	b, err := json.Marshal(h)
	if err != nil {
		t.Fatalf("marshal: %v", err)
	}
	var h2 Hello
	if err := json.Unmarshal(b, &h2); err != nil {
		t.Fatalf("unmarshal: %v", err)
	}
	if h2 != h {
		t.Errorf("round trip mismatch: got %+v, want %+v", h2, h)
	}
}

func TestPairSuccessFieldNames(t *testing.T) {
	p := PairSuccess{
		Type:            TypePairSuccess,
		RequestID:       "req-1",
		DeviceID:        "dev-1",
		DeviceSecretB64: "c2VjcmV0",
	}
	b, err := json.Marshal(p)
	if err != nil {
		t.Fatalf("marshal: %v", err)
	}
	var m map[string]interface{}
	if err := json.Unmarshal(b, &m); err != nil {
		t.Fatalf("unmarshal: %v", err)
	}
	for _, field := range []string{"type", "request_id", "device_id", "device_secret_b64"} {
		if _, ok := m[field]; !ok {
			t.Errorf("expected field %q in JSON output, got %v", field, m)
		}
	}
}

func TestActionResultOmitsEmptyReason(t *testing.T) {
	a := ActionResult{Type: TypeActionResult, RequestID: "r1", Action: ActionKillProcess, Success: true}
	b, _ := json.Marshal(a)
	var m map[string]interface{}
	json.Unmarshal(b, &m)
	if _, ok := m["reason"]; ok {
		t.Errorf("expected reason to be omitted when empty, got %v", m)
	}
}

func TestErrorMsgConstructor(t *testing.T) {
	e := NewError("req-9", ReasonUnauthenticated, "not authenticated")
	if e.Type != TypeError {
		t.Errorf("type = %q, want %q", e.Type, TypeError)
	}
	if e.Reason != ReasonUnauthenticated {
		t.Errorf("reason = %q, want %q", e.Reason, ReasonUnauthenticated)
	}
	if e.RequestID != "req-9" {
		t.Errorf("request_id = %q, want req-9", e.RequestID)
	}
}
