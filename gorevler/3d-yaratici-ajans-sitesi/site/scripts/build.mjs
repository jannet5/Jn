// Derleme: src → dist/ (tek IIFE betiği; file:// ile çift tıklayarak da açılır)
import { build } from 'esbuild';
import { mkdir, copyFile, rm, readFile, writeFile } from 'node:fs/promises';

const out = new URL('../dist/', import.meta.url);
await rm(out, { recursive: true, force: true });
await mkdir(new URL('assets/', out), { recursive: true });

await build({
  entryPoints: ['src/main.js'],
  bundle: true,
  format: 'iife',
  minify: true,
  target: ['es2020'],
  legalComments: 'eof',
  outfile: 'dist/assets/app.js',
  logLevel: 'info',
});
await build({
  entryPoints: ['src/styles.css'],
  bundle: true,
  minify: true,
  outfile: 'dist/assets/styles.css',
  logLevel: 'info',
});
await copyFile(new URL('../index.html', import.meta.url), new URL('index.html', out));
// Üçüncü taraf lisans metinleri dağıtımla birlikte gider
await copyFile(new URL('../node_modules/three/LICENSE', import.meta.url), new URL('assets/LICENSE-three.txt', out));
await copyFile(new URL('../node_modules/lenis/LICENSE', import.meta.url), new URL('assets/LICENSE-lenis.txt', out));
// Tek dosya sürüm: CSS ve JS gömülü, tek HTML olarak paylaşılabilir
let page = await readFile(new URL('index.html', out), 'utf8');
const css = await readFile(new URL('assets/styles.css', out), 'utf8');
const js = (await readFile(new URL('assets/app.js', out), 'utf8')).replace(/<\/script/gi, '<\\/script');
page = page
  .replace('<link rel="stylesheet" href="assets/styles.css">', () => `<style>${css}</style>`)
  .replace('<script src="assets/app.js" defer></script>', () => `<script>${js}</script>`);
if (page.includes('assets/')) throw new Error('tek dosya sürümde dış dosya başvurusu kaldı');
await writeFile(new URL('sarmal-studyo.html', out), page);
console.log('dist/ hazır (index.html + sarmal-studyo.html)');
