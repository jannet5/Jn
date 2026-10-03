import Lenis from 'lenis';
import { createGallery } from './gallery.js';
import { drawArtwork, WORKS } from './artwork.js';
import { initReveals } from './reveal.js';

const root = document.documentElement;
const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
root.classList.add('js');
if (reducedMotion) root.classList.add('reduced-motion');

// ---------------------------------------------------------------------------
// Büyük çalışma görselleri (aynı prosedürel kaynak, DOM tarafı)
// ---------------------------------------------------------------------------
document.querySelectorAll('[data-artwork]').forEach((el) => {
  const i = Number(el.dataset.artwork);
  const wide = el.dataset.shape === 'wide';
  const c = drawArtwork(i, wide ? { width: 1200, height: 800, label: false } : { width: 800, height: 1000, label: false });
  c.setAttribute('aria-hidden', 'true');
  el.appendChild(c);
});

// ---------------------------------------------------------------------------
// Yumuşak kaydırma + kaydırma hızı
// ---------------------------------------------------------------------------
const scrollListeners = [];
const onScroll = (fn) => scrollListeners.push(fn);
let lenis = null;

if (!reducedMotion) {
  lenis = new Lenis({ lerp: 0.085, smoothWheel: true, wheelMultiplier: 0.9 });
  lenis.on('scroll', (l) => scrollListeners.forEach((fn) => fn(l.scroll, l.velocity)));
  document.querySelectorAll('a[href^="#"]').forEach((a) => {
    a.addEventListener('click', (e) => {
      const id = a.getAttribute('href');
      if (id.length < 2) return;
      const target = document.querySelector(id);
      if (!target) return;
      e.preventDefault();
      lenis.scrollTo(target, { offset: 0, duration: 1.6 });
    });
  });
} else {
  let last = window.scrollY;
  window.addEventListener('scroll', () => {
    const y = window.scrollY;
    scrollListeners.forEach((fn) => fn(y, y - last));
    last = y;
  }, { passive: true });
}

// ---------------------------------------------------------------------------
// Spiral galeri
// ---------------------------------------------------------------------------
const hero = document.querySelector('.hero');
const stage = document.querySelector('.hero__stage');
const canvas = document.querySelector('.hero__canvas');
const label = document.querySelector('.cursor-label');
const coordX = document.querySelector('[data-coord="x"]');
const coordY = document.querySelector('[data-coord="y"]');

let gallery = null;
try {
  gallery = createGallery(canvas, stage);
  gallery.state.reducedMotion = reducedMotion;
  canvas.classList.add('is-ready');
} catch (err) {
  console.warn('WebGL başlatılamadı, statik görünüme geçiliyor:', err);
  root.classList.add('no-webgl');
}

function heroProgress() {
  const r = hero.getBoundingClientRect();
  const range = Math.max(1, r.height - window.innerHeight);
  return Math.max(0, Math.min(1, -r.top / range));
}

onScroll((_, velocity) => {
  if (!gallery) return;
  gallery.addScrollVelocity(velocity);
  gallery.setProgress(heroProgress());
});

let heroVisible = true;
new IntersectionObserver(([entry]) => { heroVisible = entry.isIntersecting; }).observe(hero);

let lastPointer = null;
window.addEventListener('pointermove', (e) => {
  lastPointer = e;
  if (coordX) {
    coordX.textContent = String(Math.round(e.clientX)).padStart(4, '0');
    coordY.textContent = String(Math.round(e.clientY)).padStart(4, '0');
  }
  if (gallery) gallery.setPointer(e.clientX, e.clientY);
  if (label) label.style.transform = `translate3d(${e.clientX + 18}px, ${e.clientY + 14}px, 0)`;
}, { passive: true });
document.addEventListener('pointerleave', () => gallery && gallery.clearPointer());

window.addEventListener('resize', () => gallery && gallery.resize());

let shownWork = -1;
let lastTime = performance.now();
function frame(time) {
  const dt = time - lastTime;
  lastTime = time;
  if (lenis) lenis.raf(time);
  if (gallery && heroVisible) {
    gallery.setProgress(heroProgress());
    gallery.update(dt);
    const w = lastPointer && stage.contains(document.elementFromPoint(lastPointer.clientX, lastPointer.clientY))
      ? gallery.hoveredWork() : -1;
    if (w !== shownWork) {
      shownWork = w;
      if (w >= 0) {
        label.querySelector('[data-label="title"]').textContent = WORKS[w].title;
        label.querySelector('[data-label="tag"]').textContent = WORKS[w].tag;
        label.classList.add('is-visible');
        stage.classList.add('is-hovering');
      } else {
        label.classList.remove('is-visible');
        stage.classList.remove('is-hovering');
      }
    }
  }
  requestAnimationFrame(frame);
}
requestAnimationFrame(frame);

// ---------------------------------------------------------------------------
// Kaydırmayla tetiklenen yavaş yazı girişleri
// ---------------------------------------------------------------------------
initReveals({ reducedMotion });

// Açılış: sayfa yüklenince kahraman alanı yavaşça belirir.
requestAnimationFrame(() => root.classList.add('is-loaded'));

document.querySelector('[data-year]').textContent = String(new Date().getFullYear());

// Test ve hata ayıklama için salt-okunur durum
window.__sarmal = {
  get gallery() { return gallery ? gallery.state : null; },
  get tileCount() { return gallery ? gallery.tileCount : 0; },
  get smooth() { return Boolean(lenis); },
};
