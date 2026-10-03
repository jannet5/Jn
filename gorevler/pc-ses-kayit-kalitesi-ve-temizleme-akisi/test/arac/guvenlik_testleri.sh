#!/bin/bash
# Ses-Temizle.ps1 ve Kurulum.ps1 için dosya güvenliği / hata yolu / tekrar kullanım testleri.
# ORTAM: Linux + PowerShell 7 (pwsh) + ffmpeg. Bu, Windows PowerShell 5.1 veya gerçek Windows testi DEĞİLDİR.
# Kullanım: DF_LINUX=/yol/deep-filter (Linux ikilisi, v0.5.6) ./guvenlik_testleri.sh
# Kurulum testleri deep-filter.exe'yi GitHub'dan gerçekten indirir (ağ gerekir).
set -u
KOK="$(cd "$(dirname "$0")/../../ses-temizleyici" && pwd)"
MAKRO="$(cd "$(dirname "$0")/../../audacity-makro" && pwd)/Ses-Temizle.txt"
: "${DF_LINUX:?DF_LINUX ortam değişkeni Linux deep-filter ikilisini göstermeli}"
IS="$(mktemp -d /tmp/sestest.XXXXXX)"
export TMPDIR="$IS/tmp"; mkdir -p "$TMPDIR"
GECTI=0; KALDI=0
ok()   { echo "  GEÇTİ: $1"; GECTI=$((GECTI+1)); }
fail() { echo "  KALDI: $1"; KALDI=$((KALDI+1)); }
esit() { if [ "$1" == "$2" ]; then ok "$3"; else fail "$3 (beklenen '$2', gelen '$1')"; fi; }
sha()  { sha256sum "$1" | cut -d' ' -f1; }
guid_kalan() { find "$TMPDIR" -maxdepth 1 -name 'sestemizle_*' | wc -l; }

