package agent

import (
	"fmt"
	"os"
	"path/filepath"
	"sync"
	"time"

	"github.com/fsnotify/fsnotify"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// FileWatcher walks configured watch roots and manages recursive fsnotify
// watches by hand: fsnotify only watches one directory non-recursively, so
// this adds a watch per subdirectory found on an initial walk, adds new
// watches when a directory is created, and removes them when a watched
// directory is removed/renamed away.
//
// Noise filtering (PROTOCOL.md §7) is applied before an event is ever
// turned into a file_event or handed to a callback, and file *content* is
// never read — only os.Stat metadata (name, size, timestamps, is_dir).
type FileWatcher struct {
	watcher     *fsnotify.Watcher
	ignoreGlobs []string
	onEvent     func(core.FileEventRecord)

	// debounce collapses rapid repeated events for the same path within a
	// short window into a single emitted event, rather than hammering the
	// callback (and the sqlite store, and the WS push) once per fsnotify
	// event on a busy directory. This is this implementation's answer to
	// the spec's under-specified "*.tmp, *.log churn is throttled"
	// language — see core.DefaultIgnoreGlobs's doc comment for the exact
	// ambiguity being flagged.
	debounce      time.Duration
	mu            sync.Mutex
	pendingTimers map[string]*time.Timer
	pendingLatest map[string]fsnotify.Event
}

// NewFileWatcher creates a watcher. onEvent is called (from a background
// goroutine) once per de-noised, de-bounced file system event.
func NewFileWatcher(ignoreGlobs []string, onEvent func(core.FileEventRecord)) (*FileWatcher, error) {
	w, err := fsnotify.NewWatcher()
	if err != nil {
		return nil, fmt.Errorf("creating fsnotify watcher: %w", err)
	}
	fw := &FileWatcher{
		watcher:       w,
		ignoreGlobs:   ignoreGlobs,
		onEvent:       onEvent,
		debounce:      500 * time.Millisecond,
		pendingTimers: make(map[string]*time.Timer),
		pendingLatest: make(map[string]fsnotify.Event),
	}
	return fw, nil
}

// AddRoot walks root and adds a watch for it and every subdirectory not
// matched by the ignore globs.
func (fw *FileWatcher) AddRoot(root string) error {
	return filepath.WalkDir(root, func(path string, d os.DirEntry, err error) error {
		if err != nil {
			// Skip unreadable subtrees rather than aborting the whole walk.
			return nil
		}
		if !d.IsDir() {
			return nil
		}
		if core.IsIgnored(path, fw.ignoreGlobs) {
			return filepath.SkipDir
		}
		if err := fw.watcher.Add(path); err != nil {
			// A single unwatchable directory (permissions, etc.) shouldn't
			// stop the rest of the tree from being watched.
			return nil
		}
		return nil
	})
}

// Run processes fsnotify events until the watcher is closed. Call this in
// its own goroutine.
func (fw *FileWatcher) Run() {
	for {
		select {
		case ev, ok := <-fw.watcher.Events:
			if !ok {
				return
			}
			fw.handleRaw(ev)
		case _, ok := <-fw.watcher.Errors:
			if !ok {
				return
			}
			// Errors are swallowed here (e.g. a transient stat failure on
			// a file that vanished between event and processing); the
			// watcher itself keeps running.
		}
	}
}

func (fw *FileWatcher) Close() error {
	fw.mu.Lock()
	for _, t := range fw.pendingTimers {
		t.Stop()
	}
	fw.mu.Unlock()
	return fw.watcher.Close()
}

func (fw *FileWatcher) handleRaw(ev fsnotify.Event) {
	if core.IsIgnored(ev.Name, fw.ignoreGlobs) {
		return
	}

	// Recursive-watch maintenance: a new directory needs its own watch
	// (and everything under it, in case it was created already-populated,
	// e.g. moved in from elsewhere); a removed/renamed-away directory's
	// watch is dropped by fsnotify automatically once the path is gone,
	// but we proactively remove it too so stale entries don't linger.
	if ev.Op&fsnotify.Create == fsnotify.Create {
		if info, err := os.Stat(ev.Name); err == nil && info.IsDir() {
			_ = fw.AddRoot(ev.Name)
		}
	}
	if ev.Op&(fsnotify.Remove|fsnotify.Rename) != 0 {
		_ = fw.watcher.Remove(ev.Name)
	}

	fw.debounced(ev)
}

// debounced coalesces bursts of events for the same path into one emitted
// event per debounce window, using only the latest event's data.
func (fw *FileWatcher) debounced(ev fsnotify.Event) {
	fw.mu.Lock()
	fw.pendingLatest[ev.Name] = ev
	if t, exists := fw.pendingTimers[ev.Name]; exists {
		t.Stop()
	}
	fw.pendingTimers[ev.Name] = time.AfterFunc(fw.debounce, func() {
		fw.mu.Lock()
		latest, ok := fw.pendingLatest[ev.Name]
		delete(fw.pendingLatest, ev.Name)
		delete(fw.pendingTimers, ev.Name)
		fw.mu.Unlock()
		if ok {
			fw.emit(latest)
		}
	})
	fw.mu.Unlock()
}

func (fw *FileWatcher) emit(ev fsnotify.Event) {
	rec := core.FileEventRecord{
		TS:   time.Now().Unix(),
		Path: ev.Name,
	}

	info, statErr := os.Lstat(ev.Name)
	switch {
	case ev.Op&fsnotify.Create == fsnotify.Create:
		rec.Op = core.OpCreated
	case ev.Op&fsnotify.Write == fsnotify.Write:
		rec.Op = core.OpModified
	case ev.Op&fsnotify.Remove == fsnotify.Remove:
		rec.Op = core.OpDeleted
	case ev.Op&fsnotify.Rename == fsnotify.Rename:
		rec.Op = core.OpRenamed
	default:
		rec.Op = core.OpModified
	}

	if statErr == nil {
		rec.IsDir = info.IsDir()
		if !rec.IsDir {
			rec.SizeBytes = info.Size()
		}
	} else if rec.Op != core.OpDeleted && rec.Op != core.OpRenamed {
		// Couldn't stat and it's not an expected-gone event: still emit
		// with size 0 rather than dropping the event entirely.
	}

	fw.onEvent(rec)
}
