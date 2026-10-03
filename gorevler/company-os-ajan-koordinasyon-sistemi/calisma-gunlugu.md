# Çalışma günlüğü: Company OS ajan koordinasyon sistemi

Tarih: 2026-10-03. Ortam: Claude Code bulut oturumu, Linux konteyneri, Python 3.11.15, Claude Code CLI 2.1.288. Çalışılan dal: `claude/determined-mendel-dhyjj8` (jannet5/Jn). Çalışma `gorevler/company-os-ajan-koordinasyon-sistemi/` altında izole tutuldu. Depodaki mevcut projelere (`android-app`, `windows-agent`, `docs`) dokunulmadı.

## 1. İstenen
Kaynak sohbet ve görev özeti şunları istiyor:
- **(a)** Hazır depoları araştırıp karşılaştırmak.
- **(b)** Ajanlar arasında iş dağıtımı ve sonuç toplama kurmak; skills ve MCP kullanmak.
- **(c)** Denetimdeki lease sahipliği, eski worker ve artifact kanıtı sorunlarını doğrulamak.

Teslim için gerçek test ve kabul kanıtı, kalıcı push, kaynak metni içermeyen özel bir ZIP ve SHA-256 isteniyor.

**Gizlilik:** Kaynak sohbet ve görev dosyası yalnız oturumun geçici çalışma alanına (scratchpad, `ozel-kaynak/`) kaydedildi. Public depoya ve ZIP'e konmadı. Denetim raporu belgelerde yalnız kendi cümlelerimle özetlendi; yerel Windows yolları ve kullanıcı adı yazılmadı.

## 2. Araştırma
- Bir araştırma alt ajanı WebSearch ve WebFetch ile resmi belgeleri, topluluk depolarını, kullanıcı deneyimlerini ve doğruluk kaynaklarını taradı. Sonuç ve tam HTTPS bağlantıları `arastirma/harita.md` içinde.
- Önemli bulgu: MCP spesifikasyonunun güncel sürümü 2026-07-28. Bu sürümde istek başına durumsuz yapı var ve `initialize` yalnız geri uyumluluk için duruyor. Bu yüzden sunucu, el sıkışma olmadan gelen `tools/list` ve `tools/call` isteklerine de yanıt veriyor.
- Karar: **A yolu**, yani yerel ince çekirdek ve Claude Code'un yerel katmanları. Gerekçe: hiçbir hazır depo lease, fencing ya da kanıt doğruluğu sağlamıyor; mevcut ürün de zaten Python + SQLite.

## 3. Uygulama (sırayla)
1. `artifacts.py`: dir_fd zinciri ile güvenli yazma, Windows için taşınabilir yol.
2. `ledger.py`: şema, `BEGIN IMMEDIATE`, `claim`, fencing, koşullu `reconcile`, kanıt çalıştırma.
3. `qa.py`: snapshot'a bağlı kabul kapısı.
4. `skills.py`, `mcp_client.py`, `mcp_server.py`, `orchestrator.py`, `cli.py`.
5. Claude Code kiti: 4 alt ajan ve orkestrasyon skill'i.
6. Örnek plan: 5 uzman görev (araştırmacı, entegrasyon/MCP, inceleyici/skill, operasyon/yeniden deneme, yazar/toplama).

## 4. Sorunlar ve çözümleri
| Sorun | Neden | Çözüm |
|---|---|---|
| Güvenli yazma yolu Linux'ta devreye girmedi (`SECURE_DIR_FD=False`) | `os.replace`, `os.supports_dir_fd` kümesinde listelenmiyor (yalnız `os.rename` var); aynı `renameat` çağrısını kullanıyor | Algılama `os.rename` üzerinden yapıldı. Yan bulgu: taşınabilir yol bu sayede tüm testlerden geçmiş oldu |
| `stale_write_rejected` olayı kaydedilmiyordu | Olay, geri alınan (ROLLBACK) işlemin içinde yazılıyordu | Reddedilen yazma bilgisi istisnaya eklendi; olay geri almadan sonra ayrı işlemde yazılıyor |
| Yarış testinde rakip bağlantının hiç beklemediği görüldü | Ölçüm, bağlantı kurulumundan sonra başlıyordu; bekleme kurucu içinde geçiyordu | Ölçüm kurucudan önce başlatıldı. Rakip 0,5 sn kilit bekliyor |
| Örnek planda görevler kabul kapısından geçmedi | Yürütücü günlüğü artifact olarak kaydediliyordu ve hiçbir kanıt onu kapsamıyordu | Günlük, snapshot dışında tanı dosyası olarak tutuluyor. Kapı doğru davranmıştı |
| Claude Code proje `.mcp.json` için "Pending approval" gösterdi | Proje MCP sunucuları etkileşimli güven onayı istiyor | Başsız modda `--mcp-config --strict-mcp-config` ve yerel kapsam kaydı kullanıldı; ikisi de bağlandı. Kullanıcı ilk `claude` açılışında onaylamalı |

