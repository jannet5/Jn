#!/usr/bin/env python3
"""katalog.json + prompts_chat_secki.json -> KATALOG.md, hazir-promptlar.md, index.html,
LISANS-ENVANTERI.md, NOTICE.md

Tek doğruluk kaynağı veri/ klasörüdür; bu betik yalnız biçimlendirir.
"""
import html
import json
from pathlib import Path

KOK = Path(__file__).resolve().parent.parent
KATALOG = json.loads((KOK / "veri" / "katalog.json").read_text(encoding="utf-8"))
SECKI = json.loads((KOK / "veri" / "prompts_chat_secki.json").read_text(encoding="utf-8"))
FABRIC = (KOK / "veri" / "kaynak-anlik" / "fabric_extract_wisdom_system.md").read_text(encoding="utf-8")
DEPOLAR = KATALOG["kaynak_depolar"]
FABRIC_DEPO = DEPOLAR["danielmiessler/Fabric"]
# Birebir alıntıların lisans belgeleri (MIT kopyalarına izin metni eklemek için)
LISANS_METIN = {p: (KOK / p).read_text(encoding="utf-8")
                for g in KATALOG["girdiler"] if g["ornek"]["kopya"]["tur"] == "birebir"
                for p in g["ornek"]["kopya"]["lisans_belgesi"]}
DURUM_AD = {"acik": "Açık lisans (CC0/MIT)", "kisitli": "Kısıtlı lisans", "tescilli": "Tescilli / üçüncü taraf",
            "bilinmiyor": "Lisans bilinmiyor"}
SINIR_NOTU = [
    "- **Google arayüzünde araştırma yapılmadı.** Ortamın kendi web arama aracı (WebSearch) ve doğrudan sayfa/dosya "
    "indirme kullanıldı; gerçek Google arama arayüzü kullanılmadı.",
    "- **ChatGPT Pro ile ortak araştırma yapılmadı:** Bu bulut oturumunun senin ChatGPT hesabına erişimi yok. "
    "Aşağıdaki prompt'u ChatGPT Pro'ya yapıştırarak listeyi ikinci bir gözle kontrol ettirebilirsin.",
    "- **Bot korumasına takılan 4 adres** (promptbase.com 403, flowgpt.com 403, cursor.directory 429, bir Capterra "
    "sayfası 403): bu oturumda içerikleri doğrulanamadı; normal bir tarayıcıda açılıp açılmadıkları test edilmedi, "
    "garanti verilmez.",
    "- Giriş isteyen galeriler (Microsoft Copilot Prompt Gallery, LangSmith Hub, GPT Mağazası) içerik olarak açılamadı; "
    "resmi belge/destek sayfalarından tarif edildi.",
    "- **Lisans:** Bir deponun public olması veya kaynak linki verilmesi yeniden dağıtım izni değildir. Metni yalnız "
    "CC0/MIT kaynaklardan aldık; MIT kopyalarında telif bildirimi + izin metni `lisanslar/` ve `NOTICE.md` içinde. "
    "Bu bir hukuki görüş değildir.",
    "- Yıldız sayıları ve HTTP durumları 3 Ekim 2026 tarihlidir; zamanla değişir.",
]

ANA_SABLON = """# KİMLİK VE AMAÇ
Sen [alan] konusunda [kaç yıl / hangi seviye] deneyimli bir [rol]sün.
Amacın: [tek cümleyle sonuç — ör. "küçük işletmem için haftalık sosyal medya planı çıkarmak"].

# BAĞLAM
- Ben kimim / işim: [kısa bilgi]
- Hedef kitle: [kim okuyacak / kullanacak]
- Elimdeki malzeme: [ekli dosya, notlar, linkler]
- Neden önemli: [bu işin amacı — model "neden"i bilince daha iyi karar verir]

# ADIMLAR
1. Önce eksik bilgi varsa en fazla [3] soru sor; yoksa doğrudan başla.
2. [adım 2 — ör. mevcut durumu analiz et]
3. [adım 3 — ör. 3 seçenek üret, artı/eksilerini yaz]
4. [adım 4 — ör. en iyisini seç ve gerekçelendir]
5. Bitirmeden önce çıktını aşağıdaki kurallara göre kendin kontrol et.

# KURALLAR
- Yapılacaklar: [ör. Türkçe yaz, somut sayı ver, kaynak göster]
- Yapılmayacaklar: [ör. uydurma veri yok; bilmediğinde "bilmiyorum" de]
- Araç/kaynak kullanımı: [ne zaman web araması, ne zaman yalnız verdiğim metin]

# ÇIKTI BİÇİMİ
- [ör. Başlıklı Markdown; en sonda 5 maddelik eylem listesi]
- Uzunluk: [ör. en fazla 1 sayfa]

# BAŞARI ÖLÇÜTÜ
Çıktı şu durumda başarılıdır: [ör. "hiç bilgisi olmayan biri okuyunca yarın uygulayabilir"].

# GİRDİ
[metnini / verini buraya yapıştır]"""

