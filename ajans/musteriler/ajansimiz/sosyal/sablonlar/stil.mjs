// Vitrin Atölyesi sosyal medya şablonları: ortak stil + bileşenler.
// Token'lar ajans/musteriler/ajansimiz/DESIGN.md §2-3'ten aynen alındı.
export const T = {
  bg: '#FFFFFF', surface: '#F3F5F2', fg: '#0E1512', muted: '#4F5B55', border: '#DCE2DE',
  primary: '#D7381E', primaryFg: '#FFFFFF', ink: '#0E1512', success: '#1F7A4D',
  dFg: '#EEF2EF', dMuted: '#A3AFA9', dBorder: '#26322C', dPrimary: '#FF5A3C', dSurface: '#151E1A',
};

export const CSS = `
@font-face{font-family:Archivo;font-style:normal;font-display:block;font-weight:100 900;font-stretch:62% 125%;
  src:url(../font/archivo-latin-ext-wdth-normal.woff2) format('woff2');
  unicode-range:U+0100-02BA,U+02BD-02C5,U+02C7-02CC,U+02CE-02D7,U+02DD-02FF,U+0304,U+0308,U+0329,U+1D00-1DBF,U+1E00-1E9F,U+1EF2-1EFF,U+2020,U+20A0-20AB,U+20AD-20C0,U+2113,U+2C60-2C7F,U+A720-A7FF}
@font-face{font-family:Archivo;font-style:normal;font-display:block;font-weight:100 900;font-stretch:62% 125%;
  src:url(../font/archivo-latin-wdth-normal.woff2) format('woff2');
  unicode-range:U+0000-00FF,U+0131,U+0152-0153,U+02BB-02BC,U+02C6,U+02DA,U+02DC,U+0304,U+0308,U+0329,U+2000-206F,U+20AC,U+2122,U+2191,U+2193,U+2212,U+2215,U+FEFF,U+FFFD}
*{box-sizing:border-box;margin:0;padding:0}
html,body{background:#fff}
body{font-family:Archivo,sans-serif;-webkit-font-smoothing:antialiased;color:${T.fg}}
.s{position:relative;overflow:hidden;display:flex;flex-direction:column;padding:28px;background:${T.bg};color:${T.fg}}
.s.r45{width:420px;height:525px}
.s.r916{width:360px;height:640px;padding:28px 28px}
.s.r11{width:360px;height:360px;padding:0}
.s.surface{background:${T.surface}}
.s.ink{background:${T.ink};color:${T.dFg}}
.s.prim{background:${T.primary};color:#fff}
.top{display:flex;align-items:center;gap:8px;font-size:13px;font-weight:500;letter-spacing:.01em;min-height:24px}
.top .ad{flex:1}
.mark{width:22px;height:22px;flex:none}
.etiket{font-size:12px;font-weight:500;padding:4px 10px;border-radius:999px;border:1px solid ${T.border};color:${T.muted};background:${T.bg}}
.ink .etiket{border-color:${T.dBorder};color:${T.dMuted};background:${T.ink}}
.prim .etiket{border-color:rgba(255,255,255,.55);color:#fff;background:${T.primary}}
.sayac{font-size:13px;font-weight:500;color:${T.muted}}
.ink .sayac{color:${T.dMuted}}
.main{flex:1;display:flex;flex-direction:column;justify-content:center;min-height:0}
.main>*{flex-shrink:0}
.eb{display:flex;align-items:center;gap:8px;font-size:14px;font-weight:500;letter-spacing:.01em;color:${T.muted};margin-bottom:14px}
.eb::before{content:"";width:8px;height:8px;border-radius:50%;background:${T.primary};flex:none}
.ink .eb{color:${T.dMuted}} .ink .eb::before{background:${T.dPrimary}}
.prim .eb{color:#fff} .prim .eb::before{background:#fff}
.d{font-weight:800;font-stretch:112%;font-size:44px;line-height:.95;letter-spacing:-.035em;text-wrap:balance}
.h2{font-weight:800;font-stretch:112%;font-size:32px;line-height:1.0;letter-spacing:-.03em;text-wrap:balance}
.h3{font-weight:800;font-stretch:112%;font-size:22px;line-height:1.15;letter-spacing:-.015em;text-wrap:balance}
.lead{font-size:18px;line-height:1.45;text-wrap:pretty}
.body{font-size:16px;line-height:1.5;text-wrap:pretty}
.sm{font-size:14px;line-height:1.45;font-weight:500;letter-spacing:.01em;text-wrap:pretty}
.mut{color:${T.muted}} .ink .mut{color:${T.dMuted}} .prim .mut{color:#fff}
em{font-style:normal;color:${T.primary}} .ink em{color:${T.dPrimary}} .surface em{color:#B92E17} .prim em{color:#fff;text-decoration:underline;text-decoration-thickness:3px;text-underline-offset:4px}
.mt8{margin-top:8px}.mt12{margin-top:12px}.mt16{margin-top:16px}.mt24{margin-top:24px}
.alt{display:flex;align-items:center;justify-content:space-between;gap:12px;padding-top:14px;border-top:1px solid ${T.border};font-size:14px;font-weight:500}
.ink .alt{border-color:${T.dBorder}} .prim .alt{border-color:rgba(255,255,255,.45)}
.alt b{font-weight:800}
.btn{display:inline-flex;align-items:center;gap:8px;height:52px;padding:0 24px;border-radius:999px;background:${T.primary};color:#fff;font-size:16px;font-weight:500;align-self:flex-start}
.ink .btn{background:${T.dPrimary};color:${T.ink}}
.btn svg{width:18px;height:18px;stroke:currentColor;fill:none;stroke-width:1.75;stroke-linecap:round;stroke-linejoin:round}
.no{font-weight:800;font-stretch:112%;font-size:72px;line-height:.9;letter-spacing:-.04em;color:${T.primary}}
.ink .no{color:${T.dPrimary}}
.liste{list-style:none;display:flex;flex-direction:column;gap:10px}
.liste li{display:flex;gap:10px;align-items:flex-start;font-size:16px;line-height:1.4}
.liste li svg{flex:none;width:20px;height:20px;margin-top:1px;stroke:${T.primary};fill:none;stroke-width:2;stroke-linecap:round;stroke-linejoin:round}
.ink .liste li svg{stroke:${T.dPrimary}}
.kart{border:1px solid ${T.border};border-radius:14px;padding:14px 16px;background:${T.bg}}
.ink .kart{border-color:${T.dBorder};background:${T.dSurface}}
.chip{display:inline-flex;font-size:14px;font-weight:500;padding:6px 12px;border:1px solid ${T.border};border-radius:999px;background:${T.bg}}
.ink .chip{border-color:${T.dBorder};background:${T.ink}}
.ikon{stroke:currentColor;fill:none;stroke-width:1.75;stroke-linecap:round;stroke-linejoin:round}
/* cihaz çerçeveleri */
.tel{width:150px;height:300px;border-radius:26px;background:#0A100D;padding:7px;box-shadow:0 30px 60px -20px rgba(14,21,18,.25);flex:none}
.tel .ek{width:100%;height:100%;border-radius:20px;overflow:hidden;background:#fff;position:relative;display:flex;flex-direction:column}
.tel.k{width:126px;height:252px;border-radius:22px;padding:6px}.tel.k .ek{border-radius:17px}
.kapak .tel.k{width:112px;height:224px}
.ink .tel{background:#000;box-shadow:0 0 0 1px ${T.dBorder}}
.ciftel{display:flex;gap:16px;justify-content:center;align-items:flex-end}
.tl{font-size:13px;font-weight:500;margin-bottom:8px;display:flex;align-items:center;gap:6px}
.tl i{width:8px;height:8px;border-radius:50%;display:inline-block}
`;

