# Kabul testleri: ram-drive-tani.ps1 mantığını örnek JSON'lar, enjekte edilmiş
# listeleyici ve gerçek geçici klasörlerle sınar. Çalıştırma: pwsh -File tani-test.ps1
$ErrorActionPreference = 'Stop'
$kok = Split-Path -Parent $PSScriptRoot
$betik = Join-Path $kok 'ram-drive-tani.ps1'
. $betik
$script:hata = 0; $script:sayi = 0
function Kontrol([string]$Ad, [bool]$Kosul) {
    $script:sayi++
    if ($Kosul) { Write-Output "GEÇTİ  $Ad" } else { Write-Output "KALDI  $Ad"; $script:hata++ }
}
function Yukle($ad) { Get-Content -Raw -Encoding UTF8 (Join-Path $PSScriptRoot "ornekler/$ad") | ConvertFrom-Json }
function Kodlar($b) { @($b | ForEach-Object Kod) }
function Metin($b, $kod) { $x = @($b | Where-Object Kod -eq $kod) | Select-Object -First 1; if ($x) { [string]$x.Metin } else { '' } }

Write-Output '--- Drive/önbellek ilişkisi ---'
$b = Get-TaniBulgulari (Yukle 'g-c-ayni-kanitli.json')
Kontrol 'Kanıt (ContentCachePath=C:) + G=C → DOGRULANDI' ((Kodlar $b) -contains 'DRIVE_YANSITMA_DOGRULANDI')
Kontrol 'Doğrulanan metin kanıt kaynağını (kayıt defteri) yazar' ((Metin $b 'DRIVE_YANSITMA_DOGRULANDI') -match 'ContentCachePath')
Kontrol 'Tam ölçüm TAM olarak raporlanır' ((Kodlar $b) -contains 'ONBELLEK_TAM')

$b = Get-TaniBulgulari (Yukle 'g-c-ayni-kanitsiz.json')
Kontrol 'Kanıt yok + G=C → yalnız ADAY (doğrulandı DEĞİL)' (((Kodlar $b) -contains 'DRIVE_YANSITMA_ADAY') -and -not ((Kodlar $b) -contains 'DRIVE_YANSITMA_DOGRULANDI'))
Kontrol 'Aday metni BİLİNMİYOR der' ((Metin $b 'DRIVE_YANSITMA_ADAY') -match 'BİLİNMİYOR')
Kontrol 'Kısmi ölçüm KISMİ + EN AZ + nedenler' ((Metin $b 'ONBELLEK_KISMI') -match 'EN AZ' -and (Metin $b 'ONBELLEK_KISMI') -match 'öğe sınırı' -and (Metin $b 'ONBELLEK_KISMI') -match '3 klasöre erişilemedi')
Kontrol 'Kısmi ölçüm TAM gibi gösterilmez' (-not ((Kodlar $b) -contains 'ONBELLEK_TAM'))

$b = Get-TaniBulgulari (Yukle 'yanlis-pozitif.json')
Kontrol 'YANLIŞ POZİTİF: G=D kapasite ama önbellek kanıtla C → DOGRULANDI yok' (-not ((Kodlar $b) -contains 'DRIVE_YANSITMA_DOGRULANDI'))
Kontrol 'YANLIŞ POZİTİF: ESLESMIYOR + bilinmiyor' ((Metin $b 'DRIVE_ESLESMIYOR') -match 'C:' -and (Metin $b 'DRIVE_ESLESMIYOR') -match 'BİLİNMİYOR')

$v = Yukle 'esit-diskler-kanitli.json'
$b = Get-TaniBulgulari $v
Kontrol 'EŞİT DİSKLER: Drive harfi kayıt defteri DefaultMountPoint ile bulunur (etiket boş)' ((Metin $b 'DRIVE_SURUCU') -match 'DefaultMountPoint')
Kontrol 'EŞİT DİSKLER: kanıt D: → D: doğrulanır, C: yalnız not' ((Metin $b 'DRIVE_YANSITMA_DOGRULANDI') -match 'Önbellek konumu kanıtla D:' -and (Metin $b 'DRIVE_YANSITMA_DOGRULANDI') -match 'C: de aynı kapasitede')
$il = Get-OnbellekIliskisi $v ($v.Diskler | Where-Object Harf -eq 'G:')
Kontrol 'EŞİT DİSKLER: ilişki nesnesi Disk=D:, adaylar C: ve D:' ($il.Disk -eq 'D:' -and ($il.Adaylar -join ',') -eq 'C:,D:')

