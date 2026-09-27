package core

import (
	"regexp"
	"testing"
	"time"
)

var pairingCodeFormat = regexp.MustCompile(`^[23456789ABCDEFGHJKMNPQRSTUVWXYZ]{4}-[23456789ABCDEFGHJKMNPQRSTUVWXYZ]{4}$`)

func TestGeneratePairingCode_Format(t *testing.T) {
	for i := 0; i < 100; i++ {
		code, err := GeneratePairingCode()
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		if !pairingCodeFormat.MatchString(code) {
			t.Fatalf("code %q does not match expected XXXX-XXXX format", code)
		}
	}
}

func TestPairingManager_SuccessfulPair(t *testing.T) {
	pm := NewPairingManager()
	now := time.Now()
	code, err := pm.IssueCode(now)
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	outcome := pm.Attempt("conn1", code, now.Add(time.Second))
	if outcome != PairOK {
		t.Fatalf("outcome = %v, want PairOK", outcome)
	}
}

func TestPairingManager_SingleUse(t *testing.T) {
	pm := NewPairingManager()
	now := time.Now()
	code, _ := pm.IssueCode(now)
	if outcome := pm.Attempt("conn1", code, now); outcome != PairOK {
		t.Fatalf("first attempt should succeed, got %v", outcome)
	}
	if outcome := pm.Attempt("conn2", code, now); outcome == PairOK {
		t.Fatal("second attempt with same code must fail (single-use)")
	}
}

func TestPairingManager_ExpiredCode(t *testing.T) {
	pm := NewPairingManager()
	now := time.Now()
	code, _ := pm.IssueCode(now)
	later := now.Add(PairingCodeTTL + time.Second)
	outcome := pm.Attempt("conn1", code, later)
	if outcome != PairExpiredCode {
		t.Fatalf("outcome = %v, want PairExpiredCode", outcome)
	}
}

func TestPairingManager_InvalidCode(t *testing.T) {
	pm := NewPairingManager()
	now := time.Now()
	outcome := pm.Attempt("conn1", "0000-0000", now)
	if outcome != PairInvalidCode {
		t.Fatalf("outcome = %v, want PairInvalidCode", outcome)
	}
}

func TestPairingManager_LockoutAfter5FailedAttempts(t *testing.T) {
	pm := NewPairingManager()
	now := time.Now()
	for i := 0; i < 4; i++ {
		outcome := pm.Attempt("conn1", "BADCODE1", now)
		if outcome != PairInvalidCode {
			t.Fatalf("attempt %d: outcome = %v, want PairInvalidCode", i+1, outcome)
		}
	}
	// 5th failed attempt triggers lockout.
	outcome := pm.Attempt("conn1", "BADCODE1", now)
	if outcome != PairLockedOut {
		t.Fatalf("5th attempt: outcome = %v, want PairLockedOut", outcome)
	}
	if !pm.IsLockedOut("conn1") {
		t.Error("expected connection to be locked out")
	}
	// A correct code presented after lockout must still fail, since the
	// connection itself should be closed by the caller at this point.
	code, _ := pm.IssueCode(now)
	outcome2 := pm.Attempt("conn1", code, now)
	if outcome2 != PairLockedOut {
		t.Fatalf("post-lockout attempt = %v, want PairLockedOut", outcome2)
	}
}

func TestPairingManager_LockoutIsolatedPerConnection(t *testing.T) {
	pm := NewPairingManager()
	now := time.Now()
	for i := 0; i < 5; i++ {
		pm.Attempt("conn1", "BADCODE1", now)
	}
	if !pm.IsLockedOut("conn1") {
		t.Error("conn1 should be locked out")
	}
	if pm.IsLockedOut("conn2") {
		t.Error("conn2 should be unaffected by conn1's lockout")
	}
	// conn2 can still successfully pair.
	code, _ := pm.IssueCode(now)
	if outcome := pm.Attempt("conn2", code, now); outcome != PairOK {
		t.Fatalf("conn2 attempt = %v, want PairOK", outcome)
	}
}

func TestPairingManager_PurgeExpired(t *testing.T) {
	pm := NewPairingManager()
	now := time.Now()
	code, _ := pm.IssueCode(now)
	pm.PurgeExpired(now.Add(PairingCodeTTL + time.Minute))
	// After purge, even a fresh attempt within what would have been the
	// original window fails because the record is gone (further attempts
	// against a purged code are correctly reported as invalid, not OK).
	outcome := pm.Attempt("conn1", code, now.Add(time.Second))
	if outcome == PairOK {
		t.Error("purged code must not be usable")
	}
}
