// Ölçütlerin bilinen girdilerde doğru sonuç verdiğini gösteren testler (node --test akis/test/)
import { test, before, after } from "node:test";
import assert from "node:assert/strict";
import { chromium } from "playwright";
import { brifKapsami, aiVarsayilanlari, odakKontrolu } from "../olcutler.mjs";
import { puanla } from "../puan.mjs";

let tarayici;
before(async () => { tarayici = await chromium.launch(); });
after(async () => { await tarayici.close(); });

const TAM = `<!doctype html><html lang="tr"><body style="font-family:Georgia;background:#fff">
<nav><a href="#">Genel bakış</a><a href="#">Şubeler</a><a href="#">Ürünler</a><a href="#">Personel</a><a href="#">Ayarlar</a></nav>
<button>Bu hafta</button><button>Geçen hafta</button><button>Son 30 gün</button>
<p>Ciro</p><p>Sipariş sayısı</p><p>Ortalama sepet</p><p>İade oranı</p>
<svg width="400" height="200"><rect width="10" height="10"/></svg>
<table><tbody>${"<tr><td>x</td></tr>".repeat(6)}</tbody></table></body></html>`;

const SLOP = `<!doctype html><html lang="en"><body style="font-family:Inter, sans-serif;background:#f7f2ea">
<h1 style="font-family:Inter">Dashboard</h1>
<div style="background:linear-gradient(90deg, rgb(124,58,237), rgb(99,102,241));width:300px;height:50px"></div>
<button style="background:#b4532a;color:#fff">Go</button>
${'<div style="border-radius:16px;box-shadow:0 4px 12px #0002;width:200px;height:120px"></div>'.repeat(4)}
${'<span style="text-transform:uppercase">etiket</span>'.repeat(5)} a · b · c · d
<ol class="chart" style="width:300px;height:150px">${"<li>x</li>".repeat(7)}</ol></body></html>`;

async function sayfaAc(html) {
  const s = await tarayici.newPage({ viewport: { width: 1000, height: 800 } });
  await s.setContent(html);
  return s;
}

test("brief'i tam karşılayan sayfa 15/15 alır", async () => {
  const s = await sayfaAc(TAM);
  const k = await s.evaluate(brifKapsami);
  assert.equal(k.tamam, 15, JSON.stringify(k.maddeler.filter((m) => !m.tamam)));
  await s.close();
});

test("İngilizce ve içeriksiz sayfada brief maddeleri eksik çıkar, HTML çubuk grafik tanınır", async () => {
  const s = await sayfaAc(SLOP);
  const k = await s.evaluate(brifKapsami);
  assert.ok(k.maddeler.find((m) => m.grup === "grafik").tamam, "class=chart ve 7 öğeli liste grafik sayılmalı");
  assert.equal(k.maddeler.find((m) => m.grup === "dil").tamam, false);
  assert.equal(k.maddeler.filter((m) => m.grup === "menu" && m.tamam).length, 0);
  await s.close();
});

test("AI varsayılan işaretleri yakalanır", async () => {
  const s = await sayfaAc(SLOP);
  const v = await s.evaluate(aiVarsayilanlari);
  assert.equal(v.jenerikFont, true);
  assert.ok(v.morGradyan >= 1);
  assert.ok(v.kartKiti >= 4);
  assert.ok(v.buyukHarfEtiket >= 5);
  assert.ok(v.ortaNokta >= 3);
  assert.equal(v.kremTerakota, true);
  await s.close();
});

test("temiz sayfada AI varsayılan işareti yok", async () => {
  const s = await sayfaAc(TAM);
  const v = await s.evaluate(aiVarsayilanlari);
  assert.equal(v.jenerikFont, false);
  assert.equal(v.morGradyan, 0);
  assert.equal(v.kremTerakota, false);
  await s.close();
});

test("odak göstergesi olmayan bağlantılar sayılır ama görünür sayılmaz", async () => {
  const s = await sayfaAc(`<style>a:focus{outline:none}</style><a href="#">1</a><a href="#">2</a><button style="outline:3px solid red">3</button>`);
  const o = await odakKontrolu(s, 5);
  assert.equal(o.odaklanan, 3);
  assert.equal(o.gorunurOdak, 1);
  await s.close();
});

test("puanlama: kusursuz ölçüm 100 verir, taşma ve axe cezası düşürür", () => {
  const gorunum = (ek = {}) => ({ kapsam: { tamam: 15, toplam: 15 }, axeListesi: [], tasma: { var: false, clientWidth: 390, scrollWidth: 390 },
    odak: { odaklanan: 10, gorunurOdak: 10 }, varsayilan: { jenerikFont: false, morGradyan: 0, kartKiti: 0, buyukHarfEtiket: 0, ortaNokta: 0, kremTerakota: false },
    bayt: 50000, konsolHatalari: [], ...ek });
  assert.equal(puanla({ gorunumler: { masaustu: gorunum(), mobil: gorunum() } }).toplam, 100);
  const kotu = puanla({ gorunumler: { masaustu: gorunum({ axeListesi: [{ id: "color-contrast", etki: "serious" }] }),
    mobil: gorunum({ tasma: { var: true, clientWidth: 390, scrollWidth: 780 } }) } });
  assert.equal(kotu.kalem.erisilebilirlik, 20);
  assert.equal(kotu.kalem.mobil, 3.8);
});
