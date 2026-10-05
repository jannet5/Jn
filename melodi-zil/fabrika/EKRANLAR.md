# EKRANLAR: Melodi Zil
> En az 2 referansı olmayan ekranın kodu yazılmaz (D-003).

| # | Ekran | Rota / dosya | Amaç | Referans 1 | Referans 2 | Durumlar (dolu/boş/yük./hata) | Durum |
|---|---|---|---|---|---|---|---|
| 1 | Tanıtım | onboarding / ui/onboarding | İlk açılışta 3 adımı anlat, telif notu | Shazam ilk açılış | Google Zil Sesi onboarding | tek durum | ✅ |
| 2 | Ana sayfa | home / ui/home | Bağlantı yapıştır, dosya seç, son ziller | Shazam ana | InShot Ringtone Maker ana | dolu / boş (son ziller) / input hatası | ✅ |
| 3 | İşlem | process / ui/process | 4 adımlı ilerleme, iptal | Shazam dinleme | Google Files temizleme | yükleniyor / hata (tekrar dene, geri) | ✅ |
| 4 | Sonuç | result / ui/result | Form seç, dinle, bölüm, kaydet | InShot kesme ekranı | GarageBand enstrüman seçimi | dolu / yükleniyor (skeleton rulo) / kayıt hatası (snackbar) | ✅ |
| 5 | Zillerim | library / ui/library | Kayıtlı ziller, yeniden ayarla/paylaş/sil | Zedge İndirilenler | Google Dosyalar liste | dolu / boş | ✅ |
| 6 | Ayarlar | settings / ui/settings | Tema, gizlilik, nasıl çalışır, lisans, sürüm | Google Saat ayarları | Signal ayarları | tek durum | ✅ |

Durum: ⬜ · 📝 planlandı · 🛠️ kodlanıyor · 👀 onay bekliyor · ✅ (kullanıcı telefon testi sonrası kesinleşir)

## Ekran planları
### Ana sayfa
Header: display başlık "Melodi Zil" + body alt başlık · Bölüm 1: OutlinedTextField (bağlantı, yapıştır ikonu, yardım/hata metni) + PrimaryButton "Melodiye çevir" + SecondaryButton "Telefondan ses dosyası seç" · Bölüm 2: SectionTitle "Son zillerin" + AppCard içinde ≤3 ListRow + "Tümünü gör" · Boş durum: AppCard içinde açıklama metni · Hata: input altı kırmızı yardım metni
### İşlem
Header: heading "Melodi çıkarılıyor" + uyarı body-sm · Bölüm 1: AppCard: skeleton başlık + 4 StepRow (CheckCircle success / spinner / RadioButtonUnchecked muted) + aktif adımda LinearProgress · Alt: SecondaryButton "İptal" · Hata: ErrorState (Tekrar dene + Geri)
### Sonuç
Header: ArrowBack 24 + title + Share 24 · Bölüm 1: AppCard: kapak 56 radius 8 + başlık + "kanal · N nota" + PianoRoll 140 + SecondaryButton Dinle/Durdur · Bölüm 2: SectionTitle "Form" + FlowRow 8 FilterChip + Caption açıklama · Bölüm 3: "Uzunluk" SegmentedButton 15/20/30/40 · Bölüm 4: "Bölüm m:ss – m:ss" + TextButton Otomatik + Slider + Caption · Alt: PrimaryButton "Zil sesi yap" → ModalBottomSheet (3 KindRow radyo + Switch varsayılan + PrimaryButton Kaydet) · İzin: AlertDialog
### Zillerim
Header: display "Zillerim" · Bölüm 1: AppCard içinde ListRow listesi (enstrüman ikonu, başlık, "tını · tür · tarih") · Satır → ModalBottomSheet (5 ListRow eylem) · Sil → AlertDialog onay · Boş: EmptyState LibraryMusic + "İlk zilini yap"
### Ayarlar
Header: display "Ayarlar" · Bölüm 1: "Görünüm" SegmentedButton Sistem/Açık/Koyu · Bölüm 2: "Hakkında" AppCard: 4 ListRow (nasıl çalışır, gizlilik, lisanslar, sürüm) → AlertDialog kaydırılabilir metin
