<#
.SYNOPSIS
  Salt-okunur teşhis: Bilgisayarın gerçek RAM'ini, sayfa dosyasını ve Google Drive
  sanal sürücüsünün neden C: ile aynı kapasitede/dolulukta göründüğünü gösterir.

.DESCRIPTION
  Hiçbir ayarı DEĞİŞTİRMEZ, hiçbir dosya silmez, internete bağlanmaz.
  Windows'ta: PowerShell'i açın ve
      powershell -ExecutionPolicy Bypass -File .\ram-drive-tani.ps1
  çalıştırın. Drive harfi farklıysa:  -DriveHarfi H:
  Test/çevrimdışı mod:  -GirdiJson ornek.json  (Windows olmadan mantığı sınar)

.PARAMETER DriveHarfi   Google Drive sürücüsünün harfi (boşsa etiketten bulunur).
.PARAMETER GirdiJson    Toplama yerine bu JSON verisini kullan (test için).
.PARAMETER CiktiJson    Ham veriyi + bulguları bu dosyaya yaz.
#>
[CmdletBinding()]
param(
    [string]$DriveHarfi = '',
    [string]$GirdiJson = '',
    [string]$CiktiJson = ''
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Format-Boyut([Nullable[double]]$Bayt) {
    if ($null -eq $Bayt) { return 'bilinmiyor' }
    $gb = $Bayt / 1GB
    if ($gb -ge 1) { return ('{0:N1} GB' -f $gb) }
    return ('{0:N0} MB' -f ($Bayt / 1MB))
}

function Get-TaniVerisi {
    # Yalnızca Windows'ta çalışır; yalnız okuma yapan CIM sorguları kullanır.
    $cs   = Get-CimInstance Win32_ComputerSystem
    $os   = Get-CimInstance Win32_OperatingSystem
    $pf   = @(Get-CimInstance Win32_PageFileUsage -ErrorAction SilentlyContinue)
    $disk = @(Get-CimInstance Win32_LogicalDisk)

    $onbellek = $null
    $yol = Join-Path $env:LOCALAPPDATA 'Google\DriveFS'
    if (Test-Path $yol) {
        $onbellek = (Get-ChildItem $yol -Recurse -File -Force -ErrorAction SilentlyContinue |
                     Measure-Object Length -Sum).Sum
    }

    [pscustomobject]@{
        FizikselRamBayt     = [double]$cs.TotalPhysicalMemory
        BosRamBayt          = [double]$os.FreePhysicalMemory * 1KB
        SayfaDosyalari      = @($pf | ForEach-Object {
                                 [pscustomobject]@{ Ad = $_.Name; BoyutMB = $_.AllocatedBaseSize; KullanilanMB = $_.CurrentUsage } })
        Diskler             = @($disk | ForEach-Object {
                                 [pscustomobject]@{ Harf = $_.DeviceID; Etiket = $_.VolumeName; DosyaSistemi = $_.FileSystem
                                                    Tur = $_.DriveType; BoyutBayt = [double]$_.Size; BosBayt = [double]$_.FreeSpace } })
        DriveFsOnbellekBayt = $onbellek
    }
}

function Find-DriveSurucusu($Veri, [string]$Harf) {
    if ($Harf) {
        $h = $Harf.TrimEnd('\').ToUpper()
        if (-not $h.EndsWith(':')) { $h += ':' }
        return @($Veri.Diskler | Where-Object { $_.Harf -eq $h }) | Select-Object -First 1
    }
    return @($Veri.Diskler | Where-Object { $_.Etiket -match 'Google\s*Drive' }) | Select-Object -First 1
}

function Get-TaniBulgulari($Veri, [string]$Harf = '') {
    $bulgular = [System.Collections.Generic.List[object]]::new()
    $ekle = { param($Kod, $Metin) $bulgular.Add([pscustomobject]@{ Kod = $Kod; Metin = $Metin }) }

    # --- RAM ---
    $ram = [double]$Veri.FizikselRamBayt
    $sayfaMB = 0
    foreach ($p in @($Veri.SayfaDosyalari)) { $sayfaMB += [double]$p.BoyutMB }
    $commit = $ram + $sayfaMB * 1MB
    & $ekle 'RAM_FIZIKSEL' ("Takılı fiziksel RAM: {0}. Bu sayı yalnızca anakarttaki RAM modülleriyle artar; internet, Google Drive veya bulut hesabı bu sayıyı değiştiremez." -f (Format-Boyut $ram))
    & $ekle 'RAM_BOS' ("Şu an boş RAM: {0} (%{1:N0})." -f (Format-Boyut $Veri.BosRamBayt), (100 * $Veri.BosRamBayt / [math]::Max($ram,1)))
    & $ekle 'SANAL_BELLEK' ("Windows 'sanal bellek' sınırı ≈ RAM + sayfa dosyası = {0}. Sayfa dosyası diskte durur ve RAM'den çok daha yavaştır; RAM'in yerini tutmaz." -f (Format-Boyut $commit))
    if ($ram -gt 0 -and ($Veri.BosRamBayt / $ram) -lt 0.10) {
        & $ekle 'RAM_DUSUK' 'Boş RAM %10''un altında: açık programları/sekme sayısını azaltmak veya fiziksel RAM eklemek gerçek çözümdür.'
    }

    # --- Google Drive sanal sürücüsü ---
    $g = Find-DriveSurucusu $Veri $Harf
    if ($null -eq $g) {
        & $ekle 'DRIVE_YOK' 'Google Drive sanal sürücüsü bulunamadı (etiket "Google Drive" değil). Harfini -DriveHarfi ile verin.'
    } else {
        $ikiz = @($Veri.Diskler | Where-Object {
            $_.Harf -ne $g.Harf -and [double]$_.BoyutBayt -gt 0 -and
            [math]::Abs([double]$_.BoyutBayt - [double]$g.BoyutBayt) / [double]$_.BoyutBayt -lt 0.01
        }) | Select-Object -First 1
        $dolu = [double]$g.BoyutBayt - [double]$g.BosBayt
        if ($ikiz) {
            & $ekle 'DRIVE_YANSITMA' ("{0} (Google Drive) toplamı {1}, {2} ile aynı. Bu sayı bulut kotanız DEĞİL; Drive for desktop önbelleğinin durduğu yerel diskin ({2}) boyutunu ve boş alanını gösterir. '{3} dolu' görünen kısım aslında {2} sürücüsünün dolu kısmıdır, Drive'ınız boş olsa bile böyle görünür." -f $g.Harf, (Format-Boyut $g.BoyutBayt), $ikiz.Harf, (Format-Boyut $dolu))
        } else {
            & $ekle 'DRIVE_FARKLI' ("{0} (Google Drive) toplamı {1}; hiçbir yerel diskle eşleşmiyor. Gerçek bulut kotasını https://one.google.com/storage adresinden kontrol edin." -f $g.Harf, (Format-Boyut $g.BoyutBayt))
        }
        & $ekle 'DRIVE_BELLEK_DEGIL' 'Google Drive bir DEPOLAMA alanıdır (dosya saklar). RAM/bellek değildir; bilgisayarın RAM miktarını veya hızını artırmaz.'
    }
    if ($null -ne $Veri.DriveFsOnbellekBayt) {
        & $ekle 'DRIVE_ONBELLEK' ("Drive for desktop yerel önbelleği (%LOCALAPPDATA%\Google\DriveFS): {0}. Bu, C: üzerinde gerçekten yer kaplayan kısımdır." -f (Format-Boyut $Veri.DriveFsOnbellekBayt))
    }
    return ,$bulgular
}

# --- Ana akış (dosya dot-source edilince çalışmaz) ---
if ($MyInvocation.InvocationName -ne '.') {
    if ($GirdiJson) {
        $veri = Get-Content -Raw -Encoding UTF8 $GirdiJson | ConvertFrom-Json
    } elseif ([System.Environment]::OSVersion.Platform -ne 'Win32NT') {
        throw 'Canlı toplama yalnızca Windows''ta çalışır. Test için -GirdiJson kullanın.'
    } else {
        $veri = Get-TaniVerisi
    }
    $bulgular = Get-TaniBulgulari $veri $DriveHarfi
    Write-Output '=== RAM ve Google Drive teşhisi (salt okunur) ==='
    foreach ($b in $bulgular) { Write-Output ("[{0}] {1}" -f $b.Kod, $b.Metin) }
    if ($CiktiJson) {
        [pscustomobject]@{ Veri = $veri; Bulgular = $bulgular } |
            ConvertTo-Json -Depth 6 | Set-Content -Encoding UTF8 $CiktiJson
        Write-Output "Ham veri yazıldı: $CiktiJson"
    }
}
