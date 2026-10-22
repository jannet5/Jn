# Play politikaları ve KVKK: bildirim okuyan bir uygulama için

Araştırma kolu: yasal (kendim, 22 Ekim 2026). HUKUKİ TAVSİYE DEĞİLDİR; kesin karar için avukat ve
Kişisel Verileri Koruma Kurumu (KVKK) rehberleri esas alınmalı. Ayrıca bkz. `09` §7–§8 (Play formları,
ret riski). Etiketler: [resmi], [çoklu], [tek kaynak], [çıkarım], [doğrulanamadı].

## 1. Google Play: "belirgin açıklama ve onay" (Prominent Disclosure and Consent)

Kaynak: https://support.google.com/googleplay/android-developer/answer/11150561 [resmi]

- Ne zaman: kullanıcının makul olarak beklemeyeceği hassas veri/izin için. Sayfadaki örnekler Erişilebilirlik
  servisi, arka plan konumu, uygulama görünürlüğü (kurulu uygulama listesi). Bildirim erişimi sayfada AYRICA
  anılmıyor; ama aynı genel kural (kullanıcı verisi politikası) geçerli. [resmi + çıkarım]
- Açıklama NEDEN gerektiğini, HANGİ veri türlerini, NASIL kullanıldığını söylemeli; 13 yaşındaki bir
  çocuğun anlayacağı dilde, jargonsuz. Uzun olması sorun değil, net olmalı.
- Yer: uygulamanın İÇİNDE, izin istenmeden hemen önce. Mağaza açıklamasında veya web sitesinde olması
  yetmez. Gizlilik politikasının veya Veri güvenliği formunun yerine geçmez.
- Biçim: açık onay ("Kabul ediyorum" gibi; "Tamam/Anladım" değil) ve en az iki seçenek: onayla ve
  "Şimdi değil". Reddedene karşı uygulama zarifçe çalışmaya devam etmeli. Sistem iletişim kutusu gibi
  görünmemeli. Tekrar tekrar sormamalı.
- İzin beyan videosu (Console): açılış, açıklamanın tam metni, onay akışı, red akışı, çekirdek özellik.

### Sende'de bugünkü durum ve yapılanlar

- İzin kartının metni artık NE okunduğunu (gönderen adı ve mesaj metni), NE yaptığını (burada gösterir)
  ve verinin çıkmadığını söylüyor (dört dilde).
- Düğmeler "Erişimi aç" ve "Nasıl yapılır?" idi. Play'in istediği biçime yaklaştırmak için birincil düğme
  onay anlamı taşıyacak ve ikincil "Şimdi değil" olacak (bu turda uygulandı).

## 2. Veri güvenliği formu

- "Toplamak" = veriyi cihazdan dışarı iletmek; yalnız cihazda işlenen veri beyan edilmez. [resmi]
  https://support.google.com/googleplay/android-developer/answer/10787469 Sende'nin cevabı: toplanmıyor,
  paylaşılmıyor. Play sürümünde Billing kütüphanesi Google'a ait bir akış getirir; o bölümü Console'un
  sorularına göre işaretle. (`docs/RELEASE.md` §5)

## 3. Gizlilik politikası

- Mağazada VE uygulamada erişilebilir bir URL; PDF olmamalı. Taslak: `docs/GIZLILIK.md` (TR/EN).
- Politikada kurulu uygulama listesinin okunduğu ve nedeni (kanal seçici) yazılı olmalı. [çıkarım, 09 §8]

## 4. KVKK (6698 sayılı Kanun)

- Veri YALNIZCA kullanıcının telefonunda işleniyor ve geliştiricinin erişimi yok. Bu durumda geliştiricinin
  "veri sorumlusu" sayılıp sayılmayacağı, amaç ve yöntemi kimin belirlediğine bağlı ("karar verici irade");
  doğrudan bu soruyu ele alan bir kaynak bulamadım. [doğrulanamadı]
  Çıkarımım: Sende verinin amacını ve yöntemini belirleyen yazılım olsa da veriye erişmediği için
  klasik bir toplama yoktur; yine de şeffaflık için aydınlatma metni yayımlamak güvenlidir. [çıkarım]
