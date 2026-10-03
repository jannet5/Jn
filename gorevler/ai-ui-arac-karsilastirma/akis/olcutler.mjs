// Ölçüt fonksiyonları. brifKapsami ve aiVarsayilanlari tarayıcı içinde çalışır (page.evaluate),
// bu yüzden dış değişken kullanmazlar.

export function brifKapsami() {
  const kopya = document.body.cloneNode(true);
  kopya.querySelectorAll("script,style,noscript,template").forEach((e) => e.remove());
  const metin = (kopya.textContent || "").replace(/\s+/g, " ").toLocaleLowerCase("tr");
  const var_ = (s) => metin.includes(s);
  const gruplar = {
    menu: ["genel bakış", "şubeler", "ürünler", "personel", "ayarlar"],
    tarih: ["bu hafta", "geçen hafta", "son 30 gün"],
    olcut: ["ciro", "sipariş", "ortalama sepet", "iade"],
  };
  const maddeler = [];
  for (const [grup, liste] of Object.entries(gruplar))
    for (const s of liste) maddeler.push({ grup, madde: s, tamam: var_(s) });

  const gorunur = (e) => {
    const r = e.getBoundingClientRect();
    const st = getComputedStyle(e);
    return r.width > 0 && r.height > 0 && st.visibility !== "hidden" && st.display !== "none";
  };
  // Grafik: ≥200×100 görünür svg/canvas ya da adı/etiketi grafik olan ve içinde ≥7 çubuk/öğe bulunan HTML grafik
  const grafikAdi = /chart|grafik|graph|plot/i;
  const grafikler = [...document.querySelectorAll("svg, canvas, [class], [id], [aria-label]")].filter((e) => {
    const r = e.getBoundingClientRect();
    if (!gorunur(e) || r.width < 200 || r.height < 100) return false;
    if (e.tagName === "svg" || e.tagName === "CANVAS") return true;
    const ad = `${e.className && e.className.baseVal === undefined ? e.className : ""} ${e.id} ${e.getAttribute("aria-label") || ""}`;
    return grafikAdi.test(ad) && e.querySelectorAll("*").length >= 7;
  });
  maddeler.push({ grup: "grafik", madde: "≥200×100 görünür grafik (svg/canvas/HTML çubuk)", tamam: grafikler.length > 0 });

  const satirSay = Math.max(0,
    ...[...document.querySelectorAll("table")].map((t) => t.querySelectorAll("tbody tr").length),
    ...[...document.querySelectorAll('[role="table"],[role="grid"]')].map((t) => t.querySelectorAll('[role="row"]').length - 1));
  maddeler.push({ grup: "tablo", madde: `tabloda ≥6 satır (bulunan ${satirSay})`, tamam: satirSay >= 6 });

  const lang = document.documentElement.getAttribute("lang") || "";
  maddeler.push({ grup: "dil", madde: `html lang=tr (bulunan "${lang}")`, tamam: lang.toLowerCase().startsWith("tr") });

  const tamam = maddeler.filter((m) => m.tamam).length;
  return { tamam, toplam: maddeler.length, maddeler };
}

