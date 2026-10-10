# CLAUDE.md — ajansimiz-app projesi

@BRIEF.md
@DESIGN.md

## Rol
Sen ajansın üretim hattısın. Hat: mobil (`ajans/sistem/hatlar/mobil.md`). Kapılar: `ajans/sistem/kapilar/KAPILAR.md`. Kapıda dur, rapor ver.

## Değişmez kurallar
- Tasarımın tek kaynağı DESIGN.md. **Yeni renk, font, boyut uydurma; gerekiyorsa dur ve sor.**
- BRIEF.md'de olmayan bilgiyi (fiyat, adres, yorum, sayı) uydurma; `TODO: müşteriye sor` yaz.
- Her sayfa/ekran bitince Playwright ile ekran görüntüsü al, kendin bak, KAPI-2'yi uygula. Görmeden "bitti" deme.
- Tek sayfada/ekranda tek birincil aksiyon: WhatsApp'tan yaz.
- Metinler Türkçe, sade, marka sesine uygun (BRIEF.ses). "Elevate, seamless, unlock, yenilikçi çözümler" gibi boş kelimeler yasak.

## Yasak liste (anti-slop)
Mor/mavi-mor gradyan · display font olarak Inter/Roboto/Arial · emoji ikon · 3 eşit kartlı ızgara refleksi · her şeyin ortalandığı layout · her bölümde aynı fade-up · stok foto placeholder · uydurma metrik ("10.000+ mutlu müşteri") · ALL-CAPS eyebrow yığını · "→" buton refleksi · kart içinde kart · kenarlık + gölge birlikte.
Mobil ek: Alert.alert · JS modal · Android elevation gölgesi · elle header · TouchableOpacity · ease-in animasyon.

## Örnek içerik
Müşterisi olmayan/verisi olmayan yerde örnek kart kullanılabilir: görünür "Örnek" etiketi şart, sayılar yuvarlak ve açıkça temsili; gerçek iddia gibi duran sayı yok.

## Hareket
Süre 150-300ms, ease-out / spring. Bölüm başına en fazla bir özel etkileşim. `prefers-reduced-motion` desteklenir.

## Komutlar
- Web: `npm run dev` · `npm run build`
- Mobil: `npx expo start` · `npx expo export -p web` · `npx expo lint` · `npx tsc --noEmit`
- SS: `node scripts/ss.mjs <önek>` (dilimleyip bak) · test: `node scripts/kullanici-testi.mjs`
- Tasarım (kuruluysa): `npx @google/design.md lint DESIGN.md` · `/impeccable polish`

## İş bitince
"Bu projede CLAUDE.md'ye ne eklemeliyiz?" sorusunu cevapla ve `DERSLER.md`'ye yaz. Tekrar eden çözüm varsa sektör skill'i öner.
