package agent

import (
	"os/exec"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// Launcher launches allow-listed applications only. It always re-resolves
// app_id against the server-side allow-list (core.AllowList) before ever
// touching os/exec — a client can never supply a raw path, and there is no
// protocol message that would let it try (PROTOCOL.md §6).
type Launcher struct {
	AllowList *core.AllowList
}

// LaunchResult is the outcome of a launch attempt.
type LaunchResult struct {
	Success bool
	PID     int32
	Reason  string // core.Reason* constant when !Success
}

// Launch resolves appID against the allow-list and, only if found, execs
// the configured path. It never accepts a path from the caller.
func (l *Launcher) Launch(appID string) LaunchResult {
	app, ok := l.AllowList.Resolve(appID)
	if !ok {
		return LaunchResult{Success: false, Reason: core.ReasonNotAllowListed}
	}
	cmd := exec.Command(app.Path)
	if err := cmd.Start(); err != nil {
		return LaunchResult{Success: false, Reason: core.ReasonLaunchFailed}
	}
	pid := int32(cmd.Process.Pid)
	// Detach: we don't want to wait on or own the child's lifecycle beyond
	// launching it (it's a normal user app, not a supervised subprocess).
	go func() { _ = cmd.Wait() }()
	return LaunchResult{Success: true, PID: pid}
}
