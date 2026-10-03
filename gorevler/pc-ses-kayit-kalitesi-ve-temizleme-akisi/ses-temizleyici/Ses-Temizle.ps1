<#
.SYNOPSIS
  Kötü/dahili mikrofonla alınmış konuşma kaydını temizler ve önce/sonra karşılaştırma dosyası üretir.

.DESCRIPTION
  Bu bir Audacity eklentisi DEĞİLDİR; Audacity'den bağımsız çalışan küçük bir betiktir.
  Arka planda iki ücretsiz komut satırı aracını sırayla çalıştırır:
    1) ffmpeg        : biçim çevirme, 80 Hz alt kesim, sıkıştırma, ses yüksekliği (LUFS) ayarı, ölçüm
    2) deep-filter   : DeepFilterNet3 yapay zekâ gürültü bastırma (bilgisayarda, internetsiz çalışır)
  Her giriş dosyası için yanına şunları yazar:
    <ad>_temiz.wav        temizlenmiş, -16 LUFS'e ayarlanmış 48 kHz / 24-bit WAV
    <ad>_onceSonra.wav    aynı ses seviyesine getirilmiş HAM kayıt + bip + TEMİZ kayıt (kulakla kıyas için)
    <ad>_rapor.txt        ölçümler (LUFS, tepe, gürültü tabanı, konuşma/gürültü farkı)

