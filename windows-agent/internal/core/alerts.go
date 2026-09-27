package core

// Package-level note (flagged in the final report, not a spec edit):
// PROTOCOL.md §8 defines threshold *values* for each alert kind but never
// says how "warning" vs "critical" (the two severities named in §4.4's
// alert message) map onto them. This file's best-effort interpretation:
// crossing the configured threshold is "warning"; crossing it by 2x (a
// reasonable, configurable-in-code multiplier) is "critical". This
// multiplier is not in PROTOCOL.md and should be reconciled with whatever
// the Android side assumes for severity-based UI (e.g. color).
const criticalMultiplier = 2.0

// SizeSample is one (timestamp, size) observation used to evaluate growth
// over a rolling window (fast_growth) — the agent layer feeds these from
// periodic directory-size snapshots.
type SizeSample struct {
	TS        int64 // unix seconds
	SizeBytes int64
}

// FreeSpaceSample is one (timestamp, free bytes) observation for a single
// volume, used to evaluate disk_fill_rate over a rolling window.
type FreeSpaceSample struct {
	TS        int64
	FreeBytes int64
}

// CheckLargeFile implements the large_file rule (PROTOCOL.md §8): "a single
// new/grown file crossing this size."
func CheckLargeFile(sizeBytes, thresholdBytes int64) (triggered bool, severity string) {
	if sizeBytes < thresholdBytes {
		return false, ""
	}
	return true, severityFor(float64(sizeBytes), float64(thresholdBytes))
}

// baselineInWindow returns the earliest sample at or after (now - windowSec),
// which acts as the baseline size for a rolling-window growth check. If no
// sample falls in the window, ok is false. Samples need not be sorted.
func baselineInWindow(samples []SizeSample, now, windowSec int64) (SizeSample, bool) {
	cutoff := now - windowSec
	var best SizeSample
	found := false
	for _, s := range samples {
		if s.TS < cutoff {
			continue
		}
		if !found || s.TS < best.TS {
			best = s
			found = true
		}
	}
	return best, found
}

// CheckFastGrowth implements the fast_growth rule (PROTOCOL.md §8): "a
// directory gaining > thresholdBytes within a rolling windowSec window."
// `samples` is the size history for one directory; `currentBytes` is its
// latest known size at time `now`. Returns the delta since the earliest
// sample inside the window.
func CheckFastGrowth(samples []SizeSample, currentBytes, now, windowSec, thresholdBytes int64) (triggered bool, delta int64, severity string) {
	baseline, ok := baselineInWindow(samples, now, windowSec)
	if !ok {
		return false, 0, ""
	}
	delta = currentBytes - baseline.SizeBytes
	if delta <= thresholdBytes {
		return false, delta, ""
	}
	return true, delta, severityFor(float64(delta), float64(thresholdBytes))
}

// CheckDiskFillRate implements the disk_fill_rate rule (PROTOCOL.md §8): "a
// volume losing > thresholdPercent of total free space within windowSec."
// `history` is free-space samples for one volume; `totalBytes` is that
// volume's (assumed roughly constant) total capacity.
func CheckDiskFillRate(history []FreeSpaceSample, currentFreeBytes, totalBytes, now, windowSec int64, thresholdPercent float64) (triggered bool, lossPercent float64, severity string) {
	if totalBytes <= 0 {
		return false, 0, ""
	}
	cutoff := now - windowSec
	var baseline FreeSpaceSample
	found := false
	for _, s := range history {
		if s.TS < cutoff {
			continue
		}
		if !found || s.TS < baseline.TS {
			baseline = s
			found = true
		}
	}
	if !found {
		return false, 0, ""
	}
	lostBytes := baseline.FreeBytes - currentFreeBytes
	if lostBytes <= 0 {
		return false, 0, ""
	}
	lossPercent = (float64(lostBytes) / float64(totalBytes)) * 100.0
	if lossPercent <= thresholdPercent {
		return false, lossPercent, ""
	}
	return true, lossPercent, severityFor(lossPercent, thresholdPercent)
}

// CheckLowFreeSpace implements the low_free_space rule (PROTOCOL.md §8):
// "free space on any volume drops below thresholdPercent."
func CheckLowFreeSpace(totalBytes, freeBytes uint64, thresholdPercent float64) (triggered bool, freePercent float64, severity string) {
	if totalBytes == 0 {
		return false, 0, ""
	}
	freePercent = (float64(freeBytes) / float64(totalBytes)) * 100.0
	if freePercent >= thresholdPercent {
		return false, freePercent, ""
	}
	// For low-free-space, "more severe" means *further below* the
	// threshold, i.e. less free space, so invert the ratio used elsewhere.
	usedFraction := thresholdPercent - freePercent
	if usedFraction >= thresholdPercent/criticalMultiplier {
		return true, freePercent, SeverityCritical
	}
	return true, freePercent, SeverityWarning
}

// severityFor is the shared "how far past the threshold" rule described at
// the top of this file: warning at the threshold, critical at
// criticalMultiplier times the threshold.
func severityFor(value, threshold float64) string {
	if threshold <= 0 {
		return SeverityWarning
	}
	if value >= threshold*criticalMultiplier {
		return SeverityCritical
	}
	return SeverityWarning
}
