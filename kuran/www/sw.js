/* Çevrimdışı önbellek: ilk açılıştan sonra internet gerekmez. Sürüm değişince V'yi artır. */
var V = 'kuran-v1';
var FILES = ['./', 'index.html', 'style.css', 'app.js', 'data.js', 'fonts/amiri-quran.woff2', 'manifest.webmanifest', 'icon.svg', 'icon-192.png', 'icon-512.png'];
self.addEventListener('install', function (e) {          // kurulumda tüm dosyaları sakla
  e.waitUntil(caches.open(V).then(function (c) { return c.addAll(FILES); }).then(function () { return self.skipWaiting(); }));
});
self.addEventListener('activate', function (e) {         // eski sürümleri sil
  e.waitUntil(caches.keys().then(function (ks) { return Promise.all(ks.filter(function (k) { return k !== V; }).map(function (k) { return caches.delete(k); })); }).then(function () { return self.clients.claim(); }));
});
self.addEventListener('fetch', function (e) {            // önce önbellek, yoksa ağ
  if (e.request.method !== 'GET') return;
  e.respondWith(caches.match(e.request, { ignoreSearch: true }).then(function (r) { return r || fetch(e.request); }));
});
