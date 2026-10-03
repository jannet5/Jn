// Prosedürel görseller: dış görsel dosyası, lisans sorunu veya file:// CORS engeli yok.
// Aynı tohumdan hem WebGL dokuları hem de sayfadaki büyük görseller çizilir.

export const WORKS = [
  { title: 'Kehribar', tag: 'Kimlik', hue: 28 },
  { title: 'Dalga Odası', tag: 'Mekân', hue: 196 },
  { title: 'Gece Bahçesi', tag: 'Film', hue: 142 },
  { title: 'Tuz & Işık', tag: 'Ambalaj', hue: 42 },
  { title: 'Orbit', tag: 'Dijital', hue: 260 },
  { title: 'Kırmızı Oda', tag: 'Sergi', hue: 4 },
  { title: 'Sis', tag: 'Fotoğraf', hue: 210 },
  { title: 'Bakır Hat', tag: 'Kimlik', hue: 18 },
  { title: 'Pusula', tag: 'Web', hue: 172 },
  { title: 'Yaz Notası', tag: 'Kampanya', hue: 52 },
  { title: 'Gölge Arşivi', tag: 'Yayın', hue: 300 },
  { title: 'Lumen', tag: 'Deneyim', hue: 225 },
];

function mulberry32(seed) {
  let a = seed >>> 0;
  return () => {
    a |= 0; a = (a + 0x6d2b79f5) | 0;
    let t = Math.imul(a ^ (a >>> 15), 1 | a);
    t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t;
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  };
}

const hsl = (h, s, l, a = 1) => `hsla(${h % 360}, ${s}%, ${l}%, ${a})`;

// Altı farklı kompozisyon; indeksle seçilir, tohumla çeşitlenir.
const COMPOSITIONS = [
  function arches(ctx, w, h, r, hue) {
    for (let i = 0; i < 7; i++) {
      const rad = w * (0.55 - i * 0.07);
      ctx.beginPath();
      ctx.arc(w * 0.5, h * 0.98, rad, Math.PI, 0);
      ctx.fillStyle = hsl(hue + i * 6, 55, 18 + i * 8, 0.95);
      ctx.fill();
    }
    sun(ctx, w * (0.3 + r() * 0.4), h * 0.28, w * 0.09, hue + 20);
  },
  function stripes(ctx, w, h, r, hue) {
    const n = 14;
    for (let i = 0; i < n; i++) {
      ctx.fillStyle = hsl(hue + (i % 3) * 12, 45, 12 + (i / n) * 45, 0.9);
      const y = (i / n) * h;
      ctx.beginPath();
      ctx.moveTo(0, y);
      ctx.bezierCurveTo(w * 0.3, y - h * 0.08 * r(), w * 0.7, y + h * 0.12, w, y);
      ctx.lineTo(w, h); ctx.lineTo(0, h); ctx.closePath();
      ctx.fill();
    }
  },
  function orbits(ctx, w, h, r, hue) {
    ctx.strokeStyle = hsl(hue + 30, 70, 75, 0.55);
    ctx.lineWidth = w * 0.004;
    for (let i = 1; i < 9; i++) {
      ctx.beginPath();
      ctx.ellipse(w / 2, h / 2, w * 0.06 * i, h * 0.035 * i, -0.4, 0, Math.PI * 2);
      ctx.stroke();
    }
    sun(ctx, w / 2, h / 2, w * 0.12, hue);
    for (let i = 0; i < 40; i++) {
      ctx.fillStyle = hsl(hue + 40, 60, 85, r());
      ctx.fillRect(r() * w, r() * h, w * 0.004, w * 0.004);
    }
  },
  function blocks(ctx, w, h, r, hue) {
    for (let i = 0; i < 9; i++) {
      ctx.fillStyle = hsl(hue + r() * 40 - 20, 40 + r() * 30, 20 + r() * 50, 0.92);
      const bw = w * (0.2 + r() * 0.4);
      const bh = h * (0.1 + r() * 0.3);
      ctx.fillRect(r() * (w - bw), r() * (h - bh), bw, bh);
    }
    ctx.fillStyle = hsl(hue + 180, 70, 60, 0.9);
    ctx.beginPath();
    ctx.arc(w * 0.7, h * 0.3, w * 0.07, 0, Math.PI * 2);
    ctx.fill();
  },
  function dunes(ctx, w, h, r, hue) {
    sun(ctx, w * 0.5, h * 0.35, w * 0.18, hue + 10);
    for (let i = 0; i < 6; i++) {
      const base = h * (0.5 + i * 0.09);
      ctx.beginPath();
      ctx.moveTo(0, base);
      for (let x = 0; x <= w; x += w / 24) {
        ctx.lineTo(x, base + Math.sin(x / w * Math.PI * (2 + i) + r() * 0.3 + i) * h * 0.03);
      }
      ctx.lineTo(w, h); ctx.lineTo(0, h); ctx.closePath();
      ctx.fillStyle = hsl(hue - i * 4, 50, 30 - i * 3.5, 0.97);
      ctx.fill();
    }
  },
  function figure(ctx, w, h, r, hue) {
    const g = ctx.createRadialGradient(w * 0.5, h * 0.42, 0, w * 0.5, h * 0.42, w * 0.6);
    g.addColorStop(0, hsl(hue + 20, 60, 62, 0.9));
    g.addColorStop(1, hsl(hue, 40, 8, 0));
    ctx.fillStyle = g; ctx.fillRect(0, 0, w, h);
    ctx.fillStyle = hsl(hue, 30, 6, 0.95);
    ctx.beginPath();
    ctx.arc(w * 0.5, h * 0.4, w * 0.1, 0, Math.PI * 2);
    ctx.fill();
    ctx.beginPath();
    ctx.ellipse(w * 0.5, h * 0.95, w * 0.28, h * 0.4, 0, Math.PI, 0);
    ctx.fill();
  },
];

