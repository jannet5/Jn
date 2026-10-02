//go:build !windows

package agent

// discoverVolumes on non-Windows builds (only used for development and
// tests here) reports the root filesystem.
func discoverVolumes() []string { return []string{"/"} }
