package agent

import (
	"path/filepath"
	"testing"
	"time"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// TestPairingCode_SurvivesAcrossSeparateProcesses is the regression test
// for a real bug found while trying to actually run this agent
// end-to-end: `agent --pair` and the long-running `agent` server are two
// separate OS processes. Before this fix, core.PairingManager held its
// codes in an in-memory map, so a code issued by a `--pair` invocation
// could never be seen by the already-running (or later-started) server
// process that has to validate the phone's pair_request against it -
// pairing could never actually succeed in real use. It only worked in
// prior tests because they always issued and validated against the same
// in-process PairingManager, which no real deployment ever does.
//
// This test simulates the real two-process scenario without needing two
// actual OS processes: it opens a Store (process A's), issues a code,
// closes that Store (process A exits, as `agent --pair` always does), then
// opens a brand-new Store instance against the SAME sqlite file (process
// B - the long-running server) and validates the code against it.
func TestPairingCode_SurvivesAcrossSeparateProcesses(t *testing.T) {
	dbPath := filepath.Join(t.TempDir(), "agent.db")
	now := time.Now()

	// --- "process A": `agent --pair` ---
	storeA, err := OpenStore(dbPath)
	if err != nil {
		t.Fatalf("opening store (process A): %v", err)
	}
	pmA := core.NewPairingManager(storeA)
	code, err := pmA.IssueCode(now)
	if err != nil {
		t.Fatalf("issuing code: %v", err)
	}
	if err := storeA.Close(); err != nil {
		t.Fatalf("closing store A: %v", err)
	}

	// --- "process B": the long-running `agent` server, started separately ---
	storeB, err := OpenStore(dbPath)
	if err != nil {
		t.Fatalf("opening store (process B): %v", err)
	}
	defer storeB.Close()
	pmB := core.NewPairingManager(storeB)

	outcome := pmB.Attempt("conn-from-phone", code, now.Add(time.Second))
	if outcome != core.PairOK {
		t.Fatalf("pairing code issued by a separate process must be accepted by this process's PairingManager, got outcome=%v (want PairOK)", outcome)
	}

	// And it's still correctly single-use across processes too.
	storeC, err := OpenStore(dbPath)
	if err != nil {
		t.Fatalf("opening store (process C): %v", err)
	}
	defer storeC.Close()
	pmC := core.NewPairingManager(storeC)
	replay := pmC.Attempt("conn-replay", code, now.Add(2*time.Second))
	if replay == core.PairOK {
		t.Fatal("a code already consumed by another process must not be re-usable")
	}
}
