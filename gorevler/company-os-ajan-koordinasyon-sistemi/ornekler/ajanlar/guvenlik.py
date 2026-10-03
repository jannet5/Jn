"""İnceleyici ajan: envanterdeki hassas çağrıları skill kontrol listesine göre raporlar."""
import json, os

man = json.load(open("inputs/manifest.json", encoding="utf-8"))
inv = json.load(open("inputs/envanter/envanter.json", encoding="utf-8"))
skills = [s["name"] for s in man["skills"]]
lines = ["# Güvenlik incelemesi", "", f"Kullanılan skill'ler: {', '.join(skills) or 'yok'}", ""]
for f in inv["files"]:
    if f["sensitive_calls"]:
        lines.append(f"- `{f['file']}`: {', '.join(f['sensitive_calls'])}")
lines += ["", f"İncelenen dosya: {len(inv['files'])}"]
open("guvenlik.md", "w", encoding="utf-8").write("\n".join(lines) + "\n")
print("güvenlik incelemesi yazıldı")
