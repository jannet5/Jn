// Yol B ve C'nin girdilerini resmi kaynaklarından indirir (lisans nedeniyle depoya kopyalanmaz).
import fs from "node:fs";
import path from "node:path";
import { createHash } from "node:crypto";
import { fileURLToPath } from "node:url";

const HEDEF = path.join(path.dirname(fileURLToPath(import.meta.url)), "girdiler");
const KAYNAKLAR = {
  "frontend-design-SKILL.md": "https://raw.githubusercontent.com/anthropics/claude-code/main/plugins/frontend-design/skills/frontend-design/SKILL.md",
  "design-md-spec.md": "https://raw.githubusercontent.com/google-labs-code/design.md/main/docs/spec.md",
};
fs.mkdirSync(HEDEF, { recursive: true });
for (const [ad, url] of Object.entries(KAYNAKLAR)) {
  const yanit = await fetch(url);
  if (!yanit.ok) throw new Error(`${url} → HTTP ${yanit.status}`);
  const metin = await yanit.text();
  fs.writeFileSync(path.join(HEDEF, ad), metin);
  console.log(ad, createHash("sha256").update(metin).digest("hex"), url);
}
