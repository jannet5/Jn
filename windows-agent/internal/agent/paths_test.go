package agent

import (
	"bytes"
	"path/filepath"
	"testing"
)

func TestEnsureServerKey_GeneratesAndPersists(t *testing.T) {
	dir := t.TempDir()
	path := filepath.Join(dir, "server.key")
	k1, err := EnsureServerKey(path)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(k1) != 32 {
		t.Errorf("key length = %d, want 32", len(k1))
	}
	k2, err := EnsureServerKey(path)
	if err != nil {
		t.Fatalf("unexpected error on reload: %v", err)
	}
	if !bytes.Equal(k1, k2) {
		t.Error("expected the same key to be reloaded, not regenerated")
	}
}

func TestDataDir_OverrideWins(t *testing.T) {
	if got := DataDir("/custom/path"); got != "/custom/path" {
		t.Errorf("DataDir override = %q, want /custom/path", got)
	}
}

func TestDataDir_DefaultIsNonEmpty(t *testing.T) {
	if got := DataDir(""); got == "" {
		t.Error("expected a non-empty default data dir")
	}
}
