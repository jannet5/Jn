package core

import (
	"sync"
	"time"
)

// lockoutState tracks failures for one key (a device_id or a source IP).
type lockoutState struct {
	failures    int
	lockedUntil time.Time
}

// BackoffLockout implements a generic, per-key exponential-backoff lockout
// used for auth failures (PROTOCOL.md §2: "Auth failures are rate-limited
// per device_id and per source IP (exponential lockout, capped)"). It is
// pure (caller supplies "now"), so it's fully deterministic in tests.
type BackoffLockout struct {
	mu        sync.Mutex
	state     map[string]*lockoutState
	base      time.Duration
	max       time.Duration
	threshold int // failures allowed before backoff kicks in
}

// NewBackoffLockout creates a tracker. threshold is the number of failures
// allowed before any lockout is applied; every failure after that doubles
// the lockout duration (base, base*2, base*4, ...) up to max.
func NewBackoffLockout(base, max time.Duration, threshold int) *BackoffLockout {
	return &BackoffLockout{
		state:     make(map[string]*lockoutState),
		base:      base,
		max:       max,
		threshold: threshold,
	}
}

// RecordFailure registers a failed attempt for key at time now and returns
// the time until which key is now locked out (zero time if not locked).
func (b *BackoffLockout) RecordFailure(key string, now time.Time) time.Time {
	b.mu.Lock()
	defer b.mu.Unlock()
	s, ok := b.state[key]
	if !ok {
		s = &lockoutState{}
		b.state[key] = s
	}
	s.failures++
	if s.failures > b.threshold {
		shift := s.failures - b.threshold - 1
		delay := b.base
		for i := 0; i < shift; i++ {
			delay *= 2
			if delay >= b.max {
				delay = b.max
				break
			}
		}
		if delay > b.max {
			delay = b.max
		}
		s.lockedUntil = now.Add(delay)
	}
	return s.lockedUntil
}

// RecordSuccess clears all failure state for key (a successful auth resets
// the counter).
func (b *BackoffLockout) RecordSuccess(key string) {
	b.mu.Lock()
	defer b.mu.Unlock()
	delete(b.state, key)
}

// IsLockedOut reports whether key is currently locked out at time now, and
// until when.
func (b *BackoffLockout) IsLockedOut(key string, now time.Time) (bool, time.Time) {
	b.mu.Lock()
	defer b.mu.Unlock()
	s, ok := b.state[key]
	if !ok {
		return false, time.Time{}
	}
	if s.lockedUntil.IsZero() || now.After(s.lockedUntil) || now.Equal(s.lockedUntil) {
		return false, s.lockedUntil
	}
	return true, s.lockedUntil
}

// Failures returns the current failure count for key (0 if unknown).
func (b *BackoffLockout) Failures(key string) int {
	b.mu.Lock()
	defer b.mu.Unlock()
	if s, ok := b.state[key]; ok {
		return s.failures
	}
	return 0
}

// SimpleCounter is a hard-cap failure counter (no backoff, no expiry): used
// for pairing attempts, where PROTOCOL.md §2 specifies a flat rule ("locks
// out an offering connection after 5 failed pair_request attempts (closes
// the socket; the underlying code is invalidated)") rather than exponential
// backoff.
type SimpleCounter struct {
	mu    sync.Mutex
	count map[string]int
	limit int
}

func NewSimpleCounter(limit int) *SimpleCounter {
	return &SimpleCounter{count: make(map[string]int), limit: limit}
}

// Increment records a failure for key and reports whether key has now hit
// or exceeded the limit (i.e. the connection must be locked out / closed).
func (c *SimpleCounter) Increment(key string) (exceeded bool) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.count[key]++
	return c.count[key] >= c.limit
}

func (c *SimpleCounter) Count(key string) int {
	c.mu.Lock()
	defer c.mu.Unlock()
	return c.count[key]
}

func (c *SimpleCounter) Reset(key string) {
	c.mu.Lock()
	defer c.mu.Unlock()
	delete(c.count, key)
}
