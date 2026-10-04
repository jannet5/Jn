# Harita: Company OS ajan koordinasyon sistemi

Araştırma tarihi 2026-10-03. Yıldız sayıları aynı gün GitHub arama API'sinden alındı ve yuvarlatıldı. "(okundu)" işaretli sayfalar açılıp okundu. "(aramada görüldü)" işaretlilerin yalnız arama sonucu görüldü. Lisanslar, ayrıca belirtilmedikçe doğrulanmadı.

## 1. Hedef (kaynaktan)
Kullanıcının istediği sistemde bir iş verildiğinde yapay zekâ şunları yapmalı:
1. İşi **uzman ajanlara bölüp dağıtmalı**, ajanlar kendi aralarında haberleşip işi bitirmeli.
2. Bilgiyi **skills** kaynağından çekmeli.
3. Bir şey yaptırması gerekince bunu **MCP** üzerinden yaptırıp sonucu geri getirmeli.
4. Bunun için **hazır GitHub depoları** bulunup değerlendirilmeli.

Kaynağın sonundaki denetim raporu mevcut Company OS kodunda (`ledger.py`, `qa_runner.py`, `artifacts.py`) **beş P1 doğruluk hatası** bildiriyor. Görev bu bulguların da doğrulanmasını ve kapatılmasını istiyor.

## 2. Bağımlılıklar
| Bağımlılık | Durum | Not |
|---|---|---|
| Mevcut Company OS kaynak kodu (`company-os-native-v1-1935`) | **YOK**, yüklenmedi | Windows yolu bulut konteynerinden okunamaz. Bulgular o kod üzerinde yeniden çalıştırılamadı. Bkz. `denetim/bulgu-dogrulama.md`. |
| Kaynakta geçen TXT belgeleri | **YOK** | İlk mesajdaki "path üstündeki txt dosyaları" yüklenmedi. |
| Claude Code CLI | Var (`2.1.288`) | Başsız mod ve MCP sağlık kontrolü çalıştırıldı. |
| Python | 3.11 (hedef ≥3.10) | Yalnız standart kütüphane kullanıldı. |
| Resmi MCP Python SDK | `mcp 1.30.0`, yalnız test için | Birlikte çalışma kanıtı. Ürüne bağımlılık olarak eklenmedi. |

## 3. Hazır çözümler (karşılaştırma)

### 3a. Resmi yapı taşları
| Kaynak | Ne sağlar | Uygunluk |
|---|---|---|
| Claude Code alt ajanları: https://code.claude.com/docs/en/sub-agents (okundu) | `.claude/agents/*.md` dosyaları. Ön bilgide `tools`, `model`, `skills`, `mcpServers`, `isolation: worktree` alanları. İç içe derinlik varsayılanı 3, eş zamanlı sınır 20. | **Yerel (native) dağıtım katmanı.** Lease veya kanıt yok. |
| Claude Code Skills: https://code.claude.com/docs/en/skills (okundu) | `.claude/skills/<ad>/SKILL.md`. Aşamalı açıklama: önce yalnız açıklama yüklenir, gövde çağrılınca. | **Bilgi kaynağı katmanı.** |
| https://github.com/anthropics/skills (~179k★; README okundu) | Resmi skill örnekleri ve spec bağlantısı (agentskills.io, okunmadı). Çoğu Apache-2.0; docx/pdf/pptx/xlsx yalnız kaynak erişimli. | İçerik kaynağı. |
| MCP spesifikasyonu: https://modelcontextprotocol.io/specification/latest | Güncel sürüm **2026-07-28**. Araçlar: https://modelcontextprotocol.io/specification/2026-07-28/server/tools (okundu). Taşıma: https://modelcontextprotocol.io/specification/2026-07-28/basic/transports (okundu). stdio hâlâ standart. İstek başına durumsuz yapı geldi; `initialize` yalnız geri uyumluluk için kaldı. | **Araç çağırma katmanı.** |
| MCP Python SDK: https://github.com/modelcontextprotocol/python-sdk (~24.5k★) | Resmi istemci ve sunucu. | Birlikte çalışma testi için kullanıldı. |
| Claude Agent SDK: https://code.claude.com/docs/en/agent-sdk/overview (okundu); https://github.com/anthropics/claude-agent-sdk-python | Claude Code döngüsü kütüphane olarak. | Kendi orkestratörünü yazmanın resmi yolu. Ticari koşullara tabi. |
| Resmi kullanım rehberi: https://claude.com/blog/how-and-when-to-use-subagents-in-claude-code (okundu) | Alt ajan önerilen durumlar: ≥10 dosya keşfi ya da ≥3 bağımsız parça. Sıralı işte, aynı dosya düzenlemesinde ve sıkı ajan-ajan koordinasyonunda önerilmiyor. | Plan kuralı olarak skill'e işlendi. |

