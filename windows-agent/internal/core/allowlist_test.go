package core

import "testing"

func TestParseAllowList_Basic(t *testing.T) {
	data := []byte(`[
		{"app_id":"notepad","label":"Not Defteri","path":"C:\\Windows\\System32\\notepad.exe"},
		{"app_id":"calc","label":"Hesap Makinesi","path":"C:\\Windows\\System32\\calc.exe"}
	]`)
	al, err := ParseAllowList(data)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	app, ok := al.Resolve("notepad")
	if !ok {
		t.Fatal("expected notepad to resolve")
	}
	if app.Path != `C:\Windows\System32\notepad.exe` {
		t.Errorf("path = %q", app.Path)
	}
	if len(al.List()) != 2 {
		t.Errorf("expected 2 apps, got %d", len(al.List()))
	}
}

func TestResolve_UnknownAppIDRejected(t *testing.T) {
	al, err := ParseAllowList([]byte(`[{"app_id":"notepad","label":"x","path":"C:\\notepad.exe"}]`))
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if _, ok := al.Resolve("powershell"); ok {
		t.Error("expected unknown app_id to be rejected")
	}
	// Also confirm arbitrary attacker-supplied app_id (path injection attempt
	// disguised as an app_id) never resolves.
	if _, ok := al.Resolve(`C:\Windows\System32\cmd.exe`); ok {
		t.Error("a raw path used as app_id must never resolve")
	}
}

func TestNewAllowList_RejectsDuplicateAppID(t *testing.T) {
	_, err := NewAllowList([]AllowedApp{
		{AppID: "notepad", Label: "a", Path: "C:\\a.exe"},
		{AppID: "notepad", Label: "b", Path: "C:\\b.exe"},
	})
	if err == nil {
		t.Fatal("expected error for duplicate app_id")
	}
}

func TestNewAllowList_RejectsEmptyFields(t *testing.T) {
	if _, err := NewAllowList([]AllowedApp{{AppID: "", Label: "a", Path: "C:\\a.exe"}}); err == nil {
		t.Error("expected error for empty app_id")
	}
	if _, err := NewAllowList([]AllowedApp{{AppID: "a", Label: "a", Path: ""}}); err == nil {
		t.Error("expected error for empty path")
	}
}

func TestParseAllowList_MalformedJSON(t *testing.T) {
	if _, err := ParseAllowList([]byte(`not json`)); err == nil {
		t.Fatal("expected error for malformed JSON")
	}
}

func TestResolve_NilAllowList(t *testing.T) {
	var al *AllowList
	if _, ok := al.Resolve("notepad"); ok {
		t.Error("nil allow-list must never resolve anything")
	}
	if got := al.List(); got != nil {
		t.Errorf("nil allow-list List() = %v, want nil", got)
	}
}

func TestParseAllowList_AcceptsUTF8BOM(t *testing.T) {
	al, err := ParseAllowList([]byte("\xef\xbb\xbf[{\"app_id\":\"notepad\",\"label\":\"Not Defteri\",\"path\":\"C:\\\\Windows\\\\notepad.exe\"}]"))
	if err != nil {
		t.Fatalf("allow-list saved with a UTF-8 BOM was rejected: %v", err)
	}
	if _, ok := al.Resolve("notepad"); !ok {
		t.Error("expected notepad to be allowed")
	}
}
