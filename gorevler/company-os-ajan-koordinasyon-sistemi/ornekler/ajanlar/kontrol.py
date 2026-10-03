"""Kabul kontrolleri: kontrol.py <tür> <dosya>; başarısızsa sıfır olmayan çıkış."""
import json, sys

kind, path = sys.argv[1], sys.argv[2]
data = open(path, encoding="utf-8").read()
if kind == "envanter":
    inv = json.loads(data)
    assert inv["files"] and all(f["lines"] > 0 for f in inv["files"]), "boş envanter"
elif kind == "mcp":
    res = json.loads(data)
    assert not res.get("isError") and res["structuredContent"]["skills"], "MCP sonucu boş"
elif kind == "markdown":
    for sec in sys.argv[3:]:
        assert sec in data, f"eksik bölüm: {sec}"
elif kind == "nonempty":
    assert data.strip(), "boş dosya"
else:
    raise SystemExit(f"bilinmeyen tür {kind}")
print(f"{kind} kontrolü geçti: {path}")