$b = Get-TaniBulgulari (Yukle 'esit-diskler-kanitsiz.json')
Kontrol 'EŞİT DİSKLER kanıtsız: iki aday listelenir, hiçbiri seçilmez' ((Metin $b 'DRIVE_YANSITMA_ADAY') -match 'C:, D:' -and -not ((Kodlar $b) -contains 'DRIVE_YANSITMA_DOGRULANDI'))
Kontrol 'Ölçüm verisi yoksa önbellek bulgusu uydurulmaz' (-not (@(Kodlar $b) -match '^ONBELLEK_').Count)

$b = Get-TaniBulgulari (Yukle 'g-farkli.json')
Kontrol 'Eşleşme ve kanıt yok → BILINMIYOR' ((Kodlar $b) -contains 'DRIVE_ILISKI_BILINMIYOR')
Kontrol 'Klasör yoksa ONBELLEK_YOK' ((Kodlar $b) -contains 'ONBELLEK_YOK')
Kontrol 'Her Drive bulgusunda kota ve bellek-değil uyarısı' (((Kodlar $b) -contains 'DRIVE_KOTA') -and ((Kodlar $b) -contains 'DRIVE_BELLEK_DEGIL'))

$b = Get-TaniBulgulari (Yukle 'drive-yok.json')
Kontrol 'Drive yoksa DRIVE_YOK' ((Kodlar $b) -contains 'DRIVE_YOK')
Kontrol 'Boş RAM %6 → RAM_DUSUK' ((Kodlar $b) -contains 'RAM_DUSUK')
Kontrol '-DriveHarfi "c" (küçük, iki noktasız) kabul edilir' ((Kodlar (Get-TaniBulgulari (Yukle 'drive-yok.json') 'c')) -contains 'DRIVE_SURUCU')
$b = Get-TaniBulgulari (Yukle 'g-c-ayni-kanitli.json')
Kontrol 'RAM 8.0 GB, sanal bellek 12.8 GB' ((Metin $b 'RAM_FIZIKSEL') -match '8[.,]0 GB' -and (Metin $b 'SANAL_BELLEK') -match '12[.,]8 GB')

Write-Output '--- Sınırlı tarama (enjekte listeleyici) ---'
$agac = @{
    '/k'     = @( @{Yol='/k/a';Klasor=$true}, @{Yol='/k/yasak';Klasor=$true}, @{Yol='/k/bag';Klasor=$true;Baglanti=$true}, @{Yol='/k/f1';Boyut=100} )
    '/k/a'   = @( @{Yol='/k/a/f2';Boyut=50}, @{Yol='/k/a/f3';Boyut=25} )
}
$sahte = {
    param($k)
    if ($k -eq '/k/yasak') { throw [System.UnauthorizedAccessException]::new('erişim reddedildi') }
    if ($k -eq '/k/bag') { throw 'bağlantı izlenmemeliydi' }
    @($agac[$k] | ForEach-Object { [pscustomobject]@{ Yol=$_.Yol; Klasor=[bool]$_['Klasor']; Baglanti=[bool]$_['Baglanti']; Boyut=[double]$_['Boyut'] } })
}
$var = { param($p) $true }
$r = Measure-KlasorSinirli -Yol '/k' -Listeleyici $sahte -VarMi $var
Kontrol 'ERİŞİM HATASI: sayılır, Durum=Kismi, neden erisim_hatasi' ($r.ErisimHatasi -eq 1 -and $r.Durum -eq 'Kismi' -and ($r.KesmeNedeni -contains 'erisim_hatasi'))
Kontrol 'ERİŞİM HATASI: örnekte klasör ve istisna türü var' ($r.HataOrnekleri[0] -match '/k/yasak: UnauthorizedAccessException')
Kontrol 'Erişilebilen kısım yine sayılır (175 bayt, 3 dosya)' ($r.ToplamBayt -eq 175 -and $r.DosyaSayisi -eq 3)
Kontrol 'Bağlantı (reparse) izlenmez, atlanan sayılır' ($r.AtlananBaglanti -eq 1)

