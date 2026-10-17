package core

import "testing"

func TestExpandPathVars(t *testing.T) {
	t.Setenv("WRM_TEST_HOME", `C:\Users\Çağrı`)
	cases := map[string]string{
		`%WRM_TEST_HOME%\Downloads`: `C:\Users\Çağrı\Downloads`,
		`D:\Data`:                   `D:\Data`,
		`%WRM_NOT_SET_XYZ%\x`:       `%WRM_NOT_SET_XYZ%\x`,
		`50%off\%WRM_TEST_HOME%`:    `50%off\C:\Users\Çağrı`,
	}
	for in, want := range cases {
		if got := ExpandPathVars(in); got != want {
			t.Errorf("ExpandPathVars(%q) = %q, want %q", in, got, want)
		}
	}
}
