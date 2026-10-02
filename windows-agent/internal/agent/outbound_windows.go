//go:build windows

package agent

import (
	"net"
	"syscall"
)

// outboundIPv4Raw does the same routing-table lookup as outboundIPv4 with a
// bare Winsock socket. Go's net package also issues WSAIoctl
// SIO_UDP_NETRESET on every UDP socket and fails the dial if that ioctl is
// unsupported (as under Wine), even though connect+getsockname work fine.
func outboundIPv4Raw() string {
	s, err := syscall.Socket(syscall.AF_INET, syscall.SOCK_DGRAM, syscall.IPPROTO_UDP)
	if err != nil {
		return ""
	}
	defer syscall.Closesocket(s)
	if err := syscall.Connect(s, &syscall.SockaddrInet4{Port: 80, Addr: [4]byte{8, 8, 8, 8}}); err != nil {
		return ""
	}
	sa, err := syscall.Getsockname(s)
	if err != nil {
		return ""
	}
	in4, ok := sa.(*syscall.SockaddrInet4)
	if !ok {
		return ""
	}
	ip := net.IP(in4.Addr[:])
	if ip.IsLoopback() || ip.IsUnspecified() {
		return ""
	}
	return ip.String()
}