### 3b. Topluluk depoları (hepsinin var olduğu doğrulandı)
| Depo | Ne | Değerlendirme |
|---|---|---|
| https://github.com/obra/superpowers (~295k★) | Skill çerçevesi: alt ajan güdümlü geliştirme (beyin fırtınası → plan → TDD → inceleme) | En güçlü iş akışı ve skill paketi. **Bu sistemle birlikte kullanılabilir:** skill'leri `--skills` dizinine eklenir. |
| https://github.com/wshobson/agents (~40k★) | Alt ajan ve skill eklenti pazarı | Rol ajanları için içerik kaynağı. |
| https://github.com/VoltAgent/awesome-claude-code-subagents (~25k★) | 100+ alt ajan `.md` dosyası | Kopyalanarak kullanılır. Roller genişletilebilir. |
| https://github.com/hesreallyhim/awesome-claude-code (~55k★) | Derlenmiş liste | Keşif için. |
| https://github.com/ruvnet/claude-flow → **ruvnet/ruflo** (~74k★, MIT, okundu) | Sürü orkestrasyonu, MCP sunucusu, bellek | Ağır yapı, çok parçalı, ~1.054 açık issue. Yerel ve minimal hedefe uymuyor. |
| https://github.com/bmad-code-org/BMAD-METHOD (~54k★) | Çevik, spec güdümlü roller (PM, mimar, dev, QA) | Rol tasarımı için ilham. |
| https://github.com/SuperClaude-Org/SuperClaude_Framework (~24k★) | Persona ve komutlar | İçerik. |
| https://github.com/contains-studio/agents (~12k★) | Alt ajan seti | İçerik. |
| https://github.com/Yeachan-Heo/oh-my-claudecode (~40k★), https://github.com/gotalab/cc-sdd (~3.7k★) | Takım ve spec güdümlü orkestrasyon | Aynı sınıf. |

**Ortak sonuç:** Bu depoların hiçbiri lease, fencing token ya da kanıt bağlama sağlamıyor. Hepsi istem ve yapılandırma paketi; yürütme motoru değiller. Denetimdeki beş hatayı hiçbiri çözmüyor.

### 3c. Genel çok ajanlı çatılar
LangGraph https://github.com/langchain-ai/langgraph (~43k★, checkpoint'li grafik durumu), CrewAI https://github.com/crewAIInc/crewAI (~59k★), AutoGen https://github.com/microsoft/autogen (~61k★), Microsoft Agent Framework https://github.com/microsoft/agent-framework (~14k★), OpenAI Agents SDK https://github.com/openai/openai-agents-python (~30k★). Hiçbiri Claude Code'a yerel olarak bağlanmıyor ve hiçbirinde hazır fencing token yok.

