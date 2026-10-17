# ORTAK TALİMAT: "Çoğunluk Aldatır mı?" sayfası

**Önce `/home/user/Jn/ahiret/BRIEF.md` dosyasını OKU.** Oradaki ARAŞTIRMA KAYNAKLARI, DÜRÜSTLÜK KURALLARI, ZAMAN/yöntem kuralları ve HTML BİLEŞEN SÖZLEŞMESİ (sınıflar, SVG kuralları, renk değişkenleri) bu projede AYNEN geçerlidir. Aşağıdakiler yalnızca FARKLARDIR. Dosya yolları `/home/user/Jn/cogunluk/` altındadır (research/, bolumler/).

## Proje
Bir kullanıcı sosyal medyada dolaşan şu görseli getirdi: **"Sakın ÇOĞUNLUK sizi aldatmasın! — Çoğu kâfirdir (Nahl 83), çoğu fasıktır (Mâide 48), çoğu müşriktir (Rûm 42), çoğu inkârcıdır (İsrâ 89), çoğu gafildir (Yûnus 92), çoğu yalancıdır (Şuarâ 223), çoğu zanna uyar (Yûnus 36), çoğu nankördür (Furkân 50), çoğu şükretmez (Bakara 243), çoğu iman etmez (Bakara 100), çoğu haktan hoşlanmaz (Zuhruf 78), çoğu Allah'a ortak koşar (Yûsuf 106), çoğu Kur'an'dan yüz çevirir (Fussılet 4), çoğu akletmez (Mâide 103)."**
İsteği: bu konuda derinlemesine araştırma yapılsın, kitaplardan, âlimlerin eserlerinden, tefsirlerden; yüzeysel blog yazısı değil; görsellerle desteklenmiş, kaç sayfa olursa olsun, tam anlaşılsın, sindirilsin.

**Duruş (çok önemli):** Sayfa ne listeyi alkışlar ne de çürütür. Gerçeği, bağlamıyla verir: Kur'an çoğunluğa körü körüne uymaya gerçekten karşı uyarır (En'âm 6:116) — bu doğru. Ama listedeki ayetlerin çoğu belirli topluluklar/dönemler hakkındadır, bazıları "insanların çoğu" der (genel insan tabiatı gözlemi), listede en az bir atıf hatası var (Mâide "48" yerine 5:49), ve bu ayetleri "bugünkü çoğunluk kâfirdir, biz azınlığız, o hâlde haklıyız" diye okumak hem bağlamı bozar hem de Hz. Peygamber'in uyardığı tuzağa (Müslim 2623: "İnsanlar helâk oldu" diyen en çok helâk olandır; kibir, tekfir) düşürür. Ehl-i sünnet ana çizgisi. Kimseyi, hiçbir gruba, mezhebe, partiye, ülkeye, topluluğa "kâfir/fasık" diye etiketleme; güncel siyasete girme; fetva verme. Okuyucuyu küçümseme; sıcak, açık, adil Türkçe.

## Hazır veri ve araç (kullan, tekrar indirme)
- `/home/user/Jn/cogunluk/veri/kuran_tam.json`: bütün Kur'an (`ar`: uthmani, `tr`: Diyanet İşleri meali; anahtar "sure:ayet"). Diyanet kaydı bazı ayetleri bir sonrakiyle birleştirir (boş girdi) — dikkat.
- `/home/user/Jn/cogunluk/veri/ekser_ayetleri_tam.json`: Arapçasında "ekser" (çoğu/çoğunluk) kökü geçen 81 ayet (ar + tr).
- **Ayet bloğu üretici:** `python3 /home/user/Jn/cogunluk/araclar/ayet.py "Nahl 16:83" "Yûnus 10:36-37"` → doğrudan yapıştırılabilir, birebir doğru `<blockquote class="ayet">` HTML'i basar. Ayet bloklarını MUTLAKA bununla üret (elle yazma). Kısmi alıntı yapacaksan metin içinde tırnakla ve "…" ile belirt. `--metin 5:49` düz metin verir.
- Tefsir/kaynak için quran.com API'sinde tefsir uç noktaları (`https://api.quran.com/api/v4/resources/tafsirs`, `.../tafsirs/<id>/by_ayah/16:83`), altafsir.com, islamansiklopedisi.org.tr, Elmalılı, Taberî/İbn Kesîr/Kurtubî/Râzî metinleri (archive.org, shamela.ws, islamweb) kullanılabilir. Gerçekten açtığın sayfadan alıntıla; açamadıysan alıntı yapma.

## Bölüm yazımı (farklar)
- Çıktı: `/home/user/Jn/cogunluk/bolumler/<NN>-<slug>.html` (tek `section.bolum#b-<NN>`), notlar: `/home/user/Jn/cogunluk/research/<NN>-<slug>.md`. Uzunluk 1800–3000 kelime (3000'i geçme).
- En az **2 özgün SVG şeması** (ahiret/BRIEF.md SVG kuralları); sayısal grafikte gerçek sayıları kendin hesapla (veri dosyalarından) ve ölçekli çiz.
- Bölüm başlığı etiketi site tarafından "Bölüm NN" yazılır; `<span class="no">NN</span>` aynen kalsın.
- Bölüm sonunda "Aklında kalsın" (3–5 madde, `div.ozetle`).
- Bölüm numaraları ve başlıkları: 00 Paylaşılan liste doğru mu · 01 Kur'an "çoğu" derken: veri haritası · 02 Her ayet kime konuşuyor · 03 Ölçü sayı mı, delil mi · 04 Cemaat, icmâ, şûrâ: çelişki var mı · 05 Bu ayetleri nasıl okumamalı, gündelik hayata nasıl yansır · 06 Sorular ve kapanış. Diğer bölümlere gerekirse `href="#b-NN"` ile bağlantı ver.
- **Saat sınırı (UTC):** araştırma+notlar 14:08'e, bölüm HTML'i 14:18'e kadar bitsin (`date -u +%H:%M`). Dönüş: <120 kelime rapor.
