package agent

import (
	"context"
	"log"
	"time"

	"github.com/google/uuid"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// Runtime wires the periodic/event-driven pieces (metrics sampling, disk
// history, directory-size snapshots, file watching, alert evaluation) to
// a running Server. It owns the background goroutines; Server itself
// stays a pure message-dispatch/broadcast object.
type Runtime struct {
	Server  *Server
	Store   *Store
	Metrics *MetricsSampler
	Scanner *SizeScanner
	Watcher *FileWatcher

	Thresholds core.AlertThresholds

	// alerts suppresses repeats of a condition that is still holding.
	alerts core.AlertGate

	MetricsInterval   time.Duration
	SnapshotInterval  time.Duration
	ProcessInterval   time.Duration
}

// Start launches all background loops. Call Stop (or cancel ctx) to
// terminate them.
func (rt *Runtime) Start(ctx context.Context) {
	go rt.metricsLoop(ctx)
	go rt.snapshotLoop(ctx)
	go rt.processLoop(ctx)
	if rt.Watcher != nil {
		go rt.Watcher.Run()
	}
}

func (rt *Runtime) metricsLoop(ctx context.Context) {
	interval := rt.MetricsInterval
	if interval <= 0 {
		interval = 2 * time.Second // PROTOCOL.md §4.4 default
	}
	ticker := time.NewTicker(interval)
	defer ticker.Stop()
	for {
		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
			if !rt.Server.HasActiveSubscribers(core.TopicMetrics) {
				continue // PROTOCOL.md §4.4: only push "while at least one client is subscribed"
			}
			snap, err := rt.Metrics.Sample(ctx)
			if err != nil {
				log.Printf("metrics sample failed: %v", err)
				continue
			}
			rt.Server.BroadcastMetrics(snap)
			rt.recordDiskHistoryAndEvaluate(snap)
		}
	}
}

func (rt *Runtime) recordDiskHistoryAndEvaluate(snap core.MetricsPush) {
	now := snap.TS
	for _, d := range snap.Disks {
		if err := rt.Store.InsertDiskFreeSample(now, d.Volume, int64(d.TotalBytes), int64(d.FreeBytes)); err != nil {
			log.Printf("recording disk free sample failed: %v", err)
			continue
		}

		// low_free_space
		if trig, pct, sev := core.CheckLowFreeSpace(d.TotalBytes, d.FreeBytes, rt.Thresholds.LowFreeSpacePercent); rt.alerts.Observe(core.AlertLowFreeSpace+"|"+d.Volume, trig, sev) {
			rt.raiseAlert(core.AlertLowFreeSpace, sev, now,
				volumeMessage(core.AlertLowFreeSpace, d.Volume, pct),
				map[string]interface{}{"volume": d.Volume, "free_percent": pct})
		}

		// disk_fill_rate
		history, err := rt.Store.DiskFreeHistory(d.Volume, now-rt.Thresholds.DiskFillRateWindowSec-int64(rt.MetricsIntervalOrDefault().Seconds()))
		if err != nil {
			log.Printf("reading disk free history failed: %v", err)
			continue
		}
		if trig, lossPct, sev := core.CheckDiskFillRate(history, int64(d.FreeBytes), int64(d.TotalBytes), now, rt.Thresholds.DiskFillRateWindowSec, rt.Thresholds.DiskFillRatePercent); rt.alerts.Observe(core.AlertDiskFillRate+"|"+d.Volume, trig, sev) {
			rt.raiseAlert(core.AlertDiskFillRate, sev, now,
				volumeMessage(core.AlertDiskFillRate, d.Volume, lossPct),
				map[string]interface{}{"volume": d.Volume, "loss_percent": lossPct})
		}
	}
}

func (rt *Runtime) MetricsIntervalOrDefault() time.Duration {
	if rt.MetricsInterval <= 0 {
		return 2 * time.Second
	}
	return rt.MetricsInterval
}

func (rt *Runtime) snapshotLoop(ctx context.Context) {
	interval := rt.SnapshotInterval
	if interval <= 0 {
		interval = 60 * time.Second
	}
	ticker := time.NewTicker(interval)
	defer ticker.Stop()
	for {
		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
			rt.runOneSnapshot()
		}
	}
}

