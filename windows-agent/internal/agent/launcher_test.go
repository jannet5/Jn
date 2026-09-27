package agent

import (
	"testing"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

func testAllowList(t *testing.T, apps []core.AllowedApp) *core.AllowList {
	al, err := core.NewAllowList(apps)
	if err != nil {
		t.Fatalf("unexpected error building allow-list: %v", err)
	}
	return al
}

func TestLauncher_LaunchesRealAllowListedProcess(t *testing.T) {
	// /bin/true (or /usr/bin/true) is a real, always-present binary on
	// this Linux dev box, standing in for e.g. notepad.exe on Windows. The
	// point being tested is the allow-list gate + real os/exec.Start code
	// path, not any particular binary's behavior.
	al := testAllowList(t, []core.AllowedApp{
		{AppID: "truthy", Label: "True", Path: "/bin/true"},
	})
	l := &Launcher{AllowList: al}
	result := l.Launch("truthy")
	if !result.Success {
		t.Fatalf("expected launch to succeed, got reason=%q", result.Reason)
	}
	if result.PID <= 0 {
		t.Errorf("expected a positive real pid, got %d", result.PID)
	}
}

func TestLauncher_RejectsNonAllowListedAppID(t *testing.T) {
	al := testAllowList(t, []core.AllowedApp{
		{AppID: "truthy", Label: "True", Path: "/bin/true"},
	})
	l := &Launcher{AllowList: al}
	result := l.Launch("not-in-the-list")
	if result.Success {
		t.Fatal("launching a non-allow-listed app_id must be refused")
	}
	if result.Reason != core.ReasonNotAllowListed {
		t.Errorf("reason = %q, want %q", result.Reason, core.ReasonNotAllowListed)
	}
}

func TestLauncher_RejectsPathInjectionViaAppID(t *testing.T) {
	al := testAllowList(t, []core.AllowedApp{
		{AppID: "truthy", Label: "True", Path: "/bin/true"},
	})
	l := &Launcher{AllowList: al}
	// A malicious/modified client sending a raw path as app_id must never
	// resolve, since Resolve is keyed strictly by configured app_id.
	result := l.Launch("/bin/false")
	if result.Success {
		t.Fatal("a raw path used as app_id must never launch")
	}
	if result.Reason != core.ReasonNotAllowListed {
		t.Errorf("reason = %q, want %q", result.Reason, core.ReasonNotAllowListed)
	}
}

func TestLauncher_LaunchFailedForBadPath(t *testing.T) {
	al := testAllowList(t, []core.AllowedApp{
		{AppID: "broken", Label: "Broken", Path: "/definitely/does/not/exist/binary"},
	})
	l := &Launcher{AllowList: al}
	result := l.Launch("broken")
	if result.Success {
		t.Fatal("launching a nonexistent binary must fail")
	}
	if result.Reason != core.ReasonLaunchFailed {
		t.Errorf("reason = %q, want %q", result.Reason, core.ReasonLaunchFailed)
	}
}
