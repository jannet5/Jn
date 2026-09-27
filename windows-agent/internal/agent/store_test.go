package agent

import (
	"encoding/json"
	"path/filepath"
	"testing"
)

// TestStore_EmptyListsAreNeverNil is a regression test for a real
// interoperability bug found by actually running this agent against the
// real Android client's real protocol/crypto code (see
// LiveAgentEndToEndTest.kt in android-app/): Go's encoding/json marshals a
// nil slice as JSON `null`, not `[]`. The Android client's corresponding
// `items` fields are non-nullable Kotlin Lists, so a `null` there fails to
// decode (silently - decodeServerMessage swallows the exception) and the
// request that was waiting on it hangs until its own timeout, even though
// the agent DID reply.
//
// This asserts, for every Store query method whose result feeds a
// protocol `items` field, that an empty result set still marshals to
// `[]`, not `null`.
func TestStore_EmptyListsAreNeverNil(t *testing.T) {
	store, err := OpenStore(filepath.Join(t.TempDir(), "agent.db"))
	if err != nil {
		t.Fatalf("opening store: %v", err)
	}
	defer store.Close()

	t.Run("ListAlerts", func(t *testing.T) {
		items, err := store.ListAlerts(50)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		assertMarshalsToEmptyArrayNotNull(t, items)
	})

	t.Run("QueryHistory", func(t *testing.T) {
		items, _, err := store.QueryHistory(0, 1<<62, 50, 0)
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		assertMarshalsToEmptyArrayNotNull(t, items)
	})

	t.Run("QueryFileEvents", func(t *testing.T) {
		items, _, err := store.QueryFileEvents(FileEventQuery{ToTS: 1 << 62, Limit: 100})
		if err != nil {
			t.Fatalf("unexpected error: %v", err)
		}
		assertMarshalsToEmptyArrayNotNull(t, items)
	})
}

func assertMarshalsToEmptyArrayNotNull(t *testing.T, v interface{}) {
	t.Helper()
	if v == nil {
		t.Fatalf("expected a non-nil (possibly empty) slice, got a literal nil interface")
	}
	b, err := json.Marshal(v)
	if err != nil {
		t.Fatalf("marshaling: %v", err)
	}
	if string(b) != "[]" {
		t.Fatalf("expected an empty result set to marshal to \"[]\", got %q (a nil Go slice marshals to \"null\", which the real Android client's non-nullable List<T> fields fail to decode)", string(b))
	}
}
