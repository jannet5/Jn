// sonuclar.json → okunur karşılaştırma sayfası (ekran görüntüleri göreli yolla: ekran/*.png)
import { AGIRLIK } from "./puan.mjs";

const kacis = (s) => String(s).replace(/[&<>"]/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;" })[c]);

export function raporHtml({ tarih, tarayici, sonuclar }) {
  const sirali = [...sonuclar].sort((a, b) => b.puan.toplam - a.puan.toplam);
  const kalemler = Object.keys(AGIRLIK);
  const satirlar = sirali.map((s, i) => {
    const m = s.gorunumler.masaustu, mo = s.gorunumler.mobil;
    return `<tr><td>${i + 1}</td><th scope="row">${kacis(s.ad)}</th><td class="sayi"><strong>${s.puan.toplam}</strong></td>
      ${kalemler.map((k) => `<td class="sayi">${s.puan.kalem[k]}</td>`).join("")}
      <td class="sayi">${m.kapsam.tamam}/${m.kapsam.toplam}</td>
      <td class="sayi">${m.axe.critical}/${m.axe.serious}/${m.axe.moderate}/${m.axe.minor}</td>
      <td>${mo.tasma.var ? `var (${mo.tasma.scrollWidth}px)` : "yok"}</td>
      <td class="sayi">${(m.bayt / 1024).toFixed(0)} KB</td>
      <td>${kacis(m.varsayilan.govdeFont)}</td></tr>`;
  }).join("");

  const kartlar = sirali.map((s) => {
    const m = s.gorunumler.masaustu;
    const eksik = m.kapsam.maddeler.filter((x) => !x.tamam).map((x) => x.madde);
    const axe = [...new Map([...m.axeListesi, ...s.gorunumler.mobil.axeListesi].map((v) => [v.id, v])).values()];
    return `<section class="kart" id="${kacis(s.ad)}">
      <h2>${kacis(s.ad)} — ${s.puan.toplam}/100</h2>
      <div class="goruntuler">
        <figure><img loading="lazy" src="ekran/${kacis(s.ad)}-masaustu-ilk-ekran.png" alt="${kacis(s.ad)} masaüstü ilk ekran"><figcaption>1440×900 ilk ekran · <a href="ekran/${kacis(s.ad)}-masaustu.png">tam sayfa</a></figcaption></figure>
        <figure class="dar"><img loading="lazy" src="ekran/${kacis(s.ad)}-mobil-ilk-ekran.png" alt="${kacis(s.ad)} mobil ilk ekran"><figcaption>390×844 · <a href="ekran/${kacis(s.ad)}-mobil.png">tam sayfa</a></figcaption></figure>
      </div>
      <dl>
        <dt>Eksik brif maddeleri</dt><dd>${eksik.length ? eksik.map(kacis).join(", ") : "yok"}</dd>
        <dt>axe kuralları</dt><dd>${axe.length ? axe.map((v) => `${kacis(v.id)} (${v.etki})`).join(", ") : "ihlal yok"}</dd>
        <dt>Akış: "Geçen hafta"</dt><dd>${m.akis.tiklandi ? (m.akis.degisti ? "tıklandı, veriler değişti" : "tıklandı, sayfa değişmedi") : "seçenek bulunamadı"}</dd>
        <dt>Klavye</dt><dd>${m.odak.odaklanan} öğeye odak, ${m.odak.gorunurOdak} tanesinde görünür gösterge</dd>
        <dt>AI varsayılan işaretleri</dt><dd>gövde fontu ${kacis(m.varsayilan.govdeFont)}, başlık ${kacis(m.varsayilan.baslikFont)}, gradyan ${m.varsayilan.gradyan} (mor ${m.varsayilan.morGradyan}), gölgeli yuvarlak kart ${m.varsayilan.kartKiti}, BÜYÜK HARF etiket ${m.varsayilan.buyukHarfEtiket}, orta nokta ${m.varsayilan.ortaNokta}, krem+terakota ${m.varsayilan.kremTerakota ? "evet" : "hayır"}</dd>
        <dt>Ağ</dt><dd>${m.istek} istek, ${(m.bayt / 1024).toFixed(0)} KB, dış alan: ${m.disAlanlar.length ? m.disAlanlar.map(kacis).join(", ") : "yok"}</dd>
        <dt>Konsol hatası</dt><dd>${m.konsolHatalari.length + s.gorunumler.mobil.konsolHatalari.length}</dd>
      </dl>
    </section>`;
  }).join("");

  return `<!doctype html><html lang="tr"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>AI UI Karşılaştırması</title>
<style>
:root{--zemin:#f6f7f9;--yuzey:#fff;--metin:#16181d;--ikincil:#5b616e;--cizgi:#d9dce3;--vurgu:#0b5cad}
@media (prefers-color-scheme:dark){:root:not([data-theme="light"]){--zemin:#121418;--yuzey:#1b1e24;--metin:#e8eaee;--ikincil:#a3a9b6;--cizgi:#2e333c;--vurgu:#6fb0f2}}
:root[data-theme="dark"]{--zemin:#121418;--yuzey:#1b1e24;--metin:#e8eaee;--ikincil:#a3a9b6;--cizgi:#2e333c;--vurgu:#6fb0f2}
*{box-sizing:border-box}body{margin:0;background:var(--zemin);color:var(--metin);font:15px/1.5 system-ui,sans-serif}
main{max-width:1200px;margin:0 auto;padding:24px 16px}h1{font-size:1.6rem;margin:0 0 4px}p.alt{color:var(--ikincil);margin:0 0 20px}
.tablo{overflow-x:auto;background:var(--yuzey);border:1px solid var(--cizgi);border-radius:8px}
table{border-collapse:collapse;width:100%;min-width:900px}th,td{padding:8px 10px;border-bottom:1px solid var(--cizgi);text-align:left;font-size:14px}
thead th{color:var(--ikincil);font-weight:600}.sayi{text-align:right;font-variant-numeric:tabular-nums}
.kart{background:var(--yuzey);border:1px solid var(--cizgi);border-radius:8px;padding:16px;margin:20px 0}.kart h2{margin:0 0 12px;font-size:1.15rem}
.goruntuler{display:flex;gap:12px;align-items:flex-start;flex-wrap:wrap}figure{margin:0;flex:1 1 520px;min-width:0}figure.dar{flex:0 1 200px}
img{width:100%;height:auto;border:1px solid var(--cizgi);border-radius:4px;display:block}figcaption{font-size:13px;color:var(--ikincil)}
a{color:var(--vurgu)}dl{display:grid;grid-template-columns:max-content 1fr;gap:4px 12px;margin:12px 0 0}dt{color:var(--ikincil)}dd{margin:0;overflow-wrap:anywhere}
@media (max-width:600px){dl{grid-template-columns:1fr}}
</style></head><body><main>
<h1>AI UI araçları — aynı brief, aynı ölçüt</h1>
<p class="alt">Ölçüm: ${kacis(tarih)} · ${kacis(tarayici)} · ağırlıklar: ${kalemler.map((k) => `${k} ${AGIRLIK[k]}`).join(", ")}</p>
<div class="tablo"><table><thead><tr><th>#</th><th>Deneme</th><th class="sayi">Toplam</th>${kalemler.map((k) => `<th class="sayi">${k}</th>`).join("")}
<th class="sayi">Brif</th><th class="sayi">axe K/C/O/A</th><th>Mobil taşma</th><th class="sayi">Boyut</th><th>Gövde fontu</th></tr></thead><tbody>${satirlar}</tbody></table></div>
${kartlar}
</main></body></html>`;
}