export const ic = {
  wa: '<path d="M7.9 20A9 9 0 1 0 4 16.1L2 22Z"/>',
  check: '<path d="M20 6 9 17l-5-5"/>',
  x: '<path d="M18 6 6 18"/><path d="m6 6 12 12"/>',
  phone: '<path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/>',
  layout: '<rect width="18" height="18" x="3" y="3" rx="2"/><path d="M3 9h18"/><path d="M9 21V9"/>',
  megaphone: '<path d="m3 11 18-5v12L3 14v-3z"/><path d="M11.6 16.8a3 3 0 1 1-5.8-1.6"/>',
  smartphone: '<rect width="14" height="20" x="5" y="2" rx="2" ry="2"/><path d="M12 18h.01"/>',
  insta: '<rect width="20" height="20" x="2" y="2" rx="5" ry="5"/><path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"/><line x1="17.5" x2="17.51" y1="6.5" y2="6.5"/>',
  search: '<circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/>',
  tag: '<path d="M12.586 2.586A2 2 0 0 0 11.172 2H4a2 2 0 0 0-2 2v7.172a2 2 0 0 0 .586 1.414l8.704 8.704a2.426 2.426 0 0 0 3.42 0l6.58-6.58a2.426 2.426 0 0 0 0-3.42z"/><circle cx="7.5" cy="7.5" r=".5"/>',
  route: '<circle cx="6" cy="19" r="3"/><path d="M9 19h8.5a3.5 3.5 0 0 0 0-7h-11a3.5 3.5 0 0 1 0-7H15"/><circle cx="18" cy="5" r="3"/>',
  help: '<circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"/><path d="M12 17h.01"/>',
  grid: '<rect width="7" height="7" x="3" y="3" rx="1"/><rect width="7" height="7" x="14" y="3" rx="1"/><rect width="7" height="7" x="14" y="14" rx="1"/><rect width="7" height="7" x="3" y="14" rx="1"/>',
  clock: '<circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/>',
  map: '<path d="M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0"/><circle cx="12" cy="10" r="3"/>',
  star: '<polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>',
  msg: '<path d="M7.9 20A9 9 0 1 0 4 16.1L2 22Z"/>',
  play: '<polygon points="6 3 20 12 6 21 6 3"/>',
};
export const I = (n, size = 20, extra = '') => `<svg class="ikon" width="${size}" height="${size}" viewBox="0 0 24 24" ${extra} aria-hidden="true">${ic[n]}</svg>`;

