// 20 konsepti tek sayfalık galeriye (gallery.html) dönüştürür.
const fs = require('fs'), path = require('path');
const { CONCEPTS, BUGS, svgs } = require('./generate');
const esc = s => s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
const cards = CONCEPTS.map((c, i) => `
<article class="card" id="k${i + 1}">
  <div class="art">${svgs[i].replace('width="1280" height="800"', '')}</div>
  <div class="info">
    <h2><span class="no">${String(i + 1).padStart(2, '0')}</span>${esc(c.title)}</h2>
    <dl>
      <dt>Maskot</dt><dd>${esc(c.mascot.name)}, ${esc(c.mascot.who)}</dd>
      <dt>Rakip</dt><dd>${esc(c.rival.name)}, ${esc(c.rival.who)}</dd>
      <dt>Mekan</dt><dd>${esc(c.place)}</dd>
    </dl>
    <p class="hook">${esc(c.hook)}</p>
    <button class="dl" data-file="png/konsept-${String(i + 1).padStart(2, '0')}.png" hidden>Bu görseli indir (PNG)</button>
  </div>
</article>`).join('');
const html = `<title>Böcek Ringi Konseptleri</title>
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Baloo+2:wght@800&family=Nunito:wght@500;700;800&display=swap">
<style>
/* Layout: gece arenası; üstte kısa kural şeridi, altta 2 sütunlu konsept kartları */
:root{--bg:#141a12;--panel:#1e261b;--line:#34422e;--fg:#eef3e6;--muted:#a9b79d;--leaf:#7bc950;--sun:#ffd166;
--display:'Baloo 2','Trebuchet MS',sans-serif;--body:'Nunito',system-ui,sans-serif;color-scheme:dark}
body{background:var(--bg);color:var(--fg);font-family:var(--body);font-size:15px;line-height:1.5}
.wrap{max-width:1240px;margin:0 auto;padding-inline:16px;padding-block:28px 60px}
h1{font-family:var(--display);font-weight:800;font-size:clamp(34px,6vw,58px);line-height:1;margin:0;color:var(--sun);text-wrap:balance}
.lead{max-width:68ch;color:var(--muted);margin:10px 0 22px}
.rules{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:12px;margin-bottom:22px}
.rules div{background:var(--panel);border:1px solid var(--line);border-radius:12px;padding:12px 14px;min-width:0}
.rules b{display:block;font-family:var(--display);font-weight:800;font-size:19px;color:var(--leaf)}
.bugs{display:flex;flex-wrap:wrap;gap:6px;margin:0 0 30px;padding:0;list-style:none}
.bugs li{background:var(--panel);border:1px solid var(--line);border-radius:999px;padding:3px 10px;font-size:13px}
.grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(min(100%,520px),1fr));gap:22px}
.card{background:var(--panel);border:1px solid var(--line);border-radius:14px;overflow:hidden;min-width:0}
.art svg{display:block;width:100%;height:auto}
.info{padding:14px 16px 16px}
h2{font-family:var(--display);font-weight:800;font-size:26px;margin:0 0 8px;display:flex;gap:10px;align-items:baseline}
.no{color:var(--sun);font-size:18px;letter-spacing:.04em}
dl{display:grid;grid-template-columns:auto 1fr;gap:3px 12px;margin:0 0 10px}
dt{color:var(--muted);font-size:12px;text-transform:uppercase;letter-spacing:.06em;padding-top:2px}
dd{margin:0}
.hook{margin:0;border-left:3px solid var(--leaf);padding-left:10px;color:var(--fg)}
.dlbar{display:flex;flex-wrap:wrap;gap:12px;align-items:center;margin:0 0 22px}
.dl{font:800 15px var(--body);background:var(--leaf);color:#10200a;border:0;border-radius:10px;padding:10px 16px;cursor:pointer;margin-top:12px}
.dl.big{margin:0;font-size:17px;padding:12px 20px}.dl:focus-visible{outline:3px solid var(--sun);outline-offset:2px}
.dl:disabled{opacity:.6;cursor:wait}#dlmsg{color:var(--muted)}
.pick{margin-top:30px;color:var(--muted)}
</style>
<div class="wrap">
<h1>Böcek Ringi: 20 konsept</h1>
<p class="lead">Her kart aynı oyun mantığını farklı bir dünyada gösteriyor: maskot, mekan, böcek kapıştıran rakip, 5 slotlu arena, slotları kapatan yaprak ve sis içinde bağıran seyirci böcekler. Beğendiğin kartın numarasını söyle, o dünyayla oyunu kuralım.</p>
<div class="dlbar" id="dlbar" hidden><button class="dl big" data-file="bocek-ringi-20-konsept.zip">20 görselin hepsini indir (ZIP, 7 MB)</button><span id="dlmsg" role="status"></span></div>
<section class="rules">
<div><b>1. Hikâye</b>Maskot yeni bir yere gelir, rakiple konuşur. Oyun direkt başlamaz, sahne sahne ilerler.</div>
<div><b>2. Bak</b>Rakip 5 slota 5 böcek koyar. Birkaç saniye görürsün.</div>
<div><b>3. Yaprak kapanır</b>Böcekler yaprağın altında kalır. Neyin nerede olduğunu hafızandan hatırlarsın.</div>
<div><b>4. Karşı böcek koy</b>Her slotun karşısına kendi böceğini koyarsın. Doğru karşı böcek puan getirir.</div>
<div><b>5. Sıra arkadaşta</b>İki oyuncu sırayla oynar, en yüksek puanı alan kazanır.</div>
</section>
<ul class="bugs" aria-label="20 böcek">${BUGS.map(b => `<li>${esc(b[0])}</li>`).join('')}</ul>
<div class="grid">${cards}</div>
<p class="pick">Not: Bunlar konsept taslakları. Seçilen konseptin son çizimleri daha detaylı hazırlanacak.</p>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script>
(async () => {
  const dl = window.claude ? await window.claude.use('downloads') : null;
  if (!dl) return;
  const msg = document.getElementById('dlmsg');
  document.getElementById('dlbar').hidden = false;
  document.querySelectorAll('.dl').forEach(b => {
    b.hidden = false;
    b.addEventListener('click', async () => {
      const file = b.dataset.file, old = b.textContent;
      b.disabled = true; b.textContent = 'Hazırlanıyor...';
      try {
        let data;
        if (file.endsWith('.zip')) {
          const zip = new JSZip();
          for (let n = 1; n <= 20; n++) {
            const name = 'konsept-' + String(n).padStart(2, '0') + '.png';
            const r = await fetch('png/' + name);
            if (!r.ok) throw { code: 'fetch' };
            zip.file(name, await r.blob());
            b.textContent = 'Hazırlanıyor... ' + n + '/20';
          }
          data = await zip.generateAsync({ type: 'blob' });
        } else {
          const res = await fetch(file);
          if (!res.ok) throw { code: 'fetch' };
          data = await res.blob();
        }
        await dl.save({ filename: file.split('/').pop(), data });
        msg.textContent = 'İndirildi: ' + file.split('/').pop();
      } catch (e) {
        msg.textContent = e && e.code === 'declined' ? 'İndirme iptal edildi.' : 'İndirilemedi, tekrar dene.';
      } finally { b.disabled = false; b.textContent = old; }
    });
  });
})();
</script>`;
fs.writeFileSync(path.join(__dirname, 'gallery.html'), html);
console.log('gallery.html', (html.length / 1024).toFixed(0) + 'KB');
