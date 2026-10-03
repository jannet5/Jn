# ChatGPT Pro ile sıfırdan danışma (kullanıcının hesabında çalıştırılacak)

Kullanıcı "her şeyi sıfırdan ChatGPT Pro ile konuş, ücretli olmasın" dedi. Bulut ortamının kullanıcının
ChatGPT hesabına erişimi yok (giriş bilgisi verilmedi, verilmesi de istenmedi), bu yüzden danışma iki
ücretsiz yoldan biriyle **kullanıcının kendi oturumunda** yapılır:

1. Codex üzerinden (ChatGPT girişiyle, plan limitinden düşer):
   ```bash
   codex exec --skip-git-repo-check - < danisma/chatgpt-pro-danisma.md > danisma/cevap.md
   ```
2. ChatGPT Pro web: aşağıdaki "İstem" bölümünü yapıştırın, cevabı `danisma/cevap.md` olarak kaydedin.

Cevaptaki uygulanabilir öneriler `prompts.py` (analiz/üretim istemleri) ve `select.py` (puanlama) içine işlenir.

---
## İstem

Bir Instagram içerik sistemi kuruyorum. Kurallar: Codex beyin; ücretli API yok (yalnız ChatGPT Pro aboneliği);
Meta Graph API zorunlu değil. Akış: 1000 "tutmuş" gönderi (https bağlantı + fotoğraf + arka ses + açıklama) →
her biri için görsel, ses ve açıklamayı BİRLİKTE değerlendirip neden tuttuğunu kanıtla açıklama → aynı başarı
mantığıyla, birbirinden ve kaynaktan farklı 4 özgün görsel (toplam 4000) → dosya düzeyinde doğrulama.

Mevcut tasarım:
- Toplama: Instagram "Bilgilerini indir" JSON'u + tarayıcıda tek tık yakalama (og:description, og:image, ses etiketi).
- Seçim puanı: takipçi varsa 1000×(beğeni + 3×yorum + 0,05×izlenme)/takipçi, yoksa log10 ölçekli aynı toplam.
- Analiz şeması: why_it_worked{visual,audio,caption,combined}, ≥3 kanıtlı success_factor, audience_emotion,
  hook_type, 4 varyasyon (v1 aynı duygusal kanca/farklı sahne, v2 aynı kompozisyon mantığı/farklı konu-palet,
  v3 aynı merak boşluğu/farklı format, v4 en güçlü faktörü abartan cesur yorum).
- Üretim: `codex exec '$imagegen …'`; doğrulama: kısa kenar ≥512, 4'lü arası dHash ≥6, kaynakla ≥10.

Sorular:
1. Bu puanlama "tutmuş" gönderiyi iyi ayırır mı? Kaydetme/paylaşım görünmüyorken neyi değiştirirsin?
2. Ses + açıklama + görselin birlikte etkisini daha iyi yakalamak için şemaya hangi alanları eklerdin?
3. 4 varyasyon ekseni yeterince farklı mı? Daha iyi 4 eksen önerin varsa somut yaz.
4. 4000 görseli Pro plan limitine takılmadan en verimli nasıl sıralarım (kalite/boyut/gün başına adet)?
5. Özgünlük ve telif açısından istemlerde eksik gördüğün kural var mı?
Kısa, uygulanabilir maddelerle cevap ver.
