// Oyunu tek dosyaya birleştirir: konsept çizimleri (generate.js) + game.js + shell.html -> index.html
const fs = require('fs'), path = require('path');
const gen = fs.readFileSync(path.join(__dirname, '../concepts/generate.js'), 'utf8');
const art = gen.slice(gen.indexOf('const W = 1280'), gen.indexOf('// ---------- Kart'));
const game = fs.readFileSync(path.join(__dirname, 'game.js'), 'utf8');
const shell = fs.readFileSync(path.join(__dirname, 'shell.html'), 'utf8');
const out = `${shell}\n<script>\n${art}\n${game}\n</script>\n`;
fs.writeFileSync(path.join(__dirname, 'index.html'), out);
console.log('index.html', (out.length / 1024).toFixed(0) + 'KB');
// Tek başına açılan tam sürüm (Android / tarayıcı)
const full = `<!doctype html><html lang="tr"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1,viewport-fit=cover"><style>body{margin:0}</style></head><body>${out}</body></html>`;
fs.writeFileSync(path.join(__dirname, 'bocek-ringi.html'), full);