func (rt *Runtime) runOneSnapshot() {
	if rt.Scanner == nil {
		return
	}
	snaps, err := rt.Scanner.ScanOnce()
	if err != nil {
		log.Printf("size scan failed: %v", err)
		return
	}
	now := time.Now().Unix()
	if err := rt.Store.InsertSnapshot(now, snaps); err != nil {
		log.Printf("storing snapshot failed: %v", err)
		return
	}
	rt.evaluateFastGrowth(now, snaps)
}

func (rt *Runtime) evaluateFastGrowth(now int64, snaps []core.SizeSnapshot) {
	windowStart := now - rt.Thresholds.FastGrowthWindowSec
	for _, sn := range snaps {
		if !sn.IsDir {
			continue
		}
		history, err := rt.Store.PathSizeHistory(sn.Path, windowStart)
		if err != nil {
			continue
		}
		samples := make([]core.SizeSample, 0, len(history))
		for _, h := range history {
			samples = append(samples, h)
		}
		if trig, delta, sev := core.CheckFastGrowth(samples, sn.SizeBytes, now, rt.Thresholds.FastGrowthWindowSec, rt.Thresholds.FastGrowthBytes); rt.alerts.Observe(core.AlertFastGrowth+"|"+sn.Path, trig, sev) {
			rt.raiseAlert(core.AlertFastGrowth, sev, now,
				fastGrowthMessage(sn.Path, delta),
				map[string]interface{}{"path": sn.Path, "delta_bytes": delta})
		}
	}
}

func (rt *Runtime) processLoop(ctx context.Context) {
	interval := rt.ProcessInterval
	if interval <= 0 {
		interval = 5 * time.Second // PROTOCOL.md §4.4: processes topic, 5s if subscribed
	}
	ticker := time.NewTicker(interval)
	defer ticker.Stop()
	for {
		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
			if !rt.Server.HasActiveSubscribers(core.TopicProcesses) {
				continue
			}
			items, err := rt.Server.ProcessMgr.List(ctx, "")
			if err != nil {
				log.Printf("process snapshot failed: %v", err)
				continue
			}
			rt.Server.BroadcastProcessSnapshot(items)
		}
	}
}

// OnFileEvent is wired as the FileWatcher's callback: it stores the event,
// broadcasts it to subscribers, and evaluates the large_file alert rule.
// File content is never touched here — only the metadata already carried
// on core.FileEventRecord.
func (rt *Runtime) OnFileEvent(e core.FileEventRecord) {
	if err := rt.Store.InsertFileEvent(e); err != nil {
		log.Printf("storing file event failed: %v", err)
	}
	rt.Server.BroadcastFileEvent(e)

	if e.IsDir {
		return
	}
	// A file being written produces many modify events; report the size
	// crossing once per file (deletes/shrinks re-arm it via trig == false).
	if trig, sev := core.CheckLargeFile(e.SizeBytes, rt.Thresholds.LargeFileBytes); rt.alerts.Observe(core.AlertLargeFile+"|"+e.Path, trig && e.SizeBytes > 0, sev) {
		rt.raiseAlert(core.AlertLargeFile, sev, e.TS,
			largeFileMessage(e.Path, e.SizeBytes),
			map[string]interface{}{"path": e.Path, "size_bytes": e.SizeBytes})
	}
}

func (rt *Runtime) raiseAlert(kind, severity string, ts int64, message string, ctx map[string]interface{}) {
	rec := core.AlertRecord{ID: uuid.NewString(), TS: ts, Kind: kind, Severity: severity, Message: message, Context: ctx}
	if err := rt.Store.InsertAlert(rec); err != nil {
		log.Printf("storing alert failed: %v", err)
		return
	}
	rt.Server.BroadcastAlert(rec)
}

func volumeMessage(kind, volume string, pct float64) string {
	switch kind {
	case core.AlertLowFreeSpace:
		return "Low free space on " + volume
	case core.AlertDiskFillRate:
		return "Disk filling quickly on " + volume
	default:
		return volume
	}
}

func largeFileMessage(path string, size int64) string {
	return "Large file detected: " + path
}

func fastGrowthMessage(path string, delta int64) string {
	return "Fast growth detected: " + path
}
