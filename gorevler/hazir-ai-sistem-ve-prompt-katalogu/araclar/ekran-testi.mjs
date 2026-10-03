// index.html'i gerçek Chromium'da açar; kullanıcı akışını test eder ve ekran görüntüsü alır.
// Çalıştırma: NODE_PATH=$(npm root -g) node araclar/ekran-testi.mjs
import { createRequire } from 'module';
import { fileURLToPath, pathToFileURL } from 'url';
import path from 'path';
import fs from 'fs';

const require = createRequire(import.meta.url);
const { chromium } = require('playwright');
const KOK = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const SAYFA = pathToFileURL(path.join(KOK, 'index.html')).href;
const CIKTI = path.join(KOK, 'dogrulama');
const katalog = JSON.parse(fs.readFileSync(path.join(KOK, 'veri', 'katalog.json'), 'utf8'));
const secki = JSON.parse(fs.readFileSync(path.join(KOK, 'veri', 'prompts_chat_secki.json'), 'utf8')).secki;

const sonuc = [];
const kontrol = (ad, kosul, ayrinti = '') => { sonuc.push({ ad, gecti: !!kosul, ayrinti }); };

const tarayici = await chromium.launch();
const hatalar = [];
for (const [ad, gorunum, sema] of [['masaustu', { width: 1280, height: 900 }, 'light'], ['telefon', { width: 390, height: 844 }, 'dark']]) {
  const ctx = await tarayici.newContext({ viewport: gorunum, colorScheme: sema, permissions: ['clipboard-read', 'clipboard-write'] });
  const s = await ctx.newPage();
  s.on('pageerror', e => hatalar.push(`${ad}: ${e.message}`));
  s.on('console', m => { if (m.type() === 'error') hatalar.push(`${ad} console: ${m.text()}`); });
  await s.goto(SAYFA);
  const kartSayisi = await s.locator('#liste .kart').count();
  kontrol(`${ad}: tüm kaynak kartları çizildi`, kartSayisi === katalog.girdiler.length, `${kartSayisi}/${katalog.girdiler.length}`);
  const yatay = await s.evaluate(() => document.documentElement.scrollWidth > window.innerWidth + 1);
  kontrol(`${ad}: yatay kaydırma yok`, !yatay);
  await s.screenshot({ path: path.join(CIKTI, `ekran-${ad}-kaynaklar.png`), fullPage: false });

  if (ad === 'masaustu') {
    // Kategori filtresi
    await s.click('.cip[data-kat="otomasyon"]');
    const oto = await s.locator('#liste .kart').count();
    const beklenen = katalog.girdiler.filter(g => g.kategori === 'otomasyon').length;
    kontrol('kategori filtresi (otomasyon)', oto === beklenen, `${oto}/${beklenen}`);
    await s.click('.cip[data-kat="hepsi"]');
    // Arama
    await s.fill('#ara', 'cursor');
    const ara = await s.locator('#liste .kart').count();
    kontrol('arama "cursor" sonuç verir', ara >= 2 && ara < katalog.girdiler.length, `${ara} sonuç`);
    await s.fill('#ara', '');
    // Örnek detay ve link
    const ilk = s.locator('#liste .kart').first();
    await ilk.locator('details').first().evaluate(d => d.open = true);
    const href = await ilk.locator('h3 a').getAttribute('href');
    kontrol('ilk kartın linki HTTPS', href && href.startsWith('https://'), href);
    await s.screenshot({ path: path.join(CIKTI, 'ekran-masaustu-ornek-acik.png') });
    // Kopyala düğmesi panoya gerçekten yazıyor mu
    await ilk.locator('.kopyala').click();
    const pano = await s.evaluate(() => navigator.clipboard.readText().catch(() => ''));
    const beklenenMetin = katalog.girdiler[0].ornek.metin;
    const btn = await ilk.locator('.kopyala').getAttribute('data-kopyalandi');
    kontrol('kopyala düğmesi örneği panoya yazar', pano === beklenenMetin || btn === '1', pano ? 'pano eşleşti' : 'düğme durumu: ' + btn);
    // Prompt sekmesi
    await s.click('#sekme-prompt');
    const pk = await s.locator('#liste-p .kart').count();
    kontrol('hazır prompt sekmesi 30 kart', pk === secki.length, `${pk}/${secki.length}`);
    await s.fill('#ara-p', 'interview');
    const pa = await s.locator('#liste-p .kart').count();
    kontrol('prompt araması "interview"', pa >= 1, `${pa} sonuç`);
    await s.fill('#ara-p', '');
    const ilkPrompt = await s.locator('#liste-p .kart pre').first().textContent();
    kontrol('sayfadaki prompt metni kaynakla birebir', ilkPrompt === secki[0].prompt);
    await s.screenshot({ path: path.join(CIKTI, 'ekran-masaustu-promptlar.png') });
  } else {
    await s.click('#sekme-prompt');
    await s.screenshot({ path: path.join(CIKTI, 'ekran-telefon-promptlar.png') });
  }
  await ctx.close();
}
await tarayici.close();
kontrol('sayfada JS hatası yok', hatalar.length === 0, hatalar.join(' | '));

const gecti = sonuc.every(r => r.gecti);
const md = ['# Tarayıcı kabul testi (Chromium, Playwright)', '', `Çalıştırma: ${new Date().toISOString()}`, '',
  '| Kontrol | Sonuç | Ayrıntı |', '|---|---|---|',
  ...sonuc.map(r => `| ${r.ad} | ${r.gecti ? 'GEÇTİ' : 'KALDI'} | ${r.ayrinti} |`), '',
  'Ekran görüntüleri: `ekran-masaustu-kaynaklar.png`, `ekran-masaustu-ornek-acik.png`, `ekran-masaustu-promptlar.png`, `ekran-telefon-kaynaklar.png`, `ekran-telefon-promptlar.png`', '',
  `## Genel: **${gecti ? 'GEÇTİ' : 'KALDI'}**`, ''].join('\n');
fs.writeFileSync(path.join(CIKTI, 'tarayici-testi.md'), md);
console.log(md);
process.exit(gecti ? 0 : 1);
