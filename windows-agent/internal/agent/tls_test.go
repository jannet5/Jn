package agent

import (
	"encoding/hex"
	"path/filepath"
	"testing"
)

func TestEnsureCert_GeneratesValidCertAndFingerprint(t *testing.T) {
	dir := t.TempDir()
	certPath := filepath.Join(dir, "agent-cert.pem")
	keyPath := filepath.Join(dir, "agent-key.pem")

	cert, fp, err := EnsureCert(certPath, keyPath)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(cert.Certificate) == 0 {
		t.Fatal("expected a non-empty certificate chain")
	}
	raw, err := hex.DecodeString(fp)
	if err != nil {
		t.Fatalf("fingerprint is not valid hex: %v", err)
	}
	if len(raw) != 32 {
		t.Errorf("fingerprint decodes to %d bytes, want 32 (SHA-256)", len(raw))
	}
	if fp != stringsToLower(fp) {
		t.Errorf("fingerprint must be lowercase, got %q", fp)
	}
	if !fileExists(certPath) || !fileExists(keyPath) {
		t.Error("expected cert and key to be persisted to disk")
	}
}

func TestEnsureCert_PersistsAndReloadsSameFingerprint(t *testing.T) {
	dir := t.TempDir()
	certPath := filepath.Join(dir, "agent-cert.pem")
	keyPath := filepath.Join(dir, "agent-key.pem")

	_, fp1, err := EnsureCert(certPath, keyPath)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	_, fp2, err := EnsureCert(certPath, keyPath)
	if err != nil {
		t.Fatalf("unexpected error on reload: %v", err)
	}
	if fp1 != fp2 {
		t.Errorf("expected the same fingerprint on reload (persisted cert must be reused), got %q vs %q", fp1, fp2)
	}
}

func TestEnsureCert_DifferentInstancesGetDifferentFingerprints(t *testing.T) {
	dirA := t.TempDir()
	dirB := t.TempDir()
	_, fpA, err := EnsureCert(filepath.Join(dirA, "c.pem"), filepath.Join(dirA, "k.pem"))
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	_, fpB, err := EnsureCert(filepath.Join(dirB, "c.pem"), filepath.Join(dirB, "k.pem"))
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if fpA == fpB {
		t.Error("two independently generated certs should not collide")
	}
}

func stringsToLower(s string) string {
	b := []byte(s)
	for i, c := range b {
		if c >= 'A' && c <= 'Z' {
			b[i] = c - 'A' + 'a'
		}
	}
	return string(b)
}
