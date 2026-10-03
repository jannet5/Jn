"""youtube-video-listesi.md'deki videoların transkriptlerini indirir.

Bulut IP'lerinde YouTube engellediği için ev bağlantısında çalıştırılmalıdır.
Kullanım: pip install youtube-transcript-api && python3 dogrulama/transkript_al.py
"""
import pathlib
import re

from youtube_transcript_api import YouTubeTranscriptApi

KOK = pathlib.Path(__file__).resolve().parent.parent
liste = (KOK / "youtube-video-listesi.md").read_text(encoding="utf-8")
idler = list(dict.fromkeys(re.findall(r"watch\?v=([\w-]{11})", liste)))
cikti = KOK / "transkript"
cikti.mkdir(exist_ok=True)
api = YouTubeTranscriptApi()
for vid in idler:
    try:
        parcalar = api.fetch(vid, languages=["tr", "en"])
        metin = " ".join(p.text.replace("\n", " ") for p in parcalar)
        (cikti / f"{vid}.txt").write_text(metin, encoding="utf-8")
        print(vid, "OK", parcalar.language_code, len(metin))
    except Exception as hata:  # her videoyu ayrı raporla, biri düşünce durma
        print(vid, "HATA", type(hata).__name__)
