// Kart sürümüne özel "Proje" paneli: Android uygulaması indirme, istek listesi, rakip özellik karşılaştırması.
// Metinler yalnızca textContent ile yazılır. __APK_*__ yer tutucularını card/build.mjs doldurur.

const APK = { version: '__APK_VERSION__', sha: '__APK_SHA__', size: '__APK_SIZE__' };

const REQUESTS = [
  ['ok', 'Bildirim çubuğundaki mesajları tek uygulamada toplayan, çok özellikli Android uygulaması', `Notivo ${APK.version}. Release derlendi, lint temiz, JVM testleri geçti.`],
  ['ok', 'SEO açısından iyi marka adı', 'Notivo. NotiBox adı Play\'de zaten vardı. Play\'de ve marka kaydında müsaitliği doğrulanmadı.'],
  ['no', 'Telefonuma USB ile yükle (ve tekrar yükle)', 'Yapılamadı: bulut oturumunun USB ve telefon erişimi yok, bilgisayarındaki bir oturuma da erişilemedi. Yerine bu karttan indirebilirsin.'],
  ['no', 'Gerçek telefonda deneme', 'Yapılamadı: ortamda emülatör, cihaz ve KVM yok. Testler JVM simülasyonudur, cihaz testi değildir.'],
  ['part', 'Beğenirsem Google Play\'e atacağız', 'Hazırlık yapıldı (mağaza metni, marka notları). Yükleme sende: kendi imza anahtarın, yayınlanmış gizlilik politikası, ad kontrolü.'],
  ['ok', 'Özellik eksiği olmasın, internetteki checklist\'e bak', 'Rakip uygulamaların özellik listeleri çıkarıldı; eksik 7 özellik eklendi (Özellikler sekmesi).'],
  ['part', 'Tasarım yaparken ve özellik eklerken hep internete bak', 'Özellik, isim ve Play politikası araştırıldı. Ekran tasarımı için ayrı bir araştırma yapılmadı.'],
  ['ok', 'İstekleri tek tek karşılaştır, istenmeyeni ekleme', 'İngilizce çeviri ve mağaza görselleri istenmediği için eklenmedi.'],
  ['ok', 'WhatsApp gibi uygulamaya göre gruplansın', 'Uygulamaya göre görünüm (varsayılan): başlık, sayılar, son 3 bildirim, "N daha göster".'],
  ['ok', 'Android bildirim çubuğunun aynısı, fullstack, uygulamanın kopyasında', 'notivo-fullstack: aynı çekirdek hem sunucuda hem bu kartta çalışır.'],
  ['ok', 'Telefonda kullanacağım, kart olarak ver', 'Bu kart.'],
];

