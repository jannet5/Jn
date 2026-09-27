package core

import "strings"

// ProcessInfo is a minimal, platform-agnostic view of a process used only
// to evaluate the critical-process protection predicate. Production code
// builds this from gopsutil; tests build it directly by hand, which is what
// makes IsProtected trivially and exhaustively unit-testable.
type ProcessInfo struct {
	PID   int32
	Name  string // executable base name, e.g. "explorer.exe"
	Owner string // e.g. "NT AUTHORITY\\SYSTEM", "DESKTOP\\jake"
}

// criticalNames is the hardcoded set from PROTOCOL.md §5 rule 4. Matching is
// case-insensitive on the executable base name.
var criticalNames = map[string]struct{}{
	"system":               {},
	"smss.exe":             {},
	"csrss.exe":            {},
	"wininit.exe":          {},
	"winlogon.exe":         {},
	"services.exe":         {},
	"lsass.exe":            {},
	"lsaiso.exe":           {},
	"svchost.exe":          {},
	"fontdrvhost.exe":      {},
	"dwm.exe":              {},
	"explorer.exe":         {},
	"registry":             {},
	"memory compression":   {},
	"wudfhost.exe":         {},
	"sihost.exe":           {},
	"ctfmon.exe":           {},
}

// criticalOwners is the hardcoded set of protected owner accounts from
// PROTOCOL.md §5 rule 3. Comparison is case-insensitive.
var criticalOwners = map[string]struct{}{
	"nt authority\\system":         {},
	"nt authority\\local service":  {},
	"nt authority\\network service": {},
}

// ProtectionReason describes which rule caused a process to be protected.
// Callers that need only a bool can use IsProtected; ProtectionReason is
// exposed so both the server (for logging/history detail) and tests (for
// asserting *why* something is protected) can use it.
type ProtectionReason int

const (
	NotProtected ProtectionReason = iota
	ProtectedLowPID
	ProtectedOwnPID
	ProtectedOwnerAccount
	ProtectedNameHardcoded
)

// EvaluateProtection implements PROTOCOL.md §5 exactly. ownPID is the
// agent's own process id (rule 2).
func EvaluateProtection(p ProcessInfo, ownPID int32) ProtectionReason {
	if p.PID <= 8 {
		return ProtectedLowPID
	}
	if p.PID == ownPID {
		return ProtectedOwnPID
	}
	if _, ok := criticalOwners[strings.ToLower(strings.TrimSpace(p.Owner))]; ok {
		return ProtectedOwnerAccount
	}
	if _, ok := criticalNames[strings.ToLower(strings.TrimSpace(p.Name))]; ok {
		return ProtectedNameHardcoded
	}
	return NotProtected
}

// IsProtected reports whether a kill_process for p must be refused.
func IsProtected(p ProcessInfo, ownPID int32) bool {
	return EvaluateProtection(p, ownPID) != NotProtected
}
