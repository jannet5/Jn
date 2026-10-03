// Kaydırmayla tetiklenen yavaş girişler.
// [data-reveal]          → blok yukarı kayarak ve belirerek gelir
// [data-reveal="words"]  → başlık kelime kelime, maskeli şekilde yükselir

function splitWords(el) {
  const text = el.textContent.trim().replace(/\s+/g, ' ');
  el.setAttribute('aria-label', text);
  el.textContent = '';
  text.split(' ').forEach((word, i, arr) => {
    const mask = document.createElement('span');
    mask.className = 'w';
    mask.setAttribute('aria-hidden', 'true');
    const inner = document.createElement('span');
    inner.className = 'w__i';
    inner.style.transitionDelay = `${i * 70}ms`;
    inner.textContent = word;
    mask.appendChild(inner);
    el.appendChild(mask);
    if (i < arr.length - 1) el.appendChild(document.createTextNode(' '));
  });
}

export function initReveals({ reducedMotion }) {
  const els = [...document.querySelectorAll('[data-reveal]')];
  els.forEach((el) => {
    if (el.dataset.reveal === 'words') splitWords(el);
    const d = el.dataset.delay;
    if (d) el.style.setProperty('--delay', `${d}ms`);
  });

  if (reducedMotion || !('IntersectionObserver' in window)) {
    els.forEach((el) => el.classList.add('is-in'));
    return;
  }

  const io = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (!entry.isIntersecting) return;
      entry.target.classList.add('is-in');
      io.unobserve(entry.target);
    });
  }, { rootMargin: '0px 0px -12% 0px', threshold: 0.12 });

  els.forEach((el) => io.observe(el));
}
