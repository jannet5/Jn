package agent

import (
	"context"
	"errors"
	"os"
	"strings"
	"testing"
	"time"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// These tests exercise the real gopsutil process code path against the
// actual test machine (this Linux container running `go test`). They are
// honest verification of that code path with real live data, not mocks.

func TestProcessManager_List_RealListIncludesOwnPID(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	pm := NewProcessManager()
	items, err := pm.List(ctx, "")
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(items) == 0 {
		t.Fatal("expected at least one real process to be returned")
	}
	found := false
	for _, it := range items {
		if it.PID == int32(os.Getpid()) {
			found = true
			break
		}
	}
	if !found {
		t.Errorf("expected this test process's own pid (%d) to be present in the real process list", os.Getpid())
	}
}

func TestProcessManager_List_QueryFiltersByName(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	pm := NewProcessManager()
	all, err := pm.List(ctx, "")
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(all) == 0 {
		t.Fatal("expected at least one process")
	}
	// Query using this test process's own real (lowercased/partial) name.
	self, err := findSelf(all)
	if err != nil {
		t.Fatalf("could not find self in process list: %v", err)
	}
	if len(self.Name) < 2 {
		t.Skip("self process name too short to build a meaningful substring query")
	}
	query := self.Name[:2]
	filtered, err := pm.List(ctx, query)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	for _, it := range filtered {
		if !strings.Contains(strings.ToLower(it.Name), strings.ToLower(query)) {
			t.Errorf("filtered result %q does not contain query %q", it.Name, query)
		}
	}
}

func TestProcessManager_Kill_RefusesOwnPID(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	pm := NewProcessManager()
	result := pm.Kill(ctx, pm.OwnPID)
	if result.Success {
		t.Fatal("killing the agent's own pid must be refused")
	}
	if result.Reason != core.ReasonCriticalProcessProtected {
		t.Errorf("reason = %q, want %q", result.Reason, core.ReasonCriticalProcessProtected)
	}
}

func TestProcessManager_Kill_RefusesLowPID(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	pm := NewProcessManager()
	// PID 1 (init/systemd on Linux; analogous to a protected low PID on
	// Windows too, though the *specific* PIDs 0/4 are Windows concepts).
	result := pm.Kill(ctx, 1)
	if result.Success {
		t.Fatal("killing pid 1 must be refused")
	}
	if result.Reason != core.ReasonCriticalProcessProtected {
		t.Errorf("reason = %q, want %q", result.Reason, core.ReasonCriticalProcessProtected)
	}
}

func TestProcessManager_Kill_NotFoundForBogusPID(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	pm := NewProcessManager()
	result := pm.Kill(ctx, 999999)
	if result.Success {
		t.Fatal("killing a nonexistent pid must not succeed")
	}
	if result.Reason != core.ReasonNotFound {
		t.Errorf("reason = %q, want %q", result.Reason, core.ReasonNotFound)
	}
}

// -- helpers --

var errSelfNotFound = errors.New("self process not found in list")

func findSelf(items []core.ProcessItem) (core.ProcessItem, error) {
	pid := int32(os.Getpid())
	for _, it := range items {
		if it.PID == pid {
			return it, nil
		}
	}
	return core.ProcessItem{}, errSelfNotFound
}
