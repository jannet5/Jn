const Anthropic = require("@anthropic-ai/sdk");

const client = new Anthropic({ apiKey: process.env.ANTHROPIC_API_KEY });
const MODEL = process.env.ANTHROPIC_MODEL || "claude-haiku-4-5-20251001";

const RESPONSE_SHAPE = `Şu JSON şemasına birebir uyan TEK bir JSON nesnesi döndür, başka hiçbir metin ekleme:
{
  "originLanguage": string,        // kelimenin geldiği dil, örn. "Arapça", "Eski Türkçe", "Fransızca"
  "summary": string,               // 2-4 cümlelik akıcı, sade Türkçe anlatım: kelime hangi dilden, nasıl bir kökten, hangi anlamla gelmiş, Türkçeye nasıl ulaşmış
  "derivationChain": string,       // kısa köken zinciri, örn. "Arapça h-m-d kökü -> Ahmad -> Türkçe Ahmet"
  "meaning": string,               // kelimenin/ismin güncel ve kök anlamı
  "funFact": string | null         // varsa ilginç bir ek bilgi, yoksa null
}`;

async function explainFromStructuredData(word, structured) {
  const prompt = `Aşağıda "${word}" kelimesi için Nişanyan Sözlük kaynaklı ham etimoloji verisi var. Bu veriyi kullanarak sıradan bir kullanıcının rahatça anlayacağı, akıcı bir Türkçe açıklamaya dönüştür. Verideki dilbilimsel sembolleri/kısaltmaları çözerek yaz, veriyi uydurma bir şeyle değiştirme.

Ham veri:
- Kök dil kodu: ${structured.derivedLang || "belirtilmemiş"}
- Açıklama: ${structured.explanation}
- Tarihte ilk görülme: ${structured.firstInHistory || "bilinmiyor"}
- Ek bilgi: ${structured.additional || "yok"}

${RESPONSE_SHAPE}`;

  return callClaude(prompt);
}

async function explainFromOwnKnowledge(word) {
  const prompt = `"${word}" kelimesinin/isminin etimolojisini (kökenini) kendi bilgine dayanarak açıkla. Bu kelime için Nişanyan Sözlük gibi bir kaynakta veri bulunamadı; bu yüzden bu isim/kelime muhtemelen özel bir isim (kişi adı vb.) ya da nadir bir sözcük. Kesin olmadığın noktalarda "muhtemelen" gibi ifadeler kullan, uydurma kesin bilgi verme.

${RESPONSE_SHAPE}`;

  return callClaude(prompt);
}

async function callClaude(prompt) {
  const message = await client.messages.create({
    model: MODEL,
    max_tokens: 700,
    messages: [{ role: "user", content: prompt }],
  });

  const text = message.content
    .filter((block) => block.type === "text")
    .map((block) => block.text)
    .join("");

  const jsonMatch = text.match(/\{[\s\S]*\}/);
  if (!jsonMatch) {
    throw new Error("Yapay zekadan geçerli JSON alınamadı");
  }
  return JSON.parse(jsonMatch[0]);
}

module.exports = { explainFromStructuredData, explainFromOwnKnowledge };
