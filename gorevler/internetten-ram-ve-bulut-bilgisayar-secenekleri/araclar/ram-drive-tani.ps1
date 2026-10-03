<#
.SYNOPSIS
  Teşhis: Bilgisayarın gerçek RAM'i, sayfa dosyası ve Google Drive sanal sürücüsünün
  kapasitesinin hangi yerel diskle ilişkili OLABİLECEĞİ.

.DESCRIPTION
  NE YAPMAZ: Sistem ayarını, kayıt defterini, Drive ayarını veya hiçbir dosyayı
  değiştirmez ya da silmez; internete bağlanmaz. Yalnızca CIM sorguları, kayıt defteri
  OKUMA ve önbellek klasörünü sınırlı listeleme yapar.
  TEK YAZMA: -CiktiJson verilirse yalnız o JSON dosyasını oluşturur. Dosya zaten varsa
  -UzerineYaz verilmedikçe yazmaz (mevcut dosyayı ezmez).

  Kesinlik kuralı: Drive sürücüsünün kapasitesini bir yerel diske bağlamak için
  OKUNABİLİR KANIT gerekir (kayıt defterindeki ContentCachePath). Yalnız kapasite
  benzerliği "aday" sayılır; kanıt yoksa sonuç "bilinmiyor"dur.

  Windows'ta:  powershell -ExecutionPolicy Bypass -File .\ram-drive-tani.ps1
  Drive harfi farklıysa:  -DriveHarfi H:
  Test/çevrimdışı:  -GirdiJson ornek.json  (Windows olmadan mantığı sınar)