### 3d. Topluluk ve kullanıcı deneyimi
- Anthropic çok ajanlı araştırma sistemi: https://www.anthropic.com/engineering/multi-agent-research-system. Tek ajana göre +%90,2 başarı, buna karşılık sohbete göre ~15× token. Kazanç geniş, okuma ağırlıklı işte.
- "Don't Build Multi-Agents" (Cognition) tartışması: https://patmcguinness.substack.com/p/the-ai-agent-architecture-debate (aramada görüldü; Cognition'ın özgün yazısı açılmadı). Bölünen bağlam yanlış anlaşılmaya yol açıyor; yazma işleri tek iş parçacığında tutulmalı.
- https://mcp.directory/blog/claude-code-parallel-subagents-workflows-2026 (okundu). Dağıtma denetim, araştırma ve göçte kazandırıyor. Sıkı bağlı düzenlemelerde birleştirme maliyeti kazancı aşıyor.
- https://www.mindstudio.ai/blog/claude-code-subagents-cost-tokens (aramada görüldü). Alt ajanlar bağlamı yeniden yüklüyor, takımlar ~3–7× token harcıyor, küçük işlerde başlatma yükü kazancı yiyor.
- Reddit'te kanonik bir başlık doğrulanamadı; aramada yalnız ayna siteler çıktı.

**Ders:** Ajanlar araştırma, inceleme ve bağımsız parçalar için kullanılmalı. Ortak duruma paralel yazma, ancak tek yazar garantisi (lease ve fencing) ile güvenli olur. Bu sistem tam bu boşluğu dolduruyor.

### 3e. Doğruluk kaynakları
- Kleppmann, "How to do distributed locking": https://martin.kleppmann.com/2016/02/08/how-to-do-distributed-locking.html (okundu). Monoton fencing token, depolama tarafında koşullu yazma.
- SQLite işlemleri: https://www.sqlite.org/lang_transaction.html (okundu). `BEGIN IMMEDIATE` yazma kilidini işlemin başında alır; BUSY yalnız BEGIN'de oluşur.
- Python `os`: https://docs.python.org/3/library/os.html (okundu). `O_NOFOLLOW`, `O_EXCL`, `dir_fd`, `supports_dir_fd`.
- CERT FIO45-C TOCTOU: https://wiki.sei.cmu.edu/confluence/spaces/c/pages/87151941/FIO45-C.+Avoid+TOCTOU+race+conditions+while+accessing+files (aramada görüldü).
- openat2 `RESOLVE_BENEATH`: https://manual.cs50.io/2/openat2 (aramada görüldü).
- Güncel örnek, rsync symlink TOCTOU düzeltmesi (CVE-2026-29518): https://git.almalinux.org/rpms/rsync/commit/cc69bbdd82a094cf40114d5b21f1bd6a9e6e6613 (aramada görüldü).

## 4. A/B/C yolları

| | A: Yerel ince çekirdek + Claude Code yerel katmanları (**SEÇİLDİ**) | B: Topluluk orkestratörü (ruflo/claude-flow) | C: Genel çatı (LangGraph veya CrewAI) |
|---|---|---|---|
| Dağıtım | Claude Code alt ajanları (kit) ve kendi worker havuzu | Sürü motoru | Grafik veya ekip |
| Skills | `SKILL.md` standardı, `skills_list`/`skill_read` | Kendi biçimi ile kısmi | Yok, uyarlama gerekir |
| MCP | Kendi stdio sunucusu ve istemcisi | Var, ama ağır | Adaptör gerekir |
| Lease, fencing, kanıt | **Var**, beş bulgu kapatıldı | Yok | Yok (checkpoint var, fencing yok) |
| Yerel ve çevrimdışı | Evet, yalnız stdlib, sunucusuz | Node, çok bağımlılık | Python, çok bağımlılık |
| Risk | Bakım bizde | Büyük yüzey, açık issue yükü | Claude Code'dan kopuk |

**Karar gerekçesi:** Kaynaktaki ürün, Claude Code'u "kıdemli" yapan yerel bir Company OS. Mevcut kod zaten Python + SQLite defteri (`ledger.py`, `qa_runner.py`). Asıl eksik doğruluk; hiçbir hazır depo bunu sağlamıyor. A yolu ürünün mevcut tercihlerini koruyor (yerel, Python, SQLite), sunucu eklemiyor, Claude Code'un resmi katmanlarını (alt ajan, skill, MCP) kullanıyor. Topluluk içerik paketleri (superpowers, wshobson/agents, VoltAgent) A'nın üzerine **içerik olarak** eklenebiliyor; skill dizini ve `.claude/agents` ile uyumlu.

