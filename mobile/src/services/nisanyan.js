const BASE_URL = "https://api.etimolojiturkce.com";

// Nişanyan Sözlük verisini birleştiren üçüncü parti API - doğrudan telefondan çağrılır.
// Kapsamı: yaygın Türkçe kelimeler. Özel isimler (Ahmet, Ayşe vb.) bu kaynakta yok.
export async function lookupWord(word) {
  const controller = new AbortController();
  const timeout = setTimeout(() => controller.abort(), 8000);

  try {
    const url = `${BASE_URL}/word/${encodeURIComponent(word.trim())}`;
    const res = await fetch(url, { signal: controller.signal });
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
  } catch {
    return null;
  } finally {
    clearTimeout(timeout);
  }
}
