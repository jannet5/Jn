package core

import (
	"crypto/rand"
	"fmt"
	"sync"
	"time"
)

// PairingCodeTTL is the expiry window from PROTOCOL.md §2 ("expire after 5
// minutes").
const PairingCodeTTL = 5 * time.Minute

// MaxPairAttempts is PROTOCOL.md §2's lockout threshold ("locks out ...
// after 5 failed pair_request attempts").
const MaxPairAttempts = 5

// pairingCodeAlphabet excludes visually ambiguous characters (0/O, 1/I/L)
// to keep codes easy to read off a screen and type on a phone.
const pairingCodeAlphabet = "23456789ABCDEFGHJKMNPQRSTUVWXYZ"

// GeneratePairingCode returns a random code in "XXXX-XXXX" form, e.g.
// "7F3K-9QRT", as shown in PROTOCOL.md §4.2.
func GeneratePairingCode() (string, error) {
	buf := make([]byte, 8)
	if _, err := rand.Read(buf); err != nil {
		return "", fmt.Errorf("generating pairing code: %w", err)
	}
	out := make([]byte, 9)
	for i := 0; i < 8; i++ {
		if i == 4 {
			out[i] = '-'
		}
		idx := int(buf[i]) % len(pairingCodeAlphabet)
		pos := i
		if i >= 4 {
			pos = i + 1
		}
		out[pos] = pairingCodeAlphabet[idx]
	}
	return string(out), nil
}

// PairingCodeStore persists issued pairing codes. This exists (rather than
// an in-memory map) because `agent --pair` and the long-running `agent`
// server are two separate OS processes that cannot share Go in-memory
// state: a code issued by a short-lived `--pair` invocation must still be
// visible to the already-running (or about-to-be-started) server process
// that will actually receive the phone's pair_request. The agent package's
// sqlite-backed Store implements this interface; tests use a small
// in-memory fake (see pairing_test.go) to keep this package free of any
// storage-engine dependency.
type PairingCodeStore interface {
	PutPairingCode(code string, createdAt, expiresAt time.Time) error
	GetPairingCode(code string) (createdAt, expiresAt time.Time, used bool, found bool, err error)
	MarkPairingCodeUsed(code string) error
	PurgeExpiredPairingCodes(now time.Time) error
}

// PairingManager validates pair_request attempts against codes held in a
// PairingCodeStore, plus the per-connection failed-attempt counters that
// trigger a hard lockout (PROTOCOL.md §2). It holds no network state; the
// WS layer calls into it and acts on the result (e.g. closing the socket
// on LockedOut).
type PairingManager struct {
	mu       sync.Mutex
	store    PairingCodeStore
	attempts *SimpleCounter
}

func NewPairingManager(store PairingCodeStore) *PairingManager {
	return &PairingManager{
		store:    store,
		attempts: NewSimpleCounter(MaxPairAttempts),
	}
}

// IssueCode generates and persists a new pairing code, valid from now for
// PairingCodeTTL. This is what `agent --pair` calls; the server process
// that later validates a pair_request against this code may be a
// different OS process than the one that issued it.
func (pm *PairingManager) IssueCode(now time.Time) (string, error) {
	code, err := GeneratePairingCode()
	if err != nil {
		return "", err
	}
	if err := pm.store.PutPairingCode(code, now, now.Add(PairingCodeTTL)); err != nil {
		return "", fmt.Errorf("persisting pairing code: %w", err)
	}
	return code, nil
}

// PairOutcome is the result of validating a pair_request attempt.
type PairOutcome int

const (
	PairOK PairOutcome = iota
	PairInvalidCode
	PairExpiredCode
	PairLockedOut // connectionKey already hit MaxPairAttempts
)

func (o PairOutcome) Reason() string {
	switch o {
	case PairInvalidCode:
		return ReasonInvalidCode
	case PairExpiredCode:
		return ReasonExpiredCode
	case PairLockedOut:
		return ReasonLockedOut
	default:
		return ""
	}
}

// Attempt validates pairing_code for the given connectionKey (typically a
// per-WS-connection identifier, so lockout is scoped to that connection as
// the spec's "closes the socket" implies). On success the code is marked
// used (single-use). On failure the connection's failed-attempt counter is
// incremented; once it reaches MaxPairAttempts the code is also
// invalidated and PairLockedOut is returned so the caller closes the
// socket.
func (pm *PairingManager) Attempt(connectionKey, code string, now time.Time) PairOutcome {
	pm.mu.Lock()
	defer pm.mu.Unlock()

	if locked, _ := pm.isLockedLocked(connectionKey); locked {
		return PairLockedOut
	}

	_, expiresAt, used, found, err := pm.store.GetPairingCode(code)
	if err != nil {
		// A storage error is indistinguishable from "no such code" from
		// the caller's point of view; never treat it as success.
		found = false
	}
	valid := found && !used && now.Before(expiresAt)
	expired := found && !used && !now.Before(expiresAt)

	if valid {
		_ = pm.store.MarkPairingCodeUsed(code)
		pm.attempts.Reset(connectionKey)
		return PairOK
	}

	exceeded := pm.attempts.Increment(connectionKey)
	if found {
		// Invalidate a real-but-wrong-state code so it can't be retried,
		// per "the underlying code is invalidated" on lockout; also
		// invalidate an expired code outright since it can never succeed.
		if expired || exceeded {
			_ = pm.store.MarkPairingCodeUsed(code)
		}
	}
	if exceeded {
		return PairLockedOut
	}
	if expired {
		return PairExpiredCode
	}
	return PairInvalidCode
}

func (pm *PairingManager) isLockedLocked(connectionKey string) (bool, int) {
	n := pm.attempts.Count(connectionKey)
	return n >= MaxPairAttempts, n
}

// IsLockedOut reports whether connectionKey has already hit the attempt
// cap (e.g. to refuse further pair_request messages on an already-closed
// path defensively).
func (pm *PairingManager) IsLockedOut(connectionKey string) bool {
	pm.mu.Lock()
	defer pm.mu.Unlock()
	locked, _ := pm.isLockedLocked(connectionKey)
	return locked
}

// PurgeExpired drops spent/expired codes from the store, so it doesn't
// grow unbounded on a long-running agent. Safe to call periodically.
func (pm *PairingManager) PurgeExpired(now time.Time) error {
	return pm.store.PurgeExpiredPairingCodes(now)
}
