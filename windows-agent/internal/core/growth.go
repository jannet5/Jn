package core

import "sort"

// SizeSnapshot is one entry of a directory-size snapshot: the size of a
// path (file or directory subtree) at a point in time. The agent layer
// produces these from real os.Stat walks; this file only diffs them.
type SizeSnapshot struct {
	Path      string
	IsDir     bool
	SizeBytes int64
}

// GrowthItem is one row of a top_growth_result (PROTOCOL.md §4.7).
type GrowthItem struct {
	Path       string
	IsDir      bool
	DeltaBytes int64
	SizeBefore int64
	SizeAfter  int64
}

// TopGrowth computes, for every path present in `after`, its size delta
// relative to `before` (0 if the path is new), and returns the top `limit`
// entries sorted by descending delta. Entries with a non-positive delta are
// excluded, since this answers "what grew" (PROTOCOL.md §4.7 top_growth),
// not "what changed". A limit <= 0 means "no limit".
func TopGrowth(before, after []SizeSnapshot, limit int) []GrowthItem {
	beforeByPath := make(map[string]SizeSnapshot, len(before))
	for _, s := range before {
		beforeByPath[s.Path] = s
	}

	items := make([]GrowthItem, 0, len(after))
	for _, cur := range after {
		prev, ok := beforeByPath[cur.Path]
		sizeBefore := int64(0)
		if ok {
			sizeBefore = prev.SizeBytes
		}
		delta := cur.SizeBytes - sizeBefore
		if delta <= 0 {
			continue
		}
		items = append(items, GrowthItem{
			Path:       cur.Path,
			IsDir:      cur.IsDir,
			DeltaBytes: delta,
			SizeBefore: sizeBefore,
			SizeAfter:  cur.SizeBytes,
		})
	}

	sort.Slice(items, func(i, j int) bool {
		if items[i].DeltaBytes != items[j].DeltaBytes {
			return items[i].DeltaBytes > items[j].DeltaBytes
		}
		return items[i].Path < items[j].Path // stable, deterministic tiebreak
	})

	if limit > 0 && len(items) > limit {
		items = items[:limit]
	}
	return items
}
