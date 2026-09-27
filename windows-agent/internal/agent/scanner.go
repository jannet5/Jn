package agent

import (
	"os"
	"path/filepath"

	"github.com/jake/winremotemonitor-agent/internal/core"
)

// SizeScanner performs periodic, throttled full-tree size scans of the
// watched roots using plain os.Stat walks on a ticker — deliberately NOT
// on every single fsnotify event, so a busy large disk isn't hammered by
// a full recursive stat walk on every write. The agent wires this to a
// ticker (e.g. config.SnapshotIntervalSec, default 60s) and feeds the
// result to Store.InsertSnapshot + core.TopGrowth.
type SizeScanner struct {
	Roots []string
}

// ScanOnce walks every root and returns a size snapshot for every regular
// file AND for every directory (as the sum of its subtree), so growth
// analysis can answer both "which file got huge" and "which folder grew".
func (s *SizeScanner) ScanOnce() ([]core.SizeSnapshot, error) {
	dirTotals := make(map[string]int64)
	var files []core.SizeSnapshot

	for _, root := range s.Roots {
		err := filepath.WalkDir(root, func(path string, d os.DirEntry, err error) error {
			if err != nil {
				return nil // skip unreadable entries, keep scanning
			}
			if d.IsDir() {
				if _, ok := dirTotals[path]; !ok {
					dirTotals[path] = 0
				}
				return nil
			}
			info, err := d.Info()
			if err != nil {
				return nil
			}
			size := info.Size()
			files = append(files, core.SizeSnapshot{Path: path, IsDir: false, SizeBytes: size})
			// Attribute this file's size to every ancestor directory up to
			// (and including) root, so a directory's snapshot size is the
			// size of its whole subtree.
			dir := filepath.Dir(path)
			for {
				dirTotals[dir] += size
				if dir == root || dir == filepath.Dir(dir) {
					break
				}
				dir = filepath.Dir(dir)
			}
			return nil
		})
		if err != nil {
			return nil, err
		}
	}

	out := make([]core.SizeSnapshot, 0, len(files)+len(dirTotals))
	out = append(out, files...)
	for path, total := range dirTotals {
		out = append(out, core.SizeSnapshot{Path: path, IsDir: true, SizeBytes: total})
	}
	return out, nil
}
