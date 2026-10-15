// Uygulama simülatörü: gerçek uygulamaların NotificationManager.notify() ile yaptığını HTTP API üzerinden yapar.
// Sunucu için bu istekler gerçek bir uygulamadan gelenlerle aynıdır.

const APPS = {
  wa: { pkg: 'com.whatsapp', appName: 'WhatsApp', channels: [{ id: 'msg', name: 'Mesajlar', importance: 4 }] },
  gmail: { pkg: 'com.google.android.gm', appName: 'Gmail', channels: [{ id: 'mail', name: 'E-posta', importance: 3 }] },
  music: { pkg: 'com.spotify.music', appName: 'Müzik', channels: [{ id: 'playback', name: 'Çalan şarkı', importance: 2 }] },
  news: { pkg: 'com.haber.uygulama', appName: 'Haber', channels: [{ id: 'news', name: 'Son dakika', importance: 2 }] },
  sec: { pkg: 'com.guvenlik.alarm', appName: 'Güvenlik', channels: [{ id: 'alarm', name: 'Alarmlar', importance: 4 }] },
  dl: { pkg: 'com.android.downloads', appName: 'İndirmeler', channels: [{ id: 'dl', name: 'İndirmeler', importance: 2 }] },
  insta: { pkg: 'com.instagram.android', appName: 'Instagram', channels: [{ id: 'n', name: 'Etkinlik', importance: 3 }] },
  cal: { pkg: 'com.takvim.uygulama', appName: 'Takvim', channels: [{ id: 'rem', name: 'Hatırlatıcılar', importance: 3 }] },
};

const ready = new Set();
async function ensure(api, app) {
  if (ready.has(app.pkg)) return;
  for (const c of app.channels) await api('POST', `/api/apps/${app.pkg}/channels`, { ...c, appName: app.appName });
  ready.add(app.pkg);
}
async function notify(api, app, body) {
  await ensure(api, app);
  return api('POST', '/api/notify', { pkg: app.pkg, ...body });
}

const SENDERS = [
  { id: 1, name: 'Ayşe' },
  { id: 2, name: 'Mehmet' },
  { id: 3, name: 'Zeynep' },
];
const LINES = ['Akşam görüşelim mi?', "Toplantı 15:00'e alındı", 'Fotoğrafları gönderdim', 'Tamam, geliyorum', 'Market listesi hazır mı?'];
const MAILS = [['Banka', 'Ekstreniz hazır'], ['Takvim', 'Yarınki toplantı için davet'], ['Kargo', 'Paketiniz yola çıktı'], ['Ali', 'Re: Hafta sonu planı']];
const FOLLOWERS = ['ali_k', 'zeynep.t', 'mert_44', 'ayse.d', 'can_b'];

let waCount = 0, mailCount = 0, newsCount = 0, alarmCount = 0, calCount = 0, downloading = false, playing = true;
const convo = new Map(); // anahtar → gönderen (yanıt olayında kullanılır)

const waActions = [{ id: 'reply', title: 'Yanıtla', reply: true }, { id: 'read', title: 'Okundu işaretle' }];
const musicBody = (p) => ({
  channel: 'playback', id: 1, title: p ? 'Gece Yolculuğu' : 'Duraklatıldı', text: 'Kayıp Şehir · Sessiz Albüm', ongoing: true, onlyAlertOnce: true,
  actions: [{ id: 'prev', title: 'Önceki' }, p ? { id: 'pause', title: 'Duraklat' } : { id: 'play', title: 'Oynat' }, { id: 'next', title: 'Sonraki' }],
});

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

