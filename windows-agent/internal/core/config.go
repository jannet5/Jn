package core

import (
	"encoding/json"
	"fmt"
	"os"
)

// AlertThresholds mirrors PROTOCOL.md §8 (defaults, configurable).
type AlertThresholds struct {
	LargeFileBytes        int64   `json:"large_file_bytes"`
	FastGrowthBytes       int64   `json:"fast_growth_bytes"`
	FastGrowthWindowSec   int64   `json:"fast_growth_window_sec"`
	DiskFillRatePercent   float64 `json:"disk_fill_rate_percent"`
	DiskFillRateWindowSec int64   `json:"disk_fill_rate_window_sec"`
	LowFreeSpacePercent   float64 `json:"low_free_space_percent"`
}

// DefaultAlertThresholds returns the exact defaults from PROTOCOL.md §8.
func DefaultAlertThresholds() AlertThresholds {
	return AlertThresholds{
		LargeFileBytes:        500 * 1024 * 1024,       // 500 MiB
		FastGrowthBytes:       200 * 1024 * 1024,       // 200 MiB
		FastGrowthWindowSec:   10 * 60,                 // 10 minutes
		DiskFillRatePercent:   5.0,                      // 5%
		DiskFillRateWindowSec: 15 * 60,                 // 15 minutes
		LowFreeSpacePercent:   10.0,                     // 10%
	}
}

// DefaultIgnoreGlobs is PROTOCOL.md §7's default-ignored path glob set.
// NOTE (spec ambiguity, flagged in README/report): the spec sentence
// "*.tmp, *.log churn is throttled (see §8)" doesn't parse cleanly against
// §8 (which defines alert thresholds, not a throttle mechanism). This
// implementation's interpretation: *.tmp and *.log are ordinary additional
// ignore globs, AND rapid repeated events for the same path are separately
// debounced/batched by the file watcher (an event-rate throttle) as a
// defensive measure, independent of the noise-filter glob list itself.
func DefaultIgnoreGlobs() []string {
	return []string{
		`**\node_modules\**`,
		`**\.git\**`,
		`**\AppData\Local\Temp\**`,
		`**\$Recycle.Bin\**`,
		`**\System Volume Information\**`,
		`*.tmp`,
		`*.log`,
		`**\AppData\Local\Packages\**\TempState\**`,
	}
}

// Config is the full agent configuration (config.json).
type Config struct {
	Port            int             `json:"port"`
	WatchedRoots    []string        `json:"watched_roots"`
	IgnoreGlobs     []string        `json:"ignore_globs"`
	AlertThresholds AlertThresholds `json:"alert_thresholds"`
	AllowedAppsPath string          `json:"allowed_apps_path"`
	DataDir         string          `json:"data_dir"`
	MetricsIntervalSec  int         `json:"metrics_interval_sec"`
	ProcessesIntervalSec int        `json:"processes_interval_sec"`
	SnapshotIntervalSec  int        `json:"snapshot_interval_sec"`
}

// DefaultConfig returns sane defaults matching the protocol's defaults.
func DefaultConfig() Config {
	return Config{
		Port:                 8787,
		WatchedRoots:         nil,
		IgnoreGlobs:          DefaultIgnoreGlobs(),
		AlertThresholds:      DefaultAlertThresholds(),
		AllowedAppsPath:      "allowed_apps.json",
		DataDir:              ".",
		MetricsIntervalSec:   2,
		ProcessesIntervalSec: 5,
		SnapshotIntervalSec:  60,
	}
}

// ParseConfig parses config.json content, filling in defaults for any
// zero-valued field and appending (never replacing) DefaultIgnoreGlobs per
// PROTOCOL.md §7 ("configurable, additive").
func ParseConfig(data []byte) (Config, error) {
	cfg := DefaultConfig()
	// Decode into a shadow struct so we can tell an explicit empty list
	// ("[]") apart from an absent field, while keeping additive semantics
	// for ignore_globs as required by the spec.
	var raw struct {
		Port                 *int             `json:"port"`
		WatchedRoots         []string         `json:"watched_roots"`
		IgnoreGlobs          []string         `json:"ignore_globs"`
		AlertThresholds      *AlertThresholds `json:"alert_thresholds"`
		AllowedAppsPath      *string          `json:"allowed_apps_path"`
		DataDir              *string          `json:"data_dir"`
		MetricsIntervalSec   *int             `json:"metrics_interval_sec"`
		ProcessesIntervalSec *int             `json:"processes_interval_sec"`
		SnapshotIntervalSec  *int             `json:"snapshot_interval_sec"`
	}
	if err := json.Unmarshal(data, &raw); err != nil {
		return Config{}, fmt.Errorf("parsing config JSON: %w", err)
	}
	if raw.Port != nil {
		cfg.Port = *raw.Port
	}
	if raw.WatchedRoots != nil {
		cfg.WatchedRoots = raw.WatchedRoots
	}
	if raw.IgnoreGlobs != nil {
		cfg.IgnoreGlobs = append(append([]string{}, DefaultIgnoreGlobs()...), raw.IgnoreGlobs...)
	}
	if raw.AlertThresholds != nil {
		cfg.AlertThresholds = *raw.AlertThresholds
	}
	if raw.AllowedAppsPath != nil {
		cfg.AllowedAppsPath = *raw.AllowedAppsPath
	}
	if raw.DataDir != nil {
		cfg.DataDir = *raw.DataDir
	}
	if raw.MetricsIntervalSec != nil {
		cfg.MetricsIntervalSec = *raw.MetricsIntervalSec
	}
	if raw.ProcessesIntervalSec != nil {
		cfg.ProcessesIntervalSec = *raw.ProcessesIntervalSec
	}
	if raw.SnapshotIntervalSec != nil {
		cfg.SnapshotIntervalSec = *raw.SnapshotIntervalSec
	}
	return cfg, nil
}

// LoadConfig reads config.json from disk, or returns defaults if path is
// empty or the file does not exist (first-run friendliness).
func LoadConfig(path string) (Config, error) {
	if path == "" {
		return DefaultConfig(), nil
	}
	data, err := os.ReadFile(path)
	if err != nil {
		if os.IsNotExist(err) {
			return DefaultConfig(), nil
		}
		return Config{}, fmt.Errorf("reading config %s: %w", path, err)
	}
	return ParseConfig(data)
}
