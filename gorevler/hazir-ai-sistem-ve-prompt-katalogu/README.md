# Hazır AI Sistem ve Prompt Kataloğu

**Amaç tek cümlede:** Gidip bakıp "bu güzelmiş, kullanayım" diyebileceğin **hazır prompt sitelerini**,
**hazır ajan/sistem paketlerini** ve **otomasyon şablonlarını** her alandan toplayan, linkli ve
örnekli bir rehber. (Prompt yazma teorisi değil; hazır olanların listesi.)

## Nereden başlamalı?

| Dosya | Ne var | Nasıl açılır |
|---|---|---|
| **`index.html`** | Aranabilir, kategori ve lisans filtreli katalog + 30 hazır prompt; kopyala düğmesi yalnız lisansı izin veren (CC0/MIT) metinlerde | Çift tıkla (internet gerekmez; linkler için gerekir) |
| `KATALOG.md` | Aynı katalog düz metin: 36 kaynak, 9 kategori, her birinde link, ne işe yarar, örnek, kullanım adımları, ücret, dikkat | Herhangi bir Markdown görüntüleyici / GitHub |
| `hazir-promptlar.md` | 30 kopyala-yapıştır prompt (prompts.chat, CC0, birebir) + Fabric `extract_wisdom` tam metni + **Türkçe "tekte tam sistem prompt'u" ana şablonu** | Metin editörü |
| `LISANS-ENVANTERI.md` | Her kaynağın içerik lisansı, her birebir kopyanın commit'e sabit kaynağı, sahibi ve yeniden dağıtım sınırı | — |
| `NOTICE.md` + `lisanslar/` | Üçüncü taraf telif bildirimleri; MIT ve CC0 lisans belgelerinin tam metinleri | — |
| `harita.md` | Hedef → bağımlılık → A/B/C yolları → uygulama → test → teslim haritası | — |
| `calisma-gunlugu.md` | Ne yapıldı, hangi araç/komut, kaynaklar, kararlar, sorunlar, doğrulamalar | — |
| `dogrulama/` | Canlı link raporu, birebir alıntı raporu, lisans doğrulaması, negatif test, tarayıcı testi, ekran görüntüleri | — |

## Kategoriler

1. Her alan: hazır prompt kütüphaneleri (prompts.chat, GPT Mağazası, AIPRM, FlowGPT, PromptBase, Poe...)
2. İş ve ofis: resmi prompt paketleri (OpenAI Academy, Claude Academy, Google Gemini, Microsoft Copilot)
3. İçerik işleme ve kişisel üretkenlik sistemi (Fabric)
4. Hazır ajan ekipleri / iş akışı sistemleri (The Agency, Superpowers, BMAD, Spec Kit...)
5. Editör kuralları ve skill koleksiyonları (Anthropic Skills, awesome-copilot, awesome-cursorrules...)
6. Gerçek ürünlerin sistem promptları (Cursor, Claude, Lovable... nasıl kurulmuş)
7. Otomasyon şablonları (n8n, Dify, CrewAI)
8. Görsel ve video prompt galerileri (PromptHero, Lexica, Civitai)
9. Resmi prompt rehberleri ve prompt hub'ları

## Yeniden üretme ve doğrulama

```bash
python3 araclar/secki_cek.py      # sabit commit'teki CSV'den (SHA kontrollü) 30 prompt'u birebir çeker
python3 araclar/olustur.py        # KATALOG.md, hazir-promptlar.md, index.html, LISANS-ENVANTERI.md, NOTICE.md
python3 araclar/dogrula.py        # şema + lisans + yasak metin + canlı link + birebirlik (0 = geçti)
python3 araclar/negatif_test.py   # lisans denetimi bilerek konan 4 ihlali yakalıyor mu
NODE_PATH=$(npm root -g) node araclar/ekran-testi.mjs   # Chromium kullanıcı akışı + ekran görüntüleri
```

## Lisans kuralları (kısaca)

- Metni **yalnız** CC0-1.0 veya MIT lisanslı kaynaklardan birebir aldık. Her kopyanın yanında kaynak,
  commit'e sabitlenmiş tam dosya linki, commit ve tarih, sahip, lisans ve yeniden dağıtım sınırı yazılı.
- MIT kopyaları için telif bildirimi ve izin metni `NOTICE.md` ve `lisanslar/` içinde. `index.html` bir MIT
  örneğini kopyalarken bu bildirimi otomatik ekler. CC0 dayanağı `lisanslar/prompts.chat_LICENSE*`.
- Lisansı kısıtlı, tescilli veya bilinmeyen kaynaklarda (ticari galeriler, kullanıcı içerikli siteler, sızdırılmış
  ürün sistem prompt'ları, şirket rehberleri) metin **kopyalanmadı**; yalnız kendi açıklamamız ve link var.
  Bir deponun public olması veya linkinin verilmesi yeniden dağıtım izni değildir.
- Katalogun kendi metni ve kodu için lisans seçilmedi; bu paket sahibinin kararıdır. Bu bir hukuki görüş değildir.

## Açık sınırlar

- ChatGPT Pro ile ortak araştırma yapılmadı (hesap erişimi yok). Gerçek Google arayüzü kullanılmadı.
- Bot korumasına takılan 4 adresin normal tarayıcıda açıldığı test edilmedi, garanti verilmez.
