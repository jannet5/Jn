# Company OS teknik envanter raporu

## Envanter

- __init__.py: 2 satır, 0 fonksiyon
- __main__.py: 3 satır, 0 fonksiyon
- artifacts.py: 257 satır, 16 fonksiyon
- cli.py: 172 satır, 6 fonksiyon
- ledger.py: 551 satır, 33 fonksiyon
- mcp_client.py: 102 satır, 10 fonksiyon
- mcp_server.py: 219 satır, 9 fonksiyon
- orchestrator.py: 269 satır, 10 fonksiyon
- qa.py: 68 satır, 1 fonksiyon
- skills.py: 93 satır, 6 fonksiyon

## MCP üzerinden bulunan skill'ler

- kod-inceleme: Python kodunda güvenlik incelemesi; subprocess, os.open, symlink ve dosya yazma çağrılarını kontrol listesiyle değerlendirir.
- teknik-rapor: Birden çok ajanın çıktısını kaynak hash'leriyle birlikte tek teknik rapora toplama kuralları.

## Güvenlik incelemesi

Kullanılan skill'ler: kod-inceleme

- `artifacts.py`: os.open, os.replace
- `cli.py`: open
- `ledger.py`: subprocess.run
- `mcp_client.py`: subprocess.Popen
- `orchestrator.py`: subprocess.run
- `skills.py`: open

İncelenen dosya: 10

## Girdi kanıtları (SHA-256)

- envanter/envanter.json: `16a968f20eee509ef1312a7a2842a33942f80d4d7e6e8d3fc34d928b3878b594`
- guvenlik-incelemesi/guvenlik.md: `16bb66f973d92504db1a3be13c70810d9452d9af6b4706b396749dd3897c9c37`
- kararlilik/kararli.txt: `b205244cb99d2f77d296b18e8ffc8e59b2496190628bd58d51a433badaf32efc`
- mcp-skill-sorgusu/skills.json: `7846c17e16ec5a5449622c2b41ee115909a8dec119222837efcdc5b2d74e871d`

## Ajan mesajları

- w1-c652e6fb → yazar: Envanter hazır: 10 dosya envantere alındı
- w1-c652e6fb → yazar: Güvenlik incelemesi bitti
