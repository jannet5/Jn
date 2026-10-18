# Play Console — "Uygulama içeriği" bölümü cevapları

Play Console'da **Politika ve programlar → Uygulama içeriği** altındaki her
formu aşağıdaki gibi doldurun.

## Gizlilik politikası
URL: `https://github.com/jannet5/Jn/blob/HEAD/play-store/privacy-policy.md`
(Önce dosyadaki `[iletişim e-postanız]` yerine kendi e-postanızı yazın.)

## Reklamlar
Uygulamanızda reklam var mı? → **Hayır**

## Uygulama erişimi (App access)
"Tüm işlevler özel erişim olmadan kullanılabilir mi?" → **Hayır, bazı işlevler kısıtlı**
Talimat adı: `Companion Windows program required`
Talimat metni (İngilizce yapıştırın):

```
The app monitors the reviewer's own Windows PC, so it needs the free companion
Windows program:
1. On a Windows 10/11 (64-bit) PC, download and run:
   https://github.com/jannet5/Jn/releases/latest/download/WinRemoteMonitor-Kurulum.exe
   (Next, Next, Finish. No account needed.)
2. When setup finishes, a page with a QR code opens on the PC.
3. Put the phone on the same Wi-Fi as the PC, open the app, tap "QR Tara"
   (Scan QR), allow the camera and scan the code, then tap "Eşleştir" (Pair).
4. The dashboard shows live CPU/RAM/disk data. A new code can be shown any
   time from Start menu > "Telefonu Eşleştir".
No login or password is used anywhere.
```

## İçerik derecelendirmesi
Kategori: **Yardımcı Program, Verimlilik, İletişim veya Diğer**
Tüm şiddet, cinsellik, kumar, uyuşturucu, küfür sorularına → **Hayır**
Kullanıcılar arası iletişim / içerik paylaşımı → **Hayır**
Konum paylaşımı → **Hayır** · Dijital ürün satın alma → **Hayır**
Beklenen sonuç: **3+ / PEGI 3 / Genel**

## Hedef kitle
Yaş grupları: **18 ve üzeri**
Uygulama çocukların ilgisini çekebilir mi? → **Hayır**

## Veri güvenliği (Data safety)
- Uygulama gerekli kullanıcı verisi türlerinden herhangi birini **topluyor veya paylaşıyor mu?** → **Hayır**
  Gerekçe: Veriler yalnızca kullanıcının kendi telefonu ile kendi bilgisayarı
  arasında, yerel ağda doğrudan aktarılır; geliştiriciye veya üçüncü tarafa
  hiçbir veri gitmez, sunucu yoktur.
- Aktarılan veriler şifreleniyor mu? → **Evet** (TLS)
- Kullanıcı verilerinin silinmesini isteyebilir mi? → Uygulamayı/programı
  kaldırmak tüm verileri siler (sunucuda veri tutulmadığı için ayrıca istek
  gerekmez).

## Haber uygulaması → Hayır · Devlet uygulaması → Hayır
## Finansal özellikler → Yok · Sağlık → Yok
## COVID-19 temaslı takip → Hayır

## Kullanılan izinler (Play bunları kendisi gösterir)
- `CAMERA`: Yalnızca eşleştirme QR kodunu okumak için.
- `INTERNET`: Telefonun bilgisayardaki programa yerel ağda bağlanması için.
