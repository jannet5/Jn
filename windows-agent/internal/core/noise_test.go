package core

import "testing"

func TestMatchGlob_NodeModules(t *testing.T) {
	pattern := `**\node_modules\**`
	cases := map[string]bool{
		`C:\Users\jake\project\node_modules\lib\index.js`: true,
		`C:\Users\jake\node_modules\pkg\readme.md`:         true,
		`C:\Users\jake\project\src\index.js`:                false,
	}
	for path, want := range cases {
		if got := MatchGlob(pattern, path); got != want {
			t.Errorf("MatchGlob(%q, %q) = %v, want %v", pattern, path, got, want)
		}
	}
}

func TestMatchGlob_DotGit(t *testing.T) {
	pattern := `**\.git\**`
	if !MatchGlob(pattern, `C:\repo\.git\objects\ab\cd`) {
		t.Error("expected .git subtree to match")
	}
	if MatchGlob(pattern, `C:\repo\gitignore.txt`) {
		t.Error("gitignore.txt must not match .git pattern")
	}
}

func TestMatchGlob_AppDataLocalTemp(t *testing.T) {
	pattern := `**\AppData\Local\Temp\**`
	if !MatchGlob(pattern, `C:\Users\jake\AppData\Local\Temp\abc123.tmp`) {
		t.Error("expected AppData\\Local\\Temp subtree to match")
	}
	if MatchGlob(pattern, `C:\Users\jake\AppData\Roaming\Temp\abc.tmp`) {
		t.Error("AppData\\Roaming\\Temp must not match the Local\\Temp pattern")
	}
}

func TestMatchGlob_RecycleBin(t *testing.T) {
	pattern := `**\$Recycle.Bin\**`
	if !MatchGlob(pattern, `C:\$Recycle.Bin\S-1-5-21\file.txt`) {
		t.Error("expected $Recycle.Bin subtree to match")
	}
}

func TestMatchGlob_SystemVolumeInformation(t *testing.T) {
	pattern := `**\System Volume Information\**`
	if !MatchGlob(pattern, `C:\System Volume Information\tracking.log`) {
		t.Error("expected System Volume Information subtree to match")
	}
}

func TestMatchGlob_BareExtensionMatchesBasenameAnywhere(t *testing.T) {
	pattern := `*.tmp`
	cases := []string{
		`C:\Users\jake\Downloads\file.tmp`,
		`C:\a\b\c\d\deep.tmp`,
		`file.tmp`,
	}
	for _, path := range cases {
		if !MatchGlob(pattern, path) {
			t.Errorf("expected %q to match *.tmp", path)
		}
	}
	if MatchGlob(pattern, `C:\Users\jake\file.tmpx`) {
		t.Error("file.tmpx must not match *.tmp")
	}
	if MatchGlob(pattern, `C:\Users\jake\notatmp.txt`) {
		t.Error("notatmp.txt must not match *.tmp")
	}
}

func TestMatchGlob_BareLogExtension(t *testing.T) {
	if !MatchGlob(`*.log`, `C:\ProgramData\app\debug.log`) {
		t.Error("expected debug.log to match *.log")
	}
}

func TestMatchGlob_PackagesTempState(t *testing.T) {
	pattern := `**\AppData\Local\Packages\**\TempState\**`
	path := `C:\Users\jake\AppData\Local\Packages\Microsoft.WindowsTerminal_8wekyb3d8bbwe\TempState\cache.bin`
	if !MatchGlob(pattern, path) {
		t.Errorf("expected %q to match Packages TempState pattern", path)
	}
}

func TestMatchGlob_CaseInsensitive(t *testing.T) {
	if !MatchGlob(`**\NODE_MODULES\**`, `c:\users\jake\node_modules\x`) {
		t.Error("matching must be case-insensitive")
	}
}

func TestIsIgnored_AdditiveList(t *testing.T) {
	patterns := DefaultIgnoreGlobs()
	if !IsIgnored(`C:\repo\node_modules\x\y.js`, patterns) {
		t.Error("expected node_modules path to be ignored by default globs")
	}
	if IsIgnored(`C:\Users\jake\Documents\report.docx`, patterns) {
		t.Error("an ordinary document must not be ignored by default")
	}
	custom := append(append([]string{}, patterns...), `**\build\**`)
	if !IsIgnored(`C:\repo\build\out.dll`, custom) {
		t.Error("expected custom additive glob to also be honored")
	}
}

func TestMatchGlob_QuestionMark(t *testing.T) {
	if !MatchGlob(`file?.txt`, `file1.txt`) {
		t.Error("? should match exactly one character")
	}
	if MatchGlob(`file?.txt`, `file12.txt`) {
		t.Error("? should not match two characters")
	}
}
