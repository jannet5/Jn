package agent

import (
	"strings"
	"testing"
	"time"
)

func TestRenderPairingPage_ContainsEverythingTheUserNeeds(t *testing.T) {
	exp := time.Unix(1_800_000_000, 0)
	html, err := RenderPairingPage(PairingPage{
		Code: "AB12-CD34", Host: "192.168.1.20", Port: 8787, Fingerprint: "abc123",
		QRPNG: []byte{0x89, 'P', 'N', 'G'}, ExpiresAt: exp, OtherHosts: []string{"10.0.0.5"},
		PlayURL: "https://play.google.com/store/apps/details?id=com.jn.winremote",
	})
	if err != nil {
		t.Fatal(err)
	}
	s := string(html)
	for _, want := range []string{
		"AB12-CD34", "192.168.1.20", "8787", "abc123", "10.0.0.5",
		"data:image/png;base64,iVBORw==", "1800000000000", "QR Tara", "aynı Wi-Fi",
		"https://play.google.com/store/apps/details?id=com.jn.winremote",
	} {
		if !strings.Contains(s, want) {
			t.Errorf("pairing page is missing %q", want)
		}
	}
}
