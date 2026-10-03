/* Sahte powershell.exe: yalnız BAT sarmalayıcısının argüman ve çıkış kodu taşımasını Wine altında test etmek için. */
#include <windows.h>
#include <stdio.h>
#include <stdlib.h>
int wmain(int argc, wchar_t **argv) {
    wchar_t log[1024], kod[32];
    if (GetEnvironmentVariableW(L"FAKE_LOG", log, 1024)) {
        FILE *f = _wfopen(log, L"w, ccs=UTF-8");
        for (int i = 1; i < argc; i++) fwprintf(f, L"[%ls]\n", argv[i]);
        fclose(f);
    }
    int k = 0;
    if (GetEnvironmentVariableW(L"FAKE_KOD", kod, 32)) k = _wtoi(kod);
    return k;
}
