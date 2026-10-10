# Dynamic Workflows (resmi doküman) — S-sYlFiGFv8 "How code review inspired dynamic workflows" bölümü için

- URL: https://code.claude.com/docs/en/workflows — Çekildi 2026-10-09.

## Ne
- Claude'un yazdığı, yeniden çalıştırılabilir **JavaScript betiği** onlarca-yüzlerce subagent'ı orkestre eder; ara sonuçlar betik değişkenlerinde kalır, ana bağlama sadece sonuç girer.
- Kim planı tutar: Subagent/Skill → Claude tur tur; Agent team → lead; **Workflow → betik**. Ölçek: onlarca-yüzlerce ajan; durdurulup aynı oturumda sürdürülebilir.
- Tetikleme: prompt'ta `ultracode` anahtar kelimesi ya da "use a workflow"; `/effort ultracode` ile oturum boyu otomatik. Hazır: `/deep-research <soru>`. İzleme: `/workflows` (p duraklat, x durdur, r yeniden başlat, s kaydet). Kaydet: `.claude/workflows/` (proje) veya `~/.claude/workflows/` → `/<ad>` komutu; `args` ile girdi.

## Örnek prompt'lar (aynen)
- "use a workflow to audit every route handler under src/routes/ for missing authentication checks, and adversarially verify each finding before reporting it"
- "use a workflow to run npx tsc --noEmit and keep fixing the reported errors until the type check passes or two rounds in a row make no progress"
- "use a workflow to migrate every component under src/components/ from JavaScript to TypeScript, working on each file in its own isolated copy"
- "use a workflow to review every file changed in this PR for correctness issues, then merge the per-file findings into one ranked summary"
- "use a workflow to find flaky tests in this repo: run the suite repeatedly, record which tests fail intermittently, and stop once two rounds in a row find nothing new"

## Betik iskeleti (aynen)
```javascript
export const meta = {
  name: 'audit-routes',
  description: 'Audit every route handler for missing auth checks',
}
const found = await agent('List every .ts file under src/routes/.', {
  schema: { type: 'object', required: ['files'], properties: { files: { type: 'array', items: { type: 'string' } } } },
})
const audits = await pipeline(found.files, file =>
  agent(`Audit ${file} for missing authentication checks.`, { label: file }),
)
return audits.filter(Boolean)
```
`agent()`, `pipeline()`, `parallel()`, `phase()`, `log()`; `schema` ile JSON çıktı. Limitler: 16 eşzamanlı ajan (varsayılan), 1000 ajan/çalıştırma, 4096 öğe/pipeline. Boyut kılavuzu: `/config workflowSizeGuideline=small|medium|large`.

## Bizim fabrika için
- "Review'dan ilham" kalıbı: her teslim öncesi **bul → bağımsız ajanla çürütmeye çalış → sadece doğrulananları raporla** (adversarial verify).
- Çok müşteri / çok sayfa işleri (ör. 30 restoran sitesi için aynı şablonu uygula) → pipeline fan-out + kaydedilmiş workflow komutu.
