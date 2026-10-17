package core

import (
	"os"
	"regexp"
)

var percentVar = regexp.MustCompile(`%([A-Za-z0-9_()]+)%`)

// ExpandPathVars expands Windows-style %VAR% references (e.g.
// "%USERPROFILE%\Downloads") from the agent's own environment, so one
// installer-written config.json works for whichever user runs the agent.
// Unknown variables are left as written.
func ExpandPathVars(p string) string {
	return percentVar.ReplaceAllStringFunc(p, func(m string) string {
		if v, ok := os.LookupEnv(m[1 : len(m)-1]); ok {
			return v
		}
		return m
	})
}
