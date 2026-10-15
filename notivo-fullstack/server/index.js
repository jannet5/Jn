import { fileURLToPath } from 'node:url';
import { dirname, join, resolve } from 'node:path';
import { mkdirSync } from 'node:fs';
import { createApp } from './app.js';

const here = dirname(fileURLToPath(import.meta.url));
const root = resolve(here, '..');
const PORT = Number(process.env.PORT ?? 8790);
const HOST = process.env.HOST ?? '127.0.0.1';
const TOKEN = process.env.NOTIVO_TOKEN ?? '';
const DB = process.env.NOTIVO_DB ?? join(root, 'data', 'notivo.db');

const loopback = ['127.0.0.1', '::1', 'localhost'].includes(HOST);
if (!loopback && !TOKEN) {
  console.error('Güvenlik: HOST yerel değilken NOTIVO_TOKEN zorunlu. Örnek: NOTIVO_TOKEN=$(openssl rand -hex 24) HOST=0.0.0.0 npm start');
  process.exit(1);
}
if (!Number.isInteger(PORT) || PORT < 0 || PORT > 65535) {
  console.error('PORT geçersiz');
  process.exit(1);
}

mkdirSync(dirname(DB), { recursive: true });
const app = createApp({ dbPath: DB, token: TOKEN, webRoot: join(root, 'web') });
app.server.listen(PORT, HOST, () => {
  const where = loopback ? `http://localhost:${PORT}` : `http://${HOST}:${PORT}`;
  console.log(`Notivo bildirim çubuğu hazır: ${where}  (veritabanı: ${DB}, token: ${TOKEN ? 'açık' : 'kapalı, yalnızca localhost'})`);
});

const stop = async () => {
  await app.close();
  process.exit(0);
};
process.on('SIGINT', stop);
process.on('SIGTERM', stop);
