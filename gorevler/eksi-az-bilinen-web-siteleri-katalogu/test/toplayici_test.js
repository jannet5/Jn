// Toplayıcıyı gerçek Chromium'da sahte yerel başlık üzerinde çalıştırır.
const path = require('path'), fs = require('fs');
const { chromium } = require(process.env.PLAYWRIGHT_MODULE || 'playwright');
const PORT = process.argv[2] || '8765';
(async () => {
  const tarayici = await chromium.launch({ executablePath: process.env.CHROMIUM || undefined });
  const sayfa = await tarayici.newPage();
  await sayfa.goto(`http://127.0.0.1:${PORT}/test-basligi--123`);
  const kod = fs.readFileSync(path.join(__dirname, '..', 'araclar', 'eksi-link-toplayici.js'), 'utf8');
  const cikti = (ayar) => sayfa.evaluate(([k, a]) => { window.__eksiLinkAyar = a; return eval(k); }, [kod, ayar]);
  const hata = [];
  const kontrol = (kosul, mesaj) => { if (!kosul) hata.push(mesaj); console.log((kosul ? 'GEÇTİ  ' : 'KALDI  ') + mesaj); };

  // 1) Son 3 sayfa (5,4,3): sondan başlama ve sayfa sınırı
  let m = await cikti({ sayfaAdedi: 3, beklemeMs: 10, indirme: false });
  let veri = m.split('\n').filter(s => s && !s.startsWith('#')).map(s => s.split('\t'));
  kontrol(m.includes('# aralik: 5-3'), 'aralık son sayfadan geriye 5-3');
  kontrol(veri[0][1] === '5', 'ilk satır son sayfadan (5)');
  kontrol(!veri.some(r => r[1] === '1' || r[1] === '2'), 'aralık dışı sayfalar (1,2) alınmadı');
  kontrol(veri.some(r => r[0] === 'https://github.com/kisi/proje') && veri.some(r => r[0] === 'https://github.com/kisi/baska'), 'a.url bağlantıları toplandı');
  kontrol(veri.some(r => r[0] === 'https://yeni-site.example/araç?ref=eksi'), 'yol/sorgu ile tam bağlantı korunur');
  kontrol(veri.some(r => r[0] === 'https://i.imgur.com/abc.png'), 'ham veride görsel de var (eleme python tarafında)');
  kontrol(!veri.some(r => r[0].includes('?q=x')), 'ekşi içi (bkz) bağlantıları alınmadı');

  // 2) Tüm başlık: düz metindeki alan adı
  m = await cikti({ sayfaAdedi: 200, beklemeMs: 10, indirme: false });
  veri = m.split('\n').filter(s => s && !s.startsWith('#')).map(s => s.split('\t'));
  kontrol(m.includes('# aralik: 5-1'), 'sayfa adedi toplamdan büyükse 1. sayfada durur');
  kontrol(veri.some(r => r[0] === 'https://ornekaraclar.com'), 'linksiz yazılan alan adı yakalandı');
  kontrol(veri.some(r => r[0] === 'https://metin.dev'), 'cümle sonundaki alan adı noktasız yakalandı');
  fs.writeFileSync(path.join(__dirname, 'fixture', 'eksi-linkler.txt'), m);

  // 3) Yarıda kalma/devam: localStorage'daki ilerlemeden devam
  await sayfa.evaluate(() => localStorage.setItem('eksiLinkToplayici:/test-basligi--123',
    JSON.stringify({ toplam: 5, siradaki: 2, bitis: 1, satirlar: [['https://onceki.example/', 3, '300']], tarih: 'x' })));
  m = await cikti({ sayfaAdedi: 200, beklemeMs: 10, indirme: false });
  kontrol(m.includes('https://onceki.example/\t3\t300') && m.includes('https://ornekaraclar.com\t2'), 'kaldığı yerden devam eder');
  kontrol(!m.includes('\t5\t'), 'devamda bitmiş sayfalar tekrar gezilmez');
  await tarayici.close();
  if (hata.length) { console.error(hata.length + ' test kaldı'); process.exit(1); }
  console.log('toplayıcı testleri: hepsi geçti');
})().catch(e => { console.error(e); process.exit(1); });
