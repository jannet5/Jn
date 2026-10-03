// Derleme: src → dist/ (tek IIFE betiği; file:// ile çift tıklayarak da açılır)
import { build } from 'esbuild';
import { mkdir, copyFile, rm } from 'node:fs/promises';

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
console.log('dist/ hazır');
