package core

import (
	"crypto/aes"
	"crypto/cipher"
	"crypto/hmac"
	"crypto/rand"
	"crypto/sha256"
	"crypto/subtle"
	"fmt"
)

// NonceSize is PROTOCOL.md §4.3's challenge nonce size ("base64(16 bytes)").
const NonceSize = 16

// DeviceSecretSize is PROTOCOL.md §2's paired-device secret size ("random
// 256-bit secret").
const DeviceSecretSize = 32

// GenerateNonce returns a fresh random challenge nonce.
func GenerateNonce() ([]byte, error) {
	b := make([]byte, NonceSize)
	if _, err := rand.Read(b); err != nil {
		return nil, fmt.Errorf("generating nonce: %w", err)
	}
	return b, nil
}

// GenerateDeviceSecret returns a fresh random 256-bit device secret, given
// to the client exactly once at pair_success time.
func GenerateDeviceSecret() ([]byte, error) {
	b := make([]byte, DeviceSecretSize)
	if _, err := rand.Read(b); err != nil {
		return nil, fmt.Errorf("generating device secret: %w", err)
	}
	return b, nil
}

// ComputeAuthHMAC implements PROTOCOL.md §4.3's exact formula:
//
//	hmac = HMAC_SHA256(device_secret, nonce_bytes || ":" || device_id)
func ComputeAuthHMAC(deviceSecret, nonce []byte, deviceID string) []byte {
	mac := hmac.New(sha256.New, deviceSecret)
	mac.Write(nonce)
	mac.Write([]byte(":"))
	mac.Write([]byte(deviceID))
	return mac.Sum(nil)
}

// VerifyAuthHMAC checks a client-supplied HMAC against the expected value
// using a constant-time comparison (timing-attack defense in depth).
func VerifyAuthHMAC(deviceSecret, nonce []byte, deviceID string, candidate []byte) bool {
	expected := ComputeAuthHMAC(deviceSecret, nonce, deviceID)
	return subtle.ConstantTimeCompare(expected, candidate) == 1
}

// HashDeviceSecret is how the agent stores a paired device's secret at
// rest: "stored on the agent hashed (HMAC-SHA256 with a server-local key)"
// per PROTOCOL.md §2. serverKey is a secret generated once and persisted
// locally (never transmitted); it must never be the same value as any
// device secret.
func HashDeviceSecret(serverKey, deviceSecret []byte) []byte {
	mac := hmac.New(sha256.New, serverKey)
	mac.Write(deviceSecret)
	return mac.Sum(nil)
}

// VerifyDeviceSecretHash checks a candidate raw secret against a stored
// hash, in constant time.
func VerifyDeviceSecretHash(serverKey, candidateSecret, storedHash []byte) bool {
	got := HashDeviceSecret(serverKey, candidateSecret)
	return subtle.ConstantTimeCompare(got, storedHash) == 1
}

// GenerateServerKey returns a fresh random key used only to protect device
// secrets at rest (PROTOCOL.md §2). Generated once and persisted alongside
// the TLS cert/key.
func GenerateServerKey() ([]byte, error) {
	b := make([]byte, 32)
	if _, err := rand.Read(b); err != nil {
		return nil, fmt.Errorf("generating server key: %w", err)
	}
	return b, nil
}

// EncryptDeviceSecret and DecryptDeviceSecret exist to resolve a real
// contradiction in PROTOCOL.md §2/§4.3, flagged in this project's final
// report rather than edited in the spec file:
//
//   - §2 says the device secret is "stored on the agent hashed
//     (HMAC-SHA256 with a server-local key)" and never in plaintext.
//   - §4.3's challenge-response requires the agent to independently
//     compute HMAC_SHA256(device_secret, nonce||":"||device_id) and check
//     it against the client's value — which is only possible if the agent
//     can recover the *raw* device_secret. A one-way HMAC "hash" of the
//     secret (as in password storage) cannot be reversed, so it cannot
//     be used as the HMAC key for the challenge-response check.
//
// These two requirements are mutually exclusive as literally written: you
// cannot do symmetric challenge-response with only a one-way hash of the
// key. This implementation's best-effort resolution keeps the *spirit* of
// "never stored in plaintext" by storing the secret under authenticated
// symmetric encryption (AES-256-GCM) keyed by the same server-local key,
// which is reversible only by the agent itself and never leaves the
// machine. HashDeviceSecret/VerifyDeviceSecretHash above are kept and are
// suitable for a pure existence/integrity check that never needs to
// recover the secret; EncryptDeviceSecret/DecryptDeviceSecret are what the
// store actually persists and reads back for computing challenge HMACs.
func EncryptDeviceSecret(serverKey, deviceSecret []byte) ([]byte, error) {
	block, err := aes.NewCipher(serverKey)
	if err != nil {
		return nil, fmt.Errorf("aes cipher: %w", err)
	}
	gcm, err := cipher.NewGCM(block)
	if err != nil {
		return nil, fmt.Errorf("gcm: %w", err)
	}
	nonce := make([]byte, gcm.NonceSize())
	if _, err := rand.Read(nonce); err != nil {
		return nil, fmt.Errorf("nonce: %w", err)
	}
	return gcm.Seal(nonce, nonce, deviceSecret, nil), nil
}

func DecryptDeviceSecret(serverKey, ciphertext []byte) ([]byte, error) {
	block, err := aes.NewCipher(serverKey)
	if err != nil {
		return nil, fmt.Errorf("aes cipher: %w", err)
	}
	gcm, err := cipher.NewGCM(block)
	if err != nil {
		return nil, fmt.Errorf("gcm: %w", err)
	}
	if len(ciphertext) < gcm.NonceSize() {
		return nil, fmt.Errorf("ciphertext too short")
	}
	nonce, ct := ciphertext[:gcm.NonceSize()], ciphertext[gcm.NonceSize():]
	return gcm.Open(nil, nonce, ct, nil)
}
