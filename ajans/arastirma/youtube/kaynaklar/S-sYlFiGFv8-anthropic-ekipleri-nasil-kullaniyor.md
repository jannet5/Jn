# "How Anthropic teams use Claude Code" (blog) — S-sYlFiGFv8 için destek kaynağı

- URL: https://claude.com/blog/how-anthropic-teams-use-claude-code (eski anthropic.com/news/... yönleniyor)
- Tür: Anthropic blog. Çekildi 2026-10-09 (WebFetch özeti). Videodaki Claude Code ekibi anlatısının (Thariq Shihipar, Sid Bidasaria, Robert Boyce) kendisi değil; ekip-ekip kullanım örnekleri.

## Ekip ekip somut akışlar
- **Data Infrastructure**: yeni gelenlere tüm kod tabanı + CLAUDE.md'ler veriliyor; Claude pipeline bağımlılıklarını ve dashboard kaynaklarını anlatıyor. Kubernetes kesintisinde dashboard ekran görüntüleri paylaşıldı, Claude GCP arayüzünde yol gösterdi, pod IP tükenmesini buldu, yeni IP havuzu komutlarını verdi (~20 dk kazanç).
- **Product Engineering**: Claude Code "ilk durak"; hangi dosyalara bakılacağını buldurmak için. Örnek prompt: "Can you fix this bug? This is the behavior I'm seeing".
- **Security Engineering**: önce kod yerine **pseudocode** istiyorlar, TDD ile ilerliyorlar, periyodik check-in. Olaylarda stack trace + doküman verip kontrol akışını izletiyorlar (10-15 dk → üçte biri). Dokümanları markdown **runbook** / troubleshooting rehberine çeviriyorlar.
- **Inference**: bilmedikleri dilde (Rust) test yazdırma: ne test edileceğini tarif et, Claude dilde yazsın. Model fonksiyonlarını açıklatma (1 saat → 10-20 dk).
- **Data Science**: TypeScript bilmeden React görselleştirme uygulamaları; tek prompt + sandbox + küçük düzeltmeler.
- **Product Design**: Figma dosyası ver → otonom döngü (kod yaz, test çalıştır, yinele), insan sonda gözden geçirir. Hata durumlarını / mantık akışlarını / sistem durumlarını haritalatıp uç durumları bulma. GitHub Actions ile PR yorumlarını otomatikleştirme.
- **Growth Marketing**: yüzlerce reklamlık CSV → düşük performanslıları bul → karakter limitinde yeni varyasyonlar üret; **iki sub-agent**. Figma eklentisiyle başlık/açıklama değiştirerek 100 reklam varyasyonu.
- **Legal**: "phone tree" prototipi (doğru avukata yönlendirme).

## Bizim için çıkarımlar
- Reklam hattı: CSV → analiz → varyasyon üretimi iki alt ajanla (analist + yazar) birebir bizim Meta reklam döngümüz.
- Tasarım hattı: Figma/Stitch çıktısı → otonom "yaz-test et-yinele" döngüsü → insan son kontrol.
- Güvenlik/kalite: önce pseudocode/plan, sonra TDD; runbook'ları markdown olarak `sistem/` altında tut.
