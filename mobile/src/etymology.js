import { getApiKey } from "./apiKey";
import { getCached, setCached } from "./cache";
import * as claude from "./services/claude";
import * as nisanyan from "./services/nisanyan";

export async function fetchEtymology(word) {
  const trimmed = word.trim();

  const cached = await getCached(trimmed);
  if (cached) return { ...cached, cached: true };

  const structured = await nisanyan.lookupWord(trimmed);
  const apiKey = await getApiKey();

  let aiResult;
  let source;

  if (structured && apiKey) {
    aiResult = await claude.explainFromStructuredData(apiKey, trimmed, structured);
    source = "nisanyan";
  } else if (structured) {
    // API key girilmemişse Nişanyan'ın ham verisini olduğu gibi göster
    aiResult = {
      originLanguage: structured.derivedLang || "belirtilmemiş",
      summary: structured.explanation,
      derivationChain: structured.explanation,
      meaning: structured.explanation,
      funFact: structured.additional,
    };
    source = "nisanyan";
  } else if (apiKey) {
    aiResult = await claude.explainFromOwnKnowledge(apiKey, trimmed);
    source = "ai";
  } else {
    throw new Error(
      "Bu kelime Nişanyan Sözlük'te bulunamadı. Özel isimler ve nadir kelimeler için Ayarlar'dan bir Anthropic API key ekle."
    );
  }

  const result = { word: trimmed, verified: source === "nisanyan", source, ...aiResult };
  await setCached(trimmed, result);
  return { ...result, cached: false };
}
