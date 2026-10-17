//go:build windows

package main

import "os/exec"

// openInBrowser opens a local file with its default handler (the browser for .html).
func openInBrowser(path string) error {
	return exec.Command("rundll32", "url.dll,FileProtocolHandler", path).Start()
}
