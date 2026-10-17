// Köken web arayüzünü TEK dosyaya (dist/koken.html) toplar: css, js, yazı tipleri ve simge satır içi.
// Kullanım: node web/build-single.mjs   (ETYM=/yol/etym.js ile motor yolu değiştirilebilir)
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

// Klasör yolları: bu dosyanın yanındaki web/ ve üstündeki dist/.
const WEB = path.dirname(fileURLToPath(import.meta.url));
const DIST = path.resolve(WEB, '../dist');
const OUT = path.join(DIST, 'koken.html');

// Dosya oku (yoksa anlaşılır hata ver).
function read(rel, abs) {
  const p = abs || path.join(WEB, rel);
  if (!fs.existsSync(p)) { console.error('EKSİK DOSYA: ' + p); process.exit(1); }
  return fs.readFileSync(p);
}

// <script> içine gömülecek metinde bitiş etiketini kır (HTML ayrıştırıcısı yanlış kapatmasın).
const safeJs = (s) => s.replace(/<\/script/gi, '<\\/script');

// 1) CSS: url(fonts/..woff2) → data URI
let css = read('style.css').toString('utf8');
css = css.replace(/url\((fonts\/[^)]+\.woff2)\)/g, (_, f) => 'url(data:font/woff2;base64,' + read(f).toString('base64') + ')');

// 2) Simge: SVG'yi data URI yap
const svg = read('icons/icon.svg');
const iconUri = 'data:image/svg+xml;base64,' + svg.toString('base64');

// 3) JS: önce motor, sonra uygulama
const etym = read('etym.js', process.env.ETYM || undefined).toString('utf8');
const app = read('app.js').toString('utf8');

// 4) index.html'i dönüştür
let html = read('index.html').toString('utf8');
html = html
  .replace(/<link rel="manifest"[^>]*>\s*/, '')                       // tek dosyada manifest yok
  .replace(/<link rel="apple-touch-icon"[^>]*>\s*/, '')
  .replace(/<link rel="icon"[^>]*>/, '<link rel="icon" href="' + iconUri + '" type="image/svg+xml">')
  .replace(/<link rel="stylesheet" href="style.css">/, () => '<style>\n' + css + '\n</style>')
  .replace(/<script src="etym.js"><\/script>/, () => '<script>\n' + safeJs(etym) + '\n</script>')
  .replace(/<script src="app.js"><\/script>/, () => '<script>\n' + safeJs(app) + '\n</script>');

// 5) Dışarıya bağımlılık kalmadığını doğrula
const left = html.match(/(?:src|href)="(?!#|data:|https?:|\.\/")[^"]+"/g);
if (left) { console.error('Dışarı bağlantı kaldı:', left); process.exit(1); }

fs.mkdirSync(DIST, { recursive: true });
fs.writeFileSync(OUT, html);
console.log('yazıldı', OUT, (html.length / 1024).toFixed(0) + ' KB');