$r = Measure-KlasorSinirli -Yol '/k' -MaxOge 2 -Listeleyici $sahte -VarMi $var
Kontrol 'SINIRDA KESİLEN: MaxOge=2 → Kismi + oge_siniri, OgeSayisi=2' ($r.Durum -eq 'Kismi' -and ($r.KesmeNedeni -contains 'oge_siniri') -and $r.OgeSayisi -eq 2)
$r = Measure-KlasorSinirli -Yol '/k' -MaxSaniye 0 -Listeleyici $sahte -VarMi $var
Kontrol 'SÜRE SINIRI: MaxSaniye=0 → Kismi + zaman_siniri, hiç öğe sayılmaz' ($r.Durum -eq 'Kismi' -and ($r.KesmeNedeni -contains 'zaman_siniri') -and $r.OgeSayisi -eq 0)
$tamAgac = { param($k) @($agac['/k/a'] | ForEach-Object { [pscustomobject]@{ Yol=$_.Yol; Klasor=$false; Baglanti=$false; Boyut=[double]$_['Boyut'] } }) }
$r = Measure-KlasorSinirli -Yol '/k/a' -MaxOge 2 -Listeleyici $tamAgac -VarMi $var
Kontrol 'Tam olarak MaxOge kadar öğe varsa kesilmez → Tam' ($r.Durum -eq 'Tam' -and $r.ToplamBayt -eq 75)
$r = Measure-KlasorSinirli -Yol '/yok' -VarMi { param($p) $false }
Kontrol 'Olmayan klasör → Durum=Yok' ($r.Durum -eq 'Yok')

Write-Output '--- Gerçek dosya sistemi ---'
$tmp = Join-Path ([System.IO.Path]::GetTempPath()) ("tani-" + [guid]::NewGuid())
New-Item -ItemType Directory -Path (Join-Path $tmp 'alt') | Out-Null
1..30 | ForEach-Object { [System.IO.File]::WriteAllBytes((Join-Path $tmp "alt/d$_.bin"), [byte[]]::new(1000)) }
$r = Measure-KlasorSinirli -Yol $tmp
Kontrol 'Gerçek klasör: 30 dosya, 30000 bayt, Tam' ($r.Durum -eq 'Tam' -and $r.DosyaSayisi -eq 30 -and $r.ToplamBayt -eq 30000)
$r = Measure-KlasorSinirli -Yol $tmp -MaxOge 10
Kontrol 'Gerçek klasör sınırda kesilir: MaxOge=10 → Kismi, alt sınır < 30000' ($r.Durum -eq 'Kismi' -and $r.ToplamBayt -lt 30000)
if ($env:TANI_YASAK_KLASOR) {
    $r = Measure-KlasorSinirli -Yol $env:TANI_YASAK_KLASOR
    Kontrol 'Gerçek erişim hatası (root olmayan kullanıcı, chmod 000) → Kismi + erisim_hatasi' ($r.Durum -eq 'Kismi' -and $r.ErisimHatasi -ge 1)
}

Write-Output '--- Çıktı dosyası güvenliği ---'
$json = Join-Path $tmp 'cikti.json'
& $betik -GirdiJson (Join-Path $PSScriptRoot 'ornekler/g-farkli.json') -CiktiJson $json | Out-Null
Kontrol '-CiktiJson yeni dosya oluşturur ve geçerli JSON' ((Test-Path $json) -and ((Get-Content -Raw $json | ConvertFrom-Json).Bulgular.Count -gt 0))
$once = (Get-Item $json).LastWriteTimeUtc; $reddetti = $false
try { & $betik -GirdiJson (Join-Path $PSScriptRoot 'ornekler/g-farkli.json') -CiktiJson $json | Out-Null } catch { $reddetti = $_.Exception.Message -match 'zaten var' }
Kontrol 'Var olan dosyayı -UzerineYaz olmadan EZMEZ' ($reddetti -and (Get-Item $json).LastWriteTimeUtc -eq $once)
& $betik -GirdiJson (Join-Path $PSScriptRoot 'ornekler/g-farkli.json') -CiktiJson $json -UzerineYaz | Out-Null
Kontrol '-UzerineYaz ile yazar' ((Get-Item $json).LastWriteTimeUtc -ge $once)
Remove-Item -Recurse -Force $tmp

if ($script:hata) { Write-Output "SONUÇ: $($script:hata)/$($script:sayi) test KALDI"; exit 1 } else { Write-Output "SONUÇ: $($script:sayi)/$($script:sayi) test GEÇTİ" }
