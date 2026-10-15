// Notivo Android uygulamasındaki `Logic.kt` dosyasının birebir JS portu.
// Aynı kural eşleme, silinen-mesaj tespiti ve Türkçe harf normalleştirme burada da geçerli.

/**
 * Türkçe İ/I/ı/i harflerini aynı harf sayan, büyük/küçük harf duyarsız biçim.
 * (JS'in toLowerCase()'i "İ" harfini "i" + birleşik nokta yapar; "indirim" araması "İNDİRİM"i bulamazdı.)
 */
export function norm(s) {
  return String(s ?? '').replaceAll('İ', 'i').replaceAll('I', 'i').replaceAll('ı', 'i').toLowerCase();
}

const DELETED_PATTERNS = [
  'this message was deleted',
  'bu mesaj silindi',
  'mesaj silindi',
  'se eliminó este mensaje',
  'diese nachricht wurde gelöscht',
  'ce message a été supprimé',
];

/** Mesajlaşma uygulamaları gönderen mesajı silince bu yer tutucu metni gösterir. */
export function isDeletedNotice(text) {
  const t = String(text ?? '').trim().toLowerCase();
  return DELETED_PATTERNS.some((p) => t === p || t.endsWith(p));
}

/** Eşleşen ilk etkin kural ya da null. Kuraldaki boş pkg/keyword joker sayılır. */
export function matchRule(rules, pkg, title, text) {
  const hay = norm(`${title} ${text}`);
  return (
    rules.find(
      (r) => r.enabled && (!r.pkg || r.pkg === pkg) && (!r.keyword || hay.includes(norm(r.keyword))),
    ) ?? null
  );
}

/** 0 bugün, 1 dün, 2 bu hafta, 3 daha eski. */
export function dayBucket(now, t, zoneOffsetMs = 0) {
  const d = Math.floor((now + zoneOffsetMs) / 86_400_000) - Math.floor((t + zoneOffsetMs) / 86_400_000);
  if (d <= 0) return 0;
  if (d === 1) return 1;
  if (d < 7) return 2;
  return 3;
}

/** CSV hücresi: tırnakları kaçırır, formül enjeksiyonunu (=, +, -, @) engeller. */
export function csvCell(v) {
  let s = String(v ?? '');
  if (/^[=+\-@\t\r]/.test(s)) s = `'${s}`;
  return `"${s.replaceAll('"', '""')}"`;
}
