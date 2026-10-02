//go:build !windows

package agent

// outboundIPv4Raw is only needed on Windows; see outbound_windows.go.
func outboundIPv4Raw() string { return "" }
