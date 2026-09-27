package core

import "testing"

const ownPID int32 = 5000

func TestIsProtected_LowPIDs(t *testing.T) {
	for pid := int32(0); pid <= 8; pid++ {
		p := ProcessInfo{PID: pid, Name: "whatever.exe", Owner: "DESKTOP\\jake"}
		if !IsProtected(p, ownPID) {
			t.Errorf("pid %d should be protected (rule 1)", pid)
		}
		if got := EvaluateProtection(p, ownPID); got != ProtectedLowPID {
			t.Errorf("pid %d: reason = %v, want ProtectedLowPID", pid, got)
		}
	}
}

func TestIsProtected_OwnPID(t *testing.T) {
	p := ProcessInfo{PID: ownPID, Name: "agent.exe", Owner: "DESKTOP\\jake"}
	if !IsProtected(p, ownPID) {
		t.Error("agent's own pid should be protected")
	}
	if got := EvaluateProtection(p, ownPID); got != ProtectedOwnPID {
		t.Errorf("reason = %v, want ProtectedOwnPID", got)
	}
}

func TestIsProtected_OwnerAccounts(t *testing.T) {
	owners := []string{
		"NT AUTHORITY\\SYSTEM",
		"nt authority\\system",
		"NT AUTHORITY\\LOCAL SERVICE",
		"NT AUTHORITY\\NETWORK SERVICE",
		"  NT AUTHORITY\\SYSTEM  ",
	}
	for _, owner := range owners {
		p := ProcessInfo{PID: 9999, Name: "somehost.exe", Owner: owner}
		if !IsProtected(p, ownPID) {
			t.Errorf("owner %q should be protected", owner)
		}
	}
}

func TestIsProtected_HardcodedNames(t *testing.T) {
	names := []string{
		"system", "smss.exe", "csrss.exe", "wininit.exe", "winlogon.exe",
		"services.exe", "lsass.exe", "lsaiso.exe", "svchost.exe",
		"fontdrvhost.exe", "dwm.exe", "explorer.exe", "registry",
		"memory compression", "wudfhost.exe", "sihost.exe", "ctfmon.exe",
		// case-insensitivity
		"Explorer.EXE", "SVCHOST.EXE", "DWM.exe",
	}
	for _, name := range names {
		p := ProcessInfo{PID: 9999, Name: name, Owner: "DESKTOP\\jake"}
		if !IsProtected(p, ownPID) {
			t.Errorf("name %q should be protected", name)
		}
	}
}

func TestIsProtected_NormalUserProcessesAreNotProtected(t *testing.T) {
	cases := []ProcessInfo{
		{PID: 4242, Name: "chrome.exe", Owner: "DESKTOP\\jake"},
		{PID: 1000, Name: "notepad.exe", Owner: "DESKTOP\\jake"},
		{PID: 5555, Name: "steam.exe", Owner: "DESKTOP-ABC\\jake"},
		{PID: 100000, Name: "spotify.exe", Owner: "DESKTOP\\otheruser"},
		{PID: 9, Name: "something.exe", Owner: "DESKTOP\\jake"}, // pid 9, just above the <=8 boundary
	}
	for _, p := range cases {
		if IsProtected(p, ownPID) {
			t.Errorf("process %+v should NOT be protected", p)
		}
		if got := EvaluateProtection(p, ownPID); got != NotProtected {
			t.Errorf("process %+v: reason = %v, want NotProtected", p, got)
		}
	}
}

func TestIsProtected_BoundaryPID8And9(t *testing.T) {
	p8 := ProcessInfo{PID: 8, Name: "x.exe", Owner: "DESKTOP\\jake"}
	p9 := ProcessInfo{PID: 9, Name: "x.exe", Owner: "DESKTOP\\jake"}
	if !IsProtected(p8, ownPID) {
		t.Error("pid 8 should be protected")
	}
	if IsProtected(p9, ownPID) {
		t.Error("pid 9 should NOT be protected purely by pid rule")
	}
}

func TestIsProtected_UnrelatedOwnPIDDoesNotFalselyProtect(t *testing.T) {
	// A normal process that happens to share no relation with ownPID.
	p := ProcessInfo{PID: ownPID + 1, Name: "game.exe", Owner: "DESKTOP\\jake"}
	if IsProtected(p, ownPID) {
		t.Error("unrelated process should not be protected")
	}
}