## 5. Doğrulamalar (gerçek komutlar)
| Komut | Sonuç | Kayıt |
|---|---|---|
| `python -m unittest discover -s tests -v` | 37 test, OK (1 atlama: yarış testi taşınabilir yolda geçerli değil) | `kabul/01-birim-testleri.txt` |
| `python -m company_os run ornekler/plan-ornek.json --workers 3 --out … --report …` | 5/5 succeeded, 3 worker'a dağıtım, `kararlilik` 2. denemede tamamlandı, MCP çağrısı ve skill seçimi gerçek, mesajlar rapora aktarıldı | `kabul/02-*` |
| Resmi MCP Python SDK 1.30.0 istemcisi | protocolVersion 2025-11-25 müzakere edildi, 15 araç, kanıt 0, tamamlandı, eski token reddedildi | `kabul/03-resmi-sdk.txt` |
| `python kabul/mutasyon_kontrolu.py` | 5 hatanın her biri geri enjekte edildi ve 5/5 yakalandı | `kabul/04-mutasyon-kontrolu.txt` |
| `claude mcp add-json … -s local` ardından `claude mcp list` | `√ Connected` | `kabul/05-*` |
| `claude -p … --mcp-config .mcp.json` | `cos-uygulayici` ve `cos-qa` alt ajanları çağrıldı; defterde 2/2 succeeded; disk SHA-256 değerleri raporla eşleşti | `kabul/05-*` |

## 6. Yapılamayanlar (somut engel ve gereken girdi)
- **Mevcut Company OS kodu yüklenmedi.** Bulgular o kod üzerinde yeniden çalıştırılamadı. Gereken girdi: `company-os-native-v1-*` kaynak klasörü. Taşıma adımları `denetim/bulgu-dogrulama.md` içinde.
- **Kaynakta geçen TXT belgeleri yüklenmedi.** İçerikleri bilinmiyor ve uydurulmadı.
- **Windows ve telefon testi yapılmadı.** Windows'ta güvenli yazmanın kalan küçük yarış penceresi belgelendi.
- Model ve çaba seçimi: oturum `claude-opus-5-5` olarak yapılandırılmış. Arayüzdeki "medium" çaba seçimi buradan doğrulanamaz.

## 7. Kalıcı teslim
Push, ZIP ve SHA-256 adımları bu günlüğün sonundaki "Teslim kaydı" bölümüne eklenir.