**Kırılma planı:**
- A'da Claude Code proje MCP onayı başsız verilemiyor. Çözüm olarak `--mcp-config` + `--strict-mcp-config` ya da yerel kapsam kaydı kullanılıyor; ikisi de uygulandı ve çalıştı.
- MCP 2026-07-28 durumsuz istemciler `initialize` göndermezse sunucu yine de `tools/list` ve `tools/call` yanıtlıyor; bu test edildi.
- Ölçek tek makineyi aşarsa C yoluna (LangGraph) geçilir; defter tasarımı (token ve koşullu güncelleme) Postgres'e taşınabilir.

## 5. Bağlı zincir
```
Hedef (kaynak satır 7) ─┬─► Araştırma (§3) ─► Karar A (§4)
                        │
Denetim (kaynak satır 14) ─► 5 bulgu ─► davranışsal testler (tests/test_bulgular.py)
                                              │
Uygulama: src/company_os ◄────────────────────┘
  ledger.py (lease + fencing + koşullu reconcile) ── bulgu 1, 2, 3
  qa.py (snapshot'a bağlı kabul kapısı) ─────────── bulgu 4
  artifacts.py (dir_fd zinciri, O_EXCL|O_NOFOLLOW) ─ bulgu 5
  orchestrator.py (dağıtım, girdi aktarımı, mesaj, toplama)
  skills.py (SKILL.md) · mcp_server.py / mcp_client.py (MCP) · kit/ (Claude Code alt ajanları + skill)
        │
Test/build ─► 37 birim testi · mutasyon kontrolü (5/5 yakalandı) · örnek plan uçtan uca
              · resmi MCP SDK birlikte çalışma · gerçek Claude Code turu (cos-* alt ajanları)
        │
Kalıcı teslim ─► jannet5/Jn dalı claude/determined-mendel-dhyjj8 (push + geri okuma)
               ─► özel ZIP (kaynak metni içermez) + SHA-256
```

---

# 2. aşama haritası: mevcut ürün Codex-yerel (2026-10-03 güncellemesi)

Sağlanan gerçek kaynak, ürünün **Codex-yerel** olduğunu gösterdi:
- Codex CLI 0.154.0 App Server, `.agents/skills`, `AGENTS.md`, `.codex/config.toml`,
- SQLite WAL defteri, varsayılan olarak her şeyi reddeden stdio MCP köprüsü,
- Windows/PowerShell, yerel ve çevrimdışı V1 kabul profili (yerel kuaför sitesi).

1. aşamadaki Claude Code referans paketi bu ürünün yerine geçmez. Kaynaktaki ürün kararı (Codex-yerel, çevrimdışı V1) korunur.

