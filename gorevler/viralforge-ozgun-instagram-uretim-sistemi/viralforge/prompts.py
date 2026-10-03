"""Codex'e (beyin) giden istemler. Görsel + ses + açıklama birlikte değerlendirilir."""
from __future__ import annotations

import json

VARIANT_AXES = (
    "v1: aynı duygusal kanca, tamamen farklı sahne/konu",
    "v2: aynı kompozisyon mantığı (odak, kontrast, renk hiyerarşisi), farklı konu ve renk paleti",
    "v3: aynı izleyici gerilimi/merak boşluğu, farklı format (ör. yakın plan ↔ geniş plan, fotoğraf ↔ illüstrasyon)",
    "v4: başarı faktörlerinin en güçlüsünü abartan cesur yorum, farklı mekân ve karakter",
)


def analysis_prompt(post: dict, n_images: int) -> str:
    audio = post["audio"]
    if audio["status"] == "known":
        audio_txt = json.dumps({k: audio.get(k) for k in ("title", "artist", "original", "features")}, ensure_ascii=False)
    elif audio["status"] == "none":
        audio_txt = "Bu gönderide müzik/ses eklenmemiş (kayıtla doğrulandı)."
    else:
        audio_txt = "Ses bilgisi elde edilemedi; ses hakkında tahmin yürütme, 'bilinmiyor' yaz."
    return f"""Sen ViralForge sisteminin beynisin. Ekteki {n_images} görsel bu Instagram gönderisine ait.
Görevin: gönderinin NEDEN tuttuğunu görsel + arkadaki ses + açıklama metnini BİRLİKTE değerlendirerek kanıta dayalı açıkla,
sonra aynı başarı mantığını taşıyan ama birbirinden ve kaynaktan farklı 4 ÖZGÜN görsel briefi yaz.

Gönderi: {post['url']}
Sahip: {post.get('owner') or 'bilinmiyor'}
Metrikler: {json.dumps(post['metrics'], ensure_ascii=False)}
Açıklama (caption): {json.dumps(post.get('caption') or '', ensure_ascii=False)}
Ses: {audio_txt}

Kurallar:
- success_factors en az 3 madde; her biri gönderide gözlenen somut kanıta dayansın (renk, yüz ifadesi, metin kancası, ses ritmi vb.).
- 4 varyasyon şu eksenleri izlesin:
  {chr(10).join('  - ' + a for a in VARIANT_AXES)}
- image_prompt İngilizce, tek başına üretilebilir, ayrıntılı (konu, kompozisyon, ışık, renk, lens/çizim stili, en-boy).
- Kaynaktaki kişileri, logoları, filigranı, metni, telifli karakteri veya birebir kompozisyonu KOPYALAMA; gerçek kişi adı kullanma.
- 4 image_prompt birbirine benzemesin: farklı konu, mekân, palet ve kadraj.
- caption_draft Türkçe; audio_suggestion telifsiz/trend tür önerisi (belirli şarkıyı kopyalama zorunluluğu yok).
- shortcode alanı tam olarak "{post['shortcode']}" olsun. Yalnızca şemaya uyan JSON döndür.
"""


def image_prompt(variant: dict, out_path: str | None) -> str:
    size = {"1:1": "1024x1024", "4:5": "1024x1280", "9:16": "1088x1920"}[variant["aspect_ratio"]]
    tail = (f"After generating, copy the final PNG to this exact path: {out_path} and reply only with the path."
            if out_path else "")
    return (
        f"$imagegen Generate exactly one original image ({size}, aspect {variant['aspect_ratio']}). "
        f"{variant['image_prompt']} "
        "No text, no watermark, no logos, no real identifiable people. " + tail
    ).strip()
