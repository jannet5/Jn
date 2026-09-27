package agent

import (
	"fmt"
	"os"
	"path/filepath"
	"runtime"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// DataDir returns the directory the agent persists its state in:
// %ProgramData%\WinRemoteMonitor on Windows (PROTOCOL.md §1), or a
// same-shaped fallback under the user's home directory on any other OS
// (this dev/test environment is Linux; there is no %ProgramData% here).
func DataDir(override string) string {
	if override != "" {
		return override
	}
	if runtime.GOOS == "windows" {
		base := os.Getenv("ProgramData")
		if base == "" {
			base = `C:\ProgramData`
		}
		return filepath.Join(base, "WinRemoteMonitor")
	}
	home, err := os.UserHomeDir()
	if err != nil {
		home = "."
	}
	return filepath.Join(home, ".winremotemonitor")
}

func CertPath(dataDir string) string      { return filepath.Join(dataDir, "agent-cert.pem") }
func KeyPath(dataDir string) string       { return filepath.Join(dataDir, "agent-key.pem") }
func ServerKeyPath(dataDir string) string { return filepath.Join(dataDir, "server.key") }
func DBPath(dataDir string) string        { return filepath.Join(dataDir, "agent.db") }

// EnsureServerKey loads the persisted server-local key used to protect
// device secrets at rest (core.EncryptDeviceSecret/HashDeviceSecret),
// generating and persisting one on first run.
func EnsureServerKey(path string) ([]byte, error) {
	if data, err := os.ReadFile(path); err == nil {
		if len(data) != 32 {
			return nil, fmt.Errorf("server key at %s has unexpected length %d (want 32)", path, len(data))
		}
		return data, nil
	}
	key, err := core.GenerateServerKey()
	if err != nil {
		return nil, err
	}
	if err := os.MkdirAll(filepath.Dir(path), 0700); err != nil {
		return nil, fmt.Errorf("creating data dir: %w", err)
	}
	if err := os.WriteFile(path, key, 0600); err != nil {
		return nil, fmt.Errorf("writing server key: %w", err)
	}
	return key, nil
}