CHATGPT_PRO_PROMPT = """Aşağıdaki listede hazır prompt siteleri, hazır ajan/sistem paketleri ve otomasyon şablon galerileri var.
1) Her birinin bugün hâlâ aktif olup olmadığını kontrol et ve kaynak linki ver.
2) Bu listede OLMAYAN ama aynı amaçla (gidip hazır prompt/sistem bulup kullanmak) çok kullanılan 5 kaynak öner; her biri için HTTPS link, ne işe yaradığı ve ücretsiz mi olduğu.
3) Türkçe içerik sunan hazır prompt kaynakları varsa ayrıca listele.
Uydurma link verme; emin olmadığını belirt.

LİSTE:
{liste}"""


def kat_ad(kid):
    return next(k["ad"] for k in KATALOG["kategoriler"] if k["id"] == kid)


def lisans_satirlari(k: dict) -> list:
    """Birebir kopya için açık lisans alanları (Markdown)."""
    return [f"- Kaynak depo: `{k['kaynak_depo']}`",
            f"- Tam dosya (commit'e sabit): {k['dosya_permalink']}",
            f"- Commit: `{k['commit']}` ({k['commit_tarihi']}) · erişim: {k['erisim_tarihi']}",
            f"- Sahip: {k['sahip']}",
            f"- Lisans: **{k['lisans']}** · belge: " + ", ".join(f"`{p}`" for p in k["lisans_belgesi"]),
            f"- Yeniden dağıtım: {k['yeniden_dagitim']}"]


def mit_bildirimi(k: dict) -> str:
    """MIT kopyasına eklenecek telif + izin metni (kopyalanan metinle birlikte gider)."""
    if k["lisans"] != "MIT":
        return ""
    metin = LISANS_METIN[k["lisans_belgesi"][0]].strip()
    return (f"\n\n---\nKaynak: {k['dosya_permalink']}\nCommit: {k['commit']} ({k['commit_tarihi']})\n"
            f"Lisans: MIT — aşağıdaki telif bildirimi ve izin metni bu kopyanın parçasıdır.\n\n{metin}\n")


def md_katalog() -> str:
    g = KATALOG["girdiler"]
    o = [f"# {KATALOG['baslik']}", "",
         f"Anlık görüntü tarihi: **{KATALOG['tarih']}** · {len(g)} kaynak · {len(KATALOG['kategoriler'])} kategori", "",
         KATALOG["aciklama"], "",
         "> Hızlı başlangıç: Sadece kopyala-yapıştır prompt istiyorsan → **prompts.chat** ve bu paketteki "
         "`hazir-promptlar.md`. Hazır *sistem* (çok adımlı, rolleri yazılmış) istiyorsan → **Fabric**, **The Agency**, "
         "**Superpowers**. Otomasyon istiyorsan → **n8n şablonları**. Profesyonellerin uzun prompt'larının *yapısını* "
         "görmek istiyorsan → **system-prompts-and-models-of-ai-tools** (yalnız inceleme; metinleri tescillidir).", "",
         "> Lisans özeti: her girdide **İçerik lisansı** satırı var. Tam lisans envanteri `LISANS-ENVANTERI.md`, "
         "üçüncü taraf telif bildirimleri `NOTICE.md`, lisans belgelerinin kopyaları `lisanslar/` klasöründe.", "",
         "## İçindekiler", ""]
    for k in KATALOG["kategoriler"]:
        n = sum(1 for x in g if x["kategori"] == k["id"])
        o.append(f"- [{k['ad']}](#{k['id']}) — {n} kaynak")
    o.append("")
    o += ["## Hangisiyle başlamalıyım?", "",
          "| İhtiyacın | Git | Neden | İçerik lisansı |", "|---|---|---|---|",
          "| Her konuda hazır, kopyala-yapıştır prompt | [prompts.chat](https://prompts.chat) | Ücretsiz, 2.169 prompt | CC0-1.0 (depo beyanı) |",
          "| Ofiste e-posta/toplantı/rapor | [OpenAI Academy paketleri](https://academy.openai.com/public/clubs/work-users-ynjqu/resources/chatgpt-for-any-role) | Resmi, kısa, şablonlu | Bilinmiyor |",
          "| Video/makaleden not, özet, fikir | [Fabric](https://github.com/danielmiessler/Fabric) | Tek işi iyi yapan 250+ uzun sistem prompt'u | MIT |",
          "| Tek kişilik ajans (pazarlama, satış, kod) | [The Agency](https://github.com/msitarzewski/agency-agents) | Rolleri ve teslimatları yazılmış ajanlar | MIT |",
          "| AI ile uygulama yazdırmak | [Superpowers](https://github.com/obra/superpowers) / [Spec Kit](https://github.com/github/spec-kit) | Plan→test→kod disiplini | MIT |",
          "| Tekrarlayan işi otomatiğe bağlamak | [n8n şablonları](https://n8n.io/workflows/) | 12.895 hazır iş akışı | Bilinmiyor |",
          "| Görsel üretim fikri | [PromptHero](https://prompthero.com) | Görselin prompt'unu gör | Bilinmiyor |",
          "| Kendi uzun sistem prompt'umu yazmak | [Ürün sistem prompt'ları](https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools) (yalnız yapıyı incele) + `hazir-promptlar.md` ana şablon | Profesyonel iskeleti gör | Tescilli / üçüncü taraf |",
          ""]
    for k in KATALOG["kategoriler"]:
        o += [f'<a id="{k["id"]}"></a>', "", f"## {k['ad']}", "", k["aciklama"], ""]
        for x in (x for x in g if x["kategori"] == k["id"]):
            kn = x.get("kanit", {})
            durum = []
            if "yildiz" in kn:
                durum.append(f"⭐ {kn['yildiz']:,}".replace(",", "."))
            if "http" in kn:
                durum.append(f"HTTP {kn['http']}")
            durum.append(f"kontrol {kn.get('son_kontrol') or kn.get('son_guncelleme')}")
            il = x["icerik_lisansi"]
            kp = x["ornek"]["kopya"]
            o += [f"### {x['ad']}", "",
                  f"**Link:** {x['url']}  ", f"**Tür:** {x['tur']}  ", f"**Durum:** {' · '.join(durum)}  ",
                  f"**İçerik lisansı:** {DURUM_AD[il['durum']]} — {il['ozet']}", "",
                  f"**Ne işe yarar:** {x['ne_ise_yarar']}", "",
                  f"**Kime göre:** {x['kime']}", ""]
            if kp["tur"] == "birebir":
                o += [f"**Örnek — {x['ornek']['baslik']}:**", "", "```text", x["ornek"]["metin"], "```",
                      "Bu alıntının lisans kaydı:", ""] + lisans_satirlari(kp) + [""]
            else:
                o += [f"**{x['ornek']['baslik']}** — *kaynağın metni kopyalanmadı, açıklama bizim:*", "", "```text",
                      x["ornek"]["metin"], "```", f"Neden kopyalanmadı: {kp['neden']} · Kaynak: {x['ornek']['kaynak_url']}", ""]
            o += ["**Nasıl kullanılır:**", ""]
            o += [f"{i}. {s}" for i, s in enumerate(x["kullanim"], 1)]
            o += ["", f"**Ücret:** {x['ucret_lisans']}  ",
                  f"**Topluluk / not:** {x['topluluk']['not']} ({x['topluluk']['url']})  ",
                  f"**Dikkat:** {x['dikkat']}", ""]
            if x.get("ek_linkler"):
                o += ["Ek linkler: " + " · ".join(x["ek_linkler"]), ""]
    o += ["## Sınırlar ve dürüstlük notu", ""] + SINIR_NOTU + ["",
          "### ChatGPT Pro'ya sorulacak hazır doğrulama prompt'u", "", "```text",
          CHATGPT_PRO_PROMPT.format(liste="\n".join(f"- {x['ad']}: {x['url']}" for x in g)), "```", ""]
    return "\n".join(o)