## Güncel kaynaklar (okundu = sayfa açıldı)
| Konu | Kaynak | Mevcut ürün için anlamı |
|---|---|---|
| App Server protokolü | https://learn.chatgpt.com/docs/app-server (okundu) | stdio üzerinde satır ayrımlı JSON-RPC; `thread/*`, `turn/*` ve `turn/completed` durumları. `codex app-server generate-json-schema` ile şema sürüme sabitlenebilir. |
| Skills | https://learn.chatgpt.com/docs/build-skills (okundu) | `.agents/skills` repo kökünden yukarı doğru taranıyor; `name` ve `description` zorunlu. Ürünün düzeni uyumlu. |
| AGENTS.md | https://learn.chatgpt.com/docs/agent-configuration/agents-md (okundu) | Git kökünden çalışma dizinine doğru birleştiriliyor; varsayılan sınır 32 KiB. |
| Yapılandırma | https://learn.chatgpt.com/docs/config-file/config-reference (okundu) | `approval_policy = "never"` ve `sandbox_mode = "read-only"` geçerli. `"untrusted"` artık desteklenmiyor; ürün onu kullanmıyor. `[agents] max_concurrent_threads_per_session` geçerli anahtar. |
| Alt ajanlar | https://learn.chatgpt.com/docs/agent-configuration/subagents (okundu) | Roller `.codex/agents/*.toml` dosyalarında; ebeveynin sandbox ve onay ayarları devralınıyor. Etkileşimsiz akışta onay gösterilemezse akış başarısız oluyor; `never` ayarı bu riski azaltıyor. |
| Codex MCP | https://learn.chatgpt.com/docs/extend/mcp?surface=cli (okundu) | `enabled_tools` ve `disabled_tools` ile süzme yapılıyor. Ürünün kendi izin listesiyle örtüşüyor. |
| Sürümler | https://github.com/openai/codex/releases (okundu); https://github.com/openai/codex/releases/tag/rust-v0.160.0 (okundu) | Güncel kararlı sürüm 0.160.0. Bu sürüm Windows sandbox, PowerShell, uzun yol ve SQLite bağlantı takılması düzeltmeleri içeriyor. 0.154.0'dan yükseltmeden önce şema ve yapılandırma farkı kontrol edilmeli. |
| Topluluk deneyimi | https://github.com/openai/codex/issues/19197 (okundu): yetim alt ajanlar eş zamanlılık sınırını tüketiyor · https://github.com/openai/codex/issues/25779 (okundu): Windows'ta büyüyen thread durumu, durdurulamayan turn · https://github.com/openai/codex/issues/23712 (okundu): Windows'ta `unelevated` sandbox modunda tekrar tekrar gelen yükseltme istemi · https://community.openai.com/t/local-skills-in-agents-skills-are-no-longer-discovered-in-new-codex-sessions/1379522 (okundu): skill keşfi gerilemesi | Ürünün "durum otoritesi defterdir, thread yalnız bağlamdır" kararını destekliyor. Öneriler: skill keşfi için bir duman testi; turn kimliklerini defterde tutmak. |
| Windows dosya güvenliği | https://learn.microsoft.com/en-us/windows/win32/api/fileapi/nf-fileapi-createfilew (okundu) · https://learn.microsoft.com/en-us/windows/win32/api/winbase/nf-winbase-movefileexw (okundu) · https://learn.microsoft.com/en-us/windows/win32/api/winbase/ns-winbase-file_rename_info (okundu) | `FILE_SHARE_DELETE` olmadan açılan tutamak, başkasının silme erişimini, dolayısıyla yeniden adlandırmayı engelliyor. Ürünün Windows dalı bu ilkeye dayanıyor. **Bu ortamda Windows'ta test edilemedi.** |
| Lease doğruluğu | https://martin.kleppmann.com/2016/02/08/how-to-do-distributed-locking.html · https://www.sqlite.org/lang_transaction.html · https://www.sqlite.org/wal.html · https://docs.python.org/3.11/library/sqlite3.html (hepsi okundu) | `(owner, attempt)` fence ve `BEGIN IMMEDIATE` doğru desen. **Tuzak:** varsayılan `isolation_level` ile açık bir örtük işlem varken gönderilen `BEGIN IMMEDIATE` hata veriyor. Mevcut kodda tetiklenmiyor (42 test ve prob temiz). Uzun vadeli öneri: `isolation_level=None` ile açık işlem yönetimi. |
| MCP güvenliği | https://modelcontextprotocol.io/specification/2025-11-25/basic/security_best_practices (okundu) · https://modelcontextprotocol.io/specification/versioning (okundu) | Varsayılan red, stdio ve token aktarımının olmaması önerilerle uyumlu. Güncel spec 2026-07-28; ürünün köprüsü `2024-11-05` bildiriyor ve geri uyumluluk kapsamında çalışıyor. |