// [özellik, durum, not] durum: ok | part | no
const FEATURES = [
  ['Bildirim geçmişi ve arama', 'ok', 'Kapatılan bildirimler de saklanır; Türkçe harf duyarsız arama.'],
  ['Uygulamaya göre zaman çizelgesi', 'ok', 'Uygulamaya göre / zamana göre / konuşmalara göre görünüm.'],
  ['Uygulamayı kara listeye alma', 'ok', 'Toplama dışı bırakma + kurallar (engelle, sessize al).'],
  ['Hassas içeriği saklamama', 'ok', 'Gizli ve süregelen bildirimleri atlama ayarı.'],
  ['Parmak izi / ekran kilidi', 'ok', 'Telefonda ekran kilidi yoksa açılmasına izin verilmez.'],
  ['Yedekle / geri yükle, dışa aktar', 'ok', 'JSON yedek ve CSV.'],
  ['Otomatik silme (gün limiti)', 'ok', '7 / 30 / 90 gün ya da süresiz.'],
  ['İstatistik, günlük özet', 'ok', 'Son 14 gün grafiği ve isteğe bağlı günlük özet.'],
  ['Kaydırarak silme, favoriler', 'ok', 'Yıldızlılar temizlemede korunur.'],
  ['Okundu işareti bırakmadan okuma', 'ok', 'Notivo yalnızca dinler; gönderene okundu bilgisi gitmez.'],
  ['Hatırlat / ertele', 'ok', 'Yeniden başlatmada kaybolmaz.'],
  ['Bildirimi yeniden gösterme (FilterBox, Pinnit)', 'ok', 'Yeni: özgün saat ve uygulama simgesiyle, sessiz kanalda.'],
  ['Metni kopyalama (FilterBox)', 'ok', 'Yeni: bildirim menüsünde.'],
  ['Sıralama: yeni/eski (Pinnit)', 'ok', 'Yeni: tüm görünümlerde, seçim hatırlanır.'],
  ['Tarih filtresi (Pinnit, Notification History)', 'ok', 'Yeni: tüm zaman, bugünkü, dünkü, 7 gün, 30 gün.'],
  ['Kendi hatırlatıcını ekleme (Pinnit)', 'ok', 'Yeni: başlık, not, 15 dk – yarın.'],
  ['Sesli okuma (NotiLog, Shouter)', 'part', 'Yeni: telefonun metin okuma motoruyla. Cihazda denenmedi.'],
  ['Ana ekran widget\'ı (Notification History Log)', 'part', 'Yeni: okunmamış sayısı + en yeni 3 bildirim. Cihazda denenmedi.'],
  ['Bildirim sesini değiştiren kural (FilterBox)', 'no', 'Android başka uygulamanın sesini değiştirmeye izin vermez; eklenmedi.'],
  ['İngilizce arayüz', 'no', 'İstenmedi, eklenmedi (kaynak listelerinde de geçmiyor).'],
];

const $ = (s, r = document) => r.querySelector(s);
const el = (tag, cls, text) => {
  const e = document.createElement(tag);
  if (cls) e.className = cls;
  if (text !== undefined && text !== null) e.textContent = text;
  return e;
};
const CHIP = { ok: ['chip-ok', 'Yapıldı'], part: ['chip-part', 'Kısmen'], no: ['chip-no', 'Yok'] };

function rows(list) {
  return list.map(([st, title, note]) => {
    const [cls, label] = CHIP[st];
    const r = el('div', 'req');
    const top = el('div', 'req-top');
    top.append(el('span', 'req-title', title), el('span', cls, label));
    r.append(top, el('span', 'req-note', note));
    return r;
  });
}

let apkBlob = null;
async function downloadApk(statusEl, btn) {
  btn.disabled = true;
  statusEl.textContent = 'Hazırlanıyor…';
  try {
    const dl = await window.claude?.use?.('downloads');
    if (!dl) throw new Error('Bu görünümde dosya kaydetme kullanılamıyor. Bilgisayardan USB yolunu kullan.');
    if (!apkBlob) {
      const t = await (await fetch('notivo-android.b64.txt')).text();
      const bin = atob(t.trim());
      const u = new Uint8Array(bin.length);
      for (let i = 0; i < bin.length; i++) u[i] = bin.charCodeAt(i);
      apkBlob = new Blob([u], { type: 'application/zip' });
    }
    await dl.save({ filename: 'notivo-android.zip', data: apkBlob });
    statusEl.textContent = 'Kaydedildi. Dosyalar uygulamasında zip\'i aç, içindeki notivo-release.apk\'ya dokun.';
  } catch (e) {
    statusEl.textContent = e?.code === 'declined' ? 'Vazgeçtin. Tekrar denemek için düğmeye bas.' : (e?.message || 'Kaydedilemedi.');
  } finally {
    btn.disabled = false;
  }
}

