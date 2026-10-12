# Claude Code for Mobile Devs: The 5 Skills I Use on Every App — Code with Beto — 8 dk — https://www.youtube.com/watch?v=mvfQGjQurAQ

## Tek paragraf: ne yapıyor, sonuç ne
Beto, yayındaki uygulaması Inkigo (AI Tattoo Try On, yaklaşık 650 $ MRR) üzerinde her uygulamada çalıştırdığı Claude Code skill'lerini gösteriyor. Sırayla: (1) **ASO Audit**: App Store listesinin sağlık kontrolü, puanı ve rakip karşılaştırması. (2) **Apple Search Ads keyword stratejisi**: marka, kategori, long-tail ve rakip anahtar kelimelerine teklif önerisi. (3) **App Store Preflight**: gönderim öncesi red riskini tarıyor, App Store Connect CLI ile ya da yerelde. (4) **App Icon**: kendi skill'i, SnapAI CLI ve OpenAI ile `.icon` formatında ikon üretiyor. (5) **Native UI skill'leri**: Expo ekibinin "Building Native UI" skill'i, ayrıca Swift/SwiftUI skill reposu ve Google'ın resmi Android skill'leri. Sonuç: tek bir App Store reddinin 9 güne mal olduğu süreçleri skill'lerle önceden yakalamak ve UI kalitesini resmî skill'lerle yükseltmek.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **5 skill'in kurulum komutları ve notları (blog):** https://codewithbeto.dev/blog/best-claude-skills-for-mobile-development (kurulum komutları videoda gösterilmiyor, bu sayfada)
- Kitap: From Idea to App Store with Claude Code: https://cwb.sh/book?r=yt-best-claude-skills
- `/aso-audit` skill: App Store ASO sağlık kontrolü (yapımcısı videoda "App Annie" diye geçiyor; ASO skill serisi yayınlayan bir ekip)
- Apple Search Ads keyword skill (aynı yapımcıdan)
- App Store Preflight skill (App Store Connect CLI opsiyonel)
- App Icon skill (Beto'nun kendi skill'i) + SnapAI CLI (OpenAI API key gerekiyor, key yerelde saklanıyor) + Apple Icon Composer
- Expo resmi skill'i: "Building Native UI for React Native and Expo" (Expo Router navigasyonu, SF Symbols, animasyonlar, liquid glass)
- Swift/SwiftUI skill reposu (adı videoda söylenmiyor)
- Google ekibinin resmi Android skill'leri (navigation, XML'den Compose'a geçiş, edge-to-edge)
- Platano şablonu: https://cwb.sh/platano?r=yt · RN kursu: https://cwb.sh/rn?r=yt · Pro: https://cwb.sh/pro?r=yt
- Quick Push (push testi, macOS): https://apple.co/4tvT4wF · Inkigo: https://cwb.sh/inkigo?r=yt
- Newsletter: https://cwb.sh/newsletter?r=yt · Discord: https://cwb.sh/discord?r=yt

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:00:42] **ASO Audit:** Uygulamanın yayında olması gerekiyor. Çağrı `/aso-audit <app-id>`. App ID, App Store URL'indeki `id` kısmından sonra gelen rakamlar.
   - Çıktı: genel skor (Inkigo için 49/100), "biggest problem", rakip araştırmasına dayalı öneriler, rakip karşılaştırma tablosu ve en yüksek etkili adımlar listesi.
   - Bulgu [00:01:58]: subtitle ("Create & preview AI tattoos") uygulama adını ("AI Tattoo Try On") tekrar ettiği için değerli bir alan boşa gidiyor.
   - Yanlış pozitif: skill ikonu ve videoyu göremediği için app preview videosu yok sanıyor. Beto'nun önerisi: uygulama videosu mutlaka olsun ve kullanıcının ilk gördüğü şey o olsun.
   - Öneri [00:02:36]: kullanıcıdan rating istemek. Beto bunu görsel kaydedildikten sonra "review bırakmak ister misin?" diye soruyor.
