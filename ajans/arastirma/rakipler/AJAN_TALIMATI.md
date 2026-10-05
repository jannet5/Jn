# Alt-ajan talimatı (rakip ajans incelemesi)

Çalışma dizini: /home/user/Jn/ajans/arastirma/rakipler  (SADECE bu dizin altına yaz; git commit/push YAPMA, ana oturum yapar)

Her ajans için sırayla:
1. Siteyi doğrula: WebFetch ile ana sayfayı oku (site yoksa/çalışmıyorsa o ajansı atla ve yerine yeni bir tane bul).
   Ana sayfa + hizmetler + fiyat/paket + portföy/referans sayfalarını WebFetch ile oku. Fiyat sayfası yoksa
   WebSearch ile "<ajans> fiyat / pricing" ara. Instagram kullanıcı adını sitedeki linkten al (HTML'de instagram.com/… geçer;
   gerekirse `curl -sL <site> | grep -o 'instagram.com/[A-Za-z0-9_.]*'`). Facebook/LinkedIn/TikTok linklerini de aynı yolla çek.
2. Ekran görüntüsü (zorunlu, dizinde çalıştır):
   `cd /home/user/Jn/ajans/arastirma/rakipler && timeout 240 python3 ss_al.py <slug> <site-url>`   → ss/<slug>-web.png, ss/<slug>-mobil.png
   `cd /home/user/Jn/ajans/arastirma/rakipler && timeout 150 python3 ig_al.py <slug> <instagram-kullanici>` → ss/<slug>-instagram.png + ss/<slug>-instagram.json
   ig_al.py çıktısındaki takipçi/gönderi/tür dağılımını ve ss/<slug>-instagram.json içindeki son gönderi caption'larını nota işle.
   Instagram bio metni embed ucunda yok: WebSearch ile "<kullanici> instagram" arayıp snippet'ten al; bulamazsan "bio bulunamadı" yaz.
   Bir SS başarısız olursa bir kez daha dene; yine olmazsa notta "SS alınamadı: <sebep>" yaz.
   slug: küçük harf, sadece a-z0-9 ve tire (örn. `webtures`, `sitelyform`).
3. Site yapısını çıkarmak için ham HTML'e de bak: `curl -sL <site> | python3 -c "import sys,re;h=sys.stdin.read();print(re.sub(r'<script.*?</script>|<style.*?</style>','',h,flags=re.S)[:20000])"`
   ya da h1/h2/h3 ve buton metinlerini çek: `curl -sL <site> | grep -oiE '<(h1|h2|h3|button|a)[^>]*>[^<]{3,120}' | head -80`.
   Bölüm sırasını (hero → hizmetler → portföy → …) gerçekten gördüğün sırayla yaz, uydurma. Web SS'ini Read ile görüntüleyip doğrula.
4. SABLON.md'deki şablonu birebir doldurup `ajanslar/<slug>.md` olarak kaydet. Türkçe yaz. Bilinmeyen alanlara "bulunamadı" yaz, boş bırakma.
   Fiyatları para birimiyle ve kaynak linkiyle yaz. Alıntıları (hero başlığı, CTA, bio) birebir aktar.
5. İyi/kötü en az 3+3, "bizim için notlar" en az 2 madde.

Bitince son mesajında, her ajans için şu satırı ver (index.md'ye kopyalanacak, tek satır, | ile ayrılmış):
`| Ajans | Ülke | [site](url) | [@kullanici](https://www.instagram.com/kullanici/) | hizmetler (kısa) | fiyat (kısa) | hedef | ss/<slug>-web.png, -mobil.png, -instagram.png (ya da "IG SS yok") |`
Ayrıca 5-8 maddelik "bu partide göze çarpan kalıplar" listesi ver.
