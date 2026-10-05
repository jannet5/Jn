#!/usr/bin/env python3
"""YouTube transcript aracı (bulut ortamı için dayanıklı sürüm).

Kullanım:
  python3 ajans/araclar/yt_transcript.py VIDEO_ID_veya_URL [--dil en,tr] [--cikti klasor]

Sıra ile dener:
  1. yt-dlp (android istemcisi) -> otomatik/manuel altyazı (json3)
  2. yt-dlp (diğer istemciler: web, ios, mweb, tv)
  3. youtube-transcript-api
  4. Invidious örnekleri (/api/v1/captions)
Çıktı: <cikti>/<id>.txt  (düz metin)  +  <cikti>/<id>.meta.json (başlık, kanal, süre, açıklama, kaynak)
"""
import argparse, json, re, subprocess, sys, urllib.request, urllib.parse, pathlib, html

INVIDIOUS = ["invidious.f5.si", "inv.nadeko.net", "invidious.nerdvpn.de", "yewtu.be"]

def vid_id(s):
    m = re.search(r"(?:v=|youtu\.be/|shorts/|live/)([A-Za-z0-9_-]{11})", s)
    return m.group(1) if m else s.strip()

def run(cmd, timeout=180):
    p = subprocess.run(cmd, capture_output=True, text=True, timeout=timeout)
    return p.returncode, p.stdout, p.stderr

def json3_to_text(path):
    d = json.load(open(path, encoding="utf-8"))
    out = []
    for e in d.get("events", []):
        segs = e.get("segs") or []
        t = "".join(s.get("utf8", "") for s in segs).replace("\n", " ").strip()
        if t:
            ms = e.get("tStartMs", 0)
            out.append((ms, t))
    return out

def vtt_to_text(txt):
    out = []
    for block in re.split(r"\n\n+", txt):
        lines = [l for l in block.strip().splitlines() if l.strip()]
        if not lines: continue
        tl = next((l for l in lines if "-->" in l), None)
        if not tl: continue
        h, m, s = tl.split("-->")[0].strip().split(":")[-3:] if tl.count(":") >= 2 else ("0", "0", tl.split("-->")[0].strip())
        try:
            ms = int((int(h) * 3600 + int(m) * 60 + float(s.replace(",", "."))) * 1000)
        except Exception:
            ms = 0
        text = " ".join(re.sub(r"<[^>]+>", "", l) for l in lines if "-->" not in l and not l.strip().isdigit())
        text = html.unescape(text).strip()
        if text and (not out or out[-1][1] != text):
            out.append((ms, text))
    return out

def fmt(ms):
    s = ms // 1000
    return f"{s//3600:02d}:{(s%3600)//60:02d}:{s%60:02d}"

def write(out_dir, vid, lines, meta, source):
    out_dir.mkdir(parents=True, exist_ok=True)
    meta = dict(meta or {}); meta["kaynak"] = source; meta["id"] = vid
    meta["url"] = f"https://www.youtube.com/watch?v={vid}"
    (out_dir / f"{vid}.meta.json").write_text(json.dumps(meta, ensure_ascii=False, indent=1), encoding="utf-8")
    with open(out_dir / f"{vid}.txt", "w", encoding="utf-8") as f:
        f.write(f"# {meta.get('title','')}\n# {meta['url']}\n# kanal: {meta.get('channel','')} | süre: {meta.get('duration','')} sn | kaynak: {source}\n\n")
        for ms, t in lines:
            f.write(f"[{fmt(ms)}] {t}\n")
    print(f"OK {vid} -> {out_dir/(vid+'.txt')}  ({len(lines)} satır, {source})")