const PRESETS = [
  {
    title: 'WhatsApp mesajı', desc: 'Konuşma, yüksek önem: heads-up + yanıt',
    run: async (api) => {
      const s = SENDERS[waCount % SENDERS.length];
      const text = LINES[(waCount * 2 + 1) % LINES.length];
      waCount++;
      convo.set(`${APPS.wa.pkg}|${s.id}|`, s);
      return notify(api, APPS.wa, { channel: 'msg', id: s.id, title: s.name, text, conversation: true, category: 'msg', contentIntent: `whatsapp://chat/${s.name.toLowerCase()}`, actions: waActions });
    },
  },
  {
    title: 'Mesaj silindi', desc: 'Gönderen son mesajı siler: Notivo işaretler',
    run: (api) => notify(api, APPS.wa, { channel: 'msg', id: 1, title: 'Ayşe', text: 'Bu mesaj silindi', conversation: true }),
  },
  {
    title: 'Gmail iletisi', desc: 'Grup + özet: ≥2 ileti tek grupta',
    run: async (api) => {
      const [from, subject] = MAILS[mailCount % MAILS.length];
      const r = await notify(api, APPS.gmail, { channel: 'mail', id: 100 + mailCount, group: 'inbox', title: from, text: subject, contentIntent: 'mailto:gelen-kutusu' });
      mailCount++;
      await notify(api, APPS.gmail, { channel: 'mail', id: 1, group: 'inbox', groupSummary: true, title: 'Gelen kutusu', text: `${mailCount} yeni ileti` });
      return r;
    },
  },
  {
    title: 'Müzik çalar', desc: 'Süregelen: kaydırılamaz, temizlenmez',
    run: (api) => { playing = true; return notify(api, APPS.music, musicBody(true)); },
  },
  {
    title: 'Sessiz haber', desc: 'Düşük önem: sessiz bölüm, ses yok',
    run: (api) => notify(api, APPS.news, { channel: 'news', id: 200 + newsCount++, title: 'Haber', text: `Gündem özeti #${newsCount}` }),
  },
  {
    title: 'Alarm', desc: "Kategori alarm: Rahatsız Etme'yi aşar",
    run: (api) => notify(api, APPS.sec, { channel: 'alarm', id: 300 + alarmCount++, title: 'Güvenlik uyarısı', text: 'Kapı sensörü tetiklendi', category: 'alarm', actions: [{ id: 'ack', title: 'Gördüm' }] }),
  },
  {
    title: 'İndirme ilerlemesi', desc: 'Aynı bildirim yerinde güncellenir, sonra zaman aşımı',
    run: async (api, ctx) => {
      if (downloading) return { status: 'busy' };
      downloading = true;
      (async () => {
        try {
          for (let p = 0; p <= 100; p += 10) {
            await notify(api, APPS.dl, { channel: 'dl', id: 1, title: 'dosya.zip indiriliyor', text: `%${p}`, ongoing: true, onlyAlertOnce: true });
            await sleep(600);
          }
          await notify(api, APPS.dl, { channel: 'dl', id: 1, title: 'dosya.zip', text: 'İndirme tamamlandı (8 sn sonra kalkar)', timeoutAfterMs: 8000, contentIntent: 'file://dosya.zip' });
        } catch (e) {
          ctx.toast(e.message);
        } finally {
          downloading = false;
        }
      })();
      return { status: 'started' };
    },
  },
  {
    title: 'Instagram ×5', desc: 'Gruplanmamış 4+ bildirim otomatik gruplanır',
    run: async (api) => {
      let r;
      for (let i = 0; i < 5; i++) r = await notify(api, APPS.insta, { channel: 'n', id: 400 + i, title: FOLLOWERS[i], text: 'gönderini beğendi' });
      return r;
    },
  },
  {
    title: 'Takvim (10 sn)', desc: 'setTimeoutAfter: 10 sn sonra kendiliğinden kalkar',
    run: (api) => notify(api, APPS.cal, { channel: 'rem', id: 500 + calCount++, title: 'Hatırlatıcı', text: 'Bu bildirim 10 saniye sonra kalkar', timeoutAfterMs: 10_000 }),
  },
];