2. [00:02:47] **Apple Search Ads keyword stratejisi:** Çıktı şu tablolar: marka anahtar kelimelerine yüksek teklif (rakipler senin adına teklif verebilir), kategori ve çekirdek anahtar kelimeler, kategori long-tail (düşük teklif, yüksek dönüşüm) ve rakip anahtar kelimeleri. Ayrıca teklif tutarları, öncelikli doğrulama listesi ve öneriler.
3. [00:04:17] **App Store Preflight:** App ID veriliyor. iOS/macOS için kapsamlı gönderim öncesi test. App Store Connect CLI ile metadata çekilebiliyor, istenmezse yerel kontrol çalışıyor.
   - Uyarılar: kullanılmayan Facebook Ads ve TikTok SDK kalıntıları. Doğru yapılanlar: entitlements, API declarations (privacy manifest). Hatırlatma: Google ile giriş varsa Apple **Sign in with Apple**'ı da zorunlu tutuyor.
4. [00:05:57] **App Icon (SnapAI):** Skill kuruluyor, Claude'a prompt veriliyor (aşağıda). OpenAI API key gerekiyor ve yerelde saklanıyor. SnapAI CLI doğrudan da kullanılabiliyor (model ve ayar parametreleriyle). Çıktı Android, iOS 18 ve iOS 26 uyumlu; yeni `.icon` formatında üretiliyor. Icon Composer'da açılıp liquid glass efekti eklenebiliyor. Yaklaşık 1 dakika sürüyor.
   - Hata: OpenAI hesabında kredi yoktu, kredi yükleyince çalıştı.
5. [00:07:09] **Native UI:** Expo'nun "Building Native UI for React Native and Expo" skill'i (Expo Router navigasyonu, SF Symbols, animasyonlar, liquid glass). Swift/SwiftUI skill reposu da RN'de native modül yazarken işe yarıyor. Google'ın resmi Android skill'leri: navigation tree kurulumu, XML'den Compose'a geçiş, edge-to-edge.
6. [00:08:22] Özet. Tüm kurulum komutları blog yazısında.

## Kullandığı prompt'lar (varsa aynen)
```
/aso-audit <APP_ID>
```
```
Let's create an icon, minimalistic format, Tattoo Try On application.
```
(Search Ads ve Preflight skill'lerine yalnızca app ID verilmiş. Tam prompt metni gösterilmiyor.)

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- UI kalitesi için resmî Expo skill'ine güveniyor: native UI kalıpları, SF Symbols, liquid glass, Expo Router native navigasyon ve animasyonlar.
- Android tarafında Google'ın resmi skill'leri: edge-to-edge ve Compose kalıpları (ham Android görünümüne karşı).
- İkon: tek prompt ile minimalist ikon, sonra Icon Composer'da liquid glass ile cilalama. iOS 26 `.icon` formatı.
- Mağaza görseli: app preview videosu ilk sırada olmalı.

## Hatalar ve çözümleri
- ASO Audit ikon ve videoyu göremiyor, bu yüzden yanlış öneri veriyor. Çözüm: çıktıyı elle doğrulayıp geçersiz önerileri atmak.
- SnapAI kredi hatası. Çözüm: OpenAI API hesabına kredi yüklemek.
- App Store reddi (9 gün kayıp): Sign in with Apple eksikliği, API declarations. Çözüm: gönderim öncesi Preflight skill'ini çalıştırmak.
- Kullanılmayan SDK kalıntıları (Facebook, TikTok) red riski taşıyor. Çözüm: Preflight uyarısıyla temizlemek.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Blog sayfasındaki (https://codewithbeto.dev/blog/best-claude-skills-for-mobile-development) 5 skill'in kurulum komutlarını alıp fabrika skill setine ekle (otomatik çekim boş döndü, sayfa elle açılmalı).
- **Tasarım aşamasına zorunlu skill:** Expo "Building Native UI for React Native and Expo" ve Android için Google'ın resmi Android skill'leri (edge-to-edge, Compose). Bu, "ham Android pop-up" şikâyetine doğrudan çözüm.
- **Yayın öncesi kapı (gate):** her build için App Store Preflight skill'i. Kontrol listesine ekle: Sign in with Apple (başka bir sosyal giriş varsa), privacy manifest/API declarations, entitlements, kullanılmayan SDK temizliği.
- **İkon aşaması:** App Icon skill + SnapAI (OpenAI key fabrikanın env'inde). iOS 26 `.icon` + Android adaptive icon çıktısı, ardından Icon Composer'da liquid glass.
- **Yayın sonrası:** `/aso-audit <app-id>` raporu müşteriye teslim paketine girsin. Subtitle'ın adı tekrar etmemesi, app preview videosu ve uygulama içi rating isteme (önemli bir aksiyondan sonra) kuralları.
- Ücretli edinim isteyen müşteriler için Apple Search Ads keyword skill'i: marka, kategori, long-tail ve rakip tabloları.
