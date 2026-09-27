package core

import (
	"bytes"
	"testing"
)

func TestGenerateNonce_SizeAndUniqueness(t *testing.T) {
	n1, err := GenerateNonce()
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(n1) != NonceSize {
		t.Errorf("nonce len = %d, want %d", len(n1), NonceSize)
	}
	n2, _ := GenerateNonce()
	if bytes.Equal(n1, n2) {
		t.Error("two generated nonces should not be equal")
	}
}

func TestGenerateDeviceSecret_Size(t *testing.T) {
	s, err := GenerateDeviceSecret()
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if len(s) != DeviceSecretSize {
		t.Errorf("secret len = %d, want %d", len(s), DeviceSecretSize)
	}
}

func TestComputeAndVerifyAuthHMAC_RoundTrip(t *testing.T) {
	secret, _ := GenerateDeviceSecret()
	nonce, _ := GenerateNonce()
	deviceID := "device-123"
	mac := ComputeAuthHMAC(secret, nonce, deviceID)
	if !VerifyAuthHMAC(secret, nonce, deviceID, mac) {
		t.Error("expected valid HMAC to verify")
	}
}

func TestVerifyAuthHMAC_WrongSecretFails(t *testing.T) {
	secret, _ := GenerateDeviceSecret()
	otherSecret, _ := GenerateDeviceSecret()
	nonce, _ := GenerateNonce()
	mac := ComputeAuthHMAC(secret, nonce, "device-1")
	if VerifyAuthHMAC(otherSecret, nonce, "device-1", mac) {
		t.Error("HMAC computed with a different secret must not verify")
	}
}

func TestVerifyAuthHMAC_WrongDeviceIDFails(t *testing.T) {
	secret, _ := GenerateDeviceSecret()
	nonce, _ := GenerateNonce()
	mac := ComputeAuthHMAC(secret, nonce, "device-1")
	if VerifyAuthHMAC(secret, nonce, "device-2", mac) {
		t.Error("HMAC bound to device-1 must not verify for device-2 (prevents replay across devices)")
	}
}

func TestVerifyAuthHMAC_WrongNonceFails(t *testing.T) {
	secret, _ := GenerateDeviceSecret()
	nonce1, _ := GenerateNonce()
	nonce2, _ := GenerateNonce()
	mac := ComputeAuthHMAC(secret, nonce1, "device-1")
	if VerifyAuthHMAC(secret, nonce2, "device-1", mac) {
		t.Error("HMAC bound to nonce1 must not verify against nonce2 (prevents replay across sessions)")
	}
}

func TestVerifyAuthHMAC_TamperedMACFails(t *testing.T) {
	secret, _ := GenerateDeviceSecret()
	nonce, _ := GenerateNonce()
	mac := ComputeAuthHMAC(secret, nonce, "device-1")
	tampered := append([]byte{}, mac...)
	tampered[0] ^= 0xFF
	if VerifyAuthHMAC(secret, nonce, "device-1", tampered) {
		t.Error("tampered HMAC must not verify")
	}
}

func TestHashDeviceSecret_DeterministicAndSensitive(t *testing.T) {
	key, _ := GenerateServerKey()
	secret, _ := GenerateDeviceSecret()
	h1 := HashDeviceSecret(key, secret)
	h2 := HashDeviceSecret(key, secret)
	if !bytes.Equal(h1, h2) {
		t.Error("hashing is deterministic for the same key+secret")
	}
	otherSecret, _ := GenerateDeviceSecret()
	h3 := HashDeviceSecret(key, otherSecret)
	if bytes.Equal(h1, h3) {
		t.Error("different secrets must hash differently")
	}
}

func TestVerifyDeviceSecretHash(t *testing.T) {
	key, _ := GenerateServerKey()
	secret, _ := GenerateDeviceSecret()
	stored := HashDeviceSecret(key, secret)
	if !VerifyDeviceSecretHash(key, secret, stored) {
		t.Error("correct secret should verify against stored hash")
	}
	wrong, _ := GenerateDeviceSecret()
	if VerifyDeviceSecretHash(key, wrong, stored) {
		t.Error("wrong secret must not verify")
	}
}

func TestEncryptDecryptDeviceSecret_RoundTrip(t *testing.T) {
	key, _ := GenerateServerKey()
	secret, _ := GenerateDeviceSecret()
	ct, err := EncryptDeviceSecret(key, secret)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if bytes.Contains(ct, secret) {
		t.Error("ciphertext must not contain the plaintext secret")
	}
	pt, err := DecryptDeviceSecret(key, ct)
	if err != nil {
		t.Fatalf("unexpected decrypt error: %v", err)
	}
	if !bytes.Equal(pt, secret) {
		t.Error("decrypted secret must round-trip exactly (needed to compute challenge HMACs)")
	}
}

func TestDecryptDeviceSecret_WrongKeyFails(t *testing.T) {
	key, _ := GenerateServerKey()
	otherKey, _ := GenerateServerKey()
	secret, _ := GenerateDeviceSecret()
	ct, _ := EncryptDeviceSecret(key, secret)
	if _, err := DecryptDeviceSecret(otherKey, ct); err == nil {
		t.Error("decrypting with the wrong key must fail")
	}
}

func TestDecryptDeviceSecret_TamperedCiphertextFails(t *testing.T) {
	key, _ := GenerateServerKey()
	secret, _ := GenerateDeviceSecret()
	ct, _ := EncryptDeviceSecret(key, secret)
	ct[len(ct)-1] ^= 0xFF
	if _, err := DecryptDeviceSecret(key, ct); err == nil {
		t.Error("tampered ciphertext must fail authentication (GCM tag check)")
	}
}
