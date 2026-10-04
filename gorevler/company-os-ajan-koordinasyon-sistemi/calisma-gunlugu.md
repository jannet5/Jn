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

---

# 3. aşama: 5b sorununun çözümü (2026-10-03)

## İstenen
Çökmüş işten kalan geçici dosya sonraki yayınları kalıcı olarak durdurmamalı. Koşullar:
- canlı başka bir işin dosyası silinmemeli,
- eski/yetim dosya güvenle ayırt edilmeli,
- eski test yeni sözleşmeye göre güncellenmeli,
- saldırı ve regresyon testleri çalıştırılmalı,
- güncel özel ZIP, yama, bundle ve SHA-256 verilmeli.

## Okunan sözleşme
- architecture.md: yayın aynı DB yazma kilidi altında yapılır; depo başına tek yazıcı; geçici dosya dışlayıcı oluşturulur.
- threat-model.md: POSIX'te tutamak tabanlı yarış koruması gerekir.
- operations.md: canlı lease çalınmaz.
- Kodda ek bulgu: `snapshot()` adayı oluşturmadan önce `git status` ile beklenmeyen dosyaları reddediyor. Kalan geçici dosya bu yüzden toparlanmayı ikinci bir yoldan da bozuyordu.

## Tasarım kararları
1. Her yayında benzersiz geçici ad kullanılıyor (`ad.company-os-tmp-<16 hex>`). Dosya yine `O_EXCL|O_NOFOLLOW` ile açılıyor; hata durumunda yayın kendi geçici dosyasını siliyor.
2. Yetim ayrımı ancak `publish_guard` tutulurken yapılıyor. O sırada aynı defteri kullanan başka bir yayın sürüyor olamaz; bu bir testle kanıtlandı: ikinci yazıcı kilidi bekliyor ve birincinin canlı geçici dosyasını görmüyor.
   - Yalnız tam kalıba uyan **normal dosyalar** siliniyor.
   - Symlink, dizin ve reparse point silinmiyor, raporlanıyor.
   - Windows'ta paylaşım ihlali veren (açık tutulan) dosya atlanıyor.
3. Kilit dışındaki çağrılar hiçbir şey silmiyor.
4. Kararlar `orphan_temp_files` olayıyla deftere yazılıyor. Olay, kilit bırakıldıktan sonra yazılıyor; çünkü kilit içinde `with db` bloğu işlemi erken commit ederdi.
5. Mimari belgeye yeni sözleşmeyi anlatan 4 satır eklendi.

