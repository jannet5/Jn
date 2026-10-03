# Harita: Hedef → Bağımlılıklar → A/B/C Yolları → Uygulama → Test → Kalıcı Teslim

Her düğümün bir **kabul ölçütü** ve bir **durum** alanı var. Durumlar 3 Ekim 2026
itibarıyla gerçek sonuçlardır.

```
[H] Hedef
 └─► [B1] Kaynak okuma ──► [B2] Güncel web araştırması ──► [B3] Topluluk araştırması
                                     │                          │
                                     ▼                          ▼
                              [U1] Kanıt matrisi ◄──── [B4] YouTube transkriptleri (A/B/C)
                                     │
                     ┌───────────────┼────────────────┐
                     ▼               ▼                ▼
             [U2] Risk tablosu  [U3] Ana rapor   [U4] Video listesi
                     └───────────────┼────────────────┘
                                     ▼
                              [T1] Otomatik kontrol (kontrol.py)
                                     ▼
                     [K1] Push + geri okuma  ──►  [K2] Özel ZIP + SHA-256 + geri okuma
```

## H — Hedef

Yapay zekânın iş ve gündelik hayatı nasıl etkileyebileceğini; forum, topluluk ve video
kaynaklarından da yararlanarak, **kitap gibi okunan** Türkçe bir araştırma olarak
derlemek.

**Kabul ölçütleri (görev tanımından):**

| # | Ölçüt | Karşılayan çıktı | Durum |
|---|---|---|---|
| AC1 | Forum, Medium, X ve YouTube deneyimleri incelenmiş; eksikler nedeniyle ayrı yazılmış | `arastirma.md` Bölüm 4, `kaynaklar.md` B bölümü, `youtube-video-listesi.md` | **Kısmen** — HN/forum/Medium/X(haber üzerinden)/Türkçe Substack okundu; YouTube transkriptleri ve Instagram **alınamadı** (nedeni yazıldı) |
| AC2 | Yazılım ve fiziksel meslekler ayrı risklerle değerlendirilmiş | `meslek-risk-tablosu.md` A/B bölümleri, `arastirma.md` Bölüm 2–3 | **Tamam** |
| AC3 | Tahminler kanıtlanmış gelişmelerden ayrılmış | `kanit-matrisi.md` (38 iddia, 5 sınıf), rapordaki etiketler | **Tamam** |
| AC4 | Kitap okur gibi akıcı, toparlanmış anlatım | `arastirma.md` (önsöz → 6 bölüm → sonsöz) | **Tamam** |

## Bağımlılıklar

| Düğüm | Gerektirdiği | Kabul ölçütü | Durum |
|---|---|---|---|
| B1 Kaynak okuma | Kullanıcının yüklediği kaynak ve görev metni | Tam metin okunmuş, özel alana kaydedilmiş, depoya girmemiş | Tamam (özel alanda) |
| B2 Resmî araştırma | Web arama + sayfa okuma | ≥8 resmî/akademik kaynak, tarihli | Tamam (24 R kodu) |
| B3 Topluluk | HN API, forumlar, Medium RSS, X aktarımı | ≥4 farklı platform | Tamam (HN, HeatingHelp, Medium, X-haber, Substack) |
| B4 YouTube | Transkript erişimi | ≥15 video transkripti | **Engellendi** → Yol C |

## A/B/C yolları (kırılan noktalar ve seçilen alternatif)

### B4 — YouTube transkriptleri
- **Yol A:** `youtube-transcript-api` → 16/16 `IpBlocked`. **Kırıldı.**
- **Yol B:** `yt-dlp` otomatik altyazı → HTTP 429 + bot doğrulaması. **Kırıldı.**
- **Yol C (seçilen):** Videolar gerçek aramayla seçilip listelendi; içerik uydurulmadı;
  videolardaki ana figürlerin sözleri bağımsız yazılı kaynaklardan doğrulandı;
  kullanıcının ev bağlantısında çalıştırması için `dogrulama/transkript_al.py` hazır.

### B3 — Topluluk sayfaları
- **Yol A:** Reddit / Ekşi / Technopat doğrudan okuma → 403/engelli. **Kırıldı.**
- **Yol B (seçilen):** Hacker News Algolia API (9 tartışma, ~200 yorum) + doğrudan açılan
  forum (HeatingHelp) + Medium RSS tam metin + X tartışmasını aktaran haber.
- **Yol C (yedek):** Arama motoru özetleri — yalnız B açılmadığında, "Arama özeti"
  etiketiyle.

### Teslim biçimi
- **Yol A (seçilen):** Markdown araştırma paketi + otomatik kabul betiği. Gerekçe: görev
  bir araştırma; uygulama/sunucu inşa etmek gereksiz teknoloji dayatması olurdu.
- Yol B (reddedildi): Etkileşimli web sitesi — kaynak bunu istemiyor.
- Yol C (reddedildi): Yalnız sohbet özeti — kalıcı teslim sayılmıyor.

## Uygulama → Test → Teslim

| Adım | Kabul ölçütü | Durum |
|---|---|---|
| U1 Kanıt matrisi | Her iddia 5 sınıftan birinde, en az bir kaynak kodu | Tamam |
| U2 Risk tablosu | Yazılım/fiziksel ayrı, her satır dayanaklı | Tamam |
| U3 Ana rapor | Etiketli, bölümlü, kitap akışında | Tamam |
| U4 Video listesi | ≥15 benzersiz gerçek video, transkript durumu dürüst | Tamam (16 video, transkript yok) |
| T1 `python3 dogrulama/kontrol.py` | Çıkış kodu 0 | Çalışma günlüğünde sonuç |
| K1 Push + geri okuma | Uzak daldaki dosya hash'leri yerelle aynı | Çalışma günlüğünde sonuç |
| K2 Özel ZIP | SHA-256 kaydı + açıp hash karşılaştırma | Çalışma günlüğünde sonuç |

## Checkpoint ve devam komutu

Platform turu keserse, kaldığı yerden devam için:

```
Dal: claude/bold-bohr-so7g6t — klasör: gorevler/ai-gelecek-is-ve-hayat-arastirmasi/
Devam: "harita.md'deki durumu 'Kırıldı/Engellendi' veya boş olan ilk adımdan sürdür;
python3 dogrulama/kontrol.py çalıştır; sonra push + geri okuma + özel ZIP adımlarını yap."
```

YouTube eksikliğini kapatmak için (ev bağlantısında):
`pip install youtube-transcript-api && python3 dogrulama/transkript_al.py`
