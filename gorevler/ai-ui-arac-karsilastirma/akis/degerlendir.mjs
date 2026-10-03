// Aynı brief ile üretilmiş her denemeyi (denemeler/*/dist/index.html) aynı ölçütlerle ölçer.
// Çıktı: sonuclar/sonuclar.json, sonuclar/rapor.html, sonuclar/ekran/*.png
// Kullanım: node akis/degerlendir.mjs [deneme-klasoru ...]
import { chromium } from "playwright";
import AxeBuilder from "@axe-core/playwright";
import http from "node:http";
import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { brifKapsami, aiVarsayilanlari, odakKontrolu, tarihAkisi } from "./olcutler.mjs";
import { puanla } from "./puan.mjs";
import { raporHtml } from "./rapor.mjs";

const KOK = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "..");
const DENEMELER = path.join(KOK, "denemeler");
const CIKTI = path.join(KOK, "sonuclar");
const EKRAN = path.join(CIKTI, "ekran");
const PROXY_VAR = Boolean(process.env.HTTPS_PROXY || process.env.https_proxy);
const GORUNUMLER = [
  { ad: "masaustu", width: 1440, height: 900 },
  { ad: "mobil", width: 390, height: 844 },
];

const MIME = { ".html": "text/html; charset=utf-8", ".js": "text/javascript", ".css": "text/css",
  ".svg": "image/svg+xml", ".png": "image/png", ".woff2": "font/woff2", ".json": "application/json" };

function statikSunucu(kok) {
  const srv = http.createServer((req, res) => {
    const yol = decodeURIComponent(new URL(req.url, "http://x").pathname);
    let dosya = path.join(kok, yol);
    if (!dosya.startsWith(kok)) { res.writeHead(403).end(); return; }
    if (fs.existsSync(dosya) && fs.statSync(dosya).isDirectory()) dosya = path.join(dosya, "index.html");
    if (!fs.existsSync(dosya)) { res.writeHead(404).end(); return; }
    res.writeHead(200, { "content-type": MIME[path.extname(dosya)] || "application/octet-stream" });
    fs.createReadStream(dosya).pipe(res);
  });
  return new Promise((ok) => srv.listen(0, "127.0.0.1", () => ok(srv)));
}