.PARAMETER Mod
  ai     : DeepFilterNet3 (varsayılan, testlerde en iyi sonuç)
  klasik : yalnız ffmpeg afftdn (deep-filter yoksa otomatik seçilir; Audacity Noise Reduction'a benzer)

.PARAMETER Guc
  ai modunda en fazla kaç dB gürültü azaltılacağı. 100 = sınırsız (varsayılan; testlerde en iyi ölçüm),
  30 = doğal (konuşma aralarında hafif oda sesi kalır), 18 = hafif. Ses "kesik kesik/boğuk" geldiyse 30 deneyin.

.PARAMETER Kontrol
  Temizleme yapmaz; yalnız kaydı ölçer ve kayıt seviyesinin/odanın uygun olup olmadığını söyler.

.PARAMETER UzerineYaz
  Varsayılan olarak hiçbir mevcut dosyanın üzerine yazılmaz: <ad>_temiz.wav varsa çıktılar
  "<ad>_temiz (2).wav" gibi yeni adla yazılır. Yalnız bu anahtar verilirse eski çıktıların üzerine yazılır.
  Giriş dosyasının kendisine hiçbir durumda dokunulmaz.

.NOTES
  Çıkış kodları: 0 = tümü başarılı, 1 = dosya verilmedi, 2 = ffmpeg yok, 3 = en az bir dosya başarısız.
  Tüm ara dosyalar her çalıştırmada yeni oluşturulan GUID adlı geçici klasöre yazılır ve klasör hata
  olsa bile (finally) silinir. Nihai dosyalar ancak tüm adımlar ve son doğrulama başarılıysa taşınır;
  yarım kalan iş kullanıcı klasörüne dosya bırakmaz.

.EXAMPLE
  .\Ses-Temizle.ps1 kayit1.wav kayit2.m4a
  .\Ses-Temizle.ps1 -Guc 100 kayit.wav
  .\Ses-Temizle.ps1 -Kontrol deneme.wav
#>
[CmdletBinding(PositionalBinding = $false)]
param(
    [ValidateSet('ai', 'klasik')][string]$Mod = 'ai',
    [ValidateRange(6, 100)][int]$Guc = 100,
    [ValidateRange(-30, -10)][double]$HedefLUFS = -16,
    [ValidateRange(-6, -0.5)][double]$TepeDB = -1.5,
    [switch]$Kontrol,
    [switch]$UzerineYaz,
    [Parameter(ValueFromRemainingArguments = $true)][string[]]$Dosyalar
)

$ErrorActionPreference = 'Stop'
$Kok = Split-Path -Parent $MyInvocation.MyCommand.Path
$Inv = [System.Globalization.CultureInfo]::InvariantCulture
$WindowsMu = ($env:OS -eq 'Windows_NT')

function Yaz([string]$m, [string]$renk = 'Gray') { Write-Host $m -ForegroundColor $renk }

function Bul-Arac([string]$ad) {
    $exe = if ($WindowsMu) { "$ad.exe" } else { $ad }
    $adaylar = @((Join-Path $Kok "araclar\$exe"), (Join-Path $Kok "araclar/$exe"))
    if ($env:LOCALAPPDATA) {
        $adaylar += (Join-Path $env:LOCALAPPDATA "Microsoft\WinGet\Links\$exe")
        $paket = Join-Path $env:LOCALAPPDATA 'Microsoft\WinGet\Packages'
        if (Test-Path $paket) {
            $bulunan = Get-ChildItem -Path $paket -Filter $exe -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1
            if ($bulunan) { $adaylar += $bulunan.FullName }
        }
    }
    foreach ($a in $adaylar) { if (Test-Path -LiteralPath $a -PathType Leaf) { return (Resolve-Path -LiteralPath $a).Path } }
    $cmd = Get-Command $ad -ErrorAction SilentlyContinue
    if ($cmd) { return $cmd.Source }
    return $null
}

function Tirnakla([string]$s) {
    if ($s -eq '') { return '""' }
    if ($s -notmatch '[\s"]') { return $s }
    return '"' + ($s -replace '(\\*)"', '$1$1\"' -replace '(\\+)$', '$1$1') + '"'
}

# Harici programı çalıştırır; stdout+stderr metnini döndürür. (Windows PowerShell 5.1 ile uyumlu.)
function Calistir([string]$exe, [string[]]$argumanlar, [string]$calismaDizini) {
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = $exe
    $psi.Arguments = (($argumanlar | ForEach-Object { Tirnakla $_ }) -join ' ')
    $psi.UseShellExecute = $false
    $psi.RedirectStandardOutput = $true
    $psi.RedirectStandardError = $true
    $psi.CreateNoWindow = $true
    if ($calismaDizini) { $psi.WorkingDirectory = $calismaDizini }
    $p = [System.Diagnostics.Process]::Start($psi)
    $o = $p.StandardOutput.ReadToEndAsync()
    $e = $p.StandardError.ReadToEndAsync()
    $p.WaitForExit()
    $metin = $o.Result + "`n" + $e.Result
    if ($p.ExitCode -ne 0) {
        $son = ($metin -split "`n" | Where-Object { $_.Trim() } | Select-Object -Last 8) -join "`n"
        throw "$([System.IO.Path]::GetFileName($exe)) hata verdi (kod $($p.ExitCode)):`n$son"
    }
    return $metin
}

function Sayi([string]$s) { return [double]::Parse($s, $Inv) }
function F([double]$d, [string]$bicim = '0.0') { return $d.ToString($bicim, $Inv) }

# loudnorm 1. geçiş ölçümü (JSON)
function Olc-Loudnorm([string]$giris, [string]$onFiltre) {
    $af = "loudnorm=I=$(F $HedefLUFS):TP=$(F $TepeDB):LRA=11:print_format=json"
    if ($onFiltre) { $af = "$onFiltre,$af" }
    $t = Calistir $script:FFMPEG @('-hide_banner', '-nostats', '-i', $giris, '-af', $af, '-f', 'null', '-') $null
    $js = $t.Substring($t.LastIndexOf('{'))
    $js = $js.Substring(0, $js.IndexOf('}') + 1)
    return ($js | ConvertFrom-Json)
}

function Loudnorm-Filtre($m) {
    if ("$($m.input_i)" -match 'inf' -or "$($m.input_tp)" -match 'inf') {
        throw 'Ses bu adımda tamamen sessiz kaldı (ölçülen ses yüksekliği -inf). Kayıt boş olabilir ya da gürültü bastırma sesi de sildi; "-Guc 30" (Ses-Temizle-Dogal.bat) ile deneyin.'
    }
    return ("loudnorm=I=$(F $HedefLUFS):TP=$(F $TepeDB):LRA=11:measured_I=$($m.input_i):measured_TP=$($m.input_tp)" +
        ":measured_LRA=$($m.input_lra):measured_thresh=$($m.input_thresh):offset=$($m.target_offset):linear=true")
}

# Ölçüm: tümleşik LUFS, gerçek tepe, 50 ms pencerelerde gürültü tabanı (en sessiz %10) ve konuşma seviyesi (en yüksek %10)
function Olc([string]$dosya, [string]$gecici) {
    $t = Calistir $script:FFMPEG @('-hide_banner', '-nostats', '-i', $dosya, '-af', 'ebur128=peak=true', '-f', 'null', '-') $null
    $ozet = $t.Substring($t.LastIndexOf('Summary:'))
    $lufs = Sayi ([regex]::Match($ozet, 'I:\s+(-?[\d\.]+|-inf) LUFS').Groups[1].Value -replace '-inf', '-120')
    $tepe = Sayi ([regex]::Match($ozet, 'Peak:\s+(-?[\d\.]+|-inf) dBFS').Groups[1].Value -replace '-inf', '-120')
    $rmsDosya = Join-Path $gecici 'rms.txt'
    if (Test-Path $rmsDosya) { Remove-Item $rmsDosya -Force }
    $null = Calistir $script:FFMPEG @('-hide_banner', '-nostats', '-i', $dosya, '-ac', '1', '-ar', '48000', '-af',
        'asetnsamples=n=2400:p=0,astats=metadata=1:reset=1,ametadata=mode=print:key=lavfi.astats.Overall.RMS_level:file=rms.txt',
        '-f', 'null', '-') $gecici
    $degerler = New-Object System.Collections.Generic.List[double]
    foreach ($satir in [System.IO.File]::ReadAllLines($rmsDosya)) {
        if ($satir -match 'RMS_level=(-?[\d\.]+)\s*$') { $degerler.Add((Sayi $Matches[1])) }
        elseif ($satir -match 'RMS_level=-inf') { $degerler.Add(-120.0) }   # sayısal sessizlik
    }
    $dizi = $degerler.ToArray(); [Array]::Sort($dizi)
    if ($dizi.Length -lt 10) { throw "Kayıt ölçüm için çok kısa: $dosya" }
    $taban = $dizi[[int][Math]::Floor($dizi.Length * 0.10)]
    $konusma = $dizi[[int][Math]::Floor($dizi.Length * 0.90)]
    return [pscustomobject]@{ LUFS = $lufs; Tepe = $tepe; Taban = $taban; Konusma = $konusma; Fark = ($konusma - $taban); Sure = $dizi.Length * 0.05 }
}

function Degerlendir($m) {
    $notlar = @()
    if ($m.Tepe -le -100 -or $m.LUFS -le -69) {
        return @('Kayıt SESSİZ: hiçbir ses alınmamış. Windows mikrofon iznini ve Audacity''de seçili kayıt aygıtını kontrol edin.')
    }
    if ($m.Tepe -ge -0.3) { $notlar += 'KIRPILMA riski: tepe 0 dBFS''ye dayanmış. Windows giriş seviyesini düşürüp tekrar kaydedin.' }
    elseif ($m.Tepe -gt -3) { $notlar += 'Tepe çok yüksek (-3 dBFS üstü); biraz düşürmek güvenli olur.' }
    elseif ($m.Tepe -lt -24) { $notlar += 'Kayıt çok kısık (tepe -24 dBFS altı). Giriş seviyesini artırın veya mikrofona yaklaşın.' }
    else { $notlar += 'Kayıt seviyesi uygun aralıkta.' }
    if ($m.Taban -le -60) { $notlar += 'Oda/gürültü tabanı çok iyi (-60 dBFS ve altı).' }
    elseif ($m.Taban -le -50) { $notlar += 'Gürültü tabanı kabul edilebilir; temizleme sonrası iyi olur.' }
    else { $notlar += 'Gürültü tabanı yüksek: fan/klima kapatın, mikrofona yaklaşın, yumuşak yüzeyli köşede kaydedin.' }
    if ($m.Fark -lt 25) { $notlar += 'Konuşma ile gürültü arası fark düşük (<25 dB): yazılım bunu tam kurtaramaz, kayıt ortamını iyileştirin.' }
    return $notlar
}

function Satir($ad, $m) {
    return ('{0,-22} LUFS {1,7}  Tepe {2,6} dBFS  Gürültü tabanı {3,6} dBFS  Konuşma-gürültü farkı {4,5} dB' -f $ad, (F $m.LUFS), (F $m.Tepe), (F $m.Taban), (F $m.Fark))
}

# ---------------------------------------------------------------------------------------------
if (-not $Dosyalar -or $Dosyalar.Count -eq 0) {
    Yaz 'Kullanım: ses dosyalarını Ses-Temizle.bat üzerine sürükleyip bırakın' 'Yellow'
    Yaz '   veya:  Ses-Temizle.ps1 [-Guc 30] [-Mod ai|klasik] [-Kontrol] dosya1.wav dosya2.mp3 ...' 'Yellow'
    exit 1
}

$script:FFMPEG = Bul-Arac 'ffmpeg'
if (-not $script:FFMPEG) {
    Yaz 'ffmpeg bulunamadı. Önce Kurulum.bat dosyasını çalıştırın (veya: winget install --id Gyan.FFmpeg -e).' 'Red'
    exit 2
}
$DF = $null
if (-not $Kontrol -and $Mod -eq 'ai') {
    $DF = Bul-Arac 'deep-filter'
    if (-not $DF) { Yaz 'deep-filter bulunamadı; klasik moda geçiliyor (Kurulum.bat ile AI aracı kurulabilir).' 'Yellow'; $Mod = 'klasik' }
}

$kompresor = 'acompressor=threshold=0.125:ratio=3:attack=20:release=150:knee=2.5'  # eşik -18 dBFS, 3:1

# Üç çıktı için ortak, çakışmayan ad seçer: <ad>_temiz.wav, <ad>_temiz (2).wav ... (rapor ve önce/sonra aynı numarayı alır)
function Cikis-Adlari([string]$klasor, [string]$ad) {
    for ($n = 1; $n -lt 1000; $n++) {
        $ek = if ($n -eq 1) { '' } else { " ($n)" }
        $yollar = [pscustomobject]@{
            Temiz     = [System.IO.Path]::Combine($klasor, "${ad}_temiz$ek.wav")
            OnceSonra = [System.IO.Path]::Combine($klasor, "${ad}_onceSonra$ek.wav")
            Rapor     = [System.IO.Path]::Combine($klasor, "${ad}_rapor$ek.txt")
        }
        if ($UzerineYaz) { return $yollar }
        if (-not ((Test-Path -LiteralPath $yollar.Temiz) -or (Test-Path -LiteralPath $yollar.OnceSonra) -or (Test-Path -LiteralPath $yollar.Rapor))) { return $yollar }
    }
    throw "Çıktı için boş dosya adı bulunamadı: $klasor"
}

# Geçici klasördeki bitmiş dosyayı hedefe taşır. Varsayılan: hedef varsa HATA (asla üzerine yazmaz).
function Tasi([string]$kaynak, [string]$hedef) {
    if ($UzerineYaz -and (Test-Path -LiteralPath $hedef)) { Remove-Item -LiteralPath $hedef -Force }
    [System.IO.File]::Move($kaynak, $hedef)
}

$hata = 0
foreach ($girisHam in $Dosyalar) {
    $gecici = $null
    try {
        if (-not (Test-Path -LiteralPath $girisHam -PathType Leaf)) { throw "Dosya bulunamadı: $girisHam" }
        $giris = (Resolve-Path -LiteralPath $girisHam).Path
        $ad = [System.IO.Path]::GetFileNameWithoutExtension($giris)
        $klasor = Split-Path -Parent $giris
        $gecici = [System.IO.Path]::Combine([System.IO.Path]::GetTempPath(), 'sestemizle_' + [guid]::NewGuid().ToString('N'))
        New-Item -ItemType Directory -Path $gecici | Out-Null
        Yaz "`n=== $ad ===" 'Cyan'

        $ham = Olc $giris $gecici
        if ($Kontrol) {
            Yaz (Satir 'Ölçüm' $ham)
            foreach ($n in (Degerlendir $ham)) { Yaz " - $n" 'White' }
            continue
        }

        # Tüm ffmpeg yazımları geçici klasöre ve -n (asla üzerine yazma) ile yapılır.
        # 1) 48 kHz mono + 80 Hz alt kesim (masa/klima uğultusunu keser)
        $ara = Join-Path $gecici 'giris.wav'
        $null = Calistir $script:FFMPEG @('-hide_banner', '-n', '-i', $giris, '-vn', '-ac', '1', '-ar', '48000', '-af', 'highpass=f=80:poles=2', '-c:a', 'pcm_s16le', $ara) $null

        # 2) Seviye ön ayarı (-20 LUFS) + 3:1 sıkıştırma. Gürültü bastırmadan ÖNCE yapılır: sıkıştırmanın
        #    yükselttiği arka plan gürültüsü bir sonraki adımda temizlenir (testlerde gürültü tabanı ~17 dB daha iyi).
        Yaz '  Seviye ayarı ve 3:1 sıkıştırma...'
        $m0 = Olc-Loudnorm $ara $null
        if ("$($m0.input_i)" -match 'inf') { throw 'Kayıt tamamen sessiz görünüyor (ses yüksekliği ölçülemedi). Mikrofonun seçili ve izinli olduğunu kontrol edip tekrar kaydedin.' }
        $kazanc = -20 - (Sayi $m0.input_i)
        if ($kazanc -gt 40) { $kazanc = 40 }
        $sik = Join-Path $gecici 'sikistirilmis.wav'
        $null = Calistir $script:FFMPEG @('-hide_banner', '-n', '-i', $ara, '-af', "volume=$(F $kazanc '0.00')dB,$kompresor,alimiter=limit=0.89:level=false", '-c:a', 'pcm_s16le', $sik) $null

        # 3) Gürültü bastırma
        $temizAra = Join-Path $gecici 'temiz_ara.wav'
        if ($Mod -eq 'ai') {
            Yaz "  DeepFilterNet3 gürültü bastırma (en fazla $Guc dB)..."
            $dfCikis = Join-Path $gecici 'df'
            $null = Calistir $DF @('-D', '-a', "$Guc", '-o', $dfCikis, $sik) $null
            $dfDosya = Join-Path $dfCikis 'sikistirilmis.wav'
            if (-not (Test-Path -LiteralPath $dfDosya)) { throw 'deep-filter çıktı dosyası üretmedi.' }
            [System.IO.File]::Move($dfDosya, $temizAra)
        }
        else {
            # afftdn'e kaydın ölçülen gürültü tabanı verilir (Audacity'nin "gürültü profili" mantığına yakın)
            $nf = [Math]::Max(-80, [Math]::Min(-20, (Olc $sik $gecici).Taban + 7))
            Yaz "  Klasik spektral gürültü azaltma (ffmpeg afftdn, taban $(F $nf) dB)..."
            $null = Calistir $script:FFMPEG @('-hide_banner', '-n', '-i', $sik, '-af', "afftdn=nr=20:nf=$(F $nf):nt=w", '-c:a', 'pcm_s16le', $temizAra) $null
        }

        # 4) İki geçişli EBU R128 ses yüksekliği (hedef LUFS, gerçek tepe sınırı), 48 kHz / 24-bit çıkış
        Yaz "  Ses yüksekliği ($(F $HedefLUFS) LUFS, tepe $(F $TepeDB) dBTP)..."
        $m2 = Olc-Loudnorm $temizAra $null
        $gTemiz = Join-Path $gecici 'cikis_temiz.wav'
        $null = Calistir $script:FFMPEG @('-hide_banner', '-n', '-i', $temizAra, '-af', (Loudnorm-Filtre $m2), '-ar', '48000', '-c:a', 'pcm_s24le', $gTemiz) $null

        # 5) Önce/sonra dosyası: ham kayıt da aynı LUFS'e getirilir ki "daha yüksek = daha iyi" yanılgısı olmasın
        $hamSeviyeli = Join-Path $gecici 'ham_seviyeli.wav'
        $mh = Olc-Loudnorm $ara $null
        $null = Calistir $script:FFMPEG @('-hide_banner', '-n', '-i', $ara, '-af', (Loudnorm-Filtre $mh), '-ar', '48000', '-c:a', 'pcm_s24le', $hamSeviyeli) $null
        $gOnceSonra = Join-Path $gecici 'cikis_onceSonra.wav'
        $fc = 'anullsrc=r=48000:cl=mono,atrim=duration=0.6[s1];sine=f=880:r=48000:d=0.25,volume=0.25[bip];anullsrc=r=48000:cl=mono,atrim=duration=0.6[s2];' +
              '[0:a]aformat=sample_rates=48000:channel_layouts=mono[a];[1:a]aformat=sample_rates=48000:channel_layouts=mono[b];' +
              '[a][s1][bip][s2][b]concat=n=5:v=0:a=1[o]'
        $null = Calistir $script:FFMPEG @('-hide_banner', '-n', '-i', $hamSeviyeli, '-i', $gTemiz, '-filter_complex', $fc, '-map', '[o]', '-c:a', 'pcm_s24le', $gOnceSonra) $null

        # 6) Son doğrulama: temiz çıktı okunabilir ve süresi girişle uyumlu olmalı
        $son = Olc $gTemiz $gecici
        $hamS = Olc $hamSeviyeli $gecici
        if ([Math]::Abs($son.Sure - $ham.Sure) -gt 1.0) { throw "Çıktı süresi girişle uyuşmuyor ($(F $son.Sure) sn / $(F $ham.Sure) sn)." }
        if ($son.LUFS -lt -60) { throw 'Çıktı neredeyse sessiz; temizleme başarısız sayıldı.' }

        # 7) Rapor ve taşıma (yalnız her şey başarılıysa; varsayılan olarak mevcut dosyalara dokunulmaz)
        $hedef = Cikis-Adlari $klasor $ad
        $rapor = @(
            "Ses-Temizle raporu - $(Get-Date -Format 'yyyy-MM-dd HH:mm')",
            "Giriş : $giris",
            "Mod   : $Mod$(if ($Mod -eq 'ai') { " (DeepFilterNet3, en fazla $Guc dB)" })",
            "Süre  : $(F $ham.Sure) sn",
            '',
            (Satir 'Ham (olduğu gibi)' $ham),
            (Satir 'Ham (seviye eşit)' $hamS),
            (Satir 'Temiz' $son),
            '',
            "Gürültü tabanı aynı seviyede kıyasla $(F ($hamS.Taban - $son.Taban)) dB düştü.",
            '',
            'Kayıt hakkında notlar (ham kayda göre):'
        ) + ((Degerlendir $ham) | ForEach-Object { " - $_" }) + @(
            '',
            "Bu sayılar kulağın yerini tutmaz: $([System.IO.Path]::GetFileName($hedef.OnceSonra)) dosyasını kulaklıkla dinleyin",
            '(önce ham, bipten sonra temiz). Ses robotik, boğuk veya kesik kesik geldiyse',
            '"Ses-Temizle-Dogal.bat" ile (-Guc 30) tekrar deneyin.'
        )
        $gRapor = Join-Path $gecici 'cikis_rapor.txt'
        [System.IO.File]::WriteAllLines($gRapor, [string[]]$rapor, (New-Object System.Text.UTF8Encoding($true)))
        Tasi $gTemiz $hedef.Temiz
        Tasi $gOnceSonra $hedef.OnceSonra
        Tasi $gRapor $hedef.Rapor
        $rapor | Select-Object -Skip 5 -First 6 | ForEach-Object { Yaz $_ }
        Yaz "  -> $($hedef.Temiz)" 'Green'
        Yaz "  -> $($hedef.OnceSonra)" 'Green'
        Yaz "  -> $($hedef.Rapor)" 'Green'
    }
    catch {
        $hata++
        Yaz "HATA ($girisHam): $($_.Exception.Message)" 'Red'
    }
    finally {
        # Yalnız bu çalıştırmanın oluşturduğu GUID klasörü silinir.
        if ($gecici -and (Test-Path -LiteralPath $gecici)) { Remove-Item -LiteralPath $gecici -Recurse -Force -ErrorAction SilentlyContinue }
    }
}
if ($hata -gt 0) {
    Yaz "`n$hata dosya başarısız oldu." 'Red'
    exit 3
}
exit 0
