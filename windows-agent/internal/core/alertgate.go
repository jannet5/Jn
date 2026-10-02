package core

import "sync"

// AlertGate turns the level-style checks in alerts.go (evaluated on every
// metrics tick, snapshot or file event) into the crossing events PROTOCOL.md
// §8 describes: a (rule, subject) pair is reported once when it starts
// holding, again only if its severity escalates, and re-armed once it stops
// holding. Without this, a disk sitting below the free-space threshold raised
// a new alert every 2 seconds, and every write event on a large file raised
// another large_file alert.
//
// The zero value is ready to use and safe for concurrent use.
type AlertGate struct {
	mu     sync.Mutex
	active map[string]string // key -> severity last reported
}

// Observe records whether the condition identified by key currently holds
// and returns true when an alert should be raised for it now.
func (g *AlertGate) Observe(key string, triggered bool, severity string) bool {
	g.mu.Lock()
	defer g.mu.Unlock()
	if !triggered {
		delete(g.active, key)
		return false
	}
	if prev, ok := g.active[key]; ok && severityRank(severity) <= severityRank(prev) {
		return false
	}
	if g.active == nil {
		g.active = make(map[string]string)
	}
	g.active[key] = severity
	return true
}

func severityRank(s string) int {
	if s == SeverityCritical {
		return 2
	}
	return 1
}
