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
    // Key yok, Nişanyan'da da yok (çoğunlukla özel isimler) — hata fırlatmak yerine
    // nazik bir "bulunamadı" sonucu döndür, uygulama key'siz hiç kırılmasın.
    return {
      word: trimmed,
      verified: false,
      source: "not_found",
      cached: false,
    };
  }

  const result = { word: trimmed, verified: source === "nisanyan", source, ...aiResult };
  await setCached(trimmed, result);
  return { ...result, cached: false };
}
