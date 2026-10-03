# Basit kabul testleri: ram-drive-tani.ps1 mantığını örnek JSON'larla sınar.
$ErrorActionPreference = 'Stop'
$kok = Split-Path -Parent $PSScriptRoot
. (Join-Path $kok 'ram-drive-tani.ps1')
$hata = 0
function Kontrol([string]$Ad, [bool]$Kosul) {
    if ($Kosul) { Write-Output "GEÇTİ  $Ad" } else { Write-Output "KALDI  $Ad"; $script:hata++ }
}
function Yukle($ad) { Get-Content -Raw -Encoding UTF8 (Join-Path $PSScriptRoot "ornekler/$ad") | ConvertFrom-Json }
function Kodlar($b) { @($b | ForEach-Object Kod) }

$b1 = Get-TaniBulgulari (Yukle 'g-c-ayni.json')
Kontrol 'G=C kapasite eşleşmesi yansıtma olarak raporlanır' ((Kodlar $b1) -contains 'DRIVE_YANSITMA')
Kontrol 'Yansıtma metni C: diskini adlandırır' ((@($b1 | Where-Object Kod -eq 'DRIVE_YANSITMA')[0].Metin) -match 'C:')
Kontrol 'Drive bellek değil uyarısı var' ((Kodlar $b1) -contains 'DRIVE_BELLEK_DEGIL')
Kontrol 'Önbellek boyutu raporlanır' ((Kodlar $b1) -contains 'DRIVE_ONBELLEK')
Kontrol 'Fiziksel RAM 8.0 GB okunur' ((@($b1 | Where-Object Kod -eq 'RAM_FIZIKSEL')[0].Metin) -match '8[.,]0 GB')
Kontrol 'Sanal bellek = 8 GB + 4864 MB = 12.8 GB' ((@($b1 | Where-Object Kod -eq 'SANAL_BELLEK')[0].Metin) -match '12[.,]8 GB')

$b2 = Get-TaniBulgulari (Yukle 'g-farkli.json')
Kontrol 'Farklı boyutta yansıtma iddia edilmez' (-not ((Kodlar $b2) -contains 'DRIVE_YANSITMA'))
Kontrol 'Farklı boyut DRIVE_FARKLI verir' ((Kodlar $b2) -contains 'DRIVE_FARKLI')
Kontrol 'Önbellek bilinmiyorsa uydurulmaz' (-not ((Kodlar $b2) -contains 'DRIVE_ONBELLEK'))
Kontrol 'Bol RAM varken düşük RAM uyarısı yok' (-not ((Kodlar $b2) -contains 'RAM_DUSUK'))

$b3 = Get-TaniBulgulari (Yukle 'drive-yok.json')
Kontrol 'Drive yoksa DRIVE_YOK' ((Kodlar $b3) -contains 'DRIVE_YOK')
Kontrol 'Boş RAM %6 iken RAM_DUSUK uyarısı' ((Kodlar $b3) -contains 'RAM_DUSUK')

$b4 = Get-TaniBulgulari (Yukle 'drive-yok.json') 'c'
Kontrol '-DriveHarfi "c" küçük harf/iki noktasız kabul edilir' (-not ((Kodlar $b4) -contains 'DRIVE_YOK'))

if ($hata) { Write-Output "SONUÇ: $hata test KALDI"; exit 1 } else { Write-Output 'SONUÇ: tüm testler GEÇTİ' }
