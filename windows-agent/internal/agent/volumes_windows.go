//go:build windows

package agent

import "golang.org/x/sys/windows"

// discoverVolumes lists this PC's local drive letters ("C:\", "D:\", ...),
// re-read on every sample so a drive plugged in later shows up. Network
// (DRIVE_REMOTE) and optical drives are skipped: a disconnected share can
// stall GetDiskFreeSpaceEx for a long time and would freeze the metrics loop.
func discoverVolumes() []string {
	mask, err := windows.GetLogicalDrives()
	if err != nil || mask == 0 {
		return []string{`C:\`}
	}
	var out []string
	for i := 0; i < 26; i++ {
		if mask&(1<<uint(i)) == 0 {
			continue
		}
		root := string(rune('A'+i)) + `:\`
		p, err := windows.UTF16PtrFromString(root)
		if err != nil {
			continue
		}
		switch windows.GetDriveType(p) {
		case windows.DRIVE_FIXED, windows.DRIVE_REMOVABLE:
			out = append(out, root)
		}
	}
	if len(out) == 0 {
		return []string{`C:\`}
	}
	return out
}
