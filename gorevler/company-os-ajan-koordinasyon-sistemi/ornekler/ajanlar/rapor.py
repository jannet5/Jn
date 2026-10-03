"""Yazar ajan: bağımlılık çıktılarını ve mesajları tek rapora toplar."""
import json

man = json.load(open("inputs/manifest.json", encoding="utf-8"))
inv = json.load(open("inputs/envanter/envanter.json", encoding="utf-8"))
mcp = json.load(open("inputs/mcp-skill-sorgusu/skills.json", encoding="utf-8"))
guv = open("inputs/guvenlik-incelemesi/guvenlik.md", encoding="utf-8").read()
out = ["# Company OS teknik envanter raporu", "", "## Envanter", ""]
out += [f"- {f['file']}: {f['lines']} satır, {len(f['functions'])} fonksiyon" for f in inv["files"]]
out += ["", "## MCP üzerinden bulunan skill'ler", ""]
out += [f"- {s['name']}: {s['description']}" for s in mcp["structuredContent"]["skills"]]
out += ["", guv.replace("# ", "## ", 1), "## Girdi kanıtları (SHA-256)", ""]
for dep, d in sorted(man["dependencies"].items()):
    for p, h in sorted(d["files"].items()):
        out.append(f"- {dep}/{p}: `{h}`")
out += ["", "## Ajan mesajları", ""] + [f"- {m['sender']} → {m['recipient']}: {m['body']}" for m in man["messages"]]
open("RAPOR.md", "w", encoding="utf-8").write("\n".join(out) + "\n")
print("rapor yazıldı")