function sun(ctx, x, y, rad, hue) {
  const g = ctx.createRadialGradient(x, y, 0, x, y, rad * 2.6);
  g.addColorStop(0, hsl(hue + 25, 90, 82, 1));
  g.addColorStop(0.35, hsl(hue + 15, 85, 62, 0.85));
  g.addColorStop(1, hsl(hue, 80, 40, 0));
  ctx.fillStyle = g;
  ctx.beginPath();
  ctx.arc(x, y, rad * 2.6, 0, Math.PI * 2);
  ctx.fill();
}

function grain(ctx, w, h, r) {
  const n = Math.floor(w * h * 0.012);
  for (let i = 0; i < n; i++) {
    ctx.fillStyle = r() > 0.5 ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)';
    ctx.fillRect(r() * w, r() * h, 1.2, 1.2);
  }
}

/**
 * index'e karşılık gelen eseri bir canvas üzerine çizer.
 * @param {number} index
 * @param {{width?:number,height?:number,label?:boolean}} opts
 */
export function drawArtwork(index, { width = 512, height = 640, label = true } = {}) {
  const work = WORKS[index % WORKS.length];
  const canvas = document.createElement('canvas');
  canvas.width = width;
  canvas.height = height;
  const ctx = canvas.getContext('2d');
  const r = mulberry32(index * 9973 + 17);
  const hue = work.hue;

  const bg = ctx.createLinearGradient(0, 0, width * 0.3, height);
  bg.addColorStop(0, hsl(hue + 8, 35, 10));
  bg.addColorStop(1, hsl(hue - 12, 45, 4));
  ctx.fillStyle = bg;
  ctx.fillRect(0, 0, width, height);

  COMPOSITIONS[index % COMPOSITIONS.length](ctx, width, height, r, hue);
  grain(ctx, width, height, r);

  if (label) {
    const pad = width * 0.06;
    ctx.fillStyle = 'rgba(245, 240, 232, 0.92)';
    ctx.font = `500 ${Math.round(width * 0.034)}px "Helvetica Neue", Arial, sans-serif`;
    ctx.textBaseline = 'top';
    ctx.fillText(String(index + 1).padStart(2, '0') + ' — ' + work.tag.toUpperCase(), pad, pad);
    ctx.font = `400 ${Math.round(width * 0.075)}px Georgia, "Times New Roman", serif`;
    ctx.textBaseline = 'bottom';
    ctx.fillText(work.title, pad, height - pad);
  }
  return canvas;
}