def md_promptlar() -> str:
    o = ["# Hazır Promptlar — kopyala, yapıştır, kullan", "",
         "Bu dosyada üç şey var:", "",
         "1. **prompts.chat'ten 30 seçme prompt** — commit'e sabitlenmiş CSV'den birebir; her birinin altında "
         "kaynak, permalink, commit/tarih, sahip, lisans ve yeniden dağıtım sınırı yazılı. Türkçe yanıt için sonuna "
         "`Yanıtlarını Türkçe ver.` ekle.",
         "2. **Fabric `extract_wisdom` sistem prompt'unun tamamı** (MIT) — telif bildirimi ve MIT izin metniyle birlikte.",
         "3. **Türkçe ana şablon** — bu paketin özgün metni; kendi sistemini kurman için.", "",
         f"Kaynak CSV: {SECKI['kaynak']} · SHA-256 `{SECKI['csv_sha256']}` · lisans dayanağı: "
         "`lisanslar/prompts.chat_LICENSE` (ikili lisans beyanı: prompt içeriği CC0) ve `lisanslar/prompts.chat_LICENSE-CC0`.", "",
         "## Nasıl kullanılır", "",
         "1. Aşağıdan işine yarayanı bul.",
         "2. Kod bloğundaki metnin tamamını kopyala.",
         "3. ChatGPT / Claude / Gemini'de yeni sohbet aç, yapıştır.",
         "4. Metnin sonundaki \"My first request is ...\" kısmını kendi isteğinle değiştir; `${Alan:Varsayılan}` "
         "biçimindeki yerleri doldur.", "",
         "## 1. Seçme prompt'lar (CC0-1.0)", ""]
    kat = None
    for i, x in enumerate(SECKI["secki"], 1):
        if x["kategori"] != kat:
            kat = x["kategori"]
            o += [f"### {kat}", ""]
        lk = x["lisans_kaydi"]
        o += [f"#### {i}. {x['baslik_tr']} — *{x['act']}*", "", f"**Ne işe yarar:** {x['fayda']}", "",
              "```text", x["prompt"], "```", "",
              f"- Kaynak: {lk['kaynak']} · kayıt: {lk['csv_kaydi']}",
              f"- Tam dosya (commit'e sabit): {lk['dosya_permalink']}",
              f"- Commit: `{lk['commit']}` ({lk['commit_tarihi']}) · erişim: {lk['erisim_tarihi']}",
              f"- Sahip: {lk['sahip']}",
              f"- Lisans: **{lk['lisans']}** · dayanak: " + ", ".join(f"`{p}`" for p in lk["lisans_dayanagi"]),
              f"- Yeniden dağıtım: {lk['yeniden_dagitim']}", ""]
    fk = FABRIC_DEPO
    o += ["## 2. Fabric — extract_wisdom (tam metin, MIT)", "",
          f"- Kaynak depo: `danielmiessler/Fabric`",
          f"- Tam dosya (commit'e sabit): https://github.com/danielmiessler/Fabric/blob/{fk['commit']}/data/patterns/extract_wisdom/system.md",
          f"- Commit: `{fk['commit']}` ({fk['commit_tarihi']}) · erişim: {fk['erisim_tarihi']}",
          f"- Sahip: {fk['sahip']}",
          "- Lisans: **MIT** · belge: `lisanslar/Fabric_LICENSE` (tam metni aşağıda da var)",
          "- Yeniden dağıtım: serbest; aşağıdaki telif bildirimi ve izin metni her kopyada bulunmalı.", "",
          "Kullanım: tamamını yapıştır, en alttaki INPUT kısmına video transkriptini veya makaleyi ekle.", "",
          "````markdown", FABRIC.rstrip(), "````", "",
          "Fabric telif bildirimi ve MIT izin metni (LICENSE dosyasından aynen):", "",
          "```text", LISANS_METIN["lisanslar/Fabric_LICENSE"].strip(), "```", "",
          "## 3. Türkçe ana şablon — 'tekte tam sistem prompt'u", "",
          "Bu iskelet bu paketin **özgün metnidir** (bir siteden kopya değildir; başka kaynaktan metin içermez). "
          "Şu kaynakların ortak *yapısından* esinlenildi: Fabric (KİMLİK/ADIMLAR/ÇIKTI), The Agency (kimlik, yetenek, "
          "kurallar, teslimat, başarı ölçütü), ürün sistem prompt'ları (araç: ne zaman kullan / kullanma), Google rehberi "
          "(kimlik-görev-bağlam-biçim) ve Anthropic rehberi ('neden'i açıkla, örnek ver). Lisansını seçmek paket sahibine "
          "bırakıldı; kişisel kullanımın için kısıt yok.", "",
          "```text", ANA_SABLON, "```", ""]
    return "\n".join(o)


