<#
.SYNOPSIS
  "Fareyi almadan, pencereyi öne getirmeden" otomasyon mümkün mü? Windows Hesap
  Makinesi üzerinde UI Automation (InvokePattern) ile 1 + 2 = işlemini yapar ve
  imleç konumu / ön plan penceresinin DEĞİŞİP DEĞİŞMEDİĞİNİ ölçer.

.DESCRIPTION
  Senaryolar:
    Arkada     : Hesap Makinesi açık ama başka bir pencerenin arkasında.
    Kucultulmus: Hesap Makinesi önce simge durumuna küçültülür, sonra denenir.
  Her senaryo için: sonuç doğru mu, imleç kıpırdadı mı, ön plan değişti mi?
  Sonuç ekrana ve -Json dosyasına yazılır. Windows PowerShell 5.1 önerilir.

.EXAMPLE
  powershell -ExecutionPolicy Bypass -File .\UIA-ArkaPlan-Deneyi.ps1 -Json .\uia-sonuc.json
#>
param(
    [ValidateSet('Arkada','Kucultulmus','Hepsi')][string]$Senaryo = 'Hepsi',
    [int]$GecikmeSaniye = 6,
    [string]$Json
)

if ($env:OS -ne 'Windows_NT') { throw 'Bu betik yalnız Windows üzerinde çalışır.' }
Add-Type -AssemblyName UIAutomationClient, UIAutomationTypes
Add-Type -TypeDefinition @'
using System; using System.Runtime.InteropServices; using System.Text;
public static class Ekran {
  [StructLayout(LayoutKind.Sequential)] public struct POINT { public int X, Y; }
  [DllImport("user32.dll")] public static extern bool GetCursorPos(out POINT p);
  [DllImport("user32.dll")] public static extern IntPtr GetForegroundWindow();
  [DllImport("user32.dll", CharSet=CharSet.Unicode)] static extern int GetWindowText(IntPtr h, StringBuilder sb, int n);
  public static string Baslik(IntPtr h){ var sb=new StringBuilder(512); GetWindowText(h,sb,512); return sb.ToString(); }
}
'@

$AE = [System.Windows.Automation.AutomationElement]
$TS = [System.Windows.Automation.TreeScope]

function Find-HesapMakinesi {
    $kosul = New-Object System.Windows.Automation.PropertyCondition($AE::AutomationIdProperty, 'CalculatorResults')
    foreach ($w in $AE::RootElement.FindAll($TS::Children, [System.Windows.Automation.Condition]::TrueCondition)) {
        try { if ($w.FindFirst($TS::Descendants, $kosul)) { return $w } } catch { }
    }
    return $null
}

function Invoke-Dugme($pencere, [string]$id) {
    $k = New-Object System.Windows.Automation.PropertyCondition($AE::AutomationIdProperty, $id)
    $el = $pencere.FindFirst($TS::Descendants, $k)
    if (-not $el) { throw "Düğme bulunamadı: $id" }
    $el.GetCurrentPattern([System.Windows.Automation.InvokePattern]::Pattern).Invoke()
    Start-Sleep -Milliseconds 150
}

function Get-Durum {
    $p = New-Object Ekran+POINT; [void][Ekran]::GetCursorPos([ref]$p)
    $h = [Ekran]::GetForegroundWindow()
    [pscustomobject]@{ x = $p.X; y = $p.Y; onplan = [Ekran]::Baslik($h); hwnd = $h.ToInt64() }
}

function Invoke-Senaryo([string]$ad) {
    $pencere = Find-HesapMakinesi
    if (-not $pencere) { throw 'Hesap Makinesi penceresi bulunamadı (Standart modda açık olmalı).' }
    $wp = $null
    try { $wp = $pencere.GetCurrentPattern([System.Windows.Automation.WindowPattern]::Pattern) } catch { }
    if ($ad -eq 'Kucultulmus') {
        if (-not $wp) { throw 'WindowPattern yok; küçültme senaryosu çalıştırılamadı.' }
        $wp.SetWindowVisualState([System.Windows.Automation.WindowVisualState]::Minimized)
        Start-Sleep -Seconds 3   # UWP uygulamalarının askıya alınmasına zaman tanı
    } elseif ($wp -and $wp.Current.WindowVisualState -eq 'Minimized') {
        $wp.SetWindowVisualState([System.Windows.Automation.WindowVisualState]::Normal)
    }

    if ($ad -eq 'Arkada') {
        Write-Host ("[{0}] {1} saniye içinde BAŞKA bir pencereye (ör. Not Defteri) tıklayıp fareyi bir köşede bırakın..." -f $ad, $GecikmeSaniye)
        Start-Sleep -Seconds $GecikmeSaniye
    }
    $once = Get-Durum
    $hata = $null; $sonuc = $null
    try {
        $pencere = Find-HesapMakinesi
        foreach ($id in 'clearButton','num1Button','plusButton','num2Button','equalButton') { Invoke-Dugme $pencere $id }
        $k = New-Object System.Windows.Automation.PropertyCondition($AE::AutomationIdProperty, 'CalculatorResults')
        $sonuc = $pencere.FindFirst($TS::Descendants, $k).Current.Name
    } catch { $hata = $_.Exception.Message }
    $sonra = Get-Durum
    $dogru = [bool]($sonuc -and $sonuc -match '(^|\D)3(\D|$)')

    [pscustomobject]@{
        senaryo           = $ad
        sonuc_metni       = $sonuc
        sonuc_dogru       = $dogru
        hata              = $hata
        imlec_degismedi   = ($once.x -eq $sonra.x -and $once.y -eq $sonra.y)
        onplan_degismedi  = ($once.hwnd -eq $sonra.hwnd)
        onplan_once       = $once.onplan
        onplan_sonra      = $sonra.onplan
        KABUL             = ($dogru -and $once.x -eq $sonra.x -and $once.y -eq $sonra.y -and $once.hwnd -eq $sonra.hwnd)
    }
}

if (-not (Find-HesapMakinesi)) {
    Write-Host 'Hesap Makinesi açılıyor (bu ilk açılış pencereyi öne getirir; ölçüme dahil değildir)...'
    Start-Process calc.exe
    for ($i = 0; $i -lt 20 -and -not (Find-HesapMakinesi); $i++) { Start-Sleep -Milliseconds 500 }
}

$senaryolar = if ($Senaryo -eq 'Hepsi') { 'Arkada','Kucultulmus' } else { ,$Senaryo }
$sonuclar = foreach ($s in $senaryolar) { Invoke-Senaryo $s }
$sonuclar | Format-List
if ($Json) { $sonuclar | ConvertTo-Json -Depth 4 | Set-Content -Path $Json -Encoding UTF8; Write-Host "JSON: $Json" }
