# Gerçek Claude Code ile kabul (2026-10-03, bulut konteyneri, Linux)

Ortam: `claude 2.1.288 (Claude Code)`, Python 3.11.15. Hedef proje: boş `/tmp/cc-proj`.

## 1. Kit kurulumu
```
PYTHONPATH=<paket>/src python -m company_os install-kit /tmp/cc-proj
→ .claude/agents/cos-{arastirmaci,uygulayici,inceleyici,qa}.md
→ .claude/skills/company-os-orkestrasyon/SKILL.md
→ .mcp.json (company-os stdio sunucusu)
```

## 2. Claude Code'un MCP sağlık kontrolü
- Proje kapsamı (`.mcp.json`): `claude mcp get company-os` sunucuyu doğru okudu. Durum
  `⏸ Pending approval`. Bu, Claude Code'un proje MCP sunucuları için istediği **etkileşimli
  güven onayı**. Başsız ortamda verilemez. `enableAllProjectMcpServers` yerel ayarı da bu
  sürümde `mcp list` onayını aşmadı.
- Aynı yapılandırma yerel kapsamda `claude mcp add-json cos-yerel … -s local` ile eklendi.
  Sonuç: `cos-yerel: … - √ Connected`. Claude Code sunucuyla el sıkıştı ve araçları listeledi.
  Test kaydı daha sonra silindi.

## 3. Başsız orkestrasyon turu
```
claude -p "<company-os-orkestrasyon skill'ini uygula; iki görevli plan: hesap (uygulayici) → test (qa)…>" \
  --mcp-config .mcp.json --strict-mcp-config \
  --allowedTools mcp__company-os Agent Task Read Skill --output-format json
```
- Çıkış 0, `is_error: false`, maliyet 0.2229 USD (CLI raporu).
- Konuşma kaydında alt ajan çağrıları var: `subagent_type: "cos-uygulayici"` ×2 ve
  `subagent_type: "cos-qa"` ×2. Her çağrı için bir istek ve bir yanıt kaydı tutuluyor.

## 4. Claude'un iddiasından bağımsız defter doğrulaması
```
hesap  succeeded  attempt 1
test   succeeded  attempt 1
hesap  claimed   owner=uygulayici-1 token=1 → artifact hesap.py → evidence exit 0 → succeeded
test   promoted → claimed owner=qa-agent-1 token=2 → artifact hesap.py, test_hesap.py → evidence exit 0 → succeeded
evidence(test): python3 -m unittest test_hesap -v → "Ran 2 tests … OK"
sha256sum (disk):
df5e5a79…39bc6c  tasks/hesap/attempt-1/hesap.py
df5e5a79…39bc6c  tasks/test/attempt-1/hesap.py
04385b43…934c5  tasks/test/attempt-1/test_hesap.py
```
Disk hash'leri hem Claude'un raporladığı değerlerle hem defter kaydıyla birebir aynı.

## Sınırlar
- Windows'ta ve kullanıcının kendi Claude Code kurulumunda çalıştırılmadı. Proje
  `.mcp.json` onayı ilk `claude` açılışında kullanıcı tarafından verilmelidir.
- Model çıktısı deterministik değildir. Bu tur bir kez çalıştırıldı.
