const BASE_URL = "https://api.etimolojiturkce.com";

// Nişanyan Sözlük verisini birleştiren üçüncü parti API.
// Kapsamı: yaygın Türkçe kelimeler. Özel isimler (Ahmet, Ayşe vb.) bu kaynakta yok.
async function lookupWord(word) {
  const url = `${BASE_URL}/word/${encodeURIComponent(word.trim())}`;
  const res = await fetch(url, { signal: AbortSignal.timeout(8000) });
  if (!res.ok) return null;

  const data = await res.json();
  if (data.return !== "1" || !data.explanation) return null;

  return {
    word: data.word || word,
    derivedLang: data.derivedLang || null,
    explanation: data.explanation,
    firstInHistory: data.firstInHistory || null,
    additional: data.additional || null,
  };
}

module.exports = { lookupWord };