# Ürün klasörünün kopyası (gerçek klasör kirlenmesin)
kur_kopya() {
  rm -rf "$IS/urun"; mkdir -p "$IS/urun/ses-temizleyici/araclar" "$IS/urun/audacity-makro"
  cp "$KOK"/*.ps1 "$KOK"/*.bat "$IS/urun/ses-temizleyici/"; cp "$MAKRO" "$IS/urun/audacity-makro/"
}
kur_kopya
cp "$DF_LINUX" "$IS/urun/ses-temizleyici/araclar/deep-filter"
BETIK="$IS/urun/ses-temizleyici/Ses-Temizle.ps1"
calistir() { pwsh -NoProfile -File "$BETIK" "$@" > "$IS/son.log" 2>&1; echo $?; }

# Test girdisi: 8 sn konuşma benzeri ton + pembe gürültü, boşluklu ve Türkçe karakterli klasörde
D="$IS/Kayıtlar ğüşİ"; mkdir -p "$D"
ffmpeg -loglevel error -n -f lavfi -i "sine=f=200:d=8,volume=0.3,tremolo=f=4:d=0.9" -f lavfi -i "anoisesrc=d=8:c=pink:a=0.03" \
  -filter_complex "[0][1]amix=inputs=2:normalize=0" -ar 44100 -ac 1 "$D/ses 1.wav"
GIRIS_SHA=$(sha "$D/ses 1.wav")

echo "== T1 Boşluklu/Türkçe yol, ilk çalıştırma"
esit "$(calistir "$D/ses 1.wav")" 0 "çıkış kodu 0"
for f in "ses 1_temiz.wav" "ses 1_onceSonra.wav" "ses 1_rapor.txt"; do [ -s "$D/$f" ] && ok "oluştu: $f" || fail "yok: $f"; done
esit "$(sha "$D/ses 1.wav")" "$GIRIS_SHA" "giriş dosyası değişmedi"
esit "$(guid_kalan)" 0 "geçici GUID klasörü kalmadı"
T1_SHA=$(sha "$D/ses 1_temiz.wav"); R1_SHA=$(sha "$D/ses 1_rapor.txt")

echo "== T2 Tekrar kullanım: eski çıktılar korunur, yeni ad (2)"
esit "$(calistir "$D/ses 1.wav")" 0 "çıkış kodu 0"
esit "$(sha "$D/ses 1_temiz.wav")" "$T1_SHA" "ilk _temiz.wav değişmedi"
esit "$(sha "$D/ses 1_rapor.txt")" "$R1_SHA" "ilk _rapor.txt değişmedi"
for f in "ses 1_temiz (2).wav" "ses 1_onceSonra (2).wav" "ses 1_rapor (2).txt"; do [ -s "$D/$f" ] && ok "oluştu: $f" || fail "yok: $f"; done
grep -q "ses 1_onceSonra (2).wav" "$D/ses 1_rapor (2).txt" && ok "rapor yeni önce/sonra adını gösteriyor" || fail "rapor adı yanlış"

echo "== T3 Kullanıcının kendi dosyası (aynı adlı) korunur"
D3="$IS/t3"; mkdir -p "$D3"; cp "$D/ses 1.wav" "$D3/kayit.wav"
echo "KULLANICININ ÖNEMLİ DOSYASI" > "$D3/kayit_onceSonra.wav"; U_SHA=$(sha "$D3/kayit_onceSonra.wav")
esit "$(calistir "$D3/kayit.wav")" 0 "çıkış kodu 0"
esit "$(sha "$D3/kayit_onceSonra.wav")" "$U_SHA" "kullanıcının kayit_onceSonra.wav dosyası değişmedi"
[ ! -e "$D3/kayit_temiz.wav" ] && [ -s "$D3/kayit_temiz (2).wav" ] && ok "çıktılar ortak numarayla (2) yazıldı" || fail "numaralandırma yanlış"

echo "== T4 -UzerineYaz yalnız açıkça istenince üzerine yazar"
echo "ESKİ" > "$D3/kayit_temiz.wav"; echo "ESKİ" > "$D3/kayit_onceSonra.wav"; echo "ESKİ" > "$D3/kayit_rapor.txt"
esit "$(calistir -UzerineYaz "$D3/kayit.wav")" 0 "çıkış kodu 0"
[ "$(head -c 4 "$D3/kayit_temiz.wav")" == "RIFF" ] && ok "_temiz.wav yeni WAV ile değişti" || fail "_temiz.wav değişmedi"

echo "== T5 Bozuk girdi: çıktı yok, geçici klasör temiz, kod 3"
D5="$IS/t5"; mkdir -p "$D5"; echo "bu bir ses dosyası değil" > "$D5/bozuk.wav"
esit "$(calistir "$D5/bozuk.wav")" 3 "çıkış kodu 3"
esit "$(ls "$D5" | wc -l)" 1 "klasörde yalnız giriş dosyası var"
esit "$(guid_kalan)" 0 "geçici GUID klasörü kalmadı"
grep -q "HATA" "$IS/son.log" && ok "Türkçe hata mesajı yazıldı" || fail "hata mesajı yok"

echo "== T6 Olmayan dosya + iyi dosya: iyi olan işlenir, kod 3"
D6="$IS/t6"; mkdir -p "$D6"; cp "$D/ses 1.wav" "$D6/iyi.wav"
esit "$(calistir "$D6/yok.wav" "$D6/iyi.wav")" 3 "çıkış kodu 3"
[ -s "$D6/iyi_temiz.wav" ] && ok "iyi dosya yine de işlendi" || fail "iyi dosya işlenmedi"

echo "== T7 Ara adım başarısız (deep-filter hata verir / çıktı üretmez)"
GERCEK_DF="$IS/urun/ses-temizleyici/araclar/deep-filter"; mv "$GERCEK_DF" "$IS/df.yedek"
printf '#!/bin/sh\necho "model yuklenemedi" >&2\nexit 1\n' > "$GERCEK_DF"; chmod +x "$GERCEK_DF"
D7="$IS/t7"; mkdir -p "$D7"; cp "$D/ses 1.wav" "$D7/a.wav"
esit "$(calistir "$D7/a.wav")" 3 "deep-filter kod 1 -> çıkış kodu 3"
esit "$(ls "$D7" | wc -l)" 1 "yarım çıktı bırakılmadı"
esit "$(guid_kalan)" 0 "geçici GUID klasörü kalmadı (finally)"
printf '#!/bin/sh\nexit 0\n' > "$GERCEK_DF"
esit "$(calistir "$D7/a.wav")" 3 "deep-filter dosya üretmedi -> çıkış kodu 3"
grep -q "çıktı dosyası üretmedi" "$IS/son.log" && ok "anlamlı hata mesajı" || fail "mesaj yok"
esit "$(ls "$D7" | wc -l)" 1 "yarım çıktı bırakılmadı"
mv "$IS/df.yedek" "$GERCEK_DF"

echo "== T8 -Kontrol hiçbir dosya yazmaz"
D8="$IS/t8"; mkdir -p "$D8"; cp "$D/ses 1.wav" "$D8/k.wav"
esit "$(calistir -Kontrol "$D8/k.wav")" 0 "çıkış kodu 0"
esit "$(ls "$D8" | wc -l)" 1 "klasöre dosya yazılmadı"

echo "== T9 Argümansız -> 1; ffmpeg yok -> 2"
esit "$(calistir)" 1 "argümansız çıkış kodu 1"
YOL="$IS/yalnizpwsh"; mkdir -p "$YOL"; ln -sf "$(command -v pwsh)" "$YOL/pwsh"
esit "$(PATH="$YOL" "$YOL/pwsh" -NoProfile -File "$BETIK" "$D8/k.wav" >/dev/null 2>&1; echo $?)" 2 "ffmpeg yokken çıkış kodu 2"

echo "== T10 Sessiz kayıt: anlaşılır hata, çıktı yok; -Kontrol 'SESSİZ' der"
D10="$IS/t10"; mkdir -p "$D10"; ffmpeg -loglevel error -n -f lavfi -i anullsrc=r=48000:cl=mono -t 5 "$D10/sessiz.wav"
esit "$(calistir "$D10/sessiz.wav")" 3 "sessiz kayıt -> çıkış kodu 3"
grep -q "tamamen sessiz" "$IS/son.log" && ok "anlaşılır mesaj" || fail "mesaj yok"
esit "$(ls "$D10" | wc -l)" 1 "çıktı yazılmadı"
esit "$(calistir -Kontrol "$D10/sessiz.wav")" 0 "-Kontrol çıkış kodu 0"
grep -q "Kayıt SESSİZ" "$IS/son.log" && ok "-Kontrol sessizliği bildirdi" || fail "mesaj yok"

# ------------------------------------------------------------------------------------------------
KURULUM="$IS/urun/ses-temizleyici/Kurulum.ps1"
export LOCALAPPDATA="$IS/localappdata" APPDATA="$IS/appdata"; mkdir -p "$LOCALAPPDATA" "$APPDATA/audacity/Macros"
FF="$(command -v ffmpeg)"
yol_ffmpegsiz() { rm -rf "$IS/yol"; mkdir -p "$IS/yol"; ln -sf "$(command -v pwsh)" "$IS/yol/pwsh"; }
kurulum() { PATH="$IS/yol:/usr/bin:/bin" "$IS/yol/pwsh" -NoProfile -File "$KURULUM" > "$IS/kur.log" 2>&1; echo $?; }
# /usr/bin'de ffmpeg olduğundan, ffmpeg'siz senaryolar için ayrı bir PATH kullanılır:
kurulum_ffmpegsiz() { PATH="$IS/yol" "$IS/yol/pwsh" -NoProfile -File "$KURULUM" > "$IS/kur.log" 2>&1; echo $?; }
sahte_winget() { printf '#!/bin/sh\n%s\nexit %s\n' "$2" "$1" > "$IS/yol/winget"; chmod +x "$IS/yol/winget"; }

echo "== K1 Bozuk deep-filter.exe kenara alınır, GitHub'dan indirilip SHA-256 doğrulanır"
yol_ffmpegsiz; echo "bozuk" > "$IS/urun/ses-temizleyici/araclar/deep-filter.exe"
esit "$(kurulum)" 0 "kurulum çıkış kodu 0 (ffmpeg PATH'te)"
esit "$(sha "$IS/urun/ses-temizleyici/araclar/deep-filter.exe")" 75e11fa16445f560cb6b021521ddb89e89270d13b83089705d98776f58fd7915 "deep-filter.exe SHA-256 doğru"
[ "$(ls "$IS/urun/ses-temizleyici/araclar/" | grep -c 'deep-filter.exe.bozuk-')" -ge 1 ] && ok "bozuk dosya silinmedi, kenara alındı" || fail "bozuk dosya kayıp"
grep -q "Çalışma testi geçti" "$IS/kur.log" && ok "çalışma testi geçti" || fail "çalışma testi mesajı yok"
esit "$(guid_kalan)" 0 "kurulum testi GUID klasörü kalmadı"

echo "== K2 Audacity makrosu: kullanıcının değiştirdiği makro korunur"
# K1 makroyu kopyaladı; şimdi kullanıcı onu değiştirmiş olsun
echo "KULLANICI MAKROSU" > "$APPDATA/audacity/Macros/Ses-Temizle.txt"; M_SHA=$(sha "$APPDATA/audacity/Macros/Ses-Temizle.txt")
esit "$(kurulum)" 0 "kurulum çıkış kodu 0"
esit "$(sha "$APPDATA/audacity/Macros/Ses-Temizle.txt")" "$M_SHA" "kullanıcı makrosu değişmedi"
esit "$(sha "$APPDATA/audacity/Macros/Ses-Temizle (2).txt")" "$(sha "$MAKRO")" "yeni makro ayrı adla eklendi"
esit "$(kurulum)" 0 "tekrar kurulum çıkış kodu 0"
[ ! -e "$APPDATA/audacity/Macros/Ses-Temizle (3).txt" ] && ok "tekrar çalıştırmada kopya çoğalmadı" || fail "gereksiz (3) oluştu"
grep -q "zaten güncel" "$IS/kur.log" && ok "'zaten güncel' bildirildi" || fail "mesaj yok"

echo "== K3 winget senaryoları (ffmpeg yokken kurulum başarı SAYILMAMALI)"
yol_ffmpegsiz
esit "$(kurulum_ffmpegsiz)" 3 "winget yok -> kod 3"; grep -q "winget komutu bulunamadı" "$IS/kur.log" && ok "mesaj: winget bulunamadı" || fail "mesaj yok"
# Linux'ta süreç çıkış kodu 8 bittir; winget'in 32 bitlik HRESULT kodları sahte süreçle taşınamaz.
# Bu yüzden sınıflandırma, Kurulum.ps1 içindeki Winget-Sonuc fonksiyonunun KENDİSİ (AST'den alınıp) ile test edilir:
SINIF=$(KURULUM_YOL="$KURULUM" pwsh -NoProfile -c '
  $t=$null;$e=$null; $ast=[System.Management.Automation.Language.Parser]::ParseFile($env:KURULUM_YOL,[ref]$t,[ref]$e)
  $f=$ast.Find({ param($n) $n -is [System.Management.Automation.Language.FunctionDefinitionAst] -and $n.Name -eq "Winget-Sonuc" }, $true)
  Invoke-Expression $f.Extent.Text
  foreach($k in 0,-1978335135,-1978335212,-1978335224,-1978334969,-2147012889,-1978335207,-1978335174,-2147024891,-1978335167,-1978335227,12345){ "$k=" + (Winget-Sonuc $k)[0] }' | tr '\n' ' ')
for beklenen in "0=basari" "-1978335135=zaten" "-1978335212=bulunamadi" "-1978335224=ag" "-1978334969=ag" "-2147012889=ag" \
                "-1978335207=izin" "-1978335174=izin" "-2147024891=izin" "-1978335167=sozlesme" "-1978335227=iptal" "12345=bilinmeyen"; do
  case " $SINIF " in *" $beklenen "*) ok "winget kodu sınıfı $beklenen";; *) fail "winget kodu sınıfı $beklenen (gelen: $SINIF)";; esac
done
sahte_winget 5 'echo bilinmeyen hata'; esit "$(kurulum_ffmpegsiz)" 3 "winget bilinmeyen kod 5 -> kod 3"; grep -q "winget hata kodu 5" "$IS/kur.log" && ok "mesaj: bilinmeyen kod" || fail "mesaj yok"
mkdir -p "$LOCALAPPDATA/Microsoft/WinGet/Links"
sahte_winget 0 "/bin/ln -sf $FF '$LOCALAPPDATA/Microsoft/WinGet/Links/ffmpeg'"
esit "$(kurulum_ffmpegsiz)" 0 "winget gerçekten kurunca (Links/ffmpeg) -> kod 0"
rm -f "$LOCALAPPDATA/Microsoft/WinGet/Links/ffmpeg"
printf '#!/bin/sh\nexit 1\n' > "$IS/yol/ffmpeg"; chmod +x "$IS/yol/ffmpeg"; sahte_winget -1978335135 ''
esit "$(kurulum_ffmpegsiz)" 3 "çalışmayan ffmpeg + 'zaten kurulu' -> kod 3"
rm -f "$IS/yol/ffmpeg"

echo "== K4 Çalışma testi başarısızsa kurulum başarısız (kod 4) ve geçici klasör temiz"
mv "$GERCEK_DF" "$IS/df.yedek"; printf '#!/bin/sh\nexit 1\n' > "$GERCEK_DF"; chmod +x "$GERCEK_DF"
yol_ffmpegsiz; esit "$(kurulum)" 4 "kurulum çıkış kodu 4"
esit "$(guid_kalan)" 0 "kurulum testi GUID klasörü kalmadı"
mv "$IS/df.yedek" "$GERCEK_DF"

echo
echo "SONUÇ: $GECTI geçti, $KALDI kaldı"
rm -rf "$IS"
[ "$KALDI" -eq 0 ]
