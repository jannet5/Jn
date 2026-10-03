#!/usr/bin/env python3
"""katalog.json + prompts_chat_secki.json -> KATALOG.md, hazir-promptlar.md, index.html

Tek doğruluk kaynağı veri/ klasörüdür; bu betik yalnız biçimlendirir.
"""
import html
import json
from pathlib import Path

KOK = Path(__file__).resolve().parent.parent
KATALOG = json.loads((KOK / "veri" / "katalog.json").read_text(encoding="utf-8"))
SECKI = json.loads((KOK / "veri" / "prompts_chat_secki.json").read_text(encoding="utf-8"))
FABRIC = (KOK / "veri" / "kaynak-anlik" / "fabric_extract_wisdom_system.md").read_text(encoding="utf-8")

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


def md_katalog() -> str:
    g = KATALOG["girdiler"]
    o = [f"# {KATALOG['baslik']}", "",
         f"Anlık görüntü tarihi: **{KATALOG['tarih']}** · {len(g)} kaynak · {len(KATALOG['kategoriler'])} kategori", "",
         KATALOG["aciklama"], "",
         "> Hızlı başlangıç: Sadece kopyala-yapıştır prompt istiyorsan → **prompts.chat** ve bu paketteki "
         "`hazir-promptlar.md`. Hazır *sistem* (çok adımlı, rolleri yazılmış) istiyorsan → **Fabric**, **The Agency**, "
         "**Superpowers**. Otomasyon istiyorsan → **n8n şablonları**. Profesyonellerin uzun prompt'larını görmek "
         "istiyorsan → **system-prompts-and-models-of-ai-tools**.", "",
         "## İçindekiler", ""]
    for k in KATALOG["kategoriler"]:
        n = sum(1 for x in g if x["kategori"] == k["id"])
        o.append(f"- [{k['ad']}](#{k['id']}) — {n} kaynak")
    o.append("")
    o += ["## Hangisiyle başlamalıyım?", "",
          "| İhtiyacın | Git | Neden |", "|---|---|---|",
          "| Her konuda hazır, kopyala-yapıştır prompt | [prompts.chat](https://prompts.chat) | Ücretsiz, 2.169 prompt, CC0 |",
          "| Ofiste e-posta/toplantı/rapor | [OpenAI Academy paketleri](https://academy.openai.com/public/clubs/work-users-ynjqu/resources/chatgpt-for-any-role) | Resmi, kısa, şablonlu |",
          "| Video/makaleden not, özet, fikir | [Fabric](https://github.com/danielmiessler/Fabric) | Tek işi iyi yapan 250+ uzun sistem prompt'u |",
          "| Tek kişilik ajans (pazarlama, satış, kod) | [The Agency](https://github.com/msitarzewski/agency-agents) | Rolleri ve teslimatları yazılmış ajanlar |",
          "| AI ile uygulama yazdırmak | [Superpowers](https://github.com/obra/superpowers) / [Spec Kit](https://github.com/github/spec-kit) | Plan→test→kod disiplini |",
          "| Tekrarlayan işi otomatiğe bağlamak | [n8n şablonları](https://n8n.io/workflows/) | 12.895 hazır iş akışı |",
          "| Görsel üretim fikri | [PromptHero](https://prompthero.com) | Görselin prompt'unu kopyala |",
          "| Kendi uzun sistem prompt'umu yazmak | [Ürün sistem prompt'ları](https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools) + `hazir-promptlar.md` ana şablon | Profesyonel iskeleti gör |",
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
            o += [f"### {x['ad']}", "",
                  f"**Link:** {x['url']}  ", f"**Tür:** {x['tur']}  ", f"**Durum:** {' · '.join(durum)}", "",
                  f"**Ne işe yarar:** {x['ne_ise_yarar']}", "",
                  f"**Kime göre:** {x['kime']}", "",
                  f"**Örnek — {x['ornek']['baslik']}:**", "", "```text", x["ornek"]["metin"], "```",
                  f"Kaynak: {x['ornek']['kaynak_url']}", "",
                  "**Nasıl kullanılır:**", ""]
            o += [f"{i}. {s}" for i, s in enumerate(x["kullanim"], 1)]
            o += ["", f"**Ücret / lisans:** {x['ucret_lisans']}  ",
                  f"**Topluluk / not:** {x['topluluk']['not']} ({x['topluluk']['url']})  ",
                  f"**Dikkat:** {x['dikkat']}", ""]
            if x.get("ek_linkler"):
                o += ["Ek linkler: " + " · ".join(x["ek_linkler"]), ""]
    o += ["## Sınırlar ve dürüstlük notu", "",
          "- **ChatGPT Pro ile ortak araştırma yapılamadı:** Bu bulut oturumunun senin ChatGPT hesabına erişimi yok. "
          "Aşağıdaki prompt'u ChatGPT Pro'ya yapıştırarak listeyi ikinci bir gözle kontrol ettirebilirsin.",
          "- Giriş isteyen galeriler (Microsoft Copilot Prompt Gallery, LangSmith Hub, GPT Mağazası) içerik olarak "
          "açılamadı; resmi belge/destek sayfalarından tarif edildi.",
          "- PromptBase otomatik kontrolde 403 (bot koruması) verdi.",
          "- Yıldız sayıları ve HTTP durumları 3 Ekim 2026 tarihlidir; zamanla değişir.", "",
          "### ChatGPT Pro'ya sorulacak hazır doğrulama prompt'u", "", "```text",
          CHATGPT_PRO_PROMPT.format(liste="\n".join(f"- {x['ad']}: {x['url']}" for x in g)), "```", ""]
    return "\n".join(o)


def md_promptlar() -> str:
    o = ["# Hazır Promptlar — kopyala, yapıştır, kullan", "",
         "Bu dosyada üç şey var:", "",
         "1. **prompts.chat'ten 30 seçme prompt** — kaynaktan birebir (CC0). Türkçe yanıt için sonuna "
         "`Yanıtlarını Türkçe ver.` ekle.",
         "2. **Fabric `extract_wisdom` sistem prompt'unun tamamı** (MIT) — 'tekte tam prompt' nasıl yazılır örneği.",
         "3. **Türkçe ana şablon** — yukarıdaki sistemlerin ortak iskeletinden derlenmiş, kendi sistemini kurman için.", "",
         f"Kaynak CSV: {SECKI['kaynak']} (anlık görüntü SHA-256 `{SECKI['anlik_goruntu_sha256']}`)", "",
         "## Nasıl kullanılır", "",
         "1. Aşağıdan işine yarayanı bul.",
         "2. Kod bloğundaki metnin tamamını kopyala.",
         "3. ChatGPT / Claude / Gemini'de yeni sohbet aç, yapıştır.",
         "4. Metnin sonundaki \"My first request is ...\" kısmını kendi isteğinle değiştir; `${Alan:Varsayılan}` "
         "biçimindeki yerleri doldur.", "",
         "## 1. Seçme prompt'lar", ""]
    kat = None
    for i, x in enumerate(SECKI["secki"], 1):
        if x["kategori"] != kat:
            kat = x["kategori"]
            o += [f"### {kat}", ""]
        o += [f"#### {i}. {x['baslik_tr']} — *{x['act']}*", "", f"**Ne işe yarar:** {x['fayda']}", "",
              "```text", x["prompt"], "```", ""]
    o += ["## 2. Fabric — extract_wisdom (tam metin)", "",
          "Kaynak: https://github.com/danielmiessler/Fabric/blob/main/data/patterns/extract_wisdom/system.md — "
          "MIT Lisansı, telif Fabric katkıcılarına aittir.", "",
          "Kullanım: tamamını yapıştır, en alttaki INPUT kısmına video transkriptini veya makaleyi ekle.", "",
          "````markdown", FABRIC.rstrip(), "````", "",
          "## 3. Türkçe ana şablon — 'tekte tam sistem prompt'u", "",
          "Bu iskelet bizim derlememizdir (bir siteden kopya değildir). Şu kaynakların ortak yapısından çıkarıldı: "
          "Fabric (KİMLİK/ADIMLAR/ÇIKTI), The Agency (kimlik, yetenek, kurallar, teslimat, başarı ölçütü), "
          "ürün sistem prompt'ları (araç: ne zaman kullan / kullanma), Google rehberi (Persona-Görev-Bağlam-Biçim) "
          "ve Anthropic rehberi ('neden'i açıkla, örnek ver).", "",
          "```text", ANA_SABLON, "```", ""]
    return "\n".join(o)


def html_sayfa() -> str:
    veri = {"katalog": KATALOG, "secki": SECKI["secki"]}
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
<div class="cipler" id="cipler"></div>
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

$('#ozet').textContent = `${K.girdiler.length} kaynak · ${K.kategoriler.length} kategori · ${VERI.secki.length} hazır prompt · anlık görüntü ${K.tarih}. Her linke tıklayıp siteye gidebilir, örnekleri kopyalayabilirsin.`;
$('#alt').textContent = 'Örnek prompt metinleri kaynaklarından birebir alınmıştır (prompts.chat: CC0; Fabric/Agency/Superpowers: MIT). Yıldız ve HTTP durumları 3 Ekim 2026 tarihlidir.';

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
  const g = K.girdiler.filter(x => (secili === 'hepsi' || x.kategori === secili) && (!q || JSON.stringify(x).toLocaleLowerCase('tr-TR').includes(q)));
  $('#sayac').textContent = `${g.length} kaynak gösteriliyor`;
  for (const x of g) {
    const kn = x.kanit || {};
    const uyari = String(kn.http).startsWith('403') || kn.arsivli;
    const ol = el('ol'); x.kullanim.forEach(s => ol.append(el('li', {}, s)));
    l.append(el('article', { class: 'kart', 'data-id': x.id },
      el('span', { class: 'etiket' }, katAd(x.kategori) + ' · ' + x.tur),
      el('h3', {}, el('a', { href: x.url, target: '_blank', rel: 'noopener' }, x.ad + ' ↗')),
      el('span', { class: 'durum' + (uyari ? ' uyari' : '') }, durumMetni(kn)),
      el('p', {}, x.ne_ise_yarar),
      el('p', { class: 'kucuk' }, 'Kime göre: ' + x.kime),
      el('details', {}, el('summary', {}, 'Örnek: ' + x.ornek.baslik), el('pre', {}, x.ornek.metin),
        el('p', { class: 'kucuk' }, 'Kaynak: ', el('a', { href: x.ornek.kaynak_url, target: '_blank', rel: 'noopener' }, x.ornek.kaynak_url))),
      el('button', { class: 'kopyala', onclick: e => kopyala(x.ornek.metin, e.target) }, 'Örneği kopyala'),
      el('details', {}, el('summary', {}, 'Nasıl kullanılır'), ol),
      el('p', { class: 'kucuk' }, 'Ücret/lisans: ' + x.ucret_lisans),
      el('p', { class: 'kucuk' }, 'Dikkat: ' + x.dikkat),
      el('p', { class: 'kucuk' }, 'Topluluk: ' + x.topluluk.not + ' ', el('a', { href: x.topluluk.url, target: '_blank', rel: 'noopener' }, '[kaynak]'))
    ));
  }
}

function cizP() {
  const q = $('#ara-p').value.trim().toLocaleLowerCase('tr-TR');
  const l = $('#liste-p'); l.innerHTML = '';
  const s = VERI.secki.filter(x => !q || JSON.stringify(x).toLocaleLowerCase('tr-TR').includes(q));
  $('#sayac-p').textContent = `${s.length} prompt · prompts.chat'ten birebir (CC0). Türkçe yanıt için sonuna "Yanıtlarını Türkçe ver." ekle.`;
  for (const x of s) {
    l.append(el('article', { class: 'kart', 'data-act': x.act },
      el('span', { class: 'etiket' }, x.kategori + ' · ' + x.act),
      el('h3', {}, x.baslik_tr),
      el('p', {}, x.fayda),
      el('pre', {}, x.prompt),
      el('button', { class: 'kopyala', onclick: e => kopyala(x.prompt, e.target) }, 'Prompt\'u kopyala')
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
    for f in ("KATALOG.md", "hazir-promptlar.md", "index.html"):
        print(f, (KOK / f).stat().st_size, "bayt")


if __name__ == "__main__":
    main()
