# HAT: REKLAM (Meta + Google)

Hedef: yerel işletmeye ölçülebilir lead (arama / WhatsApp / form / rezervasyon). Her şey PAUSED başlar, insan onayıyla açılır.

## Bant
```
R0 Kurulum     Meta Business + reklam hesabı + Pixel + CAPI; Google Ads + Google İşletme Profili; dönüşüm olayları (arama tıklama, WhatsApp, form)
R1 Rakip/İçgörü  Meta Ad Library'de rakiplerin uzun süredir yayında olan reklamları (= kanıtlanmış açılar); müşteri yorumlarından dil madenciliği
R2 Kreatif     aynı DESIGN.md'den 10-20 varyasyon (statik 1080×1350 + 9:16 video kurgusu) + marketingskills ad-creative metni
R3 Skor        ad-score 0-100: hook 25 · netlik 20 · teklif 20 · kanıt 10 · CTA 15 · marka 10; <70 yayına girmez
               lansman modu (müşteri yok): kanıt yerine şeffaflık puanlanır (açık fiyat, 'örnek konsept' etiketi)
R4 Kurulum     facebook-python-business-sdk / Meta Ads MCP: campaign → adset → ad, status=PAUSED
               hedefleme: konum yarıçapı (restoran 3-8 km, klinik 10-25 km); amaç OUTCOME_LEADS (WhatsApp/Instant Form) ya da OUTCOME_ENGAGEMENT (mesaj)
R5 Onay        insan kontrol → ACTIVE
R6 Optimizasyon haftalık: harcama, CPM, CTR, lead maliyeti; yorgun kreatif değiş, kazananı çoğalt
R7 Rapor       aylık değer defteri: harcama → lead → maliyet/lead
```

## Kurallar
- Başlangıç günlük bütçe önerisi: 150 / 300 / 600 ₺ (ilk hafta gerçek CPM ile güncellenir).
- Mesaj hedefi WhatsApp numarası olmadan yalnız Instagram Direct'e gider.
- Yeni kampanya her zaman `PAUSED`. Bütçe değişikliği insan onaylı.
- Sağlık/klinik: Meta sağlık reklam politikası kontrolü.
- Google tarafı: yerel işletmede önce Google İşletme Profili + yorum toplama, sonra Local Services / Performance Max.
- Araçlar: AgriciDaniel-claude-ads, mathiaschu-meta-ads-analyzer, pipeboard meta-ads-mcp, coreyhaines31 marketingskills (`ads`, `ad-creative`) — kopyalar `arastirma/github/kopyalar/`.