## A/B/C (mevcut ürün için)
| | A: Mevcut ürünü yerinde onarmak (**SEÇİLDİ**) | B: 1. aşama referans paketine taşımak | C: LangGraph veya Agents SDK ile yeniden kurmak |
|---|---|---|---|
| Kapsam | V1 profili, Codex-yerel ve çevrimdışı yapı korunur | Codex'ten Claude'a geçiş ve ürün değişikliği gerekir | ADR 002 bunu ölçülmüş bir ihtiyaç olmadan erteliyor |
| Risk | En düşük: 3 dosya, +161/−1 satır, mevcut 37 test korunuyor | Yüksek: davranış sözleşmeleri değişir | Yüksek |
| Gerekçe | Bulgu 1–4 zaten kapalıydı; yalnız POSIX bulgu 5 ve yeni 3b gerekiyordu | — | — |

## Bağlı zincir
```
Kaynak ZIP (hash ✓) → kod okuma → mevcut testler (37 OK)
   → düşmanca prob (19 senaryo: B1–B4 kapalı, POSIX B5 / 3b / 5b açık)
   → A: yerinde onarım (ledger.reconcile, artifacts POSIX tutamak zinciri) + 5 regresyon testi
   → yeni testler orijinalde 2 FAIL, onarımda 42 OK · prob: 1 açık (5b, bilinçli olarak bırakıldı)
   → yama temiz kaynağa git am ile uygulandı ✓
   → özel ZIP (kaynak, yama, kanıt, bundle) + SHA-256 + geri okuma
   ✗ engelli: Codex hesabıyla uçtan uca çalıştırma · Windows testleri · eksik dosyalar (salon.jpg, önceki run çıktısı, .git)
```
Yol kırılırsa: Windows dalı Windows'ta başarısız olursa, araştırmadaki `FileRenameInfo` ile tutamak tabanlı yeniden adlandırma alternatifine geçilir.

---

# 5. aşama: Windows kanıtı ve kalan kabul yolu (2026-10-04)

## Ayrı kanıt düğümü: Windows fixture deltası (kullanıcının çalıştırması)
- **Tarih ve yer:** 2026-10-03, kullanıcının bilgisayarında izole bir kopya. Kaynak aslı değişmedi. Yalnız düz metin test deltası uygulandı; tam ZIP indirilmedi.
- **Test dosyası:** SHA-256 `716101480350538663e5a51bdd08188e52556c7173d7f6ed4c7dc76a74dd3143`.
- **Sonuç:** `unittest` 49 test: **46 geçti, 3 atlandı, 0 hata, exit 0**. `tools/check.py` exit 0.
- **Sonuç JSON'u:** SHA-256 `394bd38b7a6486bd67c69f837a6a1833ae28ffe892f21e50b1ce59b5612761bb`. Kullanıcı 2026-10-04'te geri okuduğunu bildirdi; bu ortamda yeniden üretilmedi.
- **Kapsam dışı:** Bu kanıt gerçek NTFS junction/reparse point testini ve Codex akışını **kapsamaz**.

## Bulut ortamında yapılan bağımsız adımlar
1. **Codex'siz güvenli başarısızlık kabulü (K1–K9):**
   - `.git` olmadan intake iş oluşturmuyor.
   - Sentetik, tek kullanımlık bir git kökünde `run` Codex yokluğunda görünür biçimde başarısız oluyor.
   - `resume` ve `accept` reddediliyor; `repair` deneme bütçesiyle sınırlı.
   - `package` dürüst rapor veriyor: native_app_server=False, artifacts=0, başarılı geçiş 0.
   - Bu **gerçek uçtan uca kabul değil**; yalnız önkoşullar eksikken sahte başarı olmadığını kanıtlar.
2. **NTFS junction testi eklendi** (yalnız Windows; `mklink /J` ayrıcalık gerektirmez). Windows'ta **çalıştırılmadı**.

## Kalan kabul zinciri (en küçük yol, kullanıcı makinesinde)
```
W0 junction test deltası → unittest (token yok)
W1 Codex kimliği doğrulanmış → probe (handshake, token yok)
W2 gerçek .git → intake
W3 → run --stop-after design (1 Codex turn)
W4 salon.jpg → resume --stop-after implement
W5 Node + Playwright → resume (QA + review → awaiting_human)
W6 package → yalnız insan incelemesinden sonra accept --human-accepted
```
Her adım, bir önceki adımın kanıtı olmadan başlatılmamalı. Hiçbir önkoşul varmış gibi sayılmadı.