def md_envanter() -> str:
    g = KATALOG["girdiler"]
    from collections import Counter
    sayi = Counter(x["icerik_lisansi"]["durum"] for x in g)
    o = ["# Lisans envanteri", "",
         f"Tarih: {KATALOG['tarih']}. Kural: metni **yalnız** CC0-1.0 veya MIT lisanslı kaynaktan birebir aldık. "
         "Lisansı kısıtlı, tescilli veya bilinmeyen kaynaklar için kendi açıklamamızı ve linki verdik. "
         "Bir deponun public olması ya da linkinin verilmesi yeniden dağıtım izni değildir. Bu belge hukuki görüş değildir.", "",
         "Kaynakların içerik lisansı dağılımı: " + " · ".join(f"{DURUM_AD[k]}: {sayi.get(k, 0)}" for k in DURUM_AD), "",
         "## A. Katalog girdileri", "",
         "| Girdi | İçerik lisansı | Ayrıntı | Katalogdaki örnek | Kopyala düğmesi |", "|---|---|---|---|---|"]
    for x in g:
        kp = x["ornek"]["kopya"]
        orn = f"Birebir ({kp['lisans']}, commit `{kp['commit'][:12]}`)" if kp["tur"] == "birebir" else "Özgün açıklama (metin kopyalanmadı)"
        dug = ("Var + MIT bildirimi eklenir" if kp.get("lisans") == "MIT" else "Var") if kp["kopyalanabilir"] else "Yok"
        o.append(f"| {x['ad']} | {DURUM_AD[x['icerik_lisansi']['durum']]} | {x['icerik_lisansi']['ozet']} | {orn} | {dug} |")
    o += ["", "## B. Kaynak depolar (commit'e sabit)", "",
          "| Depo | Commit | Commit tarihi | Lisans | Sahip | Lisans dosyası |", "|---|---|---|---|---|---|"]
    for r, v in KATALOG["kaynak_depolar"].items():
        lf = v["lisans_permalink"] or "yok"
        o.append(f"| {r} | `{v['commit']}` | {v['commit_tarihi']} | {v['lisans']} | {v['sahip']} | {lf} |")
    o += ["", "## C. hazir-promptlar.md içeriği", "",
          "| İçerik | Lisans | Dayanak |", "|---|---|---|",
          f"| prompts.chat seçkisi ({len(SECKI['secki'])} prompt) | CC0-1.0 | `lisanslar/prompts.chat_LICENSE`, `lisanslar/prompts.chat_LICENSE-CC0` |",
          "| Fabric extract_wisdom tam metni | MIT | `lisanslar/Fabric_LICENSE` + metin altındaki bildirim |",
          "| Türkçe ana şablon | Paketin özgün metni | — |", "",
          "## D. veri/kaynak-anlik/ (doğrulama için tutulan tam dosyalar)", "",
          "| Dosya | Lisans | Belge |", "|---|---|---|",
          "| fabric_extract_wisdom_system.md | MIT | `lisanslar/Fabric_LICENSE` |",
          "| agency_marketing-growth-hacker.md | MIT | `lisanslar/agency-agents_LICENSE` |",
          "| superpowers_brainstorming_SKILL.md | MIT | `lisanslar/superpowers_LICENSE` |",
          "| voltagent_api-designer.md | MIT | `lisanslar/awesome-claude-code-subagents_LICENSE` |", "",
          "prompts.chat CSV'sinin tamamı ürüne **konmadı**; doğrulama commit'e sabit adresten (SHA-256 kontrolüyle) yapılır.", ""]
    return "\n".join(o)