export const mark = (size = 22, dark = false) =>
  `<svg class="mark" style="width:${size}px;height:${size}px" viewBox="0 0 32 32" aria-hidden="true"><rect width="32" height="32" rx="8" fill="${dark ? '#EEF2EF' : T.fg}"/><path d="M8 9l8 15 8-15" stroke="${T.primary}" stroke-width="4" fill="none" stroke-linecap="round" stroke-linejoin="round"/></svg>`;

export const top = (tema, sag = '') =>
  `<div class="top">${mark(22, tema === 'ink')}<span class="ad">Vitrin Atölyesi</span>${sag}</div>`;
export const etiket = (t = 'Örnek konsept') => `<span class="etiket">${t}</span>`;
export const sayac = (i, n) => `<span class="sayac">${i}/${n}</span>`;
export const alt = (sol = 'Ücretsiz örnek ana sayfa', sag = "DM'den <b>ÖRNEK</b> yaz") => `<div class="alt"><span>${sol}</span><span>${sag}</span></div>`;

export const sayfa = (icerik, baslik = 'slayt') => `<!doctype html><html lang="tr"><head><meta charset="utf-8"><title>${baslik}</title><style>${CSS}</style></head><body>${icerik}</body></html>`;

// ---- mini site önizlemeleri (CSS ile çizilmiş, gerçek işletme değil) ----
// "Önce": masaüstü için yapılmış, telefonda küçülen temsili eski site. Bilerek kötü; data-dek = dekoratif, kontrast testinden muaf.
export const eskiSite = (baslik, renk = '#2B4C9B') => `
<div data-dek style="font-family:'Times New Roman',serif;background:#E9E9E9;height:100%;padding:4px;color:#333;font-size:5px;line-height:1.2">
  <div style="background:${renk};color:#fff;padding:5px 4px;font-size:8px;font-weight:bold;text-align:center">${baslik}</div>
  <div style="display:flex;gap:2px;margin:3px 0;font-size:4.5px">${['ANA SAYFA', 'HAKKIMIZDA', 'HİZMETLER', 'GALERİ', 'REFERANSLAR', 'İLETİŞİM'].map(x => `<span style="background:#ccc;padding:2px">${x}</span>`).join('')}</div>
  <div style="display:flex;gap:3px">
    <div style="flex:2;background:#fff;padding:3px;border:1px solid #bbb">
      <div style="font-size:7px;font-weight:bold;color:#a00;margin-bottom:2px">HOŞGELDİNİZ</div>
      ${'<div style="height:2px;background:#bbb;margin:2px 0"></div>'.repeat(14)}
      <div style="height:34px;background:#cfcfcf;margin:3px 0;display:flex;align-items:center;justify-content:center;color:#888">resim</div>
      ${'<div style="height:2px;background:#bbb;margin:2px 0"></div>'.repeat(10)}
    </div>
    <div style="flex:1;background:#fff;padding:3px;border:1px solid #bbb">
      <div style="height:26px;background:#d8d8d8;margin-bottom:3px"></div>
      ${'<div style="height:2px;background:#bbb;margin:2px 0"></div>'.repeat(8)}
      <div style="height:20px;background:#ffd84d;margin-top:3px;display:flex;align-items:center;justify-content:center;font-size:5px;color:#333">TEL: 0 (2xx) xxx xx xx</div>
    </div>
  </div>
  ${'<div style="height:2px;background:#bbb;margin:3px 0"></div>'.repeat(10)}
  <div style="background:#555;color:#ddd;padding:3px;font-size:4px;margin-top:6px;text-align:center">Tüm hakları saklıdır</div>
</div>`;

