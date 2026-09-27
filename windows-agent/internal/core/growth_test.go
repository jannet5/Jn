package core

import "testing"

func TestTopGrowth_BasicDeltas(t *testing.T) {
	before := []SizeSnapshot{
		{Path: `C:\Videos`, IsDir: true, SizeBytes: 1000},
		{Path: `C:\Photos`, IsDir: true, SizeBytes: 500},
	}
	after := []SizeSnapshot{
		{Path: `C:\Videos`, IsDir: true, SizeBytes: 2073741824},
		{Path: `C:\Photos`, IsDir: true, SizeBytes: 500}, // unchanged
	}
	items := TopGrowth(before, after, 20)
	if len(items) != 1 {
		t.Fatalf("expected 1 growth item (unchanged dir excluded), got %d: %+v", len(items), items)
	}
	if items[0].Path != `C:\Videos` {
		t.Errorf("path = %q", items[0].Path)
	}
	if items[0].DeltaBytes != 2073741824-1000 {
		t.Errorf("delta = %d, want %d", items[0].DeltaBytes, 2073741824-1000)
	}
	if items[0].SizeBefore != 1000 || items[0].SizeAfter != 2073741824 {
		t.Errorf("before/after = %d/%d", items[0].SizeBefore, items[0].SizeAfter)
	}
}

func TestTopGrowth_NewPathTreatedAsGrowthFromZero(t *testing.T) {
	after := []SizeSnapshot{{Path: `C:\NewFolder`, IsDir: true, SizeBytes: 4096}}
	items := TopGrowth(nil, after, 20)
	if len(items) != 1 {
		t.Fatalf("expected 1 item, got %d", len(items))
	}
	if items[0].SizeBefore != 0 {
		t.Errorf("size_before = %d, want 0 for a brand-new path", items[0].SizeBefore)
	}
	if items[0].DeltaBytes != 4096 {
		t.Errorf("delta = %d, want 4096", items[0].DeltaBytes)
	}
}

func TestTopGrowth_ShrinkingOrUnchangedExcluded(t *testing.T) {
	before := []SizeSnapshot{
		{Path: `C:\Shrunk`, SizeBytes: 5000},
		{Path: `C:\Same`, SizeBytes: 42},
	}
	after := []SizeSnapshot{
		{Path: `C:\Shrunk`, SizeBytes: 1000},
		{Path: `C:\Same`, SizeBytes: 42},
	}
	items := TopGrowth(before, after, 20)
	if len(items) != 0 {
		t.Errorf("expected 0 growth items, got %+v", items)
	}
}

func TestTopGrowth_SortedDescendingAndLimited(t *testing.T) {
	after := []SizeSnapshot{
		{Path: "a", SizeBytes: 100},
		{Path: "b", SizeBytes: 300},
		{Path: "c", SizeBytes: 200},
	}
	items := TopGrowth(nil, after, 2)
	if len(items) != 2 {
		t.Fatalf("expected 2 items (limited), got %d", len(items))
	}
	if items[0].Path != "b" || items[1].Path != "c" {
		t.Errorf("expected order [b, c] (descending delta), got %v", items)
	}
}

func TestTopGrowth_TieBreakByPathIsDeterministic(t *testing.T) {
	after := []SizeSnapshot{
		{Path: "z", SizeBytes: 100},
		{Path: "a", SizeBytes: 100},
	}
	items := TopGrowth(nil, after, 20)
	if len(items) != 2 || items[0].Path != "a" || items[1].Path != "z" {
		t.Errorf("expected deterministic tie-break by path, got %v", items)
	}
}

func TestTopGrowth_NoLimitReturnsAll(t *testing.T) {
	after := []SizeSnapshot{
		{Path: "a", SizeBytes: 10}, {Path: "b", SizeBytes: 20}, {Path: "c", SizeBytes: 30},
	}
	items := TopGrowth(nil, after, 0)
	if len(items) != 3 {
		t.Errorf("expected all 3 items with limit=0, got %d", len(items))
	}
}

func TestTopGrowth_EmptyInputs(t *testing.T) {
	items := TopGrowth(nil, nil, 10)
	if len(items) != 0 {
		t.Errorf("expected no items, got %v", items)
	}
}