---

# 6. aşama: NTFS junction Windows kabulü (2026-10-04)

## Kanıt düğümü W0: tamamlandı (koordinatörün bağımsız Windows çalıştırması)
- **Yer ve tarih:** 2026-10-04, korunmuş başlangıçtan alınmış yeni izole kopya. Native Windows, Python 3.11.
- **Uygulanan delta:** 1983 bayt. Delta SHA-256 `9ab84ae76680ee42e6e5d1f096ad277caa76f15acabad8b4a014e83b42926b72`.
  - Bu değer, bulut ortamındaki `windows-ntfs-junction-delta.patch` dosyasının SHA'sıyla **birebir aynı**; bulutta yeniden hesaplanıp karşılaştırıldı.
- **Değişen tek dosya:** `tests/test_audit_regressions.py`, `71610148…3143` → `6b6292b5…6ada`. Başlangıç kaynak hash'leri değişmedi.
- **Sonuç:** `unittest discover` exit 0; **50 test: 48 geçti, 2 atlandı (POSIX), 0 hata.**
  - `test_guarded_sweep_leaves_ntfs_junction` gerçek `mklink /J` ile **geçti**.
  - `test_guarded_sweep_leaves_planted_symlink` bu ortamda **geçti**.
- **Yerel dosyalar:** Sonuç JSON SHA `a59ae931f34ab837925c56ff0550fe72c7fb30ee4744d5c41b6c6d38ecc9c5da`, unittest log SHA `18463c576100d04f89b0fb0ffe85fcfd96fd5c2c138918c39aa7a35a588fb651`. Bu dosyalar koordinatörün makinesinde; bulut ortamında **yok** ve yeniden üretilmedi.
- **Beklenti ile gözlem:** Beklenti 47 geçer / 3 atlanır idi; gözlenen 48 / 2. Fark, symlink alt testinin bu ortamda symlink oluşturabilmesinden kaynaklanıyor. Ortamın neden symlink oluşturabildiği **bilinmiyor**; tahmin yapılmadı.
- **Tarihsel kayıt (değiştirilmedi):** 2026-10-03 fixture deltası 49 test: 46 geçti, 3 atlandı (symlink ayrıcalığı yoktu).

## Güncel kabul durumu
| Düğüm | Durum |
|---|---|
| Denetim bulguları 1–5, 3b, 5b (Linux) | Kapalı: testler, 20/20 prob, 8/8 negatif kontrol |
| Windows test paketi (W0) | **Geçti:** 50 / 48 / 2 / 0 |
| Gerçek NTFS junction | **Geçti** (W0 içinde) |
| Codex'siz güvenli başarısızlık (K1–K9) | Geçti: sahte başarı yok (bulut ortamı, sentetik git kökü) |
| W1 Codex `probe` | **Yapılmadı:** kimliği doğrulanmış Codex gerekiyor |
| W2 gerçek `.git` ile intake | **Yapılmadı:** projenin gerçek `.git` geçmişi gerekiyor |
| W3–W5 design, implement, QA, review | **Yapılmadı:** Codex, `salon.jpg`, Node ve Playwright gerekiyor |
| W6 package ve insan kabulü | **Yapılmadı:** W5 olmadan anlamsız; sahte insan kabulü yapılmayacak |
| Yeni tam ZIP'in indirilmesi | **Yapılmadı:** tarayıcı indirmesi engellendi |

**Uygulanabilir sıradaki adım: W1.** Kimliği doğrulanmış Codex CLI bulunan Windows makinesinde, aynı izole kopyada `$env:PYTHONPATH='src'; python -m company_os.cli probe` çalıştırılır. Beklenen kanıt `runs/capabilities.json` içinde `app_server_handshake_verified`. Bu adım token harcamaz. Sonuç `unavailable_in_current_runtime` olursa W2'ye geçilmez.