// "Sonra": mobil öncelikli mini site. p = mini palet (konseptin kendi rengi)
export const yeniSite = ({ ad, slogan, satirlar, buton = 'Hemen ara', ikon = 'phone', p }) => `
<div style="height:100%;display:flex;flex-direction:column;background:${p.bg};color:${p.fg}">
  <div style="display:flex;align-items:center;justify-content:space-between;padding:10px 10px 6px;font-size:9.5px;font-weight:800;font-stretch:112%">
    <span>${ad}</span><span style="width:12px;height:8px;border-top:1.5px solid ${p.fg};border-bottom:1.5px solid ${p.fg}"></span>
  </div>
  <div style="padding:6px 10px 10px">
    <div style="font-size:7.5px;font-weight:500;color:${p.mut};margin-bottom:4px">${slogan[0]}</div>
    <div style="font-size:17px;line-height:.98;font-weight:800;font-stretch:112%;letter-spacing:-.03em">${slogan[1]}</div>
  </div>
  <div style="display:flex;flex-direction:column;gap:5px;padding:0 10px">
    ${satirlar.map(([a, b]) => `<div style="display:flex;justify-content:space-between;align-items:center;border:1px solid ${p.line};border-radius:7px;padding:6px 7px;font-size:8px;font-weight:500"><span>${a}</span><span style="color:${p.mut};white-space:nowrap;margin-left:4px">${b}</span></div>`).join('')}
  </div>
  <div style="flex:1"></div>
  <div style="margin:8px;height:28px;border-radius:999px;background:${p.acc};color:${p.accFg};display:flex;align-items:center;justify-content:center;gap:5px;font-size:9px;font-weight:500">${I(ikon, 10)}${buton}</div>
</div>`;
