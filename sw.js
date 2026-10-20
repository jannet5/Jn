/* ==========================================================
   Köken servis çalışanı — YALNIZ kabuk dosyalarını önbelleğe alır.
   Vikisözlük / API / başka origin isteklerine ASLA dokunmaz
   (respondWith çağrılmaz, tarayıcı normal ağ akışını sürdürür).
   ========================================================== */

// Sürümü artırınca eski önbellekler silinir.
const CACHE = 'koken-kabuk-v1';

// Önbelleğe alınacak kabuk dosyaları (göreli yollar).
const KABUK = [
  './',
  './index.html',
  './style.css',
  './app.js',
  './etym.js',
  './manifest.webmanifest',
  './icons/icon.svg',
  './icons/icon-192.png',
  './icons/icon-512.png',
  './fonts/fraunces-latin-400-normal.woff2',
  './fonts/fraunces-latin-ext-400-normal.woff2',
  './fonts/fraunces-latin-600-normal.woff2',
  './fonts/fraunces-latin-ext-600-normal.woff2',
  './fonts/fraunces-latin-400-italic.woff2',
  './fonts/fraunces-latin-ext-400-italic.woff2',
  './fonts/source-serif-4-latin-400-normal.woff2',
  './fonts/source-serif-4-latin-ext-400-normal.woff2'
];

// Kurulum: kabuğu indir; tek dosya eksik olsa bile kurulumu bozma.
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE)
      .then((cache) => Promise.all(KABUK.map((u) => cache.add(u).catch(() => null))))
      .then(() => self.skipWaiting())
  );
});

// Etkinleşme: eski sürüm önbelleklerini temizle ve açık sayfaları devral.
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys()
      .then((keys) => Promise.all(keys.filter((k) => k !== CACHE).map((k) => caches.delete(k))))
      .then(() => self.clients.claim())
  );
});

// İstek yakalama: yalnız aynı origin + GET + kabuk yolu; gerisi dokunulmadan geçer.
self.addEventListener('fetch', (event) => {
  const req = event.request;
  if (req.method !== 'GET') return;
  const url = new URL(req.url);
  // Başka origin (Vikisözlük, API, çeviri uçları...) → hiç karışma.
  if (url.origin !== self.location.origin) return;
  // Kabuk dışı yol → hiç karışma.
  const base = new URL('./', self.location.href).pathname;
  const rel = url.pathname.startsWith(base) ? url.pathname.slice(base.length) : null;
  if (rel === null) return;
  const bilinen = rel === '' || KABUK.some((k) => k.replace(/^\.\//, '') === rel);
  if (!bilinen) return;

  // Önbellekten hemen ver, arka planda tazele (stale-while-revalidate).
  event.respondWith(
    caches.open(CACHE).then((cache) =>
      cache.match(req, { ignoreSearch: true }).then((hit) => {
        const ag = fetch(req).then((resp) => {
          if (resp && resp.ok) cache.put(req, resp.clone());
          return resp;
        }).catch(() => null);
        if (hit) { ag.catch(() => null); return hit; }
        // Önbellekte yoksa ağdan; ağ da yoksa sayfa gezintisi için index.html'e düş.
        return ag.then((resp) => resp || (req.mode === 'navigate' ? cache.match('./index.html') : Response.error()));
      })
    )
  );
});
