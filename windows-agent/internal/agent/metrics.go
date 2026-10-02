package agent

import (
	"context"
	"fmt"
	"time"

	"github.com/shirou/gopsutil/v3/cpu"
	"github.com/shirou/gopsutil/v3/disk"
	"github.com/shirou/gopsutil/v3/mem"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// MetricsSampler samples real CPU/RAM/disk metrics via gopsutil. It is the
// only place in the codebase that calls gopsutil's cpu/mem/disk packages,
// so it is easy to point at in review and easy to exercise directly in
// tests on this Linux dev box (the numbers will be real Linux numbers
// here; the same code path reports real Windows numbers when the binary
// is cross-compiled and run on Windows — see README limitations).
type MetricsSampler struct {
	// Volumes lists mount points/drive letters to report on. Empty means
	// "discover on every sample": all local drive letters on Windows
	// ("C:\\", "D:\\", ...), "/" elsewhere. Tests set it explicitly.
	Volumes []string
}

// SampleCPUPercent returns the current overall CPU utilization percent,
// measured over a short real interval (blocking).
func (m *MetricsSampler) SampleCPUPercent(ctx context.Context) (float64, error) {
	percents, err := cpu.PercentWithContext(ctx, 200*time.Millisecond, false)
	if err != nil {
		return 0, fmt.Errorf("cpu.Percent: %w", err)
	}
	if len(percents) == 0 {
		return 0, fmt.Errorf("cpu.Percent returned no samples")
	}
	return percents[0], nil
}

// SampleRAM returns real total/used RAM via gopsutil/mem.
func (m *MetricsSampler) SampleRAM(ctx context.Context) (core.RAMInfo, error) {
	vm, err := mem.VirtualMemoryWithContext(ctx)
	if err != nil {
		return core.RAMInfo{}, fmt.Errorf("mem.VirtualMemory: %w", err)
	}
	return core.RAMInfo{TotalBytes: vm.Total, UsedBytes: vm.Used}, nil
}

// SampleDisks returns real disk usage for each configured volume via
// gopsutil/disk.
func (m *MetricsSampler) SampleDisks(ctx context.Context) ([]core.DiskInfo, error) {
	volumes := m.Volumes
	if len(volumes) == 0 {
		volumes = discoverVolumes()
	}
	out := make([]core.DiskInfo, 0, len(volumes))
	for _, v := range volumes {
		usage, err := disk.UsageWithContext(ctx, v)
		if err != nil {
			// A single inaccessible/ejected volume shouldn't take down the
			// whole metrics push; skip it.
			continue
		}
		out = append(out, core.DiskInfo{
			Volume:     v,
			TotalBytes: usage.Total,
			UsedBytes:  usage.Used,
			FreeBytes:  usage.Free,
		})
	}
	return out, nil
}

// Sample gathers one full metrics snapshot as pushed over the wire
// (PROTOCOL.md §4.4).
func (m *MetricsSampler) Sample(ctx context.Context) (core.MetricsPush, error) {
	cpuPct, err := m.SampleCPUPercent(ctx)
	if err != nil {
		return core.MetricsPush{}, err
	}
	ram, err := m.SampleRAM(ctx)
	if err != nil {
		return core.MetricsPush{}, err
	}
	disks, err := m.SampleDisks(ctx)
	if err != nil {
		return core.MetricsPush{}, err
	}
	return core.MetricsPush{
		Type:       core.TypeMetrics,
		TS:         time.Now().Unix(),
		CPUPercent: cpuPct,
		RAM:        ram,
		Disks:      disks,
	}, nil
}