function androidPanel() {
  const p = el('div', 'panel');
  p.append(
    el('p', '', `Notivo ${APK.version}: telefona gelen tüm bildirimleri tek kutuda toplar; arar, filtreler, erteler, silinenleri işaretler. Veriler yalnızca telefonda kalır, İnternet izni yoktur.`),
  );
  const btn = el('button', 'btn primary big', `notivo-android.zip indir (${APK.size})`);
  btn.type = 'button';
  const status = el('p', 'hint');
  status.setAttribute('aria-live', 'polite');
  btn.addEventListener('click', () => downloadApk(status, btn));
  p.append(btn, status, el('h3', '', 'Kurulum'));
  const ol = el('ol', 'steps');
  for (const s of [
    'Telefonda bu sayfayı açıp yukarıdaki düğmeyle zip\'i indir.',
    'Dosyalar uygulamasında zip\'i aç, içindeki notivo-release.apk\'ya dokun.',
    'Telefon "bilinmeyen kaynaklardan yükle" izni isterse ver. Güvenlik uyarılarını atlatmak için bir şey yapma; uyarıyı kendin onaylarsın.',
    'Notivo\'yu aç, "Bildirim erişimini aç" de, listeden Notivo\'yu etkinleştir. Android 13+ "kısıtlı ayar" derse: Ayarlar → Uygulamalar → Notivo → ⋮ → Kısıtlı ayarlara izin ver.',
  ]) ol.append(el('li', '', s));
  p.append(ol, el('h3', '', 'Bilgisayardan USB ile'), el('p', 'mono', 'git fetch origin claude/focused-wright-uldgap && git checkout claude/focused-wright-uldgap\nadb install -r notivo/dist/notivo-release.apk'),
    el('h3', '', 'Doğrulama'), el('p', 'mono', `APK SHA-256: ${APK.sha}`),
    el('p', 'hint', 'Gerçek telefonda denenmedi (derleme ve JVM testleri yapıldı). Hata çıkarsa yaz.'));
  return p;
}

function build() {
  const quick = $('.quick');
  if (!quick || $('#tileProject')) return;
  const tile = el('button', 'tile');
  tile.type = 'button';
  tile.id = 'tileProject';
  tile.append(el('span', '', 'Proje'));
  quick.append(tile);

  const dlg = el('dialog', 'sheet tall');
  dlg.id = 'dlgProject';
  dlg.setAttribute('aria-labelledby', 'projTitle');
  const h2 = el('h2', '', 'Proje');
  h2.id = 'projTitle';
  const tabs = el('div', 'tabs');
  tabs.setAttribute('role', 'tablist');
  const body = el('div', 'panel');
  const defs = [
    ['Android', androidPanel],
    ['İstekler', () => { const p = el('div', 'panel'); p.append(...rows(REQUESTS)); return p; }],
    ['Özellikler', () => { const p = el('div', 'panel'); p.append(el('p', 'hint', 'Rakip uygulamaların özellik listeleriyle (Play sayfaları, Android Ally, TechCrunch/Pinnit) karşılaştırma.'), ...rows(FEATURES)); return p; }],
  ];
  const show = (i) => {
    [...tabs.children].forEach((b, j) => b.setAttribute('aria-selected', String(i === j)));
    body.replaceWith(body.cloneNode(false));
    const fresh = dlg.querySelector('.panel');
    fresh.replaceWith(defs[i][1]());
  };
  defs.forEach(([label], i) => {
    const b = el('button', 'tab', label);
    b.type = 'button';
    b.setAttribute('role', 'tab');
    b.addEventListener('click', () => show(i));
    tabs.append(b);
  });
  const close = el('button', 'sheet-close', 'Kapat');
  close.type = 'button';
  close.setAttribute('data-close', '');
  dlg.append(h2, tabs, body, close);
  dlg.addEventListener('click', (e) => {
    if (e.target.closest('[data-close]')) return dlg.close();
    const r = dlg.getBoundingClientRect();
    if (e.target === dlg && (e.clientX < r.left || e.clientX > r.right || e.clientY < r.top || e.clientY > r.bottom)) dlg.close();
  });
  document.body.append(dlg);
  tile.addEventListener('click', () => { show(0); dlg.showModal(); });
}

build();
