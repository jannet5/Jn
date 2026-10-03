<#
  Kurulum: Ses-Temizle için gereken iki ücretsiz aracı hazırlar ve gerçek bir çalışma testi yapar.
    - deep-filter.exe (DeepFilterNet 0.5.6, MIT/Apache-2.0) -> araclar\ (yoksa GitHub'dan indirir, SHA-256 doğrular)
    - ffmpeg -> zaten varsa dokunmaz; yoksa winget ile (Gyan.FFmpeg) kurmayı dener ve sonucu ayrıntılı raporlar
    - Audacity 3.x makrosu "Ses-Temizle" -> %APPDATA%\audacity\Macros
      (aynı adlı FARKLI bir makro varsa ONA DOKUNMAZ; yeni dosyayı "Ses-Temizle (2).txt" gibi ayrı adla koyar)
    - Çalışma testi: her seferinde yeni GUID klasöründe test sesi üretir, temizler, çıktıyı doğrular, klasörü siler

  Çıkış kodu: 0 = kurulum ve çalışma testi başarılı; 2 = deep-filter hazırlanamadı; 3 = ffmpeg yok/kurulamadı;
              4 = çalışma testi başarısız. (Makro kopyalanamaması uyarıdır, kurulumu başarısız saymaz.)
#>
$ErrorActionPreference = 'Stop'
$Kok = Split-Path -Parent $MyInvocation.MyCommand.Path
$WindowsMu = ($env:OS -eq 'Windows_NT')
$Araclar = [System.IO.Path]::Combine($Kok, 'araclar')
$DfUrl = 'https://github.com/Rikorose/DeepFilterNet/releases/download/v0.5.6/deep-filter-0.5.6-x86_64-pc-windows-msvc.exe'
$DfSha = '75E11FA16445F560CB6B021521DDB89E89270D13B83089705D98776F58FD7915'
$ExeEk = if ($WindowsMu) { '.exe' } else { '' }

function Yaz([string]$m, [string]$renk = 'Gray') { Write-Host $m -ForegroundColor $renk }
function Tamam([string]$m) { Yaz "[TAMAM] $m" 'Green' }
function Uyari([string]$m) { Yaz "[UYARI] $m" 'Yellow' }
function Hata([string]$m) { Yaz "[HATA]  $m" 'Red' }

function Tirnakla([string]$s) {
    if ($s -eq '') { return '""' }
    if ($s -notmatch '[\s"]') { return $s }
    return '"' + ($s -replace '(\\*)"', '$1$1\"' -replace '(\\+)$', '$1$1') + '"'
}
# Harici program: stdout+stderr birlikte toplanır, çıkış kodu döner. WinPS 5.1'de yerel komutun stderr'i
# yönlendirilince hata kaydına dönüşüp $ErrorActionPreference=Stop ile betiği durdurduğu için bu yol kullanılır.
function Yerel([string]$exe, [string[]]$argumanlar) {
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = $exe
    $psi.Arguments = (($argumanlar | ForEach-Object { Tirnakla $_ }) -join ' ')
    $psi.UseShellExecute = $false
    $psi.RedirectStandardOutput = $true
    $psi.RedirectStandardError = $true
    $psi.CreateNoWindow = $true
    $p = [System.Diagnostics.Process]::Start($psi)
    $o = $p.StandardOutput.ReadToEndAsync()
    $e = $p.StandardError.ReadToEndAsync()
    $p.WaitForExit()
    return [pscustomobject]@{ Kod = $p.ExitCode; Metin = ($o.Result + "`n" + $e.Result) }
}

if (-not (Test-Path -LiteralPath $Araclar)) { New-Item -ItemType Directory -Path $Araclar | Out-Null }

# ---------------------------------------------------------------------------------------------
# 1) deep-filter.exe (Windows ikilisi; SHA-256 sabit)
$df = [System.IO.Path]::Combine($Araclar, 'deep-filter.exe')
try {
    if (Test-Path -LiteralPath $df) {
        $h = (Get-FileHash -Algorithm SHA256 -LiteralPath $df).Hash
        if ($h -ne $DfSha) {
            $yedek = "$df.bozuk-$((Get-Date).ToString('yyyyMMddHHmmss'))"
            Uyari "deep-filter.exe özeti beklenenden farklı ($h). Eski dosya '$([System.IO.Path]::GetFileName($yedek))' adıyla kenara alındı, yeniden indirilecek."
            [System.IO.File]::Move($df, $yedek)
        }
    }
    if (-not (Test-Path -LiteralPath $df)) {
        Yaz 'deep-filter.exe indiriliyor (~27 MB)...'
        [Net.ServicePointManager]::SecurityProtocol = [Net.ServicePointManager]::SecurityProtocol -bor [Net.SecurityProtocolType]::Tls12
        $ProgressPreference = 'SilentlyContinue'
        $parca = "$df.indiriliyor"
        if (Test-Path -LiteralPath $parca) { Remove-Item -LiteralPath $parca -Force }
        try {
            Invoke-WebRequest -Uri $DfUrl -OutFile $parca -UseBasicParsing
            $h = (Get-FileHash -Algorithm SHA256 -LiteralPath $parca).Hash
            if ($h -ne $DfSha) { throw "İndirilen dosya SHA-256 doğrulamasını geçemedi ($h)." }
            [System.IO.File]::Move($parca, $df)
        }
        finally { if (Test-Path -LiteralPath $parca) { Remove-Item -LiteralPath $parca -Force } }
    }
    Tamam 'deep-filter.exe hazır (SHA-256 doğrulandı)'
}
catch {
    Hata "deep-filter hazırlanamadı: $($_.Exception.Message)"
    Hata "Elle indirme: $DfUrl  ->  $df"
    exit 2
}

# ---------------------------------------------------------------------------------------------
# 2) ffmpeg
function Bul-Ffmpeg {
    $exe = "ffmpeg$ExeEk"
    $adaylar = @([System.IO.Path]::Combine($Araclar, $exe))
    if ($env:LOCALAPPDATA) {
        $adaylar += [System.IO.Path]::Combine($env:LOCALAPPDATA, 'Microsoft', 'WinGet', 'Links', $exe)
        $paket = [System.IO.Path]::Combine($env:LOCALAPPDATA, 'Microsoft', 'WinGet', 'Packages')
        if (Test-Path -LiteralPath $paket) {
            $b = Get-ChildItem -LiteralPath $paket -Filter $exe -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1
            if ($b) { $adaylar += $b.FullName }
        }
    }
    foreach ($a in $adaylar) { if (Test-Path -LiteralPath $a -PathType Leaf) { return $a } }
    $c = Get-Command ffmpeg -CommandType Application -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($c) { return $c.Source }
    return $null
}

# ffmpeg gerçekten çalışıyor mu? (yalnız dosyanın varlığı yetmez)
function Ffmpeg-Calisiyor([string]$yol) {
    try { return ((Yerel $yol @('-hide_banner', '-version')).Kod -eq 0) }
    catch { return $false }
}

# winget çıkış kodunu sınıflandırır (kaynak: microsoft/winget-cli doc/.../returnCodes.md)
function Winget-Sonuc([int]$kod) {
    $hex = '0x{0:X8}' -f $kod
    switch ($kod) {
        0 { return @('basari', 'winget kurulumu tamamladı') }
        -1978335135 { return @('zaten', "paket zaten kurulu görünüyor ($hex PACKAGE_ALREADY_INSTALLED)") }
        -1978335212 { return @('bulunamadi', "Gyan.FFmpeg paketi bulunamadı ($hex NO_APPLICATIONS_FOUND); 'winget source update' deneyin") }
        { $_ -in @(-1978335224, -1978335186, -1978334969, -1978335098, -1978335163, -1978335157, -1978335217, -2147012889, -2147012867, -2147012894) } {
            return @('ag', "ağ/indirme hatası ($hex): internet bağlantısını, proxy/güvenlik duvarını kontrol edip tekrar deneyin") }
        { $_ -in @(-1978335207, -1978335174, -1978335205, -1978335146, -1978335107, -2147024891) } {
            return @('izin', "izin/ilke engeli ($hex): yönetici izni veya kurum ilkesi gerekiyor; ffmpeg'i elle kurun") }
        { $_ -in @(-1978335167, -1978335162) } { return @('sozlesme', "paket/kaynak sözleşmesi kabul edilmedi ($hex)") }
        -1978335227 { return @('iptal', "kullanıcı iptal etti ($hex)") }
        default { return @('bilinmeyen', "winget hata kodu $kod ($hex)") }
    }
}

$ff = Bul-Ffmpeg
if ($ff -and -not (Ffmpeg-Calisiyor $ff)) { Uyari "ffmpeg bulundu ama çalışmadı: $ff"; $ff = $null }
if (-not $ff) {
    $winget = Get-Command winget -CommandType Application -ErrorAction SilentlyContinue | Select-Object -First 1
    if (-not $winget) {
        Hata 'ffmpeg yok ve winget komutu bulunamadı (Windows 10 eski sürümlerinde "Uygulama Yükleyici" kurulu olmayabilir).'
    }
    else {
        Yaz 'ffmpeg winget ile kuruluyor (Gyan.FFmpeg)...'
        $kod = $null
        try {
            & $winget.Source install --id Gyan.FFmpeg -e --accept-source-agreements --accept-package-agreements
            $kod = $LASTEXITCODE
        }
        catch { Hata "winget başlatılamadı: $($_.Exception.Message)" }
        if ($null -ne $kod) {
            $sonuc = Winget-Sonuc $kod
            if ($sonuc[0] -eq 'basari' -or $sonuc[0] -eq 'zaten') { Yaz "winget: $($sonuc[1])" } else { Hata "winget: $($sonuc[1])" }
        }
        # Başarıyı winget'in sözüne değil, ffmpeg'in gerçekten bulunup çalışmasına göre değerlendir
        $ff = Bul-Ffmpeg
        if ($ff -and -not (Ffmpeg-Calisiyor $ff)) { $ff = $null }
        if (-not $ff -and $null -ne $kod -and $kod -eq 0) {
            Uyari 'winget başarı bildirdi ama ffmpeg bulunamadı; yeni bir pencerede Kurulum.bat''ı tekrar çalıştırın (PATH güncellenmemiş olabilir).'
        }
    }
}
if (-not $ff) {
    Hata 'ffmpeg hazır değil, kurulum TAMAMLANMADI.'
    Uyari 'Elle kurulum: https://www.gyan.dev/ffmpeg/builds/ -> "ffmpeg-release-essentials.zip" indirin,'
    Uyari "içindeki bin\ffmpeg.exe dosyasını şu klasöre koyun ve Kurulum.bat'ı tekrar çalıştırın: $Araclar"
    exit 3
}
Tamam "ffmpeg çalışıyor: $ff"

# ---------------------------------------------------------------------------------------------
# 3) Audacity 3.x makrosu: mevcut kullanıcı makrosunun üzerine ASLA yazılmaz
$makroKaynak = [System.IO.Path]::Combine((Split-Path -Parent $Kok), 'audacity-makro', 'Ses-Temizle.txt')
$audacityAyar = if ($env:APPDATA) { [System.IO.Path]::Combine($env:APPDATA, 'audacity') } else { $null }
try {
    if (-not (Test-Path -LiteralPath $makroKaynak)) { Uyari "Makro kaynağı bulunamadı: $makroKaynak" }
    elseif (-not $audacityAyar -or -not (Test-Path -LiteralPath $audacityAyar)) {
        Uyari 'Audacity 3.x ayar klasörü bulunamadı; makro kopyalanmadı. (Audacity 4.0 makro desteklemiyor; makro için 3.7.x gerekir.)'
    }
    else {
        $makroKlasor = [System.IO.Path]::Combine($audacityAyar, 'Macros')
        if (-not (Test-Path -LiteralPath $makroKlasor)) { New-Item -ItemType Directory -Path $makroKlasor | Out-Null }
        $kaynakOzet = (Get-FileHash -Algorithm SHA256 -LiteralPath $makroKaynak).Hash
        $hedef = $null; $ayniVar = $null
        for ($n = 1; $n -lt 100; $n++) {
            $adi = if ($n -eq 1) { 'Ses-Temizle.txt' } else { "Ses-Temizle ($n).txt" }
            $yol = [System.IO.Path]::Combine($makroKlasor, $adi)
            if (Test-Path -LiteralPath $yol) {
                if ((Get-FileHash -Algorithm SHA256 -LiteralPath $yol).Hash -eq $kaynakOzet) { $ayniVar = $yol; break }
            }
            elseif (-not $hedef) { $hedef = $yol }
        }
        if ($ayniVar) { Tamam "Audacity makrosu zaten güncel: $ayniVar" }
        elseif ($hedef) {
            [System.IO.File]::Copy($makroKaynak, $hedef, $false)   # $false: var olan dosyanın üzerine yazmaz
            if ([System.IO.Path]::GetFileName($hedef) -ne 'Ses-Temizle.txt') {
                Uyari "Sizin değiştirdiğiniz 'Ses-Temizle.txt' korundu; yeni makro ayrı adla eklendi: $hedef"
            }
            Tamam "Audacity makrosu eklendi: $hedef  (Audacity 3.7.x > Tools > Apply Macro)"
        }
        else { Uyari 'Makro için boş ad bulunamadı; kopyalanmadı.' }
    }
}
catch { Uyari "Makro kopyalanamadı: $($_.Exception.Message)" }

# ---------------------------------------------------------------------------------------------
# 4) Çalışma testi: yeni GUID klasöründe 6 sn test sesi üret -> ayrı süreçte Ses-Temizle -> çıktıyı doğrula -> klasörü sil
#    (-Guc 30: yapay test tonu konuşma olmadığından tam güçte bazen tamamen silinebiliyor; testte ölçüldü)
$testKlasor = [System.IO.Path]::Combine([System.IO.Path]::GetTempPath(), 'sestemizle_kurulum_' + [guid]::NewGuid().ToString('N'))
$testGecti = $false
try {
    New-Item -ItemType Directory -Path $testKlasor | Out-Null
    $test = [System.IO.Path]::Combine($testKlasor, 'kurulum testi.wav')
    $r = Yerel $ff @('-hide_banner', '-loglevel', 'error', '-n', '-f', 'lavfi', '-i', 'sine=f=220:d=6,volume=0.3,tremolo=f=3:d=0.9',
        '-f', 'lavfi', '-i', 'anoisesrc=d=6:c=pink:a=0.02', '-filter_complex', '[0][1]amix=inputs=2:normalize=0', '-ar', '48000', '-ac', '1', $test)
    if ($r.Kod -ne 0 -or -not (Test-Path -LiteralPath $test)) { throw "Test sesi üretilemedi: $($r.Metin)" }

    $ps = (Get-Process -Id $PID).Path
    $r = Yerel $ps @('-NoProfile', '-ExecutionPolicy', 'Bypass', '-File', ([System.IO.Path]::Combine($Kok, 'Ses-Temizle.ps1')), '-Guc', '30', $test)
    if ($r.Kod -ne 0) { throw "Ses-Temizle çıkış kodu $($r.Kod). Çıktı:`n$($r.Metin)" }

    $cikti = [System.IO.Path]::Combine($testKlasor, 'kurulum testi_temiz.wav')
    $rapor = [System.IO.Path]::Combine($testKlasor, 'kurulum testi_rapor.txt')
    if (-not (Test-Path -LiteralPath $cikti) -or -not (Test-Path -LiteralPath $rapor)) { throw 'Beklenen çıktı dosyaları oluşmadı.' }
    # İçerik doğrulaması: süre ~6 sn olmalı ve ses sessiz olmamalı
    $bilgi = (Yerel $ff @('-hide_banner', '-nostats', '-i', $cikti, '-af', 'ebur128', '-f', 'null', '-')).Metin
    $sure = [regex]::Match($bilgi, 'Duration: (\d+):(\d+):([\d\.]+)')
    $sn = [int]$sure.Groups[1].Value * 3600 + [int]$sure.Groups[2].Value * 60 + [double]::Parse($sure.Groups[3].Value, [Globalization.CultureInfo]::InvariantCulture)
    $lufsM = [regex]::Match($bilgi.Substring($bilgi.LastIndexOf('Summary:')), 'I:\s+(-?[\d\.]+) LUFS')
    if ([Math]::Abs($sn - 6.0) -gt 0.5) { throw "Çıktı süresi beklenmedik: $sn sn." }
    if (-not $lufsM.Success -or [double]::Parse($lufsM.Groups[1].Value, [Globalization.CultureInfo]::InvariantCulture) -lt -30) { throw 'Çıktı sessiz veya ölçülemedi.' }
    $testGecti = $true
    Tamam "Çalışma testi geçti: temizleme zinciri bu bilgisayarda çalışıyor ($([Math]::Round($sn,1)) sn, $($lufsM.Groups[1].Value) LUFS)."
}
catch { Hata "Çalışma testi başarısız: $($_.Exception.Message)" }
finally {
    if (Test-Path -LiteralPath $testKlasor) { Remove-Item -LiteralPath $testKlasor -Recurse -Force -ErrorAction SilentlyContinue }
}
if (-not $testGecti) { exit 4 }

Yaz ''
Yaz 'Kurulum bitti. Kullanım: kayıt dosyanızı "Ses-Temizle.bat" üzerine sürükleyip bırakın.' 'Cyan'
exit 0
