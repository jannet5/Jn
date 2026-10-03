# Analiz-Et.ps1 ve gömülü C# kodu için platformdan bağımsız birim testleri.
# Not: Bu testler SENTETİK CSV kullanır; gerçek Windows ölçümü yerine geçmez.
$ErrorActionPreference = 'Stop'
$kok = Split-Path $PSScriptRoot -Parent
. (Join-Path $kok 'Analiz-Et.ps1') -Csv 'yok'   # yalnız fonksiyonu yükle (dot-source)
$hata = 0
function Assert($kosul, $mesaj) { if ($kosul) { Write-Host "GEÇTİ  $mesaj" } else { Write-Host "KALDI  $mesaj"; $script:hata++ } }

function Yeni-Satir($t, $surec, $baslik, $fh, $eh, $ft, $et, $fk, $ek) {
    [pscustomobject]@{ zaman = ([datetime]'2026-10-03T12:00:00Z').ToUniversalTime().AddSeconds($t).ToString('o'); x=0; y=0;
        onplan_surec=$surec; onplan_baslik=$baslik; fiziksel_hareket=$fh; enjekte_hareket=$eh; fiziksel_tik=$ft; enjekte_tik=$et; fiziksel_tus=$fk; enjekte_tus=$ek }
}

# 1) Ajan yok: yalnız kullanıcı yazıyor
$s1 = @(
  (Yeni-Satir 0 'notepad' 'Not' 0 0 0 0 0 0),
  (Yeni-Satir 1 'notepad' 'Not' 10 0 1 0 5 0),
  (Yeni-Satir 2 'notepad' 'Not' 20 0 1 0 12 0))
$o1 = Get-GirdiOzeti -Satirlar $s1
Assert ($o1.karar -eq 'SENTETIK_GIRDI_GORULMEDI') 'Senaryo 1: ajan girdisi yokken karar SENTETIK_GIRDI_GORULMEDI'
Assert ($o1.cakisma_araligi -eq 0) 'Senaryo 1: çakışma 0'
Assert ($o1.toplamlar.fiziksel_tus -eq 12) 'Senaryo 1: fiziksel tuş toplamı 12'

# 2) Ajan hesap makinesini öne getiriyor, kullanıcı aynı anda yazıyor
$s2 = @(
  (Yeni-Satir 0 'notepad' 'Not' 0 0 0 0 0 0),
  (Yeni-Satir 1 'notepad' 'Not' 5 0 0 0 4 0),
  (Yeni-Satir 2 'CalculatorApp' 'Hesap Makinesi' 5 40 0 2 4 0),   # ajan tıkladı, kullanıcı tık/tuş yapmadı -> şüpheli
  (Yeni-Satir 3 'CalculatorApp' 'Hesap Makinesi' 9 60 0 3 9 0),   # ikisi aynı anda -> çakışma
  (Yeni-Satir 4 'notepad' 'Not' 15 60 1 3 9 0))                    # kullanıcı geri tıkladı -> şüpheli değil
$o2 = Get-GirdiOzeti -Satirlar $s2
Assert ($o2.karar -eq 'AJAN_GERCEK_GIRDIYI_KULLANDI') 'Senaryo 2: karar AJAN_GERCEK_GIRDIYI_KULLANDI'
Assert ($o2.onplan_degisimi -eq 2) 'Senaryo 2: 2 ön plan değişimi'
Assert ($o2.supheli_ajan_onplan -eq 1) 'Senaryo 2: yalnız 1 değişim ajana atfedildi'
Assert ($o2.cakisma_araligi -eq 1) 'Senaryo 2: 1 çakışma aralığı'
Assert ($o2.toplamlar.enjekte_tik -eq 3) 'Senaryo 2: enjekte tık toplamı 3'
Assert ([math]::Abs(($o2.onplan_segmentleri | Measure-Object saniye -Sum).Sum - 4) -lt 0.01) 'Senaryo 2: segment süreleri toplamı 4 sn'

# 3) Hatalı girdi
try { Get-GirdiOzeti -Satirlar @($s1[0]) | Out-Null; Assert $false 'Senaryo 3: tek satırda hata beklenir' } catch { Assert $true 'Senaryo 3: tek satırda anlamlı hata' }

# 4) CSV gidiş-dönüş (Import-Csv ile string alanlar)
$tmp = Join-Path ([IO.Path]::GetTempPath()) ("analiz-test-{0}.csv" -f [guid]::NewGuid())
$s2 | Export-Csv -Path $tmp -NoTypeInformation -Encoding UTF8
$o4 = Get-GirdiOzeti -Satirlar (Import-Csv $tmp -Encoding UTF8)
Remove-Item $tmp
Assert ($o4.supheli_ajan_onplan -eq 1 -and $o4.cakisma_araligi -eq 1) 'Senaryo 4: CSV dosyasından okununca aynı sonuç'

# 5) Gömülü C# kodları derleniyor mu? (Linux'ta derlenir; user32 çağrısı yapılmaz)
foreach ($b in 'Izle-GirdiGaspi.ps1','UIA-ArkaPlan-Deneyi.ps1') {
    $metin = Get-Content (Join-Path $kok $b) -Raw
    $m = [regex]::Match($metin, "(?s)-TypeDefinition @'\r?\n(.*?)\r?\n'@")
    try { Add-Type -TypeDefinition $m.Groups[1].Value -ErrorAction Stop; Assert $true "Senaryo 5: $b içindeki C# derlendi" }
    catch { Assert $false "Senaryo 5: $b C# derleme hatası: $($_.Exception.Message)" }
}

Write-Host ''
if ($hata) { Write-Host "SONUÇ: $hata test KALDI"; exit 1 } else { Write-Host 'SONUÇ: tüm testler GEÇTİ'; exit 0 }