## Doğrulama
| Kontrol | Sonuç |
|---|---|
| Tüm testler (37 mevcut, 1'i güncellendi + 10 yeni) | 47 OK (1 Windows testi atlandı) |
| Gerçek süreç çöküşü (`os._exit` ile yazma ortasında) → kilit altında yeniden yayın | Yetim silindi; dizinde yalnız hedef dosya kaldı |
| Negatif kontrol: önceki sürüm + 5 hatalı varyant | 6/6 yakalandı |
| Düşmanca prob (20 senaryo) | Orijinal 4 açık → onarım 0 açık |
| Yama temiz kaynağa `git am` | Uygulandı; onarım kopyasıyla birebir aynı |
| `tools/check.py` | exit 0 |

## Sorunlar ve çözümleri
| Sorun | Çözüm |
|---|---|
| Canlı yazıcı testi eski kodda sonsuza kadar bekledi: A iş parçacığı hata alınca B'yi uyaran olay hiç tetiklenmiyordu | Teste `try/finally`, bekleme zaman aşımı ve hata kaydı eklendi; negatif kontrol betiğindeki her koşuya `timeout` konuldu |
| Takılan arka plan işini `pkill -f` ile durdururken desen kendi kabuğumla da eşleşti ve komut yarıda kaldı | Dosya durumu kontrol edildi; yarım kalan düzenleme ayrı adımda yeniden uygulandı |
| Bir `rm -f $T/*.patch` komutu güvenlik denetimine takıldı (boş değişken riski) | Komut `"${T:?}"` korumasıyla yeniden yazıldı |

## Teslim
- Özel ZIP: `company-os-ozel-teslim.zip`, 74 dosya, SHA-256 `59415e7078bae14dc2be5aaad3d75eea82fc7dd2135b607c443d3be2af916edc`.
  - İçindekiler: 2 commit'lik yama, yalnız 5b yaması, 3 commit'lik bundle, onarılmış ağaç, prob ve negatif kontrol betikleri, kanıt dosyaları, rapor.
- Geri okuma:
  - Hash OK.
  - Bundle onarılmış ağaçla aynı.
  - Klonda 47 test OK, prob 0/20 açık, negatif kontrol 6/6 yakalandı.
  - Ham sohbet/görev metni yok.
  - Yerel yol yalnız projenin kendi orijinal dosyasında var (`docs/native-subagent-review.json`).
- Public depoya ürün kaynağı, yama ve ham girdi konmadı.

## Açık kalanlar
- Windows'ta çalıştırılmadı: `_sweep_windows` ve Windows tutamak kilidi.
- Gerçek Codex uçtan uca akışı çalıştırılmadı: kimliği doğrulanmış Codex CLI ve Playwright gerekiyor.

---

# 4. aşama: Windows sonuçlarına göre test ayrımı (2026-10-03)

## İstenen
Kullanıcının Windows çalıştırmasında 1 ERROR çıktı: symlink ayrıcalığı yok (WinError 1314). İstenenler:
- fixture yalnız gerçekten oluşturulamayan symlink alt senaryosunu gerekçeyle atlamalı,
- diğer senaryolar çalışmaya devam etmeli,
- Windows dalı için sentetik test, gerçek NTFS testinden ayrı adlandırılmalı,
- yükseltme veya Developer Mode istenmemeli,
- Linux ile Windows farkı rapora işlenmeli,
- yeni temiz özel ZIP ve hash teslim edilmeli.

## Yapılanlar
1. Hata veren test ikiye ayrıldı: `…removes_only_matching_regular_files` (ayrıcalıksız; kalıba uyan dizin eklendi) ve `…leaves_planted_symlink` (`symlink_unavailable` yardımcısı WinError 1314'te gerekçeyle atlar). POSIX'e özgü iki symlink testi de aynı korumayı aldı.
2. `test_windows_sweep_decisions_synthetic` eklendi. Sahte `lstat` reparse bayrağı ve sahte paylaşım ihlali ile `_sweep_windows` kararlarını her işletim sisteminde sınıyor. Adında ve belge metninde "sentetik" olduğu açıkça yazıyor.
3. Prob betiği symlink oluşturamadığında senaryoları **ATLANDI** olarak raporluyor. POSIX yarış senaryosu Windows'ta, tutamak kilidi testine yönlendirilerek atlanıyor.
4. Negatif kontrole Windows dalı mutasyonları eklendi: M6 (reparse point yok sayılıyor) ve M7 (paylaşım ihlalinde hata fırlatılıyor). İkisi de sentetik testçe yakalandı.

## Doğrulama (Linux)
| Kontrol | Sonuç |
|---|---|
| Tüm testler | 49 OK (1 atlandı) |
| WinError 1314 taklidi | 49 testte 0 hata, 4 gerekçeli atlama |
| Yeni testler orijinal kodda | 8/12 başarısız (hatayı yakalıyor) |
| Negatif kontrol | 8/8 yakalandı |
| Prob | 0/20 açık |
| 3 commit'lik yama temiz kaynağa | uygulandı, onarım kopyasıyla birebir aynı |

## Teslim
- Özel ZIP `company-os-ozel-teslim.zip`: 163471 bayt, 74 dosya. SHA-256 `1e31ecf4faf889ac0ea35b23da222358997feff51c5b59649ccef3b89af3e25b`.
- Geri okuma: hash ve CRC OK; bundle ağacı aynı; klonda testler, prob ve negatif kontrol OK; ham metin yok.

## Sorunlar ve çözümleri
| Sorun | Çözüm |
|---|---|
| Rapordaki "yeni testler orijinalde" sayısını önce 7/12 yazdım; gerçek çıktı 8/12 | Kanıt dosyası yeniden okunup düzeltildi; ZIP yeniden üretildi |
| `kanit/03` ve `kanit/07` önceki test kümesinden kalmıştı | 03 yeniden üretildi; 07 güncel prob çıktısı (04) ile aynı olduğu için kaldırıldı |

## Açık
- Güncel paket Windows'ta yeniden çalıştırılmadı.
- Gerçek NTFS reparse point ve junction senaryosu çalıştırılmadı.
- Gerçek Codex uçtan uca akışı çalıştırılmadı (kimlik doğrulama ve Playwright gerekiyor).

---

# 5. aşama: Windows kanıtı ve kalan kabul (2026-10-04)

## İstenen
- Kullanıcının Windows sonucunu ayrı kanıt olarak işlemek.
- Kalan kabul için en küçük uygulanabilir yolu çıkarıp mümkün adımları uygulamak.
- `salon.jpg`, `.git` ve kimlik doğrulamasını varmış gibi saymamak.

## Yapılanlar
1. **Windows kanıtı işlendi:** Kullanıcı beyanı olarak haritaya, özel rapora (4b) ve bu günlüğe yazıldı.
   - 46 geçti / 3 atlandı / 0 hata; `check.py` exit 0.
   - Test SHA `71610148…3143`, sonuç JSON SHA `394bd38b…61bb`.
2. **Kaynaktan çıkarılan yol:** `cli.py`, `engine.py`, `codex_executor.py` ve `worktrees.py` okundu. Önkoşullar: `probe` için Codex; `intake` için gerçek `.git`; design ve review için Codex; implement için `salon.jpg`; QA için Node ve Playwright.
3. **Codex'siz kabul betiği** (`kabul_codexsiz.sh`) yazıldı ve çalıştırıldı:
   - K1 probe → kullanılamaz.
   - K2 `.git` yokken intake → exit 1, iş yok.
   - K3 yalnız sentetik git kökünde intake.
   - K4 run → güvenli başarısız.
   - K5 resume ret; K6 accept ret.
   - K7 repair ×3 → bütçe sınırı.
   - K8 package: dürüst rapor.
   - K9 olay hash bütünlüğü OK.
4. **NTFS junction testi** eklendi (yalnız Windows). Linux'ta 50 test geçiyor (2 atlama); Windows'ta çalıştırılmadı.
5. **Özel ZIP:** 170978 bayt, 77 dosya, SHA-256 `0d2d5864e710e1166a5c96944e400ab0112296c6feea2cd30e07e10dabccdbed`.
   - Geri okuma: hash ve CRC OK; bundle ağacı aynı; klonda testler OK, prob 0/20 açık; ham metin yok.

## Sorun ve çözüm
| Sorun | Çözüm |
|---|---|
| Kabul betiğinde K2 satırı boş çıktı verdi: traceback'in son satırı boş | Son boş olmayan satır yazdırıldı; kanıt yeniden üretildi |

## Açık
- NTFS junction testi Windows'ta çalıştırılmadı.
- Gerçek Codex akışı (W1–W6) yapılmadı. Gerçek `.git`, `salon.jpg`, kimliği doğrulanmış Codex ve Playwright gerekiyor.
- **Ürün tamamen bitmedi.**
