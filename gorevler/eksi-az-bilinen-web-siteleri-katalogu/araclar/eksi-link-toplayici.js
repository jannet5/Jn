/*
 * Ekşi başlık link toplayıcı (kendi tarayıcında çalışır).
 *
 * Kullanım:
 *   1. Normal tarayıcında başlığı aç:
 *      https://eksisozluk.com/az-kisinin-bildigi-muhtesem-web-siteleri--2764697
 *   2. F12 → Console sekmesi → bu dosyanın tamamını yapıştır → Enter.
 *   3. Bitince "eksi-linkler.txt" iner. Bu dosyada yalnız linkler,
 *      sayfa numarası ve entry numarası vardır; entry metinleri yoktur.
 *
 * Son sayfadan geriye SAYFA_ADEDI kadar sayfa gezer (varsayılan 200:
 * istenen ilk 100 + sonraki 100). Sayfalar arasında BEKLEME_MS kadar
 * bekler. Yarıda kesilirse aynı sekmede tekrar çalıştırınca kaldığı
 * yerden devam eder (ilerleme localStorage'da tutulur).
 */
(async function eksiLinkToplayici(ayar) {
  'use strict';
  const SAYFA_ADEDI = (ayar && ayar.sayfaAdedi) || 200;
  const BEKLEME_MS = (ayar && ayar.beklemeMs) || 1500;
  const INDIR = !(ayar && ayar.indirme === false);

  // Düz metin içinde link verilmeden yazılan alan adları (ör. "mapc.am").
  const TLD = 'com|net|org|io|co|app|dev|ai|me|info|tv|xyz|site|online|tech|' +
    'tools|world|link|cc|ly|gg|fm|am|to|so|sh|is|it|de|fr|uk|us|eu|ca|nl|' +
    'tr|com\\.tr|org\\.tr|net\\.tr|gov\\.tr|edu\\.tr|biz|pro|page|space|fun|' +
    'live|studio|design|club|zone|wiki|guru|run|top|one|art|app';
  const ALAN_ADI = new RegExp(
    '(?:^|[\\s(\\[,;:"\'])((?:https?:\\/\\/)?(?:[a-z0-9-]+\\.)+(?:' + TLD +
    ')(?:\\/[^\\s)\\]"\'<>,]*)?)(?=$|[\\s)\\].,;:!?"\'])', 'gi');

  const anahtar = 'eksiLinkToplayici:' + location.pathname;
  const yol = location.pathname.match(/^\/[^/?#]+--\d+/);
  if (!yol) { throw new Error('Önce bir Ekşi başlık sayfası aç.'); }
  const taban = location.origin + yol[0];

  const bekle = (ms) => new Promise((r) => setTimeout(r, ms));
  async function belgeGetir(sayfa) {
    for (let deneme = 1; deneme <= 4; deneme++) {
      try {
        const yanit = await fetch(taban + '?p=' + sayfa, { credentials: 'include' });
        if (yanit.ok) {
          const html = await yanit.text();
          return new DOMParser().parseFromString(html, 'text/html');
        }
        console.warn('sayfa', sayfa, 'HTTP', yanit.status, '— tekrar denenecek');
      } catch (e) {
        console.warn('sayfa', sayfa, 'hata', e.message);
      }
      await bekle(BEKLEME_MS * deneme * 2);
    }
    throw new Error('Sayfa ' + sayfa + ' alınamadı; komutu tekrar çalıştırınca devam eder.');
  }

  function linkleriCikar(belge, sayfa) {
    const satirlar = [];
    belge.querySelectorAll('#entry-item-list > li[data-id]').forEach((li) => {
      const entry = li.getAttribute('data-id');
      const icerik = li.querySelector('.content');
      if (!icerik) return;
      const gorulen = new Set();
      icerik.querySelectorAll('a.url[href]').forEach((a) => {
        const url = a.getAttribute('href');
        if (!gorulen.has(url)) { gorulen.add(url); satirlar.push([url, sayfa, entry]); }
      });
      // Linksiz yazılmış alan adları: a.url metinlerini çıkarıp ara.
      const kopya = icerik.cloneNode(true);
      kopya.querySelectorAll('a.url').forEach((a) => a.remove());
      const metin = kopya.textContent;
      let m;
      ALAN_ADI.lastIndex = 0;
      while ((m = ALAN_ADI.exec(metin)) !== null) {
        const ham = m[1].replace(/[.,;:!?]+$/, '');
        const url = /^https?:\/\//i.test(ham) ? ham : 'https://' + ham;
        if (!gorulen.has(url)) { gorulen.add(url); satirlar.push([url, sayfa, entry]); }
      }
    });
    return satirlar;
  }

  let durum = null;
  try { durum = JSON.parse(localStorage.getItem(anahtar) || 'null'); } catch (e) { durum = null; }

  if (!durum) {
    const ilk = await belgeGetir(1);
    const pager = ilk.querySelector('.pager[data-pagecount]');
    const toplam = pager ? parseInt(pager.getAttribute('data-pagecount'), 10) : 1;
    const bitis = Math.max(1, toplam - SAYFA_ADEDI + 1);
    durum = { toplam, siradaki: toplam, bitis, satirlar: [], tarih: new Date().toISOString() };
    console.log('Başlık', toplam, 'sayfa. Toplanacak aralık:', toplam, '→', bitis);
  } else {
    console.log('Kaldığı yerden devam:', durum.siradaki, '→', durum.bitis);
  }

  while (durum.siradaki >= durum.bitis) {
    const belge = await belgeGetir(durum.siradaki);
    const yeni = linkleriCikar(belge, durum.siradaki);
    durum.satirlar.push(...yeni);
    console.log('sayfa', durum.siradaki, '→', yeni.length, 'link');
    durum.siradaki -= 1;
    try { localStorage.setItem(anahtar, JSON.stringify(durum)); } catch (e) { /* kota dolarsa devam */ }
    if (durum.siradaki >= durum.bitis) await bekle(BEKLEME_MS);
  }

  const baslik = [
    '# kaynak: ' + taban,
    '# toplam_sayfa: ' + durum.toplam,
    '# aralik: ' + durum.toplam + '-' + durum.bitis,
    '# toplama_tarihi: ' + durum.tarih,
    '# sutunlar: url<TAB>sayfa<TAB>entry',
  ];
  const metin = baslik.concat(durum.satirlar.map((s) => s.join('\t'))).join('\n') + '\n';
  if (INDIR) {
    const a = document.createElement('a');
    a.href = URL.createObjectURL(new Blob([metin], { type: 'text/plain;charset=utf-8' }));
    a.download = 'eksi-linkler.txt';
    document.body.appendChild(a);
    a.click();
    a.remove();
  }
  try { localStorage.removeItem(anahtar); } catch (e) { /* önemsiz */ }
  console.log('Bitti:', durum.satirlar.length, 'satır. eksi-linkler.txt indirildi.');
  window.__eksiLinkSonuc = metin;
  return metin;
})(window.__eksiLinkAyar);
