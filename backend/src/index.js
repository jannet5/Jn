const express = require("express");
const cors = require("cors");
const cache = require("./cache");
const nisanyan = require("./services/nisanyan");
const ai = require("./services/ai");

const app = express();
app.use(cors());

const CACHE_TTL_MS = 1000 * 60 * 60 * 24 * 7; // 1 hafta - aynı kelime tekrar sorulunca AI'ya tekrar ödeme yapmayalım

app.get("/health", (_req, res) => {
  res.json({ status: "ok" });
});

app.get("/api/etymology/:word", async (req, res) => {
  const rawWord = req.params.word;
  const word = rawWord?.trim().toLocaleLowerCase("tr-TR");

  if (!word) {
    return res.status(400).json({ error: "Kelime boş olamaz" });
  }

  const cacheKey = `etymology:${word}`;
  const cached = cache.get(cacheKey);
  if (cached) {
    return res.json({ ...cached, cached: true });
  }

  try {
    const structured = await nisanyan.lookupWord(word).catch(() => null);
    const hasAiKey = Boolean(process.env.ANTHROPIC_API_KEY);

    let aiResult;
    let source;
    if (structured && hasAiKey) {
      aiResult = await ai.explainFromStructuredData(word, structured);
      source = "nisanyan";
    } else if (structured) {
      // ANTHROPIC_API_KEY yoksa Nişanyan'ın ham verisini olduğu gibi döndür
      aiResult = {
        originLanguage: structured.derivedLang || "belirtilmemiş",
        summary: structured.explanation,
        derivationChain: structured.explanation,
        meaning: structured.explanation,
        funFact: structured.additional,
      };
      source = "nisanyan";
    } else if (hasAiKey) {
      aiResult = await ai.explainFromOwnKnowledge(word);
      source = "ai";
    } else {
      return res.status(503).json({
        error:
          "Bu kelime Nişanyan Sözlük'te bulunamadı ve ANTHROPIC_API_KEY tanımlı değil, bu yüzden yapay zeka yedeği çalıştırılamıyor.",
      });
    }

    const result = {
      word: rawWord,
      verified: source === "nisanyan",
      source,
      ...aiResult,
    };

    cache.set(cacheKey, result, CACHE_TTL_MS);
    res.json({ ...result, cached: false });
  } catch (err) {
    console.error("Etimoloji sorgusu başarısız:", err);
    res.status(502).json({
      error: "Kelime kökeni alınamadı, lütfen daha sonra tekrar deneyin.",
    });
  }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Kelime kökeni backend'i http://localhost:${PORT} adresinde çalışıyor`);
});
