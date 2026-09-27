package agent

import (
	"context"
	"fmt"
	"os"
	"strings"

	gpprocess "github.com/shirou/gopsutil/v3/process"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// ProcessManager lists and kills OS processes via gopsutil, always
// re-checking the core critical-process predicate server-side before
// killing anything — the client's own "protected" hint is never trusted.
type ProcessManager struct {
	// OwnPID is the agent's own process id (core.EvaluateProtection rule 2).
	OwnPID int32
}

func NewProcessManager() *ProcessManager {
	return &ProcessManager{OwnPID: int32(os.Getpid())}
}

// toProcessInfo builds the platform-agnostic core.ProcessInfo used by the
// protection predicate from a live gopsutil process handle.
//
// NOTE (honest limitation, flagged in the final report): resolving a
// process's owner account to a form comparable against
// "NT AUTHORITY\SYSTEM" etc. is a real Windows-specific operation
// (typically OpenProcessToken + LookupAccountSid via WinAPI). gopsutil's
// Process.Username() wraps exactly that on Windows. This code path calls
// Username() as gopsutil provides it, but it has only been exercised on
// Linux in this dev environment (where Username() returns a Linux
// "user\\group"-shaped string, not a real Windows SID lookup) — the actual
// Windows token/SID behavior is unverified here.
func toProcessInfo(p *gpprocess.Process, ctx context.Context) core.ProcessInfo {
	name, _ := p.NameWithContext(ctx)
	owner, _ := p.UsernameWithContext(ctx)
	return core.ProcessInfo{PID: p.Pid, Name: name, Owner: owner}
}

// ProcessListItem is an internal, richer view before it's trimmed down to
// the wire ProcessItem.
type ProcessListItem struct {
	core.ProcessItem
}

// List returns real, live processes from the OS via gopsutil, optionally
// filtered by a case-insensitive substring `query` against the process
// name (PROTOCOL.md §4.5 list_processes).
func (pm *ProcessManager) List(ctx context.Context, query string) ([]core.ProcessItem, error) {
	procs, err := gpprocess.ProcessesWithContext(ctx)
	if err != nil {
		return nil, fmt.Errorf("listing processes: %w", err)
	}
	q := strings.ToLower(strings.TrimSpace(query))
	items := make([]core.ProcessItem, 0, len(procs))
	for _, p := range procs {
		info := toProcessInfo(p, ctx)
		if q != "" && !strings.Contains(strings.ToLower(info.Name), q) {
			continue
		}
		exe, _ := p.ExeWithContext(ctx)
		cpuPct, _ := p.CPUPercentWithContext(ctx)
		memInfo, _ := p.MemoryInfoWithContext(ctx)
		var ramBytes uint64
		if memInfo != nil {
			ramBytes = memInfo.RSS
		}
		items = append(items, core.ProcessItem{
			PID:        info.PID,
			Name:       info.Name,
			Exe:        exe,
			User:       info.Owner,
			CPUPercent: cpuPct,
			RAMBytes:   ramBytes,
			Protected:  core.IsProtected(info, pm.OwnPID),
		})
	}
	return items, nil
}

// KillResult is the outcome of a kill attempt.
type KillResult struct {
	Success bool
	Reason  string // core.Reason* constant when !Success
}

// Kill enforces PROTOCOL.md §5 unconditionally, server-side, on every
// call — it re-derives the process's own name/owner/pid from the live OS
// process table and re-evaluates core.IsProtected itself; it never trusts
// any client-supplied "protected" flag (there isn't one on kill_process,
// by design, but this is the enforcement point regardless of what a
// modified/malicious client might send).
func (pm *ProcessManager) Kill(ctx context.Context, pid int32) KillResult {
	p, err := gpprocess.NewProcessWithContext(ctx, pid)
	if err != nil {
		return KillResult{Success: false, Reason: core.ReasonNotFound}
	}
	exists, err := p.IsRunningWithContext(ctx)
	if err != nil || !exists {
		return KillResult{Success: false, Reason: core.ReasonNotFound}
	}

	info := toProcessInfo(p, ctx)
	if core.IsProtected(info, pm.OwnPID) {
		return KillResult{Success: false, Reason: core.ReasonCriticalProcessProtected}
	}

	if err := p.KillWithContext(ctx); err != nil {
		return KillResult{Success: false, Reason: core.ReasonAccessDenied}
	}
	return KillResult{Success: true}
}