def try_ytdlp(vid, langs, out_dir):
    tmp = out_dir / "_tmp"; tmp.mkdir(parents=True, exist_ok=True)
    for client in ["android", "web", "ios", "mweb", "tv", "web_embedded"]:
        for f in tmp.glob(f"{vid}*"): f.unlink()
        rc, so, se = run(["yt-dlp", "-q", "--no-warnings", "--skip-download", "--ignore-no-formats-error",
                          "--write-auto-sub", "--write-sub", "--sub-lang", langs, "--sub-format", "json3/vtt",
                          "--extractor-args", f"youtube:player_client={client}", "--write-info-json",
                          "-o", str(tmp / "%(id)s"), f"https://www.youtube.com/watch?v={vid}"])
        subs = sorted(tmp.glob(f"{vid}*.json3")) + sorted(tmp.glob(f"{vid}*.vtt"))
        if subs:
            # tercih sırası: dil listesindeki sıra
            pick = None
            for l in langs.split(","):
                for p in subs:
                    if f".{l}." in p.name or p.name.endswith(f".{l}.json3") or p.name.endswith(f".{l}.vtt"):
                        pick = p; break
                if pick: break
            pick = pick or subs[0]
            lines = json3_to_text(pick) if pick.suffix == ".json3" else vtt_to_text(pick.read_text(encoding="utf-8"))
            meta = {}
            ij = tmp / f"{vid}.info.json"
            if ij.exists():
                d = json.load(open(ij)); meta = {k: d.get(k) for k in ("title", "channel", "duration", "description", "upload_date", "view_count")}
            return lines, meta, f"yt-dlp:{client}:{pick.name}"
    return None

def try_yta(vid, langs):
    try:
        from youtube_transcript_api import YouTubeTranscriptApi
        t = YouTubeTranscriptApi().fetch(vid, languages=langs.split(","))
        return [(int(s.start * 1000), s.text.replace("\n", " ")) for s in t.snippets], {}, "youtube-transcript-api"
    except Exception as e:
        print(f"  yta: {type(e).__name__}", file=sys.stderr); return None

def http(url, timeout=40):
    req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
    with urllib.request.urlopen(req, timeout=timeout) as r:
        return r.read().decode("utf-8", "ignore")

def try_invidious(vid, langs):
    for host in INVIDIOUS:
        try:
            caps = json.loads(http(f"https://{host}/api/v1/captions/{vid}")).get("captions", [])
            if not caps: continue
            pick = None
            for l in langs.split(","):
                pick = next((c for c in caps if c.get("languageCode") == l), None)
                if pick: break
            pick = pick or caps[0]
            body = http(f"https://{host}/api/v1/captions/{vid}?label=" + urllib.parse.quote(pick["label"]))
            if "WEBVTT" not in body[:50]: continue
            lines = vtt_to_text(body)
            if not lines: continue
            meta = {}
            try:
                d = json.loads(http(f"https://{host}/api/v1/videos/{vid}?fields=title,author,lengthSeconds,description,viewCount"))
                meta = {"title": d.get("title"), "channel": d.get("author"), "duration": d.get("lengthSeconds"), "description": d.get("description"), "view_count": d.get("viewCount")}
            except Exception: pass
            return lines, meta, f"invidious:{host}"
        except Exception as e:
            print(f"  invidious {host}: {type(e).__name__}", file=sys.stderr)
    return None

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("video"); ap.add_argument("--dil", default="en,tr"); ap.add_argument("--cikti", default="ajans/arastirma/youtube/transkriptler")
    a = ap.parse_args()
    vid = vid_id(a.video); out = pathlib.Path(a.cikti)
    if (out / f"{vid}.txt").exists():
        print(f"ZATEN VAR {vid}"); return
    for fn in (lambda: try_ytdlp(vid, a.dil, out), lambda: try_yta(vid, a.dil), lambda: try_invidious(vid, a.dil)):
        r = fn()
        if r:
            write(out, vid, *r); return
    print(f"BAŞARISIZ {vid}: hiçbir yol altyazı vermedi"); sys.exit(1)

if __name__ == "__main__":
    main()