export function aiVarsayilanlari() {
  const ilkAile = (ff) => (ff || "").split(",")[0].replace(/["']/g, "").trim();
  const govdeFont = ilkAile(getComputedStyle(document.body).fontFamily);
  const baslik = document.querySelector("h1, h2");
  const baslikFont = baslik ? ilkAile(getComputedStyle(baslik).fontFamily) : null;
  const jenerik = /^(inter|arial|helvetica|roboto|system-ui|-apple-system|sans-serif|segoe ui|ui-sans-serif)$/i;

  const rgbHue = (r, g, b) => {
    r /= 255; g /= 255; b /= 255;
    const mx = Math.max(r, g, b), mn = Math.min(r, g, b), d = mx - mn;
    if (d < 0.08) return null; // gri
    let h = mx === r ? ((g - b) / d) % 6 : mx === g ? (b - r) / d + 2 : (r - g) / d + 4;
    return (h * 60 + 360) % 360;
  };
  let gradyan = 0, morGradyan = 0, kartKiti = 0, buyukHarf = 0;
  const aileler = new Set();
  for (const e of document.querySelectorAll("body *")) {
    const st = getComputedStyle(e);
    const r = e.getBoundingClientRect();
    if (r.width === 0 || r.height === 0) continue;
    aileler.add(ilkAile(st.fontFamily));
    if (st.backgroundImage.includes("gradient")) {
      gradyan++;
      const renkler = [...st.backgroundImage.matchAll(/rgba?\((\d+),\s*(\d+),\s*(\d+)/g)];
      if (renkler.some((m) => { const h = rgbHue(+m[1], +m[2], +m[3]); return h !== null && h >= 245 && h <= 300; }))
        morGradyan++;
    }
    if (parseFloat(st.borderTopLeftRadius) >= 10 && st.boxShadow !== "none" && r.width * r.height > 10000) kartKiti++;
    const kendiMetni = [...e.childNodes].filter((n) => n.nodeType === 3).map((n) => n.textContent.trim()).join("");
    if (st.textTransform === "uppercase" && kendiMetni.length > 1 && kendiMetni.length < 40) buyukHarf++;
  }
  // frontend-design skill'inin 1 numaralı uyarısı: krem zemin + terakota vurgu
  const hsl = (renk) => {
    const m = renk.match(/rgba?\((\d+),\s*(\d+),\s*(\d+)(?:,\s*([\d.]+))?/);
    if (!m || m[4] === "0") return null;
    const [r, g, b] = [m[1], m[2], m[3]].map((x) => x / 255);
    const mx = Math.max(r, g, b), mn = Math.min(r, g, b), l = (mx + mn) / 2, d = mx - mn;
    if (d === 0) return { h: null, s: 0, l };
    const s = d / (1 - Math.abs(2 * l - 1));
    // Açık kremler çok düşük kroma taşır; burada gri eşiği uygulanmaz
    const h = (((mx === r ? ((g - b) / d) % 6 : mx === g ? (b - r) / d + 2 : (r - g) / d + 4) * 60) + 360) % 360;
    return { h, s, l };
  };
  const zeminler = [document.body, document.querySelector("main")].filter(Boolean)
    .map((e) => hsl(getComputedStyle(e).backgroundColor)).filter(Boolean);
  const kremZemin = zeminler.some((c) => c.h !== null && c.h >= 20 && c.h <= 55 && c.s >= 0.15 && c.l >= 0.9);
  let terakota = 0;
  for (const e of document.querySelectorAll("body *")) {
    const c = hsl(getComputedStyle(e).backgroundColor);
    if (c && c.h !== null && c.h >= 8 && c.h <= 30 && c.s >= 0.4 && c.l >= 0.3 && c.l <= 0.6) terakota++;
  }
  const kremTerakota = kremZemin && terakota > 0;
  const ortaNokta = ((document.body.innerText || "").match(/·/g) || []).length;
  return {
    govdeFont, baslikFont,
    jenerikFont: jenerik.test(govdeFont) && (!baslikFont || jenerik.test(baslikFont)),
    fontAilesiSayisi: aileler.size,
    gradyan, morGradyan, kartKiti, buyukHarfEtiket: buyukHarf, ortaNokta, kremTerakota,
  };
}

// Klavyeyle Tab gezintisi: kaç farklı öğeye odak gidiyor, kaçında görünür odak göstergesi var.
export async function odakKontrolu(sayfa, adim) {
  await sayfa.mouse.click(1, 1).catch(() => {});
  await sayfa.evaluate(() => document.activeElement && document.activeElement.blur && document.activeElement.blur());
  const goruldu = new Set();
  let gorunurOdak = 0;
  for (let i = 0; i < adim; i++) {
    await sayfa.keyboard.press("Tab");
    const bilgi = await sayfa.evaluate(() => {
      const e = document.activeElement;
      if (!e || e === document.body) return null;
      const st = getComputedStyle(e);
      const cizgi = st.outlineStyle !== "none" && parseFloat(st.outlineWidth) > 0;
      const golge = st.boxShadow && st.boxShadow !== "none";
      const kimlik = e.tagName + "|" + (e.id || "") + "|" + (e.textContent || "").trim().slice(0, 30) + "|" + Math.round(e.getBoundingClientRect().top);
      return { kimlik, gorunur: cizgi || golge };
    });
    if (!bilgi || goruldu.has(bilgi.kimlik)) continue;
    goruldu.add(bilgi.kimlik);
    if (bilgi.gorunur) gorunurOdak++;
  }
  return { odaklanan: goruldu.size, gorunurOdak };
}

// Kullanıcı akışı kabulü: "Geçen hafta" seçilince sayfadaki metin (rakamlar) değişiyor mu?
export async function tarihAkisi(sayfa) {
  const metin = () => sayfa.evaluate(() => document.body.innerText);
  const once = await metin();
  const hedef = sayfa.getByText("Geçen hafta", { exact: true }).first();
  if (!(await hedef.count()) || !(await hedef.isVisible())) return { tiklandi: false, degisti: false };
  await hedef.click({ timeout: 5000 }).catch(() => {});
  await sayfa.waitForTimeout(500);
  const sonra = await metin();
  return { tiklandi: true, degisti: once !== sonra };
}
