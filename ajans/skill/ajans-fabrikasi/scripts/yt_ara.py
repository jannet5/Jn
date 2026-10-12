#!/usr/bin/env python3
"""YouTube arama aracı. Önce yt-dlp (android), olmazsa Invidious API.
Kullanım: python3 ajans/araclar/yt_ara.py "arama metni" [--adet 20] [--cikti dosya.jsonl]
Her satır: {"id","title","duration","views","channel","q"}
"""
import argparse, json, subprocess, sys, urllib.request, urllib.parse

def ytdlp(q, n):
    p = subprocess.run(["yt-dlp", "-q", "--no-warnings", "--flat-playlist", "--extractor-args", "youtube:player_client=android",
                        "--print", "%(id)s\t%(title)s\t%(duration)s\t%(view_count)s\t%(channel)s", f"ytsearch{n}:{q}"],
                       capture_output=True, text=True, timeout=120)
    rows = []
    for l in p.stdout.splitlines():
        parts = l.split("\t")
        if len(parts) == 5 and len(parts[0]) == 11:
            rows.append({"id": parts[0], "title": parts[1], "duration": parts[2], "views": parts[3], "channel": parts[4]})
    return rows

def invidious(q, n):
    for host in ["invidious.f5.si", "inv.nadeko.net", "invidious.nerdvpn.de"]:
        try:
            u = f"https://{host}/api/v1/search?q={urllib.parse.quote(q)}&type=video&sort_by=relevance"
            req = urllib.request.Request(u, headers={"User-Agent": "Mozilla/5.0"})
            d = json.load(urllib.request.urlopen(req, timeout=40))
            rows = [{"id": v["videoId"], "title": v["title"], "duration": v.get("lengthSeconds"), "views": v.get("viewCount"), "channel": v.get("author")} for v in d if v.get("type") == "video"]
            if rows: return rows[:n]
        except Exception as e:
            print(f"  {host}: {type(e).__name__}", file=sys.stderr)
    return []

def main():
    ap = argparse.ArgumentParser(); ap.add_argument("q"); ap.add_argument("--adet", type=int, default=20); ap.add_argument("--cikti")
    a = ap.parse_args()
    rows = []
    try: rows = ytdlp(a.q, a.adet)
    except Exception as e: print(f"  yt-dlp: {type(e).__name__}", file=sys.stderr)
    if not rows: rows = invidious(a.q, a.adet)
    for r in rows:
        r["q"] = a.q
        print(f"{r['id']} | {r['title'][:70]} | {r['duration']} sn | {r['views']} görüntüleme | {r['channel']}")
    if a.cikti:
        with open(a.cikti, "a", encoding="utf-8") as f:
            for r in rows: f.write(json.dumps(r, ensure_ascii=False) + "\n")
    print(f"{len(rows)} sonuç")

if __name__ == "__main__":
    main()
