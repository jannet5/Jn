package agent

import (
	"os"
	"path/filepath"
	"sync"
	"testing"
	"time"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// collector is a thread-safe sink for events emitted by a real FileWatcher
// under test.
type collector struct {
	mu     sync.Mutex
	events []core.FileEventRecord
}

func (c *collector) add(e core.FileEventRecord) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.events = append(c.events, e)
}

func (c *collector) snapshot() []core.FileEventRecord {
	c.mu.Lock()
	defer c.mu.Unlock()
	return append([]core.FileEventRecord{}, c.events...)
}

func waitFor(t *testing.T, timeout time.Duration, cond func() bool) {
	deadline := time.Now().Add(timeout)
	for time.Now().Before(deadline) {
		if cond() {
			return
		}
		time.Sleep(20 * time.Millisecond)
	}
	t.Fatalf("condition not met within %v", timeout)
}

func newTestWatcher(t *testing.T, ignoreGlobs []string) (*FileWatcher, *collector) {
	c := &collector{}
	fw, err := NewFileWatcher(ignoreGlobs, c.add)
	if err != nil {
		t.Fatalf("unexpected error creating watcher: %v", err)
	}
	fw.debounce = 50 * time.Millisecond // keep tests fast
	t.Cleanup(func() { fw.Close() })
	go fw.Run()
	return fw, c
}

func TestFileWatcher_RealCreateEventOnRealFS(t *testing.T) {
	dir := t.TempDir()
	fw, c := newTestWatcher(t, nil)
	if err := fw.AddRoot(dir); err != nil {
		t.Fatalf("AddRoot: %v", err)
	}

	filePath := filepath.Join(dir, "hello.txt")
	if err := os.WriteFile(filePath, []byte("hello world"), 0644); err != nil {
		t.Fatalf("write file: %v", err)
	}

	waitFor(t, 3*time.Second, func() bool {
		for _, e := range c.snapshot() {
			if e.Path == filePath {
				return true
			}
		}
		return false
	})

	var found core.FileEventRecord
	for _, e := range c.snapshot() {
		if e.Path == filePath {
			found = e
		}
	}
	if found.IsDir {
		t.Error("expected is_dir=false for a plain file")
	}
	if found.SizeBytes != int64(len("hello world")) {
		t.Errorf("size_bytes = %d, want %d", found.SizeBytes, len("hello world"))
	}
}

func TestFileWatcher_IgnoresNoiseGlobs(t *testing.T) {
	dir := t.TempDir()
	fw, c := newTestWatcher(t, []string{`*.tmp`})
	if err := fw.AddRoot(dir); err != nil {
		t.Fatalf("AddRoot: %v", err)
	}

	ignoredPath := filepath.Join(dir, "cache.tmp")
	keptPath := filepath.Join(dir, "keep.txt")
	if err := os.WriteFile(ignoredPath, []byte("x"), 0644); err != nil {
		t.Fatalf("write: %v", err)
	}
	if err := os.WriteFile(keptPath, []byte("y"), 0644); err != nil {
		t.Fatalf("write: %v", err)
	}

	waitFor(t, 3*time.Second, func() bool {
		for _, e := range c.snapshot() {
			if e.Path == keptPath {
				return true
			}
		}
		return false
	})

	for _, e := range c.snapshot() {
		if e.Path == ignoredPath {
			t.Errorf("expected %q to be ignored by *.tmp glob, but it was emitted", ignoredPath)
		}
	}
}

func TestFileWatcher_RecursiveWatchOnNewSubdirectory(t *testing.T) {
	dir := t.TempDir()
	fw, c := newTestWatcher(t, nil)
	if err := fw.AddRoot(dir); err != nil {
		t.Fatalf("AddRoot: %v", err)
	}

	subdir := filepath.Join(dir, "newsub")
	if err := os.Mkdir(subdir, 0755); err != nil {
		t.Fatalf("mkdir: %v", err)
	}
	// Give the watcher a moment to notice the new directory and add a
	// recursive watch for it (this is the fsnotify limitation the
	// implementation works around by hand).
	waitFor(t, 3*time.Second, func() bool {
		for _, e := range c.snapshot() {
			if e.Path == subdir {
				return true
			}
		}
		return false
	})

	nestedFile := filepath.Join(subdir, "nested.txt")
	if err := os.WriteFile(nestedFile, []byte("nested"), 0644); err != nil {
		t.Fatalf("write: %v", err)
	}

	waitFor(t, 3*time.Second, func() bool {
		for _, e := range c.snapshot() {
			if e.Path == nestedFile {
				return true
			}
		}
		return false
	})
}

func TestFileWatcher_DebounceCollapsesBurst(t *testing.T) {
	dir := t.TempDir()
	fw, c := newTestWatcher(t, nil)
	fw.debounce = 200 * time.Millisecond
	if err := fw.AddRoot(dir); err != nil {
		t.Fatalf("AddRoot: %v", err)
	}

	filePath := filepath.Join(dir, "burst.txt")
	for i := 0; i < 10; i++ {
		if err := os.WriteFile(filePath, []byte("data"), 0644); err != nil {
			t.Fatalf("write: %v", err)
		}
		time.Sleep(5 * time.Millisecond)
	}

	time.Sleep(500 * time.Millisecond) // let debounce settle

	count := 0
	for _, e := range c.snapshot() {
		if e.Path == filePath {
			count++
		}
	}
	if count == 0 {
		t.Fatal("expected at least one emitted event for the burst")
	}
	if count > 3 {
		t.Errorf("expected debounce to collapse a 10-write burst to a small number of events, got %d", count)
	}
}
