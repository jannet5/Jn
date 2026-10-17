//go:build !windows

package main

import "os/exec"

func openInBrowser(path string) error {
	return exec.Command("xdg-open", path).Start()
}
