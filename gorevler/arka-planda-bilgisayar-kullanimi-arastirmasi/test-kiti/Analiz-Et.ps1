<#
.SYNOPSIS
  Izle-GirdiGaspi.ps1 çıktısını (CSV) özetler: ajan sentetik girdi kullandı mı,
  sizinle aynı anda mı girdi verdi, ön plan penceresini değiştirdi mi?
  Her platformda (Windows PowerShell 5.1 / PowerShell 7) çalışır.
.EXAMPLE
  .\Analiz-Et.ps1 -Csv .\girdi-izleme-20261003-120000.csv -Json .\ozet.json
#>
param(
    [Parameter(Mandatory)][string]$Csv,
    [string]$Json
)

function Get-GirdiOzeti {
    param([object[]]$Satirlar)
    $inv = [Globalization.CultureInfo]::InvariantCulture
    $alanlar = 'fiziksel_hareket','enjekte_hareket','fiziksel_tik','enjekte_tik','fiziksel_tus','enjekte_tus'
    $n = $Satirlar.Count
    if ($n -lt 2) { throw 'Analiz için en az 2 örnek satırı gerekir.' }

    $cakisma = 0; $onPlanDegisim = 0; $supheliAjanOnPlan = 0; $ajanAktifAralik = 0
    $olaylar = New-Object System.Collections.Generic.List[object]
    $segmentler = New-Object System.Collections.Generic.List[object]
    $segBas = [datetime]::Parse($Satirlar[0].zaman, $inv, [Globalization.DateTimeStyles]::RoundtripKind)
    $segAd = "$($Satirlar[0].onplan_surec) | $($Satirlar[0].onplan_baslik)"
    $oncekiAjan = $false

    for ($i = 1; $i -lt $n; $i++) {
        $a = $Satirlar[$i-1]; $b = $Satirlar[$i]
        $d = @{}
        foreach ($f in $alanlar) { $d[$f] = [long]$b.$f - [long]$a.$f }
        $enj = $d.enjekte_hareket + $d.enjekte_tik + $d.enjekte_tus
        $fiz = $d.fiziksel_hareket + $d.fiziksel_tik + $d.fiziksel_tus
        $fizKomut = $d.fiziksel_tik + $d.fiziksel_tus
        $zaman = [datetime]::Parse($b.zaman, $inv, [Globalization.DateTimeStyles]::RoundtripKind)
        if ($enj -gt 0) { $ajanAktifAralik++ }
        if ($enj -gt 0 -and $fiz -gt 0) { $cakisma++ }

        $ad = "$($b.onplan_surec) | $($b.onplan_baslik)"
        if ($ad -ne "$($a.onplan_surec) | $($a.onplan_baslik)") {
            $onPlanDegisim++
            # Bu aralıkta (ya da hemen öncesinde) sentetik girdi varsa ve kullanıcı
            # fiziksel tık/tuş yapmadıysa, değişimi ajanın yapmış olması muhtemeldir.
            $supheli = (($enj -gt 0) -or $oncekiAjan) -and ($fizKomut -eq 0)
            if ($supheli) { $supheliAjanOnPlan++ }
            $olaylar.Add([pscustomobject]@{ zaman = $b.zaman; yeni = $ad; ajan_suphesi = $supheli })
            $segmentler.Add([pscustomobject]@{ pencere = $segAd; saniye = [math]::Round(($zaman - $segBas).TotalSeconds, 2) })
            $segBas = $zaman; $segAd = $ad
        }
        $oncekiAjan = ($enj -gt 0)
    }
    $son = [datetime]::Parse($Satirlar[$n-1].zaman, $inv, [Globalization.DateTimeStyles]::RoundtripKind)
    $ilk = [datetime]::Parse($Satirlar[0].zaman, $inv, [Globalization.DateTimeStyles]::RoundtripKind)
    $segmentler.Add([pscustomobject]@{ pencere = $segAd; saniye = [math]::Round(($son - $segBas).TotalSeconds, 2) })

    $top = @{}
    foreach ($f in $alanlar) { $top[$f] = [long]$Satirlar[$n-1].$f - [long]$Satirlar[0].$f }
    $enjToplam = $top.enjekte_hareket + $top.enjekte_tik + $top.enjekte_tus

    if ($enjToplam -gt 0) {
        $karar = 'AJAN_GERCEK_GIRDIYI_KULLANDI'
        $aciklama = 'Bu ölçümde ajan sentetik (enjekte) fare/klavye olayı üretti: sizinle aynı fare imlecini ve klavye kuyruğunu paylaşıyor. Aynı anda kullanım çakışma yaratır.'
    } else {
        $karar = 'SENTETIK_GIRDI_GORULMEDI'
        $aciklama = 'Bu ölçümde sentetik fare/klavye olayı görülmedi. Ya ajan çalışmadı ya da girdi enjekte etmeyen bir yöntem (UI Automation, API, ayrı oturum/VM) kullandı.'
    }

    [pscustomobject]@{
        sure_saniye              = [math]::Round(($son - $ilk).TotalSeconds, 2)
        ornek_sayisi             = $n
        toplamlar                = [pscustomobject]$top
        ajan_aktif_aralik        = $ajanAktifAralik
        cakisma_araligi          = $cakisma
        onplan_degisimi          = $onPlanDegisim
        supheli_ajan_onplan      = $supheliAjanOnPlan
        onplan_olaylari          = $olaylar.ToArray()
        onplan_segmentleri       = $segmentler.ToArray()
        karar                    = $karar
        aciklama                 = $aciklama
    }
}

if ($MyInvocation.InvocationName -ne '.') {
    $veri = Import-Csv -Path $Csv -Encoding UTF8
    $ozet = Get-GirdiOzeti -Satirlar $veri
    Write-Host ''
    Write-Host '=== Girdi gaspı özeti ==='
    Write-Host ("Süre: {0} sn, örnek: {1}" -f $ozet.sure_saniye, $ozet.ornek_sayisi)
    Write-Host ("Enjekte (ajan) olaylar  -> hareket {0}, tık {1}, tuş {2}" -f $ozet.toplamlar.enjekte_hareket, $ozet.toplamlar.enjekte_tik, $ozet.toplamlar.enjekte_tus)
    Write-Host ("Fiziksel (siz) olaylar  -> hareket {0}, tık {1}, tuş {2}" -f $ozet.toplamlar.fiziksel_hareket, $ozet.toplamlar.fiziksel_tik, $ozet.toplamlar.fiziksel_tus)
    Write-Host ("Aynı anda girdi (çakışma) aralığı: {0}" -f $ozet.cakisma_araligi)
    Write-Host ("Ön plan değişimi: {0} (ajan şüphesi: {1})" -f $ozet.onplan_degisimi, $ozet.supheli_ajan_onplan)
    Write-Host ("KARAR: {0}" -f $ozet.karar)
    Write-Host $ozet.aciklama
    if ($Json) { $ozet | ConvertTo-Json -Depth 5 | Set-Content -Path $Json -Encoding UTF8; Write-Host "JSON: $Json" }
}
