package agent

import (
	"context"
	"testing"
	"time"
)

// These tests call the real gopsutil code path against this actual test
// machine (Linux, in this dev environment). They are honest verification
// of the code path itself: the numbers are real numbers from the machine
// running `go test`, not mocks. When cross-compiled for Windows and run
// there, the exact same code reports real Windows numbers instead.

func TestSampleCPUPercent_RealValueInRange(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	m := &MetricsSampler{}
	pct, err := m.SampleCPUPercent(ctx)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if pct < 0 || pct > 100 {
		t.Errorf("cpu percent = %v, want in [0, 100]", pct)
	}
}

func TestSampleRAM_RealTotalIsPositive(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	m := &MetricsSampler{}
	ram, err := m.SampleRAM(ctx)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if ram.TotalBytes == 0 {
		t.Error("expected total RAM > 0 on the real test machine")
	}
	if ram.UsedBytes > ram.TotalBytes {
		t.Errorf("used (%d) > total (%d), impossible", ram.UsedBytes, ram.TotalBytes)
	}
}

func TestSampleDisks_RealRootVolume(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	m := &MetricsSampler{Volumes: []string{"/"}}
	disks, err := m.SampleDisks(ctx)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(disks) != 1 {
		t.Fatalf("expected 1 disk entry for \"/\", got %d", len(disks))
	}
	d := disks[0]
	if d.TotalBytes == 0 {
		t.Error("expected total bytes > 0 for real root volume")
	}
	if d.FreeBytes > d.TotalBytes {
		t.Errorf("free (%d) > total (%d), impossible", d.FreeBytes, d.TotalBytes)
	}
}

func TestSampleDisks_SkipsInaccessibleVolume(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	m := &MetricsSampler{Volumes: []string{"/definitely-does-not-exist-xyz"}}
	disks, err := m.SampleDisks(ctx)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(disks) != 0 {
		t.Errorf("expected inaccessible volume to be skipped, got %v", disks)
	}
}

func TestSample_FullSnapshot(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	m := &MetricsSampler{Volumes: []string{"/"}}
	snap, err := m.Sample(ctx)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if snap.TS == 0 {
		t.Error("expected non-zero timestamp")
	}
	if snap.RAM.TotalBytes == 0 {
		t.Error("expected real RAM total in full snapshot")
	}
	if len(snap.Disks) != 1 {
		t.Errorf("expected 1 disk in full snapshot, got %d", len(snap.Disks))
	}
}