.PARAMETER DriveHarfi      Google Drive sürücüsünün harfi (boşsa kayıt defteri/etiketten bulunur).
.PARAMETER GirdiJson       Toplama yerine bu JSON verisini kullan (test için).
.PARAMETER CiktiJson       Ham veri + bulguları bu YENİ dosyaya yaz.
.PARAMETER UzerineYaz      -CiktiJson dosyası varsa üzerine yazmaya izin ver.
.PARAMETER OnbellekMaxOge  Önbellek taramasında en çok bu kadar öğe (dosya+klasör) gez.
.PARAMETER OnbellekMaxSaniye Önbellek taraması için süre sınırı.
#>
[CmdletBinding()]
param(
    [string]$DriveHarfi = '',
    [string]$GirdiJson = '',
    [string]$CiktiJson = '',
    [switch]$UzerineYaz,
    [int]$OnbellekMaxOge = 200000,
    [double]$OnbellekMaxSaniye = 20
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$script:KapasiteToleransi = 0.01   # %1: yalnız ADAY belirlemek için

function Format-Boyut($Bayt) {
    if ($null -eq $Bayt) { return 'bilinmiyor' }
    $b = [double]$Bayt
    if ($b -ge 1GB) { return ('{0:N1} GB' -f ($b / 1GB)) }
    return ('{0:N0} MB' -f ($b / 1MB))
}

function Get-Ozellik($Nesne, [string]$Ad) {
    # StrictMode altında eksik JSON alanlarını $null olarak okur.
    if ($null -eq $Nesne) { return $null }
    $p = $Nesne.PSObject.Properties[$Ad]
    if ($p) { return $p.Value }
    return $null
}

function Get-SurucuHarfi([string]$Yol) {
    if ($Yol -match '^\s*([A-Za-z]):') { return ($Matches[1].ToUpper() + ':') }
    return $null   # UNC, klasöre bağlama vb.: harf çıkarılamaz
}

# ---------------------------------------------------------------- Sınırlı klasör ölçümü
function Get-VarsayilanListeleyici {
    # Tembel (lazy) numaralandırıcı döndürür: büyük klasör belleğe toplanmadan gezilir.
    return { param([string]$Klasor) ,([System.IO.DirectoryInfo]::new($Klasor).EnumerateFileSystemInfos()) }
}

function ConvertTo-Oge($Ham) {
    if ($Ham -is [System.IO.FileSystemInfo]) {
        $kl = $Ham -is [System.IO.DirectoryInfo]
        return [pscustomobject]@{
            Yol = $Ham.FullName; Klasor = $kl
            Baglanti = [bool]($Ham.Attributes -band [System.IO.FileAttributes]::ReparsePoint)
            Boyut = $(if ($kl) { 0 } else { [double]$Ham.Length })
        }
    }
    return $Ham   # testlerde hazır nesne
}

function Measure-KlasorSinirli {
    <#
      Klasörü öğe ve süre sınırıyla gezer. Bağlantıları (junction/symlink) izlemez.
      Durum: 'Tam' (tamamı gezildi, hata yok) | 'Kismi' (sınır veya erişim hatası) | 'Yok'.
      Kısmi sonuçta ToplamBayt bir ALT SINIRDIR.
    #>
    param(
        [Parameter(Mandatory)][string]$Yol,
        [int]$MaxOge = 200000,
        [double]$MaxSaniye = 20,
        [scriptblock]$Listeleyici = (Get-VarsayilanListeleyici),
        [scriptblock]$VarMi = { param($p) [System.IO.Directory]::Exists($p) }
    )
    $sonuc = [ordered]@{
        Yol = $Yol; Durum = 'Tam'; KesmeNedeni = @(); ToplamBayt = [double]0
        DosyaSayisi = 0; OgeSayisi = 0; ErisimHatasi = 0; HataOrnekleri = @()
        AtlananBaglanti = 0; MaxOge = $MaxOge; MaxSaniye = $MaxSaniye
    }
    if (-not (& $VarMi $Yol)) { $sonuc.Durum = 'Yok'; return [pscustomobject]$sonuc }

    $sw = [System.Diagnostics.Stopwatch]::StartNew()
    $yigin = [System.Collections.Generic.Stack[string]]::new()
    $yigin.Push($Yol)
    $kes = $null
    while ($yigin.Count -gt 0 -and -not $kes) {
        $klasor = $yigin.Pop()
        try {
            $liste = & $Listeleyici $klasor
            foreach ($ham in $liste) {
                $o = ConvertTo-Oge $ham
                if ($sonuc.OgeSayisi -ge $MaxOge) { $kes = 'oge_siniri'; break }
                if ($sw.Elapsed.TotalSeconds -ge $MaxSaniye) { $kes = 'zaman_siniri'; break }
                $sonuc.OgeSayisi++
                if ($o.Baglanti) { $sonuc.AtlananBaglanti++; continue }
                if ($o.Klasor) { $yigin.Push($o.Yol) }
                else { $sonuc.DosyaSayisi++; $sonuc.ToplamBayt += [double]$o.Boyut }
            }
        } catch {
            $sonuc.ErisimHatasi++
            if ($sonuc.HataOrnekleri.Count -lt 5) {
                $sonuc.HataOrnekleri += ('{0}: {1}' -f $klasor, $_.Exception.GetType().Name)
            }
        }
        if (-not $kes -and $yigin.Count -gt 0 -and $sw.Elapsed.TotalSeconds -ge $MaxSaniye) { $kes = 'zaman_siniri' }
    }
    if ($kes) { $sonuc.KesmeNedeni += $kes }
    if ($sonuc.ErisimHatasi -gt 0) { $sonuc.KesmeNedeni += 'erisim_hatasi' }
    if ($sonuc.KesmeNedeni.Count -gt 0) { $sonuc.Durum = 'Kismi' }
    $sonuc.SureSaniye = [math]::Round($sw.Elapsed.TotalSeconds, 2)
    return [pscustomobject]$sonuc
}

# ---------------------------------------------------------------- Canlı toplama (Windows)
function Get-DriveFsAyari {
    # Google'ın belgelediği kayıt defteri konumları (yalnız okunur):
    # Policies (üstün) > HKCU > HKLM.  Uygulama içinden yapılan değişiklikler buraya
    # yazılmayabilir; bu yüzden değer yoksa "bilinmiyor" kabul edilir.
    $anahtarlar = @(
        'HKLM:\SOFTWARE\Policies\Google\DriveFS',
        'HKCU:\Software\Google\DriveFS',
        'HKLM:\SOFTWARE\Google\DriveFS'
    )
    $ayar = [ordered]@{ ContentCachePath = $null; ContentCacheKaynak = $null
                        DefaultMountPoint = $null; MountKaynak = $null; OkumaHatalari = @() }
    foreach ($a in $anahtarlar) {
        try {
            if (-not (Test-Path $a)) { continue }
            $p = Get-ItemProperty -Path $a
            $c = Get-Ozellik $p 'ContentCachePath'
            $m = Get-Ozellik $p 'DefaultMountPoint'
            if ($c -and -not $ayar.ContentCachePath) {
                $ayar.ContentCachePath = [Environment]::ExpandEnvironmentVariables([string]$c); $ayar.ContentCacheKaynak = "$a\ContentCachePath" }
            if ($m -and -not $ayar.DefaultMountPoint) {
                $ayar.DefaultMountPoint = [Environment]::ExpandEnvironmentVariables([string]$m); $ayar.MountKaynak = "$a\DefaultMountPoint" }
        } catch { $ayar.OkumaHatalari += ('{0}: {1}' -f $a, $_.Exception.Message) }
    }
    return [pscustomobject]$ayar
}

function Get-TaniVerisi([int]$MaxOge, [double]$MaxSaniye) {
    $cs   = Get-CimInstance Win32_ComputerSystem
    $os   = Get-CimInstance Win32_OperatingSystem
    $pf   = @(Get-CimInstance Win32_PageFileUsage -ErrorAction SilentlyContinue)
    $disk = @(Get-CimInstance Win32_LogicalDisk)
    $ayar = Get-DriveFsAyari

    $varsayilan = Join-Path $env:LOCALAPPDATA 'Google\DriveFS'
    $olculecek  = if ($ayar.ContentCachePath) { $ayar.ContentCachePath } else { $varsayilan }

    [pscustomobject]@{
        FizikselRamBayt  = [double]$cs.TotalPhysicalMemory
        BosRamBayt       = [double]$os.FreePhysicalMemory * 1KB
        SayfaDosyalari   = @($pf | ForEach-Object {
                              [pscustomobject]@{ Ad = $_.Name; BoyutMB = $_.AllocatedBaseSize; KullanilanMB = $_.CurrentUsage } })
        Diskler          = @($disk | ForEach-Object {
                              [pscustomobject]@{ Harf = $_.DeviceID; Etiket = $_.VolumeName; DosyaSistemi = $_.FileSystem
                                                 Tur = $_.DriveType; BoyutBayt = [double]$_.Size; BosBayt = [double]$_.FreeSpace } })
        DriveFsAyar      = $ayar
        VarsayilanOnbellekYolu = $varsayilan
        OnbellekOlcum    = Measure-KlasorSinirli -Yol $olculecek -MaxOge $MaxOge -MaxSaniye $MaxSaniye
    }
}

# ---------------------------------------------------------------- Analiz (saf fonksiyonlar)
function Find-DriveSurucusu($Veri, [string]$Harf) {
    $diskler = @(Get-Ozellik $Veri 'Diskler')
    if ($Harf) {
        $h = $Harf.Trim().TrimEnd('\').ToUpper(); if (-not $h.EndsWith(':')) { $h += ':' }
        $d = @($diskler | Where-Object { $_.Harf -eq $h }) | Select-Object -First 1
        if ($d) { return [pscustomobject]@{ Disk = $d; Kaynak = 'kullanıcı parametresi (-DriveHarfi)' } }
        return $null
    }
    $mp = Get-Ozellik (Get-Ozellik $Veri 'DriveFsAyar') 'DefaultMountPoint'
    $mh = if ($mp -and $mp.Trim().Length -le 3) { Get-SurucuHarfi $mp } else { $null }
    if ($mh) {
        $d = @($diskler | Where-Object { $_.Harf -eq $mh }) | Select-Object -First 1
        if ($d) { return [pscustomobject]@{ Disk = $d; Kaynak = 'kayıt defteri DefaultMountPoint' } }
    }
    $d = @($diskler | Where-Object { $_.Etiket -match 'Google\s*Drive' }) | Select-Object -First 1
    if ($d) { return [pscustomobject]@{ Disk = $d; Kaynak = 'birim etiketi "Google Drive" (sezgisel)' } }
    return $null
}

function Test-KapasiteYakin($a, $b) {
    $x = [double]$a; $y = [double]$b
    if ($x -le 0 -or $y -le 0) { return $false }
    return ([math]::Abs($x - $y) / [math]::Max($x, $y)) -lt $script:KapasiteToleransi
}

function Get-OnbellekIliskisi($Veri, $G) {
    <#
      Sonuç Karar alanı:
        DOGRULANDI   – önbellek diski kayıt defteri kanıtıyla biliniyor VE G kapasitesi ona eşit
        ESLESMIYOR   – önbellek diski kanıtla biliniyor ama G kapasitesi farklı
        ADAY         – kanıt yok; kapasitesi benzeyen disk(ler) yalnız aday
        BILINMIYOR   – kanıt yok ve aday da yok
    #>
    $ayar     = Get-Ozellik $Veri 'DriveFsAyar'
    $kanitYol = Get-Ozellik $ayar 'ContentCachePath'
    $kanitHarf = if ($kanitYol) { Get-SurucuHarfi $kanitYol } else { $null }
    $adaylar  = @(@(Get-Ozellik $Veri 'Diskler') | Where-Object {
                    $_.Harf -ne $G.Harf -and (Test-KapasiteYakin $_.BoyutBayt $G.BoyutBayt) })
    $adayHarf = @($adaylar | ForEach-Object Harf)

    if ($kanitHarf) {
        $kd = @(@(Get-Ozellik $Veri 'Diskler') | Where-Object { $_.Harf -eq $kanitHarf }) | Select-Object -First 1
        if ($kd -and (Test-KapasiteYakin $kd.BoyutBayt $G.BoyutBayt)) {
            return [pscustomobject]@{ Karar = 'DOGRULANDI'; Disk = $kanitHarf; Adaylar = $adayHarf
                                      Kanit = (Get-Ozellik $ayar 'ContentCacheKaynak') }
        }
        return [pscustomobject]@{ Karar = 'ESLESMIYOR'; Disk = $kanitHarf; Adaylar = $adayHarf
                                  Kanit = (Get-Ozellik $ayar 'ContentCacheKaynak') }
    }
    if ($adaylar.Count -gt 0) {
        return [pscustomobject]@{ Karar = 'ADAY'; Disk = $null; Adaylar = $adayHarf; Kanit = $null }
    }
    return [pscustomobject]@{ Karar = 'BILINMIYOR'; Disk = $null; Adaylar = @(); Kanit = $null }
}

function Get-TaniBulgulari($Veri, [string]$Harf = '') {
    $bulgular = [System.Collections.Generic.List[object]]::new()
    $ekle = { param($Kod, $Metin) $bulgular.Add([pscustomobject]@{ Kod = $Kod; Metin = $Metin }) }

    # --- RAM ---
    $ram = [double](Get-Ozellik $Veri 'FizikselRamBayt')
    $bos = [double](Get-Ozellik $Veri 'BosRamBayt')
    $sayfaMB = 0
    foreach ($p in @(Get-Ozellik $Veri 'SayfaDosyalari')) { if ($p) { $sayfaMB += [double]$p.BoyutMB } }
    & $ekle 'RAM_FIZIKSEL' ("Takılı fiziksel RAM: {0}. Bu sayı yalnızca anakarttaki RAM modülleriyle artar; internet, Google Drive veya bulut hesabı bu sayıyı değiştiremez." -f (Format-Boyut $ram))
    & $ekle 'RAM_BOS' ("Şu an boş RAM: {0} (%{1:N0})." -f (Format-Boyut $bos), (100 * $bos / [math]::Max($ram, 1)))
    & $ekle 'SANAL_BELLEK' ("Windows 'sanal bellek' sınırı ≈ RAM + sayfa dosyası = {0}. Sayfa dosyası diskte durur ve RAM'den çok daha yavaştır; RAM'in yerini tutmaz." -f (Format-Boyut ($ram + $sayfaMB * 1MB)))
    if ($ram -gt 0 -and ($bos / $ram) -lt 0.10) {
        & $ekle 'RAM_DUSUK' 'Boş RAM %10''un altında: açık programları/sekme sayısını azaltmak veya fiziksel RAM eklemek gerçek çözümdür.'
    }

    # --- Google Drive sanal sürücüsü ---
    $bul = Find-DriveSurucusu $Veri $Harf
    if ($null -eq $bul) {
        & $ekle 'DRIVE_YOK' 'Google Drive sanal sürücüsü bulunamadı (kayıt defterinde harf yok, etiket "Google Drive" değil). Harfini -DriveHarfi ile verin.'
    } else {
        $g = $bul.Disk
        & $ekle 'DRIVE_SURUCU' ("Drive sürücüsü: {0}, toplam {1}, boş {2}. Tespit kaynağı: {3}." -f $g.Harf, (Format-Boyut $g.BoyutBayt), (Format-Boyut $g.BosBayt), $bul.Kaynak)
        $il = Get-OnbellekIliskisi $Veri $g
        $dolu = [double]$g.BoyutBayt - [double]$g.BosBayt
        switch ($il.Karar) {
            'DOGRULANDI' {
                $ek = @($il.Adaylar | Where-Object { $_ -ne $il.Disk })
                $not = if ($ek.Count) { " ({0} de aynı kapasitede ama önbellek kanıtı {1}'yi gösteriyor.)" -f ($ek -join ', '), $il.Disk } else { '' }
                & $ekle 'DRIVE_YANSITMA_DOGRULANDI' ("Önbellek konumu kanıtla {0} üzerinde ({1}) ve {2} kapasitesi {0} ile aynı. Bu yüzden {2}'de görünen toplam/boş alan büyük olasılıkla {0}'ın yerel alanıdır, Google kotanız değildir; '{3} dolu' görünen kısım {0}'ın dolu kısmı olabilir.{4}" -f $il.Disk, $il.Kanit, $g.Harf, (Format-Boyut $dolu), $not)
            }
            'ESLESMIYOR' {
                & $ekle 'DRIVE_ESLESMIYOR' ("Önbellek konumu kanıtla {0} üzerinde ({1}) ama {2} kapasitesi {0} ile eşleşmiyor. Kapasitenin neyi gösterdiği bu veriyle BİLİNMİYOR.{3}" -f $il.Disk, $il.Kanit, $g.Harf, $(if (@($il.Adaylar).Count) { " Kapasitesi benzeyen ama kanıtsız aday: " + ($il.Adaylar -join ', ') + "." } else { '' }))
            }
            'ADAY' {
                & $ekle 'DRIVE_YANSITMA_ADAY' ("{0} kapasitesi şu disk(ler)le neredeyse aynı: {1}. Bu yalnız bir ADAY ilişkidir; önbelleğin nerede olduğuna dair kayıt defteri kanıtı yok (ayar uygulama içinden değiştirilmiş olabilir). Hangi diski yansıttığı BİLİNMİYOR. Drive → Ayarlar → Tercihler → Gelişmiş ayarlar → 'Yerel önbellek dosyaları dizini' satırına bakarak doğrulayın." -f $g.Harf, ($il.Adaylar -join ', '))
            }
            default {
                & $ekle 'DRIVE_ILISKI_BILINMIYOR' ("{0} kapasitesi hiçbir yerel diskle eşleşmiyor ve önbellek konumu için kanıt yok; ilişki BİLİNMİYOR." -f $g.Harf)
            }
        }
        & $ekle 'DRIVE_KOTA' 'Gerçek Google depolama kotası Windows sürücü kapasitesinden okunamaz: https://one.google.com/storage adresinden bakın.'
        & $ekle 'DRIVE_BELLEK_DEGIL' 'Google Drive bir DEPOLAMA alanıdır (dosya saklar). RAM/bellek değildir; bilgisayarın RAM miktarını veya hızını artırmaz.'
    }

    # --- Önbellek ölçümü ---
    $o = Get-Ozellik $Veri 'OnbellekOlcum'
    if ($null -ne $o) {
        switch ($o.Durum) {
            'Yok'  { & $ekle 'ONBELLEK_YOK' ("Önbellek klasörü bulunamadı: {0}" -f $o.Yol) }
            'Tam'  { & $ekle 'ONBELLEK_TAM' ("Önbellek ({0}): {1}, {2} dosya. Tarama TAM (sınır aşılmadı, erişim hatası yok; izlenmeyen bağlantı: {3})." -f $o.Yol, (Format-Boyut $o.ToplamBayt), $o.DosyaSayisi, $o.AtlananBaglanti) }
            default {
                $neden = @($o.KesmeNedeni | ForEach-Object {
                    switch ($_) { 'oge_siniri' { "öğe sınırı ($($o.MaxOge))" } 'zaman_siniri' { "süre sınırı ($($o.MaxSaniye) sn)" } 'erisim_hatasi' { "$($o.ErisimHatasi) klasöre erişilemedi" } default { $_ } } }) -join ', '
                & $ekle 'ONBELLEK_KISMI' ("Önbellek ({0}): EN AZ {1} ({2} dosya sayıldı). Tarama KISMİ: {3}. Gerçek boyut daha büyük olabilir." -f $o.Yol, (Format-Boyut $o.ToplamBayt), $o.DosyaSayisi, $neden)
            }
        }
    }
    return ,$bulgular
}

# ---------------------------------------------------------------- Ana akış (dot-source edilince çalışmaz)
if ($MyInvocation.InvocationName -ne '.') {
    if ($CiktiJson -and (Test-Path -LiteralPath $CiktiJson) -and -not $UzerineYaz) {
        throw "Çıktı dosyası zaten var: $CiktiJson (ezmek için -UzerineYaz ekleyin)."
    }
    if ($GirdiJson) {
        $veri = Get-Content -Raw -Encoding UTF8 $GirdiJson | ConvertFrom-Json
    } elseif ([System.Environment]::OSVersion.Platform -ne 'Win32NT') {
        throw 'Canlı toplama yalnızca Windows''ta çalışır. Test için -GirdiJson kullanın.'
    } else {
        $veri = Get-TaniVerisi $OnbellekMaxOge $OnbellekMaxSaniye
    }
    $bulgular = Get-TaniBulgulari $veri $DriveHarfi
    Write-Output '=== RAM ve Google Drive teşhisi (ayar değiştirmez, dosya silmez) ==='
    foreach ($b in $bulgular) { Write-Output ("[{0}] {1}" -f $b.Kod, $b.Metin) }
    if ($CiktiJson) {
        [pscustomobject]@{ Veri = $veri; Bulgular = $bulgular } |
            ConvertTo-Json -Depth 8 | Set-Content -Encoding UTF8 -LiteralPath $CiktiJson
        Write-Output "Yazılan tek dosya: $CiktiJson"
    }
}