const STATUS_TR = {
  posted: 'Bildirim gönderildi', updated: 'Bildirim güncellendi', blocked: 'Engellendi: kanal ya da uygulama kapalı',
  blocked_by_rule: 'Kural engelledi', muted_by_rule: 'Kural sessize aldı (yalnızca geçmişte)', duplicate: 'Kopya olduğu için atlandı',
  deleted_notice: 'Silinen mesaj işaretlendi', started: 'İndirme başladı', busy: 'İndirme zaten sürüyor',
};

export function buildSimulator(container, ctx) {
  const dlg = container.closest('dialog');
  container.replaceChildren(
    ...PRESETS.map((p) => {
      const b = document.createElement('button');
      b.type = 'button';
      b.className = 'sim-btn';
      const t = document.createElement('b');
      t.textContent = p.title;
      const d = document.createElement('span');
      d.textContent = p.desc;
      b.append(t, d);
      b.addEventListener('click', async () => {
        dlg?.close(); // heads-up'ı görmek için paneli kapat
        const r = await ctx.guarded(() => p.run(ctx.api, ctx));
        if (r) ctx.toast(STATUS_TR[r.status] ?? 'Gönderildi');
      });
      return b;
    }),
  );
}

/** Uygulama tarafı geri çağrıları: kullanıcı eylem düğmesine basınca gelen 'action' olayını işler. */
export async function handleAppEvent(ev, { api, toast }) {
  if (ev.type !== 'action') return;
  const [pkg, id, ...tagParts] = ev.key.split('|');
  const nid = Number(id);
  const tag = tagParts.join('|');
  try {
    if (pkg === APPS.wa.pkg) {
      const s = convo.get(ev.key) ?? { name: 'Sohbet' };
      if (ev.actionId === 'reply') {
        // Gerçek uygulamalar yanıttan sonra bildirimi güncelleyip dönen göstergeyi durdurur.
        await api('POST', '/api/notify', { pkg, channel: 'msg', id: nid, tag, title: s.name, text: `Sen: ${ev.text}`, conversation: true, onlyAlertOnce: true, actions: waActions });
        // kullanıcı zaten "Yanıt gönderildi" uyarısını görüyor; ikinci bir uyarı onu ezmesin
      } else if (ev.actionId === 'read') {
        await api('POST', '/api/cancel', { pkg, id: nid, tag });
      }
    } else if (pkg === APPS.music.pkg) {
      if (ev.actionId === 'pause') playing = false;
      else if (ev.actionId === 'play') playing = true;
      if (['pause', 'play'].includes(ev.actionId)) await api('POST', '/api/notify', { pkg, ...musicBody(playing) });
      else toast(ev.actionId === 'next' ? 'Sonraki şarkı' : 'Önceki şarkı');
    } else if (pkg === APPS.sec.pkg && ev.actionId === 'ack') {
      await api('POST', '/api/cancel', { pkg, id: nid, tag });
    }
  } catch (e) {
    toast(e.message);
  }
}

/** Kartın ilk açılışı: çubuk boş görünmesin; uygulamaların gerçekte yaptığı birkaç gönderi. */
export async function seedDemo(api) {
  const quiet = { toast() {}, api };
  for (const i of [0, 0, 0, 2, 2, 3, 4]) await PRESETS[i].run(api, quiet);
}

export function curlSample(origin, hasToken) {
  const auth = hasToken ? "\n  -H 'authorization: Bearer $NOTIVO_TOKEN' \\" : '';
  return `# 1) Kanalı bir kez oluştur
curl -X POST ${origin}/api/apps/com.ornek/channels \\
  -H 'content-type: application/json' \\${auth}
  -d '{"id":"mesaj","name":"Mesajlar","importance":4,"appName":"Örnek"}'

# 2) Bildirim gönder (aynı id ile tekrar çağırırsan yerinde güncellenir)
curl -X POST ${origin}/api/notify \\
  -H 'content-type: application/json' \\${auth}
  -d '{"pkg":"com.ornek","channel":"mesaj","id":1,"title":"Merhaba","text":"İlk bildirim"}'

# 3) Uygulama tarafı olayları (dokunma, eylem, kapatma)
curl -N '${origin}/api/events?pkg=com.ornek'`;
}
