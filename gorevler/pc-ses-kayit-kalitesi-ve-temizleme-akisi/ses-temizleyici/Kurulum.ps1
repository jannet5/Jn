<#
  Kurulum: Ses-Temizle için gereken iki ücretsiz aracı hazırlar ve kısa bir çalışma testi yapar.
    - deep-filter.exe (DeepFilterNet 0.5.6, MIT/Apache-2.0) -> araclar\ klasörü (yoksa GitHub'dan indirir, SHA-256 doğrular)
    - ffmpeg -> winget ile (Gyan.FFmpeg) kurar, zaten varsa dokunmaz
    - Audacity 3.x makrosu "Ses-Temizle" -> %APPDATA%\audacity\Macros (Audacity 3.x kuruluysa)
#>
$ErrorActionPreference = 'Stop'
$Kok = Split-Path -Parent $MyInvocation.MyCommand.Path
$Araclar = Join-Path $Kok 'araclar'
$DfUrl = 'https://github.com/Rikorose/DeepFilterNet/releases/download/v0.5.6/deep-filter-0.5.6-x86_64-pc-windows-msvc.exe'
$DfSha = '75E11FA16445F560CB6B021521DDB89E89270D13B83089705D98776F58FD7915'

function Yaz([string]$m, [string]$renk = 'Gray') { Write-Host $m -ForegroundColor $renk }
function Tamam([string]$m) { Yaz "[TAMAM] $m" 'Green' }
function Uyari([string]$m) { Yaz "[UYARI] $m" 'Yellow' }

if (-not (Test-Path $Araclar)) { New-Item -ItemType Directory -Path $Araclar | Out-Null }

# 1) deep-filter.exe
$df = Join-Path $Araclar 'deep-filter.exe'
if (Test-Path $df) {
    $h = (Get-FileHash -Algorithm SHA256 -LiteralPath $df).Hash
    if ($h -ne $DfSha) { Uyari "deep-filter.exe özeti beklenenden farklı ($h); yeniden indirilecek."; Remove-Item $df -Force }
}
if (-not (Test-Path $df)) {
    Yaz 'deep-filter.exe indiriliyor (~27 MB)...'
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    $ProgressPreference = 'SilentlyContinue'
    Invoke-WebRequest -Uri $DfUrl -OutFile $df -UseBasicParsing
    $h = (Get-FileHash -Algorithm SHA256 -LiteralPath $df).Hash
    if ($h -ne $DfSha) { Remove-Item $df -Force; throw "İndirilen deep-filter.exe SHA-256 doğrulamasını geçemedi ($h)." }
}
Tamam "deep-filter.exe hazır (SHA-256 doğrulandı)"

# 2) ffmpeg
function Bul-Ffmpeg {
    $adaylar = @((Join-Path $Araclar 'ffmpeg.exe'), (Join-Path $env:LOCALAPPDATA 'Microsoft\WinGet\Links\ffmpeg.exe'))
    $paket = Join-Path $env:LOCALAPPDATA 'Microsoft\WinGet\Packages'
    if (Test-Path $paket) {
        $b = Get-ChildItem -Path $paket -Filter 'ffmpeg.exe' -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($b) { $adaylar += $b.FullName }
    }
    foreach ($a in $adaylar) { if (Test-Path -LiteralPath $a) { return $a } }
    $c = Get-Command ffmpeg -ErrorAction SilentlyContinue
    if ($c) { return $c.Source }
    return $null
}
$ff = Bul-Ffmpeg
if (-not $ff) {
    if (Get-Command winget -ErrorAction SilentlyContinue) {
        Yaz 'ffmpeg winget ile kuruluyor (Gyan.FFmpeg)...'
        & winget install --id Gyan.FFmpeg -e --accept-source-agreements --accept-package-agreements
        $ff = Bul-Ffmpeg
    }
}
if (-not $ff) {
    Uyari 'ffmpeg kurulamadı. Elle kurun: https://www.gyan.dev/ffmpeg/builds/ adresinden "ffmpeg-release-essentials.zip"'
    Uyari "indirin, içindeki bin\ffmpeg.exe dosyasını şu klasöre koyun: $Araclar"
    exit 2
}
Tamam "ffmpeg bulundu: $ff"

# 3) Audacity 3.x makrosu
$makroKaynak = Join-Path (Split-Path -Parent $Kok) 'audacity-makro\Ses-Temizle.txt'
$makroHedef = Join-Path $env:APPDATA 'audacity\Macros'
if ((Test-Path $makroKaynak) -and (Test-Path (Join-Path $env:APPDATA 'audacity'))) {
    if (-not (Test-Path $makroHedef)) { New-Item -ItemType Directory -Path $makroHedef | Out-Null }
    Copy-Item -LiteralPath $makroKaynak -Destination $makroHedef -Force
    Tamam "Audacity makrosu kopyalandı: $makroHedef\Ses-Temizle.txt  (Audacity 3.7.x'te Araçlar > Makro Uygula altında görünür)"
}
else {
    Uyari 'Audacity 3.x ayar klasörü bulunamadı; makro kopyalanmadı. (Audacity 4.0 makro desteklemiyor; makro için 3.7.x gerekir.)'
}

# 4) Çalışma testi: 6 sn yapay "konuşma benzeri ton + fan gürültüsü" üretip temizlemeyi dener
$test = Join-Path $env:TEMP 'sestemizle_kurulum_testi.wav'
& $ff -hide_banner -loglevel error -y -f lavfi -i "sine=f=220:d=6,volume=0.3,tremolo=f=3:d=0.9" -f lavfi -i "anoisesrc=d=6:c=pink:a=0.02" -filter_complex "[0][1]amix=inputs=2:normalize=0" -ar 48000 -ac 1 $test
& (Join-Path $Kok 'Ses-Temizle.ps1') $test | Out-Null
if (Test-Path ($test -replace '\.wav$', '_temiz.wav')) { Tamam 'Çalışma testi geçti: temizleme zinciri bu bilgisayarda çalışıyor.' }
else { Uyari 'Çalışma testi çıktı üretmedi; yukarıdaki hata mesajlarına bakın.' }
Remove-Item ($test -replace '\.wav$', '*') -Force -ErrorAction SilentlyContinue

Yaz ''
Yaz 'Kurulum bitti. Kullanım: kayıt dosyanızı "Ses-Temizle.bat" üzerine sürükleyip bırakın.' 'Cyan'
