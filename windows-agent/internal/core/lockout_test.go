package core

import (
	"testing"
	"time"
)

func TestBackoffLockout_NoLockoutBeforeThreshold(t *testing.T) {
	b := NewBackoffLockout(time.Second, time.Minute, 3)
	now := time.Now()
	for i := 0; i < 3; i++ {
		b.RecordFailure("dev1", now)
	}
	locked, _ := b.IsLockedOut("dev1", now)
	if locked {
		t.Error("should not be locked out at or below threshold")
	}
}

func TestBackoffLockout_LocksOutAfterThresholdAndExpires(t *testing.T) {
	b := NewBackoffLockout(time.Second, time.Minute, 2)
	now := time.Now()
	b.RecordFailure("dev1", now)
	b.RecordFailure("dev1", now)
	until := b.RecordFailure("dev1", now) // 3rd failure, 1 over threshold -> base delay
	if until.IsZero() {
		t.Fatal("expected a lockout time after exceeding threshold")
	}
	locked, _ := b.IsLockedOut("dev1", now)
	if !locked {
		t.Error("expected locked out immediately after exceeding threshold")
	}
	// After the lockout window passes, no longer locked.
	later := until.Add(time.Millisecond)
	locked2, _ := b.IsLockedOut("dev1", later)
	if locked2 {
		t.Error("expected lockout to expire")
	}
}

func TestBackoffLockout_ExponentialGrowthCappedAtMax(t *testing.T) {
	b := NewBackoffLockout(time.Second, 10*time.Second, 1)
	now := time.Now()
	b.RecordFailure("dev1", now) // 1: under/at threshold, no lock
	u1 := b.RecordFailure("dev1", now) // 2: 1 over threshold -> base (1s)
	u2 := b.RecordFailure("dev1", now) // 3: 2 over -> 2s
	u3 := b.RecordFailure("dev1", now) // 4: 3 over -> 4s
	d1 := u1.Sub(now)
	d2 := u2.Sub(now)
	d3 := u3.Sub(now)
	if d1 <= 0 || d2 <= d1 || d3 <= d2 {
		t.Errorf("expected strictly increasing backoff, got %v %v %v", d1, d2, d3)
	}
	// Keep hammering failures; delay must never exceed max (10s).
	var last time.Time
	for i := 0; i < 20; i++ {
		last = b.RecordFailure("dev1", now)
	}
	if last.Sub(now) > 10*time.Second {
		t.Errorf("backoff exceeded max: %v", last.Sub(now))
	}
}

func TestBackoffLockout_PerKeyIsolation(t *testing.T) {
	b := NewBackoffLockout(time.Second, time.Minute, 1)
	now := time.Now()
	b.RecordFailure("deviceA", now)
	b.RecordFailure("deviceA", now)
	lockedA, _ := b.IsLockedOut("deviceA", now)
	lockedB, _ := b.IsLockedOut("deviceB", now)
	if !lockedA {
		t.Error("deviceA should be locked out")
	}
	if lockedB {
		t.Error("deviceB (unrelated key, e.g. different IP) should not be affected")
	}
}

func TestBackoffLockout_SuccessResetsFailures(t *testing.T) {
	b := NewBackoffLockout(time.Second, time.Minute, 1)
	now := time.Now()
	b.RecordFailure("dev1", now)
	b.RecordFailure("dev1", now)
	b.RecordSuccess("dev1")
	if b.Failures("dev1") != 0 {
		t.Errorf("expected failures reset to 0, got %d", b.Failures("dev1"))
	}
	locked, _ := b.IsLockedOut("dev1", now)
	if locked {
		t.Error("should not be locked out after success reset")
	}
}

func TestSimpleCounter_ExceedsAtLimit(t *testing.T) {
	c := NewSimpleCounter(5)
	for i := 0; i < 4; i++ {
		if c.Increment("conn1") {
			t.Fatalf("should not exceed before 5th failure (i=%d)", i)
		}
	}
	if !c.Increment("conn1") {
		t.Error("5th failure should trigger lockout")
	}
	if c.Count("conn1") != 5 {
		t.Errorf("count = %d, want 5", c.Count("conn1"))
	}
}

func TestSimpleCounter_ResetAndIsolation(t *testing.T) {
	c := NewSimpleCounter(5)
	c.Increment("conn1")
	c.Increment("conn1")
	c.Reset("conn1")
	if c.Count("conn1") != 0 {
		t.Errorf("count after reset = %d, want 0", c.Count("conn1"))
	}
	if c.Count("conn2") != 0 {
		t.Errorf("unrelated key should start at 0, got %d", c.Count("conn2"))
	}
}
