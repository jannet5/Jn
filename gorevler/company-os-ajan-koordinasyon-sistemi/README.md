# Company OS: ajan koordinasyon sistemi

Claude Code'a verilen bir işi uzman alt ajanlara böler. Ajanlar işi **lease ve fencing token** altında yürütür. Bilgiyi **Agent Skills** (`SKILL.md`) kaynağından çeker. İş yaptırmak için **MCP** kullanır. Sonuçları **çalıştırılmış kanıta bağlı bir kabul kapısından** geçirip tek yerde toplar. Çalışması yerel ve çevrimdışıdır: yalnız Python standart kütüphanesi ve SQLite kullanır, sunucu yoktur.

## Bileşenler
| Modül | Görev |
|---|---|
| `src/company_os/ledger.py` | SQLite defteri: plan/DAG, `claim` (running durumuna tek giriş), heartbeat, fencing token, koşullu `reconcile`, artifact ve kanıt kaydı, ajan mesajları, olay izi |
| `src/company_os/qa.py` | Kabul kapısı: snapshot'ı defter ve diskten üretir, kanıtı `(yol, sha256)` ile bağlar |
| `src/company_os/artifacts.py` | Kök dışına çıkamayan, symlink ve junction yarışına dayanıklı yazma ve okuma |
| `src/company_os/orchestrator.py` | Worker havuzu, bağımlılık çıktılarının hash doğrulamalı aktarımı, `command`/`mcp`/`claude` yürütücüleri, sonuç toplama (`publish`) |
| `src/company_os/skills.py` | `SKILL.md` keşfi ve göreve göre seçimi |
| `src/company_os/mcp_server.py` | Defteri Claude Code'a açan stdio MCP sunucusu (15 araç) |
| `src/company_os/mcp_client.py` | Herhangi bir stdio MCP sunucusunu çağıran asgari istemci |
| `src/company_os/kit/` | Claude Code kiti: `cos-arastirmaci`, `cos-uygulayici`, `cos-inceleyici`, `cos-qa` alt ajanları ve `company-os-orkestrasyon` skill'i |

## Kurulum
```bash
pip install .                      # ya da kurmadan: PYTHONPATH=src python -m company_os ...
company-os --version
```
Windows (PowerShell): `py -m pip install .` ardından `company-os --version`.

## Kullanım 1: Claude Code ana ajan, alt ajanlar uzman
```bash
company-os install-kit C:\yol\projem      # .claude/agents, .claude/skills, .mcp.json
cd C:\yol\projem
claude                                     # ilk açılışta "company-os" MCP sunucusunu onaylayın
```
Ardından Claude'a büyük bir iş verin, örneğin: *"company-os-orkestrasyon skill'ini kullanarak şu işi planla, dağıt ve sonuçları topla: …"*. Claude planı `plan_submit` ile yükler ve görevleri `cos-*` alt ajanlarına dağıtır. Her alt ajan `task_claim` ile kendi token'ını alır, `artifact_write` ve `evidence_run` çalıştırır, sonra `task_complete` ile bitirir. Sonuçlar `report` ile toplanır.
Başsız (CI) kullanım: `claude -p "..." --mcp-config .mcp.json --strict-mcp-config --allowedTools mcp__company-os Agent Read Skill`.

Kendi skill'lerinizi `.claude/skills/<ad>/SKILL.md` altına ekleyin. obra/superpowers ya da anthropics/skills gibi paketler de aynı biçimdedir.

## Kullanım 2: Plan dosyası ile otomatik yürütme
```bash
company-os run ornekler/plan-ornek.json --db calisma.db --workers 3 --out sonuc --report rapor.md
company-os status --db calisma.db
company-os reconcile --db calisma.db       # çöken worker'ların süresi dolmuş lease'lerini geri al
company-os retry <görev> --db calisma.db   # blocked/failed görevi yeniden kuyruğa al
company-os run plan.json --db calisma.db --resume
```
Plan biçimi (`ornekler/plan-ornek.json`):
```json
{"tasks": [{"id": "x", "title": "...", "role": "uygulayici", "depends_on": [],
            "skills": ["kod-inceleme"], "instructions": "...",
            "executor": {"type": "command", "argv": ["{python}", "betik.py"], "outputs": ["cikti.md"]},
            "acceptance": [{"argv": ["{python}", "kontrol.py", "cikti.md"], "refs": ["cikti.md"]}],
            "notify": [{"to": "yazar", "body": "x bitti: {result}"}]}]}
```
Yürütücüler:
- `command`: argv'yi çalıştırır.
- `mcp`: `{"server": [...], "tool": "...", "arguments": {}}` ile bir aracı çağırıp sonucu geri getirir.
- `claude`: `claude -p` ile görev talimatını ve seçilen skill gövdelerini çalıştırır.

Her görev deneme dizininde `inputs/manifest.json` dosyasını görür; bu dosyada bağımlılık çıktıları, hash'ler, mesajlar ve skill'ler bulunur.

## Doğruluk garantileri (denetim bulguları)
Ayrıntı için `denetim/bulgu-dogrulama.md` dosyasına bakın.
- `running` durumuna yalnız `claim()` ile girilir. `succeeded` durumuna yalnız kabul kapısı geçilince girilir.
- Süresi dolmuş ya da yerine başkası geçmiş worker'ın tüm yazmaları reddedilir (`LeaseLost`) ve olay izine yazılır.
- `reconcile()` canlı yeni bir lease'e dokunamaz.
- Kanıtsız iş, boş snapshot, eski deneme kanıtı ve sonradan değişmiş dosya kabul edilmez.
- Artifact yazımı symlink ya da junction yarışıyla kök dışına taşınamaz. POSIX'te bu tutamak tabanlı yapılır. Windows sınırı için ilgili belgeye bakın.

## Doğrulama
```bash
python -m unittest discover -s tests -v       # 37 test
python kabul/mutasyon_kontrolu.py             # 5 hatayı geri enjekte eder, testlerin yakaladığını gösterir
<mcp kurulu python> kabul/sdk_birlikte_calisma.py
```
Kayıtlı çıktılar `kabul/` altında, araştırma ve A/B/C kararı `arastirma/harita.md` içinde, adım adım günlük `calisma-gunlugu.md` içinde.

## Yapılmayanlar ve sınırlar
- Mevcut Company OS kaynak kodu yüklenmediği için bulgular **o kod üzerinde** yeniden çalıştırılmadı. Gereken girdi `denetim/bulgu-dogrulama.md` içinde yazıyor.
- Windows'ta ve telefonda test yapılmadı. Testler Linux bulut konteynerinde çalıştı.
- Proje `.mcp.json` onayı Claude Code'da ilk açılışta kullanıcı tarafından verilmelidir.
- Defter tek makine içindir (SQLite). Çok makineli kullanım için aynı token ve koşullu güncelleme tasarımı Postgres'e taşınmalıdır.
