package agent

import (
	"os"
	"path/filepath"
	"testing"
)

func writeFile(t *testing.T, path string, size int) {
	t.Helper()
	if err := os.MkdirAll(filepath.Dir(path), 0755); err != nil {
		t.Fatalf("mkdir: %v", err)
	}
	data := make([]byte, size)
	if err := os.WriteFile(path, data, 0644); err != nil {
		t.Fatalf("write: %v", err)
	}
}

func TestSizeScanner_ScanOnce_RealFilesystem(t *testing.T) {
	root := t.TempDir()
	writeFile(t, filepath.Join(root, "a.txt"), 100)
	writeFile(t, filepath.Join(root, "sub", "b.txt"), 250)
	writeFile(t, filepath.Join(root, "sub", "deeper", "c.txt"), 50)

	scanner := &SizeScanner{Roots: []string{root}}
	snaps, err := scanner.ScanOnce()
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}

	byPath := map[string]int64{}
	isDir := map[string]bool{}
	for _, s := range snaps {
		byPath[s.Path] = s.SizeBytes
		isDir[s.Path] = s.IsDir
	}

	if got := byPath[filepath.Join(root, "a.txt")]; got != 100 {
		t.Errorf("a.txt size = %d, want 100", got)
	}
	if got := byPath[filepath.Join(root, "sub", "b.txt")]; got != 250 {
		t.Errorf("b.txt size = %d, want 250", got)
	}
	if got := byPath[filepath.Join(root, "sub", "deeper", "c.txt")]; got != 50 {
		t.Errorf("c.txt size = %d, want 50", got)
	}

	// Directory totals must be the sum of their real subtree.
	if got := byPath[filepath.Join(root, "sub", "deeper")]; got != 50 {
		t.Errorf("sub/deeper dir total = %d, want 50", got)
	}
	if got := byPath[filepath.Join(root, "sub")]; got != 300 {
		t.Errorf("sub dir total = %d, want 300 (250+50)", got)
	}
	if got := byPath[root]; got != 400 {
		t.Errorf("root dir total = %d, want 400 (100+250+50)", got)
	}
	if !isDir[filepath.Join(root, "sub")] {
		t.Error("expected sub to be marked is_dir=true")
	}
	if isDir[filepath.Join(root, "a.txt")] {
		t.Error("expected a.txt to be marked is_dir=false")
	}
}

func TestSizeScanner_EmptyDirectory(t *testing.T) {
	root := t.TempDir()
	scanner := &SizeScanner{Roots: []string{root}}
	snaps, err := scanner.ScanOnce()
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(snaps) != 1 {
		t.Fatalf("expected exactly the root dir entry, got %d: %+v", len(snaps), snaps)
	}
	if snaps[0].SizeBytes != 0 || !snaps[0].IsDir {
		t.Errorf("expected empty root dir with size 0, got %+v", snaps[0])
	}
}

func TestSizeScanner_MultipleRoots(t *testing.T) {
	rootA := t.TempDir()
	rootB := t.TempDir()
	writeFile(t, filepath.Join(rootA, "x.bin"), 10)
	writeFile(t, filepath.Join(rootB, "y.bin"), 20)

	scanner := &SizeScanner{Roots: []string{rootA, rootB}}
	snaps, err := scanner.ScanOnce()
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	byPath := map[string]int64{}
	for _, s := range snaps {
		byPath[s.Path] = s.SizeBytes
	}
	if byPath[rootA] != 10 {
		t.Errorf("rootA total = %d, want 10", byPath[rootA])
	}
	if byPath[rootB] != 20 {
		t.Errorf("rootB total = %d, want 20", byPath[rootB])
	}
}