- Esnaf kullanıcı (telefon sahibi) kendi müşterilerinin verisi bakımından kendi veri sorumlusu olabilir;
  Sende yalnız bir araçtır. Uygulama içindeki gizlilik metni bunu abartmadan söylemeli.
- Aydınlatma metni: kimin işlediği, hangi amaçla, hangi hukuki sebeple, kime aktarıldığı (kimseye),
  haklar. Taslak: `docs/GIZLILIK.md`.
- **VERBİS**: 2025/1572 sayılı Kurul Kararı (Resmî Gazete 01.10.2025, 33034): yıllık çalışan sayısı 50'den az
  VE yıllık mali bilanço toplamı 100 milyon TL'den az olan, ana faaliyeti özel nitelikli veri işlemek
  olmayan veri sorumluları kayıt yükümlülüğünden muaf. 2025/2393 (25.12.2025): bilanço esasına göre
  defter tutanlarda ikisi birlikte, tutmayanlarda yalnız çalışan sayısı ölçütü. [çoklu]
  https://www.erdem-erdem.av.tr/bilgi-bankasi/verbis-kayit-yukumlulugune-iliskin-yeni-istisnalarin-uygulama-esaslari-aciklandi
  Sende'yi tek kişilik küçük işletme olarak yayınlamak bu eşiklerin altında; ama veri sorumlusu olup
  olmadığın ayrı soru. Mali müşavire/avukata sor. [çıkarım]
- Mobil uygulamalarda "tek tık rıza dönemi bitiyor, bildirim amaçları ayrılacak" yönünde 2026 tarihli
  haber var (parçalı açık rıza). [tek kaynak]
  https://www.alomaliye.com/2026/01/14/mobil-uygulamalarda-tek-tik-riza-donemi-bitiyor-bildirim-amaclari-ayrilacak/amp/
  Sende tek amaçlı olduğu için etkisi küçük; ödeme/analiz amaçlarını ayırmamak için analiz yok.

## 5. GDPR (Fransa/AB) ve BAE PDPL — kısa

- GDPR: veri yalnız cihazdaysa ve geliştirici erişmiyorsa "household/yerel" işleme tartışması vardır; yine
  de aydınlatma ve silme hakkı uygulama içinde sağlanıyor ("Tüm yerel veriyi sil", dışa aktarma). [çıkarım]
- BAE PDPL: kapsamı ve cihaz içi işleme yorumu doğrulanamadı. [doğrulanamadı] İlk yayın Türkiye ile sınırlı.

## 6. Üçüncü taraf mesajları ve marka

- Müşterilerin yazdığı mesajlar üçüncü kişi verisidir; sorumluluk telefon sahibinde. Sende bunları
  yalnız cihazda gösterir, dışarı göndermez.
- WhatsApp/Instagram/TikTok adı ve logosu: mağaza başlığında ve grafiklerde logo kullanma; gerçek uygulama
  ikonlarını çalışma zamanında telefondan okuyup göstermek bir "kullanım" sayılır ama yeniden dağıtım
  değildir. Mağaza görselleri uydurma kanal adlarıyla üretildi (`tools/make_store_assets.py`). [çıkarım]
  Meta tarafında dar durmak için araştırma 09 §8.4.

## Sende için çıkarımlar

- Açık onay düğmesi + "Şimdi değil" + red akışında uygulama çalışmaya devam eder (uygulandı).
- Gizlilik politikası ve aydınlatma metni hazır (`docs/GIZLILIK.md`); canlı bir URL gerekir.
- VERBİS ve veri sorumlusu sorusu: avukata/mali müşavire sorulacak, eşikler yukarıda.
