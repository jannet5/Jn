package agent

import (
	"path/filepath"
	"testing"
	"time"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

func newTestRuntime(t *testing.T) (*Runtime, *Store) {
	t.Helper()
	dir := t.TempDir()
	store, err := OpenStore(filepath.Join(dir, "rt.db"))
	if err != nil {
		t.Fatalf("opening store: %v", err)
	}
	t.Cleanup(func() { store.Close() })

	allowList, _ := core.NewAllowList(nil)
	serverKey, _ := core.GenerateServerKey()
	srv := NewServer(core.DefaultConfig(), store, allowList, serverKey)

	rt := &Runtime{
		Server:     srv,
		Store:      store,
		Thresholds: core.DefaultAlertThresholds(),
	}
	return rt, store
}

func TestRuntime_OnFileEvent_StoresAndRaisesLargeFileAlert(t *testing.T) {
	rt, store := newTestRuntime(t)
	rt.Thresholds.LargeFileBytes = 1000

	now := time.Now().Unix()
	rt.OnFileEvent(core.FileEventRecord{TS: now, Op: core.OpCreated, Path: `C:\Users\jake\big.zip`, SizeBytes: 5000, IsDir: false})

	events, total, err := store.QueryFileEvents(FileEventQuery{FromTS: 0, ToTS: now + 10, Limit: 10})
	if err != nil {
		t.Fatalf("querying file events: %v", err)
	}
	if total != 1 || len(events) != 1 {
		t.Fatalf("expected 1 stored file event, got total=%d items=%d", total, len(events))
	}

	alerts, err := store.ListAlerts(10)
	if err != nil {
		t.Fatalf("listing alerts: %v", err)
	}
	if len(alerts) != 1 {
		t.Fatalf("expected 1 alert, got %d", len(alerts))
	}
	if alerts[0].Kind != core.AlertLargeFile {
		t.Errorf("kind = %q, want large_file", alerts[0].Kind)
	}
}

func TestRuntime_OnFileEvent_SmallFileNoAlert(t *testing.T) {
	rt, store := newTestRuntime(t)
	rt.Thresholds.LargeFileBytes = 1000

	rt.OnFileEvent(core.FileEventRecord{TS: time.Now().Unix(), Op: core.OpCreated, Path: `C:\small.txt`, SizeBytes: 10, IsDir: false})

	alerts, err := store.ListAlerts(10)
	if err != nil {
		t.Fatalf("listing alerts: %v", err)
	}
	if len(alerts) != 0 {
		t.Errorf("expected no alerts for a small file, got %d", len(alerts))
	}
}

func TestRuntime_EvaluateFastGrowth_TriggersAcrossSnapshots(t *testing.T) {
	rt, store := newTestRuntime(t)
	rt.Thresholds.FastGrowthBytes = 100
	rt.Thresholds.FastGrowthWindowSec = 600

	now := time.Now().Unix()
	baselineTS := now - 60
	if err := store.InsertSnapshot(baselineTS, []core.SizeSnapshot{
		{Path: `C:\Videos`, IsDir: true, SizeBytes: 10},
	}); err != nil {
		t.Fatalf("inserting baseline snapshot: %v", err)
	}

	current := []core.SizeSnapshot{{Path: `C:\Videos`, IsDir: true, SizeBytes: 500}}
	rt.evaluateFastGrowth(now, current)

	alerts, err := store.ListAlerts(10)
	if err != nil {
		t.Fatalf("listing alerts: %v", err)
	}
	if len(alerts) != 1 || alerts[0].Kind != core.AlertFastGrowth {
		t.Fatalf("expected 1 fast_growth alert, got %+v", alerts)
	}
}

func TestRuntime_RecordDiskHistoryAndEvaluate_LowFreeSpace(t *testing.T) {
	rt, store := newTestRuntime(t)
	rt.Thresholds.LowFreeSpacePercent = 10.0

	snap := core.MetricsPush{
		TS: time.Now().Unix(),
		Disks: []core.DiskInfo{
			{Volume: `C:\`, TotalBytes: 1000, UsedBytes: 950, FreeBytes: 50}, // 5% free
		},
	}
	rt.recordDiskHistoryAndEvaluate(snap)

	alerts, err := store.ListAlerts(10)
	if err != nil {
		t.Fatalf("listing alerts: %v", err)
	}
	found := false
	for _, a := range alerts {
		if a.Kind == core.AlertLowFreeSpace {
			found = true
		}
	}
	if !found {
		t.Errorf("expected a low_free_space alert, got %+v", alerts)
	}

	history, err := store.DiskFreeHistory(`C:\`, 0)
	if err != nil {
		t.Fatalf("reading disk history: %v", err)
	}
	if len(history) != 1 {
		t.Errorf("expected disk free sample to be recorded, got %d", len(history))
	}
}