def md_notice() -> str:
    o = ["# NOTICE — üçüncü taraf içerik bildirimleri", "",
         "Bu paket aşağıdaki üçüncü taraf içerikleri birebir içerir. MIT lisanslı her içerik için telif bildirimi ve "
         "izin metni aşağıda aynen verilmiştir (ayrıca `lisanslar/`). CC0-1.0 içerik için atıf zorunlu değildir; "
         "kaynak yine de belirtilmiştir.", ""]
    kullanim = {}
    for x in KATALOG["girdiler"]:
        kp = x["ornek"]["kopya"]
        if kp["tur"] == "birebir":
            kullanim.setdefault(kp["kaynak_depo"], []).append(f"KATALOG.md/index.html — '{x['ad']}' örneği ({kp['dosya_permalink']})")
    kullanim.setdefault("danielmiessler/Fabric", []).append("hazir-promptlar.md §2 ve veri/kaynak-anlik/fabric_extract_wisdom_system.md")
    kullanim["msitarzewski/agency-agents"].append("veri/kaynak-anlik/agency_marketing-growth-hacker.md (tam dosya)")
    kullanim["obra/superpowers"].append("veri/kaynak-anlik/superpowers_brainstorming_SKILL.md (tam dosya)")
    kullanim["VoltAgent/awesome-claude-code-subagents"].append("veri/kaynak-anlik/voltagent_api-designer.md (tam dosya)")
    kullanim.setdefault("f/prompts.chat", []).append(f"hazir-promptlar.md §1, index.html prompt sekmesi, veri/prompts_chat_secki.json ({len(SECKI['secki'])} prompt)")
    for r, yerler in kullanim.items():
        v = KATALOG["kaynak_depolar"][r]
        o += [f"## {r}", "", f"- Commit: `{v['commit']}` ({v['commit_tarihi']})", f"- Lisans: {v['lisans']}",
              f"- Sahip: {v['sahip']}", "- Pakette kullanıldığı yerler:"] + [f"  - {y}" for y in yerler] + [""]
        for p in v["lisans_yerel_kopya"]:
            if "LICENSE-CC0" in p:
                o += [f"CC0 dayanak belgesi: `{p}` (tam metin dosyada).", ""]
                continue
            o += [f"`{p}`:", "", "```text", (KOK / p).read_text(encoding="utf-8").strip(), "```", ""]
    return "\n".join(o)


def html_sayfa() -> str:
    veri = {"katalog": KATALOG, "secki": SECKI["secki"], "lisans_metin": LISANS_METIN, "durum_ad": DURUM_AD}
    js_veri = json.dumps(veri, ensure_ascii=False).replace("</", "<\\/")
    return HTML_SABLON.replace("/*VERI*/", js_veri).replace("{{BASLIK}}", html.escape(KATALOG["baslik"]))