async function denemeyiOlc(tarayici, ad) {
  const dist = path.join(DENEMELER, ad, "dist");
  const srv = await statikSunucu(dist);
  const url = `http://127.0.0.1:${srv.address().port}/index.html`;
  const sonuc = { ad, url: `denemeler/${ad}/dist/index.html`, gorunumler: {} };
  try {
    for (const g of GORUNUMLER) {
      const ctx = await tarayici.newContext({ viewport: { width: g.width, height: g.height }, deviceScaleFactor: 1,
        // Bulut ortamındaki TLS-araya-giren proxy'nin CA'sı Chromium'a tanıtılmadıysa dış fontlar yüklenemez;
        // bu ortam kusuru denemeleri cezalandırmasın diye yalnız proxy varken sertifika hatası yok sayılır.
        ignoreHTTPSErrors: PROXY_VAR });
      const sayfa = await ctx.newPage();
      // Dış istekler (ör. Google Fonts) bulut proxy'sinde ara sıra kopuyor; ölçüm kararlı olsun diye 4 kez denenir.
      await sayfa.route((u) => !u.hostname.startsWith("127.0.0.1"), async (route) => {
        for (let deneme = 1; deneme <= 4; deneme++) {
          try { return await route.fulfill({ response: await route.fetch({ timeout: 20000 }) }); }
          catch (e) { if (deneme === 4) return route.abort(); await new Promise((r) => setTimeout(r, 500 * deneme)); }
        }
      });
      const konsolHatalari = [];
      sayfa.on("console", (m) => m.type() === "error" && konsolHatalari.push(m.text().slice(0, 200)));
      sayfa.on("pageerror", (e) => konsolHatalari.push(String(e).slice(0, 200)));
      let bayt = 0, istek = 0;
      const disIstekler = new Set();
      const basarisiz = [];
      sayfa.on("requestfailed", (r) => basarisiz.push(`${r.failure()?.errorText} ${r.url().slice(0, 120)}`));
      sayfa.on("response", async (r) => {
        istek++;
        const u = new URL(r.url());
        if (r.status() >= 400) basarisiz.push(`${r.status()} ${u.pathname}`);
        if (u.hostname !== "127.0.0.1") disIstekler.add(u.hostname);
        try { bayt += (await r.body()).length; } catch { /* yönlendirme vb. */ }
      });
      const t0 = Date.now();
      await sayfa.goto(url, { waitUntil: "networkidle", timeout: 60000 });
      await sayfa.waitForTimeout(800); // animasyonlar otursun
      const yuklemeMs = Date.now() - t0;

      const tasma = await sayfa.evaluate(() => {
        const e = document.scrollingElement;
        return { scrollWidth: e.scrollWidth, clientWidth: e.clientWidth, var: e.scrollWidth > e.clientWidth + 1 };
      });
      const axe = await new AxeBuilder({ page: sayfa }).withTags(["wcag2a", "wcag2aa", "wcag21aa"]).analyze();
      const ihlaller = { critical: 0, serious: 0, moderate: 0, minor: 0 };
      const ihlalListesi = axe.violations.map((v) => {
        ihlaller[v.impact] = (ihlaller[v.impact] || 0) + v.nodes.length;
        return { id: v.id, etki: v.impact, dugum: v.nodes.length, aciklama: v.help };
      });
      const kapsam = await sayfa.evaluate(brifKapsami);
      const varsayilan = await sayfa.evaluate(aiVarsayilanlari);
      // Ekran görüntüsü klavye testinden önce: odak halkası görüntüye karışmasın
      const png = path.join(EKRAN, `${ad}-${g.ad}.png`);
      await sayfa.screenshot({ path: png, fullPage: true });
      // Çok uzun sayfaları raporda okunur tutmak için ilk ekranı da ayrıca al
      await sayfa.screenshot({ path: path.join(EKRAN, `${ad}-${g.ad}-ilk-ekran.png`) });
      const odak = await odakKontrolu(sayfa, 20);
      const akis = await tarihAkisi(sayfa);

      sonuc.gorunumler[g.ad] = { viewport: `${g.width}x${g.height}`, yuklemeMs, bayt, istek,
        disAlanlar: [...disIstekler], basarisiz, konsolHatalari, tasma, axe: ihlaller, axeListesi: ihlalListesi,
        kapsam, varsayilan, odak, akis };
      await ctx.close();
    }
  } finally {
    srv.close();
  }
  sonuc.puan = puanla(sonuc);
  return sonuc;
}

async function main() {
  fs.mkdirSync(EKRAN, { recursive: true });
  const secilen = process.argv.slice(2);
  const adlar = (secilen.length ? secilen : fs.readdirSync(DENEMELER))
    .filter((a) => fs.existsSync(path.join(DENEMELER, a, "dist", "index.html")))
    .sort();
  if (!adlar.length) throw new Error("Ölçülecek deneme yok: denemeler/*/dist/index.html bulunamadı");
  const tarayici = await chromium.launch({ args: PROXY_VAR ? ["--disable-quic", "--disable-http2"] : [] });
  const sonuclar = [];
  for (const ad of adlar) {
    process.stdout.write(`ölçülüyor: ${ad} ... `);
    const s = await denemeyiOlc(tarayici, ad);
    sonuclar.push(s);
    console.log(`toplam ${s.puan.toplam}/100`);
  }
  await tarayici.close();
  const cikti = { tarih: new Date().toISOString(), tarayici: "chromium (Playwright)", proxySertifikaYoksayildi: PROXY_VAR, sonuclar };
  fs.writeFileSync(path.join(CIKTI, "sonuclar.json"), JSON.stringify(cikti, null, 2));
  fs.writeFileSync(path.join(CIKTI, "rapor.html"), raporHtml(cikti));
  console.log("yazıldı: sonuclar/sonuclar.json, sonuclar/rapor.html");
}

main().catch((e) => { console.error(e); process.exit(1); });
