package agent

import (
	"fmt"

	qrcode "github.com/skip2/go-qrcode"
)

// PairingQRPayload is the out-of-band pairing payload encoded in the QR
// code shown on the Windows machine's screen (PROTOCOL.md §2: "the
// pairing code and the cert fingerprint must both be obtained by the user
// directly from the Windows machine (QR code shown on screen, or the
// `agent devices` CLI output)"). PROTOCOL.md does not define an exact QR
// payload schema, so this implementation's best-effort format is a small
// JSON object carrying everything the phone needs to dial in and pin the
// cert without the user retyping anything long; this is flagged in the
// README as something the Android side should confirm/align on.
type PairingQRPayload struct {
	Host        string `json:"host"`
	Port        int    `json:"port"`
	PairingCode string `json:"pairing_code"`
	FingerprintSHA256 string `json:"fingerprint_sha256"`
}

// RenderPairingQRPNG renders payload as a QR code PNG at the given size.
func RenderPairingQRPNG(text string, size int) ([]byte, error) {
	png, err := qrcode.Encode(text, qrcode.Medium, size)
	if err != nil {
		return nil, fmt.Errorf("rendering QR code: %w", err)
	}
	return png, nil
}