HTML_SABLON = r"""<!doctype html>
<html lang="tr">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Hazır AI Kataloğu</title>
<style>
:root{--bg:#f7f6f2;--yuzey:#ffffff;--metin:#1d1d1b;--soluk:#5f5e58;--cizgi:#dedcd4;--vurgu:#1f5f8b;--vurgu-yumusak:#e3eef6;--kod:#f1efe8;--iyi:#2e7d4f;--uyari:#9a5b00}
@media (prefers-color-scheme: dark){:root:not([data-theme="light"]){--bg:#141513;--yuzey:#1d1e1b;--metin:#ecebe6;--soluk:#a5a39b;--cizgi:#34352f;--vurgu:#7fb6dc;--vurgu-yumusak:#1f2d37;--kod:#262722;--iyi:#6cc28f;--uyari:#e0a54a}}
:root[data-theme="dark"]{--bg:#141513;--yuzey:#1d1e1b;--metin:#ecebe6;--soluk:#a5a39b;--cizgi:#34352f;--vurgu:#7fb6dc;--vurgu-yumusak:#1f2d37;--kod:#262722;--iyi:#6cc28f;--uyari:#e0a54a}
*{box-sizing:border-box}
body{margin:0;background:var(--bg);color:var(--metin);font:16px/1.55 system-ui,-apple-system,"Segoe UI",Roboto,sans-serif}
header{padding:28px 16px 12px;max-width:1100px;margin:0 auto}
h1{font-size:1.7rem;margin:0 0 6px}
.alt{color:var(--soluk);margin:0}
main{max-width:1100px;margin:0 auto;padding:0 16px 48px}
.sekmeler{display:flex;gap:8px;margin:16px 0 12px;flex-wrap:wrap}
.sekme{border:1px solid var(--cizgi);background:var(--yuzey);color:var(--metin);padding:8px 14px;border-radius:999px;cursor:pointer;font:inherit}
.sekme[aria-selected="true"]{background:var(--vurgu);border-color:var(--vurgu);color:var(--bg)}
.arac{display:flex;gap:8px;flex-wrap:wrap;align-items:center;margin-bottom:12px}
input[type=search]{flex:1 1 260px;min-width:0;padding:10px 12px;border:1px solid var(--cizgi);border-radius:10px;background:var(--yuzey);color:var(--metin);font:inherit}
.cipler{display:flex;gap:6px;flex-wrap:wrap;margin-bottom:16px}
.cip{border:1px solid var(--cizgi);background:transparent;color:var(--soluk);padding:5px 10px;border-radius:999px;cursor:pointer;font-size:.85rem}
.cip[aria-pressed="true"]{background:var(--vurgu-yumusak);color:var(--metin);border-color:var(--vurgu)}
.sayac{color:var(--soluk);font-size:.9rem;margin:0 0 10px}
.izgara{display:grid;grid-template-columns:repeat(auto-fill,minmax(320px,1fr));gap:14px}
@media (max-width:420px){.izgara{grid-template-columns:1fr}}
.kart{background:var(--yuzey);border:1px solid var(--cizgi);border-radius:14px;padding:16px;display:flex;flex-direction:column;gap:8px;min-width:0}
.kart h3{margin:0;font-size:1.05rem}
.kart h3 a{color:var(--vurgu);text-decoration:none}
.etiket{font-size:.78rem;color:var(--soluk)}
.durum{font-size:.78rem;color:var(--iyi)}
.durum.uyari{color:var(--uyari)}
.kart p{margin:0}
pre{background:var(--kod);border-radius:8px;padding:10px;margin:0;white-space:pre-wrap;word-break:break-word;font:13px/1.45 ui-monospace,SFMono-Regular,Menlo,Consolas,monospace;max-height:220px;overflow:auto}
details summary{cursor:pointer;color:var(--vurgu);font-size:.9rem}
ol{margin:6px 0 0;padding-left:20px}
.kopyala{align-self:flex-start;border:1px solid var(--vurgu);color:var(--vurgu);background:transparent;border-radius:8px;padding:4px 10px;cursor:pointer;font-size:.85rem}
.kucuk{font-size:.85rem;color:var(--soluk)}
.kucuk a{color:var(--vurgu);word-break:break-all}
.gizli{display:none}
.lisans{overflow-wrap:anywhere;border-left:3px solid var(--cizgi);padding:6px 10px;font-size:.82rem;color:var(--soluk);background:var(--kod);border-radius:0 8px 8px 0}
.lisans b{color:var(--metin)}
.lisans.acik{border-color:var(--iyi)}
.lisans.kapali{border-color:var(--uyari)}
.lisans a{color:var(--vurgu);word-break:break-all}
footer{max-width:1100px;margin:0 auto;padding:0 16px 32px;color:var(--soluk);font-size:.85rem}
.tema{margin-left:auto}
</style>
</head>
<body>
<header>
<h1>{{BASLIK}}</h1>
<p class="alt" id="ozet"></p>
</header>
<main>
<div class="sekmeler" role="tablist">
<button class="sekme" role="tab" id="sekme-kaynak" aria-selected="true">Kaynaklar (site ve sistemler)</button>
<button class="sekme" role="tab" id="sekme-prompt" aria-selected="false">Hazır promptlar (kopyala)</button>
<button class="sekme tema" id="tema" title="Tema değiştir">◐ Tema</button>
</div>
<section id="panel-kaynak">
<div class="arac"><input type="search" id="ara" placeholder="Ara: ör. pazarlama, video, Cursor, ücretsiz..." aria-label="Kaynaklarda ara"></div>
<div class="cipler" id="cipler" aria-label="Kategori"></div>
<div class="cipler" id="lisans-cipler" aria-label="Lisans"></div>
<p class="sayac" id="sayac"></p>
<div class="izgara" id="liste"></div>
</section>
<section id="panel-prompt" class="gizli">
<div class="arac"><input type="search" id="ara-p" placeholder="Ara: ör. mülakat, e-posta, diyet..." aria-label="Promptlarda ara"></div>
<p class="sayac" id="sayac-p"></p>
<div class="izgara" id="liste-p"></div>
</section>
</main>
<footer id="alt"></footer>
<script>
const VERI = /*VERI*/;
const K = VERI.katalog;
const $ = s => document.querySelector(s);
const el = (t, a = {}, ...c) => { const e = document.createElement(t); for (const [k, v] of Object.entries(a)) { if (k === 'class') e.className = v; else if (k.startsWith('on')) e.addEventListener(k.slice(2), v); else e.setAttribute(k, v); } for (const x of c) e.append(x); return e; };
const katAd = id => (K.kategoriler.find(k => k.id === id) || {}).ad || id;
let secili = 'hepsi';
let lisansSecili = 'hepsi';
const kopyalanabilir = x => !!(x.ornek.kopya && x.ornek.kopya.kopyalanabilir);

$('#ozet').textContent = `${K.girdiler.length} kaynak · ${K.kategoriler.length} kategori · ${VERI.secki.length} hazır prompt · anlık görüntü ${K.tarih}. Her linke tıklayıp siteye gidebilir, örnekleri kopyalayabilirsin.`;
$('#alt').textContent = 'Metin yalnız CC0-1.0 / MIT kaynaklardan, commit\'e sabit kaynak ve lisans kaydıyla birebir alındı; MIT kopyalarına telif bildirimi ve izin metni otomatik eklenir. Lisansı kısıtlı, tescilli veya bilinmeyen kaynaklarda metin kopyalanmadı, yalnız açıklama ve link var. Public depo veya link, yeniden dağıtım izni değildir; bu sayfa hukuki görüş değildir. Yıldız ve HTTP durumları 3 Ekim 2026 tarihlidir.';

// Kopyalanacak metin: lisans izin vermiyorsa null; MIT ise telif bildirimi + izin metni eklenir
function kopyaMetni(x) {
  const k = x.ornek.kopya;
  if (!k || !k.kopyalanabilir) return null;
  if (k.lisans !== 'MIT') return x.ornek.metin;
  const lis = VERI.lisans_metin[k.lisans_belgesi[0]].trim();
  return x.ornek.metin + `\n\n---\nKaynak: ${k.dosya_permalink}\nCommit: ${k.commit} (${k.commit_tarihi})\nLisans: MIT — aşağıdaki telif bildirimi ve izin metni bu kopyanın parçasıdır.\n\n${lis}\n`;
}

function lisansKutusu(x) {
  const k = x.ornek.kopya, il = x.icerik_lisansi;
  const a = u => el('a', { href: u, target: '_blank', rel: 'noopener' }, u);
  const kutu = el('div', { class: 'lisans ' + (il.durum === 'acik' ? 'acik' : 'kapali'), 'data-lisans': il.durum },
    el('div', {}, el('b', {}, 'İçerik lisansı: ' + VERI.durum_ad[il.durum]), ' — ' + il.ozet));
  if (k.tur === 'birebir') {
    kutu.append(el('div', {}, 'Örnek birebir alıntı · lisans: ', el('b', {}, k.lisans)),
      el('div', {}, 'Tam dosya: ', a(k.dosya_permalink)),
      el('div', {}, `Commit: ${k.commit} (${k.commit_tarihi}) · erişim ${k.erisim_tarihi}`),
      el('div', {}, 'Sahip: ' + k.sahip),
      el('div', {}, 'Yeniden dağıtım: ' + k.yeniden_dagitim),
      el('div', {}, 'Lisans belgesi: ' + k.lisans_belgesi.join(', ')));
  } else {
    kutu.append(el('div', { class: 'kopyalanmadi' }, 'Kaynağın metni kopyalanmadı; örnek bizim açıklamamız. Neden: ' + k.neden));
  }
  return kutu;
}

function kopyala(metin, btn) {
  const bitti = () => { const eski = btn.textContent; btn.textContent = 'Kopyalandı ✓'; btn.dataset.kopyalandi = '1'; setTimeout(() => btn.textContent = eski, 1500); };
  if (navigator.clipboard && window.isSecureContext) { navigator.clipboard.writeText(metin).then(bitti, () => yedek()); } else yedek();
  function yedek() { const t = el('textarea'); t.value = metin; document.body.append(t); t.select(); try { document.execCommand('copy'); } catch (e) {} t.remove(); bitti(); }
}

function cipler() {
  const c = $('#cipler'); c.innerHTML = '';
  const hepsi = [{ id: 'hepsi', ad: 'Hepsi' }, ...K.kategoriler];
  for (const k of hepsi) {
    const n = k.id === 'hepsi' ? K.girdiler.length : K.girdiler.filter(g => g.kategori === k.id).length;
    c.append(el('button', { class: 'cip', 'aria-pressed': String(secili === k.id), 'data-kat': k.id, onclick: () => { secili = k.id; cipler(); ciz(); } }, `${k.ad} (${n})`));
  }
  const lc = $('#lisans-cipler'); lc.innerHTML = '';
  const secenek = [['hepsi', 'Tüm lisanslar', () => true], ['kopya', 'Metni kopyalanabilir (CC0/MIT)', kopyalanabilir], ['link', 'Yalnız açıklama + link (kısıtlı/tescilli/bilinmiyor)', x => !kopyalanabilir(x)]];
  for (const [id, ad, f] of secenek) {
    lc.append(el('button', { class: 'cip', 'aria-pressed': String(lisansSecili === id), 'data-lisans-filtre': id, onclick: () => { lisansSecili = id; cipler(); ciz(); } }, `${ad} (${K.girdiler.filter(f).length})`));
  }
}

function durumMetni(kn) {
  const p = [];
  if (kn.yildiz) p.push('⭐ ' + kn.yildiz.toLocaleString('tr-TR'));
  if (kn.http !== undefined) p.push('HTTP ' + kn.http);
  if (kn.arsivli) p.push('arşivlenmiş');
  p.push('kontrol ' + (kn.son_kontrol || kn.son_guncelleme));
  return p.join(' · ');
}

function ciz() {
  const q = $('#ara').value.trim().toLocaleLowerCase('tr-TR');
  const l = $('#liste'); l.innerHTML = '';
  const lf = lisansSecili === 'kopya' ? kopyalanabilir : lisansSecili === 'link' ? (x => !kopyalanabilir(x)) : (() => true);
  const g = K.girdiler.filter(x => (secili === 'hepsi' || x.kategori === secili) && lf(x) && (!q || JSON.stringify(x).toLocaleLowerCase('tr-TR').includes(q)));
  $('#sayac').textContent = `${g.length} kaynak gösteriliyor`;
  for (const x of g) {
    const kn = x.kanit || {};
    const uyari = String(kn.http).startsWith('403') || kn.arsivli;
    const ol = el('ol'); x.kullanim.forEach(s => ol.append(el('li', {}, s)));
    const metin = kopyaMetni(x);
    const dugme = metin === null
      ? el('a', { class: 'kucuk kaynaga-git', href: x.url, target: '_blank', rel: 'noopener' }, 'Metin kopyalanmaz — kaynağa git ↗')
      : el('button', { class: 'kopyala', onclick: e => kopyala(metin, e.target) }, x.ornek.kopya.lisans === 'MIT' ? 'Örneği kopyala (MIT bildirimiyle)' : 'Örneği kopyala');
    l.append(el('article', { class: 'kart', 'data-id': x.id },
      el('span', { class: 'etiket' }, katAd(x.kategori) + ' · ' + x.tur),
      el('h3', {}, el('a', { href: x.url, target: '_blank', rel: 'noopener' }, x.ad + ' ↗')),
      el('span', { class: 'durum' + (uyari ? ' uyari' : '') }, durumMetni(kn)),
      el('p', {}, x.ne_ise_yarar),
      el('p', { class: 'kucuk' }, 'Kime göre: ' + x.kime),
      el('details', {}, el('summary', {}, 'Örnek: ' + x.ornek.baslik), el('pre', {}, x.ornek.metin),
        el('p', { class: 'kucuk' }, 'Kaynak: ', el('a', { href: x.ornek.kaynak_url, target: '_blank', rel: 'noopener' }, x.ornek.kaynak_url))),
      dugme,
      lisansKutusu(x),
      el('details', {}, el('summary', {}, 'Nasıl kullanılır'), ol),
      el('p', { class: 'kucuk' }, 'Ücret: ' + x.ucret_lisans),
      el('p', { class: 'kucuk' }, 'Dikkat: ' + x.dikkat),
      el('p', { class: 'kucuk' }, 'Topluluk: ' + x.topluluk.not + ' ', el('a', { href: x.topluluk.url, target: '_blank', rel: 'noopener' }, '[kaynak]'))
    ));
  }
}

function cizP() {
  const q = $('#ara-p').value.trim().toLocaleLowerCase('tr-TR');
  const l = $('#liste-p'); l.innerHTML = '';
  const s = VERI.secki.filter(x => !q || JSON.stringify(x).toLocaleLowerCase('tr-TR').includes(q));
  $('#sayac-p').textContent = `${s.length} prompt · prompts.chat'ten birebir (CC0-1.0, depo beyanı; kaynak kaydı her kartta). Türkçe yanıt için sonuna "Yanıtlarını Türkçe ver." ekle.`;
  for (const x of s) {
    l.append(el('article', { class: 'kart', 'data-act': x.act },
      el('span', { class: 'etiket' }, x.kategori + ' · ' + x.act),
      el('h3', {}, x.baslik_tr),
      el('p', {}, x.fayda),
      el('pre', {}, x.prompt),
      el('button', { class: 'kopyala', onclick: e => kopyala(x.prompt, e.target) }, 'Prompt\'u kopyala'),
      el('div', { class: 'lisans acik', 'data-lisans': 'acik' },
        el('div', {}, el('b', {}, 'Lisans: ' + x.lisans_kaydi.lisans), ' · ' + x.lisans_kaydi.sahip),
        el('div', {}, 'Tam dosya: ', el('a', { href: x.lisans_kaydi.dosya_permalink, target: '_blank', rel: 'noopener' }, x.lisans_kaydi.dosya_permalink)),
        el('div', {}, `Commit: ${x.lisans_kaydi.commit} (${x.lisans_kaydi.commit_tarihi}) · ${x.lisans_kaydi.csv_kaydi}`),
        el('div', {}, 'Yeniden dağıtım: ' + x.lisans_kaydi.yeniden_dagitim),
        el('div', {}, 'Dayanak: ' + x.lisans_kaydi.lisans_dayanagi.join(', ')))
    ));
  }
}

function sekme(ad) {
  const k = ad === 'kaynak';
  $('#sekme-kaynak').setAttribute('aria-selected', String(k));
  $('#sekme-prompt').setAttribute('aria-selected', String(!k));
  $('#panel-kaynak').classList.toggle('gizli', !k);
  $('#panel-prompt').classList.toggle('gizli', k);
}
$('#sekme-kaynak').onclick = () => sekme('kaynak');
$('#sekme-prompt').onclick = () => sekme('prompt');
$('#ara').oninput = ciz;
$('#ara-p').oninput = cizP;
$('#tema').onclick = () => { const r = document.documentElement; const koyu = r.dataset.theme ? r.dataset.theme === 'dark' : matchMedia('(prefers-color-scheme: dark)').matches; r.dataset.theme = koyu ? 'light' : 'dark'; try { localStorage.setItem('tema', r.dataset.theme); } catch (e) {} };
try { const t = localStorage.getItem('tema'); if (t) document.documentElement.dataset.theme = t; } catch (e) {}
cipler(); ciz(); cizP();
</script>
</body>
</html>
"""


def main() -> None:
    (KOK / "KATALOG.md").write_text(md_katalog(), encoding="utf-8")
    (KOK / "hazir-promptlar.md").write_text(md_promptlar(), encoding="utf-8")
    (KOK / "index.html").write_text(html_sayfa(), encoding="utf-8")
    (KOK / "LISANS-ENVANTERI.md").write_text(md_envanter(), encoding="utf-8")
    (KOK / "NOTICE.md").write_text(md_notice(), encoding="utf-8")
    for f in ("KATALOG.md", "hazir-promptlar.md", "index.html", "LISANS-ENVANTERI.md", "NOTICE.md"):
        print(f, (KOK / f).stat().st_size, "bayt")


if __name__ == "__main__":
    main()
