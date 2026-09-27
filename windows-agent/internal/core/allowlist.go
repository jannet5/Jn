package core

import (
	"encoding/json"
	"fmt"
	"os"
)

// AllowedApp is one entry of allowed_apps.json (PROTOCOL.md §6).
type AllowedApp struct {
	AppID string `json:"app_id"`
	Label string `json:"label"`
	Path  string `json:"path"`
}

// AllowList is the parsed, validated set of launchable apps, keyed by
// app_id for O(1) lookup at launch_app time.
type AllowList struct {
	apps map[string]AllowedApp
	// order preserves file order for list_allowed_apps responses.
	order []string
}

// NewAllowList builds an AllowList from parsed entries, rejecting
// duplicate app_ids and empty app_id/path fields so a malformed config
// fails loudly at load time rather than silently misbehaving at launch
// time.
func NewAllowList(apps []AllowedApp) (*AllowList, error) {
	al := &AllowList{apps: make(map[string]AllowedApp, len(apps))}
	for _, a := range apps {
		if a.AppID == "" {
			return nil, fmt.Errorf("allowed app entry with empty app_id: %+v", a)
		}
		if a.Path == "" {
			return nil, fmt.Errorf("allowed app %q has empty path", a.AppID)
		}
		if _, dup := al.apps[a.AppID]; dup {
			return nil, fmt.Errorf("duplicate app_id %q in allow-list", a.AppID)
		}
		al.apps[a.AppID] = a
		al.order = append(al.order, a.AppID)
	}
	return al, nil
}

// ParseAllowList parses allowed_apps.json content.
func ParseAllowList(data []byte) (*AllowList, error) {
	var apps []AllowedApp
	if err := json.Unmarshal(data, &apps); err != nil {
		return nil, fmt.Errorf("parsing allow-list JSON: %w", err)
	}
	return NewAllowList(apps)
}

// LoadAllowList reads and parses allowed_apps.json from disk.
func LoadAllowList(path string) (*AllowList, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, fmt.Errorf("reading allow-list %s: %w", path, err)
	}
	return ParseAllowList(data)
}

// Resolve validates a launch_app request's app_id against the allow-list.
// This is the sole gate PROTOCOL.md §6 requires: "There is no protocol
// message to add/modify this list remotely." The server must call this
// (never trust a client-supplied path) before ever exec'ing anything.
func (al *AllowList) Resolve(appID string) (AllowedApp, bool) {
	if al == nil {
		return AllowedApp{}, false
	}
	a, ok := al.apps[appID]
	return a, ok
}

// List returns all allowed apps in file order, for list_allowed_apps.
func (al *AllowList) List() []AllowedApp {
	if al == nil {
		return nil
	}
	out := make([]AllowedApp, 0, len(al.order))
	for _, id := range al.order {
		out = append(out, al.apps[id])
	}
	return out
}
