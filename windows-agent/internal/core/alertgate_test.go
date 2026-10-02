package core

import "testing"

func TestAlertGate_ReportsOncePerCrossing(t *testing.T) {
	var g AlertGate
	steps := []struct {
		triggered bool
		severity  string
		want      bool
	}{
		{true, SeverityWarning, true},   // starts holding
		{true, SeverityWarning, false},  // still holding: no repeat
		{true, SeverityCritical, true},  // escalates
		{true, SeverityWarning, false},  // de-escalation is not news
		{true, SeverityCritical, false}, // back to an already-reported level
		{false, "", false},              // clears and re-arms
		{true, SeverityWarning, true},   // a new crossing is reported again
	}
	for i, s := range steps {
		if got := g.Observe("low_free_space|C:\\", s.triggered, s.severity); got != s.want {
			t.Fatalf("step %d: Observe(%v, %q) = %v, want %v", i, s.triggered, s.severity, got, s.want)
		}
	}
}

func TestAlertGate_KeysAreIndependent(t *testing.T) {
	var g AlertGate
	if !g.Observe("low_free_space|C:\\", true, SeverityWarning) {
		t.Fatal("first C: crossing should be reported")
	}
	if !g.Observe("low_free_space|D:\\", true, SeverityWarning) {
		t.Fatal("D: is a different subject and should be reported too")
	}
}
