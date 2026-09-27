package core

import "testing"

func TestCheckLargeFile(t *testing.T) {
	threshold := int64(500 * 1024 * 1024)
	if trig, _ := CheckLargeFile(threshold-1, threshold); trig {
		t.Error("just under threshold must not trigger")
	}
	trig, sev := CheckLargeFile(threshold, threshold)
	if !trig || sev != SeverityWarning {
		t.Errorf("at threshold: trig=%v sev=%v, want true/warning", trig, sev)
	}
	trig, sev = CheckLargeFile(threshold*2, threshold)
	if !trig || sev != SeverityCritical {
		t.Errorf("at 2x threshold: trig=%v sev=%v, want true/critical", trig, sev)
	}
}

func TestCheckFastGrowth_TriggersOnlyWithinWindow(t *testing.T) {
	now := int64(1_000_000)
	windowSec := int64(600) // 10 minutes
	threshold := int64(200 * 1024 * 1024)

	samples := []SizeSample{
		{TS: now - 1200, SizeBytes: 0}, // outside window, ignored
		{TS: now - 300, SizeBytes: 10 * 1024 * 1024},
	}
	current := int64(10*1024*1024 + threshold + 1)
	trig, delta, sev := CheckFastGrowth(samples, current, now, windowSec, threshold)
	if !trig {
		t.Fatal("expected fast_growth to trigger")
	}
	if delta != threshold+1 {
		t.Errorf("delta = %d, want %d", delta, threshold+1)
	}
	if sev != SeverityWarning {
		t.Errorf("severity = %q, want warning", sev)
	}
}

func TestCheckFastGrowth_NoSampleInWindow(t *testing.T) {
	now := int64(1_000_000)
	samples := []SizeSample{{TS: now - 10000, SizeBytes: 0}}
	trig, _, _ := CheckFastGrowth(samples, 999999999, now, 600, 200*1024*1024)
	if trig {
		t.Error("must not trigger when no baseline sample exists within the window")
	}
}

func TestCheckFastGrowth_BelowThresholdDoesNotTrigger(t *testing.T) {
	now := int64(1_000_000)
	samples := []SizeSample{{TS: now - 100, SizeBytes: 1000}}
	trig, delta, _ := CheckFastGrowth(samples, 1500, now, 600, 200*1024*1024)
	if trig {
		t.Error("small growth must not trigger")
	}
	if delta != 500 {
		t.Errorf("delta = %d, want 500", delta)
	}
}

func TestCheckDiskFillRate_Triggers(t *testing.T) {
	now := int64(1_000_000)
	windowSec := int64(900) // 15 minutes
	totalBytes := int64(1000 * 1024 * 1024 * 1024) // 1000 GiB
	thresholdPercent := 5.0

	history := []FreeSpaceSample{
		{TS: now - 800, FreeBytes: 500 * 1024 * 1024 * 1024},
	}
	// Lose 6% of total free space (60 GiB) -> should trigger (> 5%).
	currentFree := int64(440 * 1024 * 1024 * 1024)
	trig, lossPct, sev := CheckDiskFillRate(history, currentFree, totalBytes, now, windowSec, thresholdPercent)
	if !trig {
		t.Fatal("expected disk_fill_rate to trigger")
	}
	if lossPct < 5.9 || lossPct > 6.1 {
		t.Errorf("lossPct = %v, want ~6.0", lossPct)
	}
	if sev != SeverityWarning {
		t.Errorf("severity = %q, want warning", sev)
	}
}

func TestCheckDiskFillRate_GainingFreeSpaceDoesNotTrigger(t *testing.T) {
	now := int64(1_000_000)
	history := []FreeSpaceSample{{TS: now - 100, FreeBytes: 100}}
	trig, _, _ := CheckDiskFillRate(history, 200, 1000, now, 900, 5.0)
	if trig {
		t.Error("gaining free space must not trigger disk_fill_rate")
	}
}

func TestCheckDiskFillRate_NoBaselineInWindow(t *testing.T) {
	now := int64(1_000_000)
	history := []FreeSpaceSample{{TS: now - 10000, FreeBytes: 100}}
	trig, _, _ := CheckDiskFillRate(history, 1, 1000, now, 900, 5.0)
	if trig {
		t.Error("must not trigger without a baseline sample in the window")
	}
}

func TestCheckLowFreeSpace_Triggers(t *testing.T) {
	total := uint64(1000)
	free := uint64(50) // 5% free, threshold 10%
	trig, pct, sev := CheckLowFreeSpace(total, free, 10.0)
	if !trig {
		t.Fatal("expected low_free_space to trigger at 5% free with 10% threshold")
	}
	if pct != 5.0 {
		t.Errorf("pct = %v, want 5.0", pct)
	}
	if sev != SeverityCritical {
		t.Errorf("severity = %q, want critical (well below threshold)", sev)
	}
}

func TestCheckLowFreeSpace_JustBelowThresholdIsWarning(t *testing.T) {
	total := uint64(1000)
	free := uint64(95) // 9.5% free, threshold 10% -> just below, warning
	trig, _, sev := CheckLowFreeSpace(total, free, 10.0)
	if !trig {
		t.Fatal("expected trigger just below threshold")
	}
	if sev != SeverityWarning {
		t.Errorf("severity = %q, want warning", sev)
	}
}

func TestCheckLowFreeSpace_AboveThresholdDoesNotTrigger(t *testing.T) {
	total := uint64(1000)
	free := uint64(200) // 20% free
	trig, _, _ := CheckLowFreeSpace(total, free, 10.0)
	if trig {
		t.Error("must not trigger when free space is above threshold")
	}
}

func TestCheckLowFreeSpace_ZeroTotalIsSafe(t *testing.T) {
	trig, _, _ := CheckLowFreeSpace(0, 0, 10.0)
	if trig {
		t.Error("zero-capacity volume must not trigger (avoid div-by-zero/false alarms)")
	}
}