## Teslim kaydı
- 1. push: `a7b32ff` → `origin/claude/determined-mendel-dhyjj8`. Geri okuma için uzak daldan temiz klon alındı; orada `unittest` 37 test OK (1 atlama), mutasyon kontrolü GEÇTİ.
- Özel ZIP: `company-os-teslim.zip`. Kaynak sohbet metnini ve görev dosyasını içermez. İçinde `company-os/` (ürün dosyaları) ve `company-os.bundle` (yalnız bu ürünü içeren, geçmişi temiz, depoya hazır git paketi) var. SHA-256 değeri ZIP üretildikten sonra aşağıya eklenir.
- ZIP SHA-256: `0b304f1ebb8501a5619ba7af4d51a72200f5a79f734f71a7625a35d91e92dd8e` (`company-os-teslim.zip`, 58 dosya; ürün `803366b` commit'inden üretildi). ZIP depoya konmadı, oturumun özel alanında tutuluyor.
- ZIP geri okuması yapıldı: `sha256sum -c` OK. ZIP açıldı. Bundle'dan klon alındı (`c628ec6`), klon içeriği ZIP'teki dosyalarla `diff -r` sonucu birebir aynı. Klonda testler OK. Kaynak metin ve özel yol taraması temiz.

---

# 2. aşama: gerçek Company OS kaynağıyla doğrulama ve onarım (2026-10-03)

## İstenen
Koordinatör, mevcut Company OS native V1 kaynağını Base64 ZIP olarak verdi: 43 dosya, 54028 bayt, SHA-256 `8e5b0645…14e9`. İstenenler:
- hash ve ZIP bütünlüğünü doğrulamak,
- bulguları gerçek kodda yeniden doğrulamak,
- gereken düzeltmeleri izole kopyaya taşımak,
- mevcut testleri ve anlamlı yeni kabul testlerini çalıştırmak,
- haritayı güncellemek,
- özel ZIP/bundle üretip hash'ini alıp geri okumak.

Sıfırdan yazılan 1. aşama kodu onarım sayılmayacak. Kaynak kod ve ham metin public depoya konmayacak.

## Yapılanlar (sırayla)
1. **Base64'ün çözülmesi:** Metin elle yeniden yazılmadı (hata riski). Oturum kaydındaki kullanıcı mesajından birebir çıkarıldı ve `base64.b64decode(validate=True)` ile çözüldü. Sonuç: 54028 bayt, SHA-256 eşleşti, `testzip()` temiz, 43 dosya. Dosya oturumun özel alanında tutuldu.
2. **Kaynağın okunması:** AGENTS.md, mimari, işletim belgeleri, ADR'ler, `ledger.py`, `qa_runner.py`, `artifacts.py`, `engine.py`, `policy.safe_path` ve mevcut test paketi okundu. Tespitler:
   - Ürün **Codex-yerel**: Codex App Server, `.agents/skills`, varsayılan olarak her şeyi reddeden MCP köprüsü, SQLite WAL, Windows/PowerShell.
   - Denetimdeki satır numaraları bu sürümle örtüşmüyor; düzeltmeler ve ilgili testler zaten var gibi görünüyor.
3. **Mevcut testler:** İzole kopyada çalıştırıldı (`git init` ile orijinal kayıt altına alındı). 37 test OK (1 Windows testi atlandı). `tools/check.py` exit 0.
4. **Düşmanca doğrulama:** `dogrulama_probe.py` yazıldı (özel pakette) ve orijinal koda karşı 19 senaryo çalıştırıldı. Sonuçlar:
   - Bulgu 1–4 zaten kapalı.
   - POSIX'te bulgu 5 açık.
   - Yeni 3b (son deneme çöküşünde kalıcı kilit) açık.
   - Yeni 5b (çökme artığı geçici dosya) açık.
5. **Onarım:**
   - `reconcile` içinde deneme hakkı bitmiş ve süresi dolmuş lease aynı işlemde `failed` yapılıyor.
   - POSIX yayını tutamak zinciriyle yapılıyor; Windows dalına dokunulmadı.
   - 5 yeni regresyon testi eklendi.
   - 5b, mevcut bir test bu davranışı sözleşme olarak sabitlediği için değiştirilmedi; öneri yazıldı.
6. **Doğrulama:**
   - Onarımda 42/42 test OK.
   - Yeni testler orijinal kodda 2 FAIL veriyor; yani hatayı yakalıyorlar.
   - Prob sonucu: orijinalde 3 açık, onarımda 1 açık (bilinçli olarak bırakılan 5b).
   - Yama temiz kaynağa `git am` ile uygulandı; `diff -r` onarım kopyasıyla aynı.
   - MCP köprüsü belgedeki komutla çalıştı: izinli araç okundu, izinsiz araç reddedildi.
7. **Diğer araçlar:** `fault_experiment.py`, ZIP'te olmayan önceki demo çıktısını istiyor; orijinalde de onarımda da aynı `FileNotFoundError`. `verify_configuration.py` ağ politikası yüzünden 403 verdi. `static_check.py` ve `browser_check.cjs` üretilmiş bir site istiyor; site yok, sahte site üretilmedi.

## Sorunlar ve çözümleri
| Sorun | Çözüm |
|---|---|
| Base64'ü elle aktarmak hata riski taşıyordu | Oturum kaydından birebir çıkarıldı, hash ile doğrulandı |
| `tools/recovery.py` çalıştırılınca `recovery-report.json` oluştu ve ilk onarım commit'ine girdi | Commit'ten çıkarıldı, yama yeniden üretildi; yama yalnız 3 dosya içeriyor |

## Erişim veya eksik girdi gerektiren işler
- Uçtan uca `intake → run → resume → package` çalıştırılamadı. Gereken: kimliği doğrulanmış Codex CLI 0.154.0 App Server ve Playwright. Bu ortamda Codex yok; hesap gerektiriyor.
- Windows testleri çalıştırılamadı. Gereken: Windows makinede `python tools/check.py`.
- ZIP'te olmayan dosyalar:
  - `examples/demo-kuafor/salon.jpg` (`run` için),
  - `runs/job-84eb359ac9a46d94/worktrees/implementer/site/` (`fault_experiment` için),
  - projenin `.git` geçmişi (`intake` için),
  - varsa önceki `runs/ledger.sqlite` yedeği.

## 2. aşama teslim kaydı
- Public dal: `claude/determined-mendel-dhyjj8`. 2. aşama commit'leri `c40163d` (doğrulama özeti ve günlük) ve `9d4096e` (Codex-yerel harita). Ürün kaynağı ve yama public depoda **yok**.
- Özel ZIP: `company-os-ozel-teslim.zip`, 70 dosya, SHA-256 `5cee20deb481a3960e83fa8bee1b9651e6bf69825503c5a668cec7695366abc6`. İçinde onarım raporu, yama, git bundle (orijinal + onarım commit'i), onarılmış ağaç, prob betiği ve kanıt çıktıları var.
- Geri okuma:
  - `sha256sum -c` OK.
  - Bundle'dan klon alındı; içerik onarılmış ağaçla `diff -r` sonucunda aynı.
  - Klonda testler OK (42, 1 atlandı); prob 1 açık (5b, bilinçli olarak bırakıldı).
  - Ham sohbet metni yok. Tek tarama eşleşmesi, projenin kendi `docs/native-subagent-review.json` dosyası (orijinalle bayt bayt aynı, yerel yol içeriyor). Bu dosya yalnız özel pakette.
