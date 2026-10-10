# CLAUDE CODE FULL KURS 2 SAAT: Kur ve Sat (2026) — burhan kocabıyık — 125 dk — https://www.youtube.com/watch?v=qm0ZlYezt1Q
> Kaynak notu: Transkript yok (bulut IP engeli). Dayanak: videonun uzun açıklaması + 15 bölümlük zaman damgalı içindekiler (`transkriptler/qm0ZlYezt1Q.aciklama.txt`). Bölüm içlerindeki ayrıntılar bilinmiyor; bölüm başlığından çıkarımlar "(tahmin)" ile işaretli ve Anthropic dokümanlarıyla (`kaynaklar/gv0WHhKelSE-...md`) desteklendi. Gumroad dosya paketi (bahsedilen tüm dosyalar) çekilmedi. Güven: **orta**.

## Tek paragraf
Türkçe, yeni başlayanlar için uçtan uca 2 saatlik Claude Code kursu; "kur ve sat" vurgusu (ajans/freelance geliri). Antigravity IDE içinde kurulum ve abonelik, terminal/VS Code terminolojisi, "projenin beyni" CLAUDE.md ve hafıza yönetimi, 15 dakikanın altında ilk web uygulaması, Claude Code özellikleri ve verimli kodlama, debugging, Skills, MCP, Chrome'da Claude Code ile arayüz testi, sub-agent ve ajan takımları, Git worktrees, plugin/extension'lar, deployment (Modal) ve kurs özeti. Ayrıca birden fazla Claude Code instance'ını paralel çalıştırma, token tasarrufu ve bağlam yönetimi işleniyor.

## Önerdiği iş akışı (adım adım — bölüm sırasıyla)
1. **Kurulum ve ayarlar** (9:31): Claude aboneliği (Pro/Max), Claude Code CLI kurulumu, Antigravity IDE ile entegrasyon, başlangıç ayarları (tahmin: `/init`, izinler).
2. **Terminal okuryazarlığı** (18:00): temel komutlar, VS Code/Antigravity terminal terminolojisi.
3. **Proje beyni: CLAUDE.md + hafıza** (21:40, 13 dk — kursun en uzun kavramsal bölümü): projeye özgü kurallar, komutlar, mimari; hafıza yönetimi (tahmin: `/memory`, `#` ile hızlı not, `@import`).
4. **İlk proje: web uygulaması** (34:58): tek prompt'tan başlayıp iteratif geliştirme; 15 dk altında çalışan sonuç.
5. **Verimli kodlama özellikleri** (48:24): slash komutları, `/clear`-`/compact`, plan mode (tahmin), kısayollar.
6. **Aksiyon ve debugging** (53:57): hata çıktısını yapıştır → kök neden → doğrula.
7. **Skills** (1:01:46): `.claude/skills/*/SKILL.md` ile yeni yetenekler; tekrar eden işleri skill'e çevir.
8. **MCP** (1:08:50): dış araç/servis entegrasyonu (`claude mcp add`).
9. **Chrome'da Claude Code — arayüz testleri** (1:15:52): tarayıcıda gerçek UI'yi test ettirme (Claude in Chrome / Chrome MCP) → görsel doğrulama döngüsü.
10. **Sub-agent ve ajan takımları** (1:21:25, 11 dk): paralel alt ajanlar, `.claude/agents/`, agent teams (tahmin: `CLAUDE_CODE_EXPERIMENTAL_AGENT_TEAMS=1`).
11. **Git worktrees** (1:32:09): paralel instance'lar için izole çalışma ağaçları, saatlik işi dakikalara indirme.
12. **Plugin/extension** (1:39:04): `/plugin` marketplace.
13. **Deployment** (1:48:09): Modal gibi servislerle buluta alma, 7/24 çalışma.
14. **Özet ve vizyon** (2:00:15).

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- Açıklamadan aynen: "Projenizin 'beyni' olan CLAUDE.md dosyasını nasıl kullanacağınızı", "Antigravity içerisinde ilk projenizi 15 dakikanın altında nasıl inşa edeceğinizi", "birden fazla Claude Code örneği (instance) başlatıp bunları sizin adınıza çalıştırmayı, alt ajanlar (sub-agents) kullanarak işleri paralelleştirmeyi, Git worktree'leri ile saatler sürecek işleri dakikalar içinde bitirmeyi", "Token tasarrufu, bağlam (context) yönetimi".
- Prompt/CLAUDE.md metinleri videoda; transkript olmadan aynen alınamadı. Kursun dosya paketi: https://benburhan.gumroad.com/l/bsrxgi (ücretsiz, e-posta ile) → transkript geldiğinde buradan CLAUDE.md/skill örnekleri `kaynaklar/`a eklenmeli.

## Araçlar ve linkler
- Claude Code, Antigravity (Google IDE), VS Code terminali, Claude in Chrome, MCP, Skills, Sub-agents/Agent Teams, Git worktrees, Plugins, Modal (deploy), n8n (otomasyon, affiliate), Instantly (e-posta, affiliate)
- Diğer videoları: N8N & Claude Code 8 saat (OAt1TYTvSoc), Yapay Zeka Ajanları Kur ve Sat (bK54n3RZD5c), 3+ saatlik kurs (Xu2SIKz8B58)
- Topluluk: skool.com/doa (ücretli), skool.com/doa-zero (ücretsiz)

## Hatalar / "bunu yapma" uyarıları
- (Açıklamadan) token israfı ve bağlam şişmesi ana sorun olarak işleniyor → `/clear`, kısa CLAUDE.md, alt ajanla araştırma.
- (tahmin) UI'yi gözle test etmeden teslim etme → Chrome ile gerçek tarayıcı testi bölümünün varlık nedeni.
- Ayrıntılı uyarılar transkript gelince eklenecek.

## Bizim fabrikaya alınacaklar (somut)
1. **Türkçe onboarding kursu olarak** ekibe/yeni sohbetlere referans: bölüm 4 (CLAUDE.md), 8 (Skills), 10 (Chrome testi), 11 (sub-agent/teams), 12 (worktrees) zaman damgalarıyla `sistem/EGITIM.md`'ye link.
2. **Chrome UI test döngüsü** web hattına: her sayfa tesliminde Claude in Chrome ile gerçek tarayıcıda gez → ekran görüntüsü → farkları düzelt (best-practices "screenshot & compare" kuralıyla birleşir).
3. **Worktree başına müşteri**: paralel müşteri işleri `git worktree add ../musteri-x` ile ayrı Claude instance'larında; ana beyin dağıtır.
4. **Deploy standardı**: statik siteler Vercel/Cloudflare, uzun süreli ajanlar/otomasyonlar Modal veya benzeri (kursun önerisi) — `sistem/deploy/` içinde tek skill.
5. **"Kur ve sat" içeriği**: kursun ajans/satış bölümleri bizim fiyatlandırma ve paket tasarımı için ikinci kaynak (3 saatlik sürümde daha ayrıntılı, bkz. Xu2SIKz8B58 planı).
6. Gumroad dosya paketi indirildiğinde: CLAUDE.md ve skill şablonları → `kaynaklar/qm0ZlYezt1Q-dosya-paketi.md`, bizim şablonla karşılaştır.
