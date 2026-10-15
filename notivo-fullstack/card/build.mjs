// Kart sürümünü card/dist/ altına derler: web arayüzü + AYNI çekirdek (server/shade.js) + sayfa içi SQLite taşıması.
// Çıktı düz bir dizindir; Artifact olarak olduğu gibi yayınlanabilir. Çalıştırma: npm run build:card
import { readFileSync, writeFileSync, mkdirSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { dirname, join } from 'node:path';

const root = join(dirname(fileURLToPath(import.meta.url)), '..');
const dist = join(root, 'card', 'dist');
mkdirSync(dist, { recursive: true });

const SQLJS_VERSION = '1.13.0'; // cdnjs'de sabitlenmiş sürüm; test için node_modules'taki ile aynı olmalı
const pinned = JSON.parse(readFileSync(join(root, 'node_modules/sql.js/package.json'), 'utf8')).version;
if (pinned !== SQLJS_VERSION) throw new Error(`sql.js sürümü uyuşmuyor: kurulu ${pinned}, kart ${SQLJS_VERSION}`);

// Düz dizinde olacağı için içe aktarma yollarını düzleştir.
const flat = (src) => src.replaceAll("'../server/", "'./").replaceAll("'../web/", "'./");
const copy = (from, to, transform = (s) => s) => writeFileSync(join(dist, to), transform(readFileSync(join(root, from), 'utf8')));

for (const f of ['app.js', 'sim.js', 'style.css']) copy(`web/${f}`, f);
for (const f of ['shade.js', 'textlogic.js', 'schema.js']) copy(`server/${f}`, f);
for (const f of ['transport.js', 'local-api.js', 'sqljs-shim.js']) copy(`card/${f}`, f, flat);

// index: Artifact çerçevesi doctype/html/head/body'yi kendisi ekler; yalnızca içeriği yaz.
const html = readFileSync(join(root, 'web/index.html'), 'utf8');
const title = /<title>(.*?)<\/title>/s.exec(html)[1];
const body = /<body>\s*([\s\S]*?)\s*<\/body>/.exec(html)[1];
writeFileSync(join(dist, 'index.html'), `<title>${title}</title>
<link rel="stylesheet" href="style.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/sql.js/${SQLJS_VERSION}/sql-asm.js"></script>
<script type="module" src="app.js"></script>
${body}
`);
console.log(`Kart derlendi: ${dist}`);
