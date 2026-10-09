package core

import (
	"os"
	"path/filepath"
	"testing"
)

func TestDefaultConfig_MatchesProtocolDefaults(t *testing.T) {
	cfg := DefaultConfig()
	if cfg.Port != 8787 {
		t.Errorf("port = %d, want 8787", cfg.Port)
	}
	th := cfg.AlertThresholds
	if th.LargeFileBytes != 500*1024*1024 {
		t.Errorf("large_file_bytes = %d, want 500MiB", th.LargeFileBytes)
	}
	if th.FastGrowthBytes != 200*1024*1024 {
		t.Errorf("fast_growth_bytes = %d, want 200MiB", th.FastGrowthBytes)
	}
	if th.FastGrowthWindowSec != 600 {
		t.Errorf("fast_growth_window_sec = %d, want 600", th.FastGrowthWindowSec)
	}
	if th.DiskFillRatePercent != 5.0 {
		t.Errorf("disk_fill_rate_percent = %v, want 5.0", th.DiskFillRatePercent)
	}
	if th.DiskFillRateWindowSec != 900 {
		t.Errorf("disk_fill_rate_window_sec = %d, want 900", th.DiskFillRateWindowSec)
	}
	if th.LowFreeSpacePercent != 10.0 {
		t.Errorf("low_free_space_percent = %v, want 10.0", th.LowFreeSpacePercent)
	}
}

func TestParseConfig_OverridesAndAdditiveGlobs(t *testing.T) {
	data := []byte(`{
		"port": 9999,
		"watched_roots": ["C:\\Users\\jake"],
		"ignore_globs": ["**\\custom_ignore\\**"],
		"alert_thresholds": {"large_file_bytes": 1000, "fast_growth_bytes": 2000, "fast_growth_window_sec": 60, "disk_fill_rate_percent": 1.5, "disk_fill_rate_window_sec": 30, "low_free_space_percent": 20}
	}`)
	cfg, err := ParseConfig(data)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if cfg.Port != 9999 {
		t.Errorf("port = %d, want 9999", cfg.Port)
	}
	if len(cfg.WatchedRoots) != 1 || cfg.WatchedRoots[0] != `C:\Users\jake` {
		t.Errorf("watched_roots = %v", cfg.WatchedRoots)
	}
	// Additive: default globs must still be present alongside the custom one.
	foundDefault := false
	foundCustom := false
	for _, g := range cfg.IgnoreGlobs {
		if g == `**\node_modules\**` {
			foundDefault = true
		}
		if g == `**\custom_ignore\**` {
			foundCustom = true
		}
	}
	if !foundDefault {
		t.Error("expected default ignore glob to remain present (additive)")
	}
	if !foundCustom {
		t.Error("expected custom ignore glob to be added")
	}
	if cfg.AlertThresholds.LargeFileBytes != 1000 {
		t.Errorf("large_file_bytes override = %d, want 1000", cfg.AlertThresholds.LargeFileBytes)
	}
}

func TestParseConfig_EmptyJSONUsesDefaults(t *testing.T) {
	cfg, err := ParseConfig([]byte(`{}`))
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if cfg.Port != 8787 {
		t.Errorf("port = %d, want default 8787", cfg.Port)
	}
	if len(cfg.IgnoreGlobs) != len(DefaultIgnoreGlobs()) {
		t.Errorf("expected default ignore globs, got %v", cfg.IgnoreGlobs)
	}
}

func TestParseConfig_MalformedJSON(t *testing.T) {
	if _, err := ParseConfig([]byte(`{not json`)); err == nil {
		t.Fatal("expected error")
	}
}

func TestLoadConfig_MissingFileReturnsDefaults(t *testing.T) {
	cfg, err := LoadConfig(filepath.Join(t.TempDir(), "does-not-exist.json"))
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if cfg.Port != 8787 {
		t.Errorf("port = %d, want default 8787", cfg.Port)
	}
}

func TestLoadConfig_EmptyPathReturnsDefaults(t *testing.T) {
	cfg, err := LoadConfig("")
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if cfg.Port != 8787 {
		t.Errorf("port = %d, want default 8787", cfg.Port)
	}
}

func TestLoadConfig_ReadsRealFile(t *testing.T) {
	dir := t.TempDir()
	p := filepath.Join(dir, "config.json")
	if err := os.WriteFile(p, []byte(`{"port": 1234}`), 0644); err != nil {
		t.Fatalf("write: %v", err)
	}
	cfg, err := LoadConfig(p)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if cfg.Port != 1234 {
		t.Errorf("port = %d, want 1234", cfg.Port)
	}
}

// Notepad's "UTF-8 with BOM" (and PowerShell 5.1's Set-Content -Encoding
// UTF8) prefix the file with EF BB BF, which encoding/json rejects.
func TestParseConfig_AcceptsUTF8BOM(t *testing.T) {
	cfg, err := ParseConfig([]byte("\xef\xbb\xbf{\"port\": 9999}"))
	if err != nil {
		t.Fatalf("config saved with a UTF-8 BOM was rejected: %v", err)
	}
	if cfg.Port != 9999 {
		t.Errorf("port = %d, want 9999", cfg.Port)
	}
}
