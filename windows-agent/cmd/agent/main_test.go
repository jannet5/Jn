package main

import (
	"os"
	"path/filepath"
	"testing"
)

func TestResolveConfigPath_ExplicitFlagWins(t *testing.T) {
	if got := resolveConfigPath("custom.json"); got != "custom.json" {
		t.Fatalf("got %q, want custom.json", got)
	}
}

func TestResolveConfigPath_PicksUpConfigNextToExecutable(t *testing.T) {
	candidate := filepath.Join(exeDir(), "config.json")
	if _, err := os.Stat(candidate); err == nil {
		t.Skip("a config.json already exists next to the test binary")
	}
	if got := resolveConfigPath(""); got != "" {
		t.Fatalf("with no config.json present, got %q, want built-in defaults", got)
	}
	if err := os.WriteFile(candidate, []byte(`{"port":9999}`), 0600); err != nil {
		t.Fatalf("writing %s: %v", candidate, err)
	}
	defer os.Remove(candidate)
	if got := resolveConfigPath(""); got != candidate {
		t.Fatalf("got %q, want %q", got, candidate)
	}
}

func TestConfigBaseDir_RelativePathsResolveNextToConfigFile(t *testing.T) {
	dir := t.TempDir()
	cfg := filepath.Join(dir, "config.json")
	if got := configBaseDir(cfg); got != dir {
		t.Fatalf("got %q, want %q", got, dir)
	}
	if got := configBaseDir(""); got != exeDir() {
		t.Fatalf("with no config file, got %q, want the executable's dir %q", got, exeDir())
	}
}
