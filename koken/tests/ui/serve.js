// Küçük statik sunucu: web/ klasörünü sunar. MOCK=1 veya web/etym.js yoksa /etym.js sahte motordan gelir.
const http = require('http');
const fs = require('fs');
const path = require('path');

const ROOT = path.resolve(__dirname, '../../web');
const MIME = {
  '.html': 'text/html; charset=utf-8', '.js': 'text/javascript; charset=utf-8', '.css': 'text/css; charset=utf-8',
  '.webmanifest': 'application/manifest+json', '.svg': 'image/svg+xml', '.png': 'image/png', '.woff2': 'font/woff2', '.json': 'application/json'
};

// Sunucuyu başlat ve {server, url} döndür.
function start(opts = {}) {
  const mock = opts.mock ?? (process.env.MOCK === '1' || !fs.existsSync(path.join(ROOT, 'etym.js')));
  const server = http.createServer((req, res) => {
    let p = decodeURIComponent(req.url.split('?')[0]);
    if (p === '/') p = '/index.html';
    // Sahte motor isteği
    let file = path.join(ROOT, p);
    if (p === '/etym.js' && mock) file = path.join(__dirname, 'mock-etym.js');
    if (!file.startsWith(ROOT) && !file.endsWith('mock-etym.js')) { res.writeHead(403); return res.end(); }
    fs.readFile(file, (err, buf) => {
      if (err) { res.writeHead(404); return res.end('yok'); }
      res.writeHead(200, { 'content-type': MIME[path.extname(file)] || 'application/octet-stream', 'cache-control': 'no-store' });
      res.end(buf);
    });
  });
  return new Promise((ok) => server.listen(0, '127.0.0.1', () => ok({ server, url: 'http://127.0.0.1:' + server.address().port + '/', mock })));
}
module.exports = { start };
if (require.main === module) start().then((s) => console.log(s.url, s.mock ? '(sahte motor)' : '(gerçek motor)'));
