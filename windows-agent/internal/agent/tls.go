package agent

import (
	"crypto/ecdsa"
	"crypto/elliptic"
	"crypto/rand"
	"crypto/sha256"
	"crypto/tls"
	"crypto/x509"
	"crypto/x509/pkix"
	"encoding/hex"
	"encoding/pem"
	"fmt"
	"math/big"
	"net"
	"os"
	"path/filepath"
	"time"
)

// CertValidity is PROTOCOL.md §1's cert lifetime ("Validity: 10 years").
const CertValidity = 10 * 365 * 24 * time.Hour

// EnsureCert loads a persisted self-signed TLS cert/key pair from
// certPath/keyPath, generating and persisting a fresh one on first run if
// either file is missing. Returns the tls.Certificate ready for use with
// tls.Config, plus the SHA-256 fingerprint of the DER-encoded public key
// (hex, lowercase, no separators) exactly as PROTOCOL.md §1 specifies for
// out-of-band pinning.
func EnsureCert(certPath, keyPath string) (tls.Certificate, string, error) {
	if fileExists(certPath) && fileExists(keyPath) {
		cert, err := tls.LoadX509KeyPair(certPath, keyPath)
		if err != nil {
			return tls.Certificate{}, "", fmt.Errorf("loading existing cert/key: %w", err)
		}
		fp, err := fingerprintFromCertificate(cert)
		if err != nil {
			return tls.Certificate{}, "", err
		}
		return cert, fp, nil
	}
	return generateAndPersistCert(certPath, keyPath)
}

func fileExists(path string) bool {
	_, err := os.Stat(path)
	return err == nil
}

func generateAndPersistCert(certPath, keyPath string) (tls.Certificate, string, error) {
	priv, err := ecdsa.GenerateKey(elliptic.P256(), rand.Reader)
	if err != nil {
		return tls.Certificate{}, "", fmt.Errorf("generating key: %w", err)
	}

	serial, err := rand.Int(rand.Reader, new(big.Int).Lsh(big.NewInt(1), 128))
	if err != nil {
		return tls.Certificate{}, "", fmt.Errorf("generating serial: %w", err)
	}

	hostname, _ := os.Hostname()
	if hostname == "" {
		hostname = "winremotemonitor-agent"
	}

	template := x509.Certificate{
		SerialNumber: serial,
		Subject:      pkix.Name{CommonName: hostname},
		NotBefore:    time.Now().Add(-1 * time.Hour),
		NotAfter:     time.Now().Add(CertValidity),
		KeyUsage:     x509.KeyUsageDigitalSignature | x509.KeyUsageKeyEncipherment,
		ExtKeyUsage:  []x509.ExtKeyUsage{x509.ExtKeyUsageServerAuth},
		DNSNames:     []string{hostname, "localhost"},
		IPAddresses:  localIPs(),
	}

	derBytes, err := x509.CreateCertificate(rand.Reader, &template, &template, &priv.PublicKey, priv)
	if err != nil {
		return tls.Certificate{}, "", fmt.Errorf("creating certificate: %w", err)
	}

	certPEM := pem.EncodeToMemory(&pem.Block{Type: "CERTIFICATE", Bytes: derBytes})
	keyBytes, err := x509.MarshalECPrivateKey(priv)
	if err != nil {
		return tls.Certificate{}, "", fmt.Errorf("marshaling key: %w", err)
	}
	keyPEM := pem.EncodeToMemory(&pem.Block{Type: "EC PRIVATE KEY", Bytes: keyBytes})

	if err := os.MkdirAll(filepath.Dir(certPath), 0700); err != nil {
		return tls.Certificate{}, "", fmt.Errorf("creating cert dir: %w", err)
	}
	if err := os.WriteFile(certPath, certPEM, 0644); err != nil {
		return tls.Certificate{}, "", fmt.Errorf("writing cert: %w", err)
	}
	if err := os.WriteFile(keyPath, keyPEM, 0600); err != nil {
		return tls.Certificate{}, "", fmt.Errorf("writing key: %w", err)
	}

	cert, err := tls.X509KeyPair(certPEM, keyPEM)
	if err != nil {
		return tls.Certificate{}, "", fmt.Errorf("loading generated cert/key: %w", err)
	}
	fp, err := fingerprintFromCertificate(cert)
	if err != nil {
		return tls.Certificate{}, "", err
	}
	return cert, fp, nil
}

// fingerprintFromCertificate computes PROTOCOL.md §1's pinned fingerprint:
// "SHA-256 fingerprint (of the DER-encoded public key, hex-encoded
// lowercase, no separators)".
func fingerprintFromCertificate(cert tls.Certificate) (string, error) {
	if len(cert.Certificate) == 0 {
		return "", fmt.Errorf("certificate has no DER bytes")
	}
	parsed, err := x509.ParseCertificate(cert.Certificate[0])
	if err != nil {
		return "", fmt.Errorf("parsing certificate: %w", err)
	}
	pubDER, err := x509.MarshalPKIXPublicKey(parsed.PublicKey)
	if err != nil {
		return "", fmt.Errorf("marshaling public key: %w", err)
	}
	sum := sha256.Sum256(pubDER)
	return hex.EncodeToString(sum[:]), nil
}

// localIPs best-effort collects this machine's non-loopback IPv4
// addresses to include as SANs, so the pinned-fingerprint model (which
// does not rely on hostname/CA validation anyway) still produces a cert
// that includes something recognizable if a client ever does look at SANs.
func localIPs() []net.IP {
	ips := []net.IP{net.ParseIP("127.0.0.1")}
	addrs, err := net.InterfaceAddrs()
	if err != nil {
		return ips
	}
	for _, a := range addrs {
		if ipnet, ok := a.(*net.IPNet); ok && !ipnet.IP.IsLoopback() {
			if v4 := ipnet.IP.To4(); v4 != nil {
				ips = append(ips, v4)
			}
		}
	}
	return ips
}

// PrimaryLocalIP returns a best-effort "this machine's LAN IP" for display
// during pairing (e.g. "Connect to 192.168.1.42:8787"). It is not used for
// any security decision — only pinned fingerprint verification is.
func PrimaryLocalIP() string {
	if ip := outboundIPv4(); ip != "" {
		return ip
	}
	if c := LocalIPv4Candidates(); len(c) > 0 {
		return c[0]
	}
	return "127.0.0.1"
}

// outboundIPv4 asks the OS which local address it would use to reach the
// internet. A UDP "dial" only consults the routing table - no packet is
// sent. Picking the first interface address instead returns a Hyper-V/WSL/
// VPN virtual adapter on many Windows PCs, which a phone on the Wi-Fi can't
// reach.
func outboundIPv4() string {
	conn, err := net.Dial("udp4", "8.8.8.8:80")
	if err != nil {
		return outboundIPv4Raw()
	}
	defer conn.Close()
	addr, ok := conn.LocalAddr().(*net.UDPAddr)
	if !ok || addr.IP.IsLoopback() || addr.IP.IsUnspecified() || addr.IP.To4() == nil {
		return ""
	}
	return addr.IP.To4().String()
}

// LocalIPv4Candidates lists every non-loopback IPv4 address on this machine,
// so the pairing output can offer alternatives for manual entry.
func LocalIPv4Candidates() []string {
	var out []string
	for _, ip := range localIPs() {
		if !ip.IsLoopback() {
			out = append(out, ip.String())
		}
	}
	return out
}
