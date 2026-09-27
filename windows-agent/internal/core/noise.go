package core

import (
	"regexp"
	"strings"
	"sync"
)

// Noise-filter glob matching for file events (PROTOCOL.md §7).
//
// PROTOCOL.md gives Windows-style path globs such as `**\node_modules\**`
// and bare-name globs such as `*.tmp`. The spec does not spell out glob
// semantics precisely, so this is a documented best-effort interpretation
// (flagged in the final report, not a spec edit):
//
//   - `**` matches any number of path segments (including zero), i.e. it
//     can span multiple `\`-separated components.
//   - A single `*` matches within one path segment only (never crosses a
//     `\`).
//   - `?` matches exactly one character (not a separator).
//   - Matching is case-insensitive, since Windows paths are.
//   - A pattern containing no path separator (e.g. `*.tmp`, `*.log`) is
//     matched against the path's *base name* only, not the full path —
//     this mirrors common ignore-file conventions (.gitignore-style) and
//     is the only reading under which `*.tmp` usefully ignores temp files
//     anywhere on disk rather than only a file literally named "x.tmp" at
//     the watch root.
//   - A pattern containing a separator is matched against the full path.
//   - Both `\` and `/` are treated as path separators when matching, so
//     the same glob works whether paths arrive with Windows or POSIX
//     separators (useful for tests on this Linux dev box; the production
//     agent always deals in Windows paths).

var globCache = struct {
	sync.Mutex
	m map[string]*regexp.Regexp
}{m: make(map[string]*regexp.Regexp)}

func compileGlob(pattern string) *regexp.Regexp {
	globCache.Lock()
	if re, ok := globCache.m[pattern]; ok {
		globCache.Unlock()
		return re
	}
	globCache.Unlock()

	re := regexp.MustCompile("(?i)^" + globToRegexBody(pattern) + "$")

	globCache.Lock()
	globCache.m[pattern] = re
	globCache.Unlock()
	return re
}

func globToRegexBody(pattern string) string {
	// Normalize separators to a single canonical form for translation.
	pattern = strings.ReplaceAll(pattern, "/", `\`)

	var sb strings.Builder
	runes := []rune(pattern)
	for i := 0; i < len(runes); i++ {
		c := runes[i]
		switch c {
		case '*':
			if i+1 < len(runes) && runes[i+1] == '*' {
				// "**" : match across separators, any number of segments.
				sb.WriteString(`.*`)
				i++ // consume second '*'
				// Optionally swallow a following separator so
				// "**\foo" also matches "foo" at the very start, and
				// "foo\**" matches "foo" at the very end.
				continue
			}
			sb.WriteString(`[^\\/]*`)
		case '?':
			sb.WriteString(`[^\\/]`)
		case '\\':
			sb.WriteString(`[\\/]`)
		default:
			sb.WriteString(regexp.QuoteMeta(string(c)))
		}
	}
	return sb.String()
}

// hasSeparator reports whether pattern contains a path separator.
func hasSeparator(pattern string) bool {
	return strings.ContainsAny(pattern, `\/`)
}

func baseName(path string) string {
	path = strings.ReplaceAll(path, "/", `\`)
	if idx := strings.LastIndex(path, `\`); idx >= 0 {
		return path[idx+1:]
	}
	return path
}

// MatchGlob reports whether a single glob pattern matches path, per the
// rules documented above.
func MatchGlob(pattern, path string) bool {
	re := compileGlob(pattern)
	if hasSeparator(pattern) {
		normalized := strings.ReplaceAll(path, "/", `\`)
		return re.MatchString(normalized)
	}
	return re.MatchString(baseName(path))
}

// IsIgnored reports whether path matches any of patterns (PROTOCOL.md §7's
// "configurable, additive" ignore list).
func IsIgnored(path string, patterns []string) bool {
	for _, p := range patterns {
		if MatchGlob(p, path) {
			return true
		}
	}
	return false
}
