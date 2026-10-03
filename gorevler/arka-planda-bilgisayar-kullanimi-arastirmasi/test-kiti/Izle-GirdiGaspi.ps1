<#
.SYNOPSIS
  Bir AI "computer use" görevi sürerken fareyi / klavyeyi / ön plan penceresini
  kimin kullandığını ölçer. Windows'ta çalışır (Windows PowerShell 5.1 önerilir).

.DESCRIPTION
  - Düşük seviye fare ve klavye kancası (WH_MOUSE_LL / WH_KEYBOARD_LL) kurar.
    Her olayın "enjekte" (yazılımla, ör. SendInput ile üretilmiş) mi yoksa
    fiziksel mi olduğunu Windows'un LLMHF_INJECTED / LLKHF_INJECTED bayrağından
    okur. Hangi tuşa basıldığı KAYDEDİLMEZ; yalnız sayaç tutulur.
  - Her -OrnekMs milisaniyede bir: imleç konumu, ön plandaki pencere başlığı ve
    süreç adı, birikimli sayaçlar CSV'ye yazılır.
  - Bitince Analiz-Et.ps1 ile özet çıkarılır.

.EXAMPLE
  powershell -ExecutionPolicy Bypass -File .\Izle-GirdiGaspi.ps1 -Saniye 120
  (Bu sırada Codex/Claude'a computer use görevi verin ve kendi işinizi yapın.)
#>
param(
    [int]$Saniye = 120,
    [int]$OrnekMs = 250,
    [string]$Cikti = (Join-Path $PWD ("girdi-izleme-{0}.csv" -f (Get-Date -Format 'yyyyMMdd-HHmmss')))
)

if ($env:OS -ne 'Windows_NT') { throw 'Bu betik yalnız Windows üzerinde çalışır.' }

Add-Type -TypeDefinition @'
using System;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;

public static class GirdiIzleyici {
    const int WH_KEYBOARD_LL = 13, WH_MOUSE_LL = 14;
    const int WM_MOUSEMOVE = 0x0200, WM_LBUTTONDOWN = 0x0201, WM_RBUTTONDOWN = 0x0204, WM_MBUTTONDOWN = 0x0207, WM_MOUSEWHEEL = 0x020A;
    const int WM_KEYDOWN = 0x0100, WM_SYSKEYDOWN = 0x0104, WM_QUIT = 0x0012;
    const uint LLMHF_INJECTED = 0x01, LLMHF_LOWER_IL_INJECTED = 0x02, LLKHF_INJECTED = 0x10;

    [StructLayout(LayoutKind.Sequential)] public struct POINT { public int X, Y; }
    [StructLayout(LayoutKind.Sequential)] struct MSLLHOOKSTRUCT { public POINT pt; public uint mouseData, flags, time; public IntPtr extra; }
    [StructLayout(LayoutKind.Sequential)] struct KBDLLHOOKSTRUCT { public uint vk, scan, flags, time; public IntPtr extra; }
    [StructLayout(LayoutKind.Sequential)] struct MSG { public IntPtr hwnd; public uint message; public IntPtr w, l; public uint time; public POINT pt; }

    delegate IntPtr HookProc(int code, IntPtr w, IntPtr l);
    [DllImport("user32.dll", SetLastError=true)] static extern IntPtr SetWindowsHookEx(int id, HookProc fn, IntPtr mod, uint tid);
    [DllImport("user32.dll")] static extern bool UnhookWindowsHookEx(IntPtr h);
    [DllImport("user32.dll")] static extern IntPtr CallNextHookEx(IntPtr h, int code, IntPtr w, IntPtr l);
    [DllImport("user32.dll")] static extern int GetMessage(out MSG m, IntPtr hwnd, uint min, uint max);
    [DllImport("user32.dll")] static extern bool PostThreadMessage(uint tid, uint msg, IntPtr w, IntPtr l);
    [DllImport("kernel32.dll")] static extern uint GetCurrentThreadId();
    [DllImport("kernel32.dll")] static extern IntPtr GetModuleHandle(string n);
    [DllImport("user32.dll")] public static extern bool GetCursorPos(out POINT p);
    [DllImport("user32.dll")] public static extern IntPtr GetForegroundWindow();
    [DllImport("user32.dll", CharSet=CharSet.Unicode)] static extern int GetWindowText(IntPtr h, StringBuilder sb, int max);
    [DllImport("user32.dll")] public static extern uint GetWindowThreadProcessId(IntPtr h, out uint pid);

    public static long FizikselHareket, EnjekteHareket, FizikselTik, EnjekteTik, FizikselTus, EnjekteTus;
    static HookProc fareProc, klavyeProc;
    static Thread th; static uint thId;

    static IntPtr Fare(int code, IntPtr w, IntPtr l) {
        if (code >= 0) {
            var s = (MSLLHOOKSTRUCT)Marshal.PtrToStructure(l, typeof(MSLLHOOKSTRUCT));
            bool inj = (s.flags & (LLMHF_INJECTED | LLMHF_LOWER_IL_INJECTED)) != 0;
            int m = w.ToInt32();
            if (m == WM_MOUSEMOVE) { if (inj) Interlocked.Increment(ref EnjekteHareket); else Interlocked.Increment(ref FizikselHareket); }
            else if (m == WM_LBUTTONDOWN || m == WM_RBUTTONDOWN || m == WM_MBUTTONDOWN || m == WM_MOUSEWHEEL) {
                if (inj) Interlocked.Increment(ref EnjekteTik); else Interlocked.Increment(ref FizikselTik);
            }
        }
        return CallNextHookEx(IntPtr.Zero, code, w, l);
    }
    static IntPtr Klavye(int code, IntPtr w, IntPtr l) {
        if (code >= 0) {
            int m = w.ToInt32();
            if (m == WM_KEYDOWN || m == WM_SYSKEYDOWN) {
                var s = (KBDLLHOOKSTRUCT)Marshal.PtrToStructure(l, typeof(KBDLLHOOKSTRUCT));
                // Tuş kodu bilerek saklanmaz: yalnız sayılır.
                if ((s.flags & LLKHF_INJECTED) != 0) Interlocked.Increment(ref EnjekteTus); else Interlocked.Increment(ref FizikselTus);
            }
        }
        return CallNextHookEx(IntPtr.Zero, code, w, l);
    }
    public static void Baslat() {
        fareProc = Fare; klavyeProc = Klavye;
        var hazir = new ManualResetEvent(false);
        th = new Thread(() => {
            thId = GetCurrentThreadId();
            IntPtr mod = GetModuleHandle(null);
            IntPtr hm = SetWindowsHookEx(WH_MOUSE_LL, fareProc, mod, 0);
            IntPtr hk = SetWindowsHookEx(WH_KEYBOARD_LL, klavyeProc, mod, 0);
            hazir.Set();
            MSG msg;
            while (GetMessage(out msg, IntPtr.Zero, 0, 0) > 0) { }
            UnhookWindowsHookEx(hm); UnhookWindowsHookEx(hk);
        });
        th.IsBackground = true; th.Start(); hazir.WaitOne();
    }
    public static void Durdur() { if (th != null) { PostThreadMessage(thId, WM_QUIT, IntPtr.Zero, IntPtr.Zero); th.Join(2000); } }
    public static string Baslik(IntPtr h) { var sb = new StringBuilder(512); GetWindowText(h, sb, 512); return sb.ToString(); }
}
'@

$inv = [Globalization.CultureInfo]::InvariantCulture
$satirlar = New-Object System.Collections.Generic.List[string]
$satirlar.Add('zaman,x,y,onplan_surec,onplan_baslik,fiziksel_hareket,enjekte_hareket,fiziksel_tik,enjekte_tik,fiziksel_tus,enjekte_tus')

[GirdiIzleyici]::Baslat()
Write-Host ("İzleme başladı: {0} sn, örnek aralığı {1} ms. Çıktı: {2}" -f $Saniye, $OrnekMs, $Cikti)
Write-Host 'Şimdi AI computer use görevini başlatın ve kendi işinize devam edin. (Ctrl+C ile erken bitirebilirsiniz.)'
$bitis = (Get-Date).AddSeconds($Saniye)
try {
    while ((Get-Date) -lt $bitis) {
        $p = New-Object GirdiIzleyici+POINT
        [void][GirdiIzleyici]::GetCursorPos([ref]$p)
        $h = [GirdiIzleyici]::GetForegroundWindow()
        $procId = [uint32]0
        [void][GirdiIzleyici]::GetWindowThreadProcessId($h, [ref]$procId)
        $surec = try { (Get-Process -Id $procId -ErrorAction Stop).ProcessName } catch { '?' }
        $baslik = ([GirdiIzleyici]::Baslik($h)) -replace '"', "'"
        $satirlar.Add(('{0},{1},{2},"{3}","{4}",{5},{6},{7},{8},{9},{10}' -f
            (Get-Date).ToString('o', $inv), $p.X, $p.Y, $surec, $baslik,
            [GirdiIzleyici]::FizikselHareket, [GirdiIzleyici]::EnjekteHareket,
            [GirdiIzleyici]::FizikselTik, [GirdiIzleyici]::EnjekteTik,
            [GirdiIzleyici]::FizikselTus, [GirdiIzleyici]::EnjekteTus))
        Start-Sleep -Milliseconds $OrnekMs
    }
} finally {
    [GirdiIzleyici]::Durdur()
    [IO.File]::WriteAllLines($Cikti, $satirlar, (New-Object Text.UTF8Encoding $true))
    Write-Host "Kaydedildi: $Cikti"
    $analiz = Join-Path $PSScriptRoot 'Analiz-Et.ps1'
    if (Test-Path $analiz) { & $analiz -Csv $Cikti }
}
