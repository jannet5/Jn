# Hazır AI Sistem ve Prompt Kataloğu

**Amaç tek cümlede:** Gidip bakıp "bu güzelmiş, kullanayım" diyebileceğin **hazır prompt sitelerini**,
**hazır ajan/sistem paketlerini** ve **otomasyon şablonlarını** her alandan toplayan, linkli ve
örnekli bir rehber. (Prompt yazma teorisi değil; hazır olanların listesi.)

## Nereden başlamalı?

| Dosya | Ne var | Nasıl açılır |
|---|---|---|
| **`index.html`** | Aranabilir, kategori filtreli katalog + 30 hazır prompt; her kartta "kopyala" düğmesi | Çift tıkla, tarayıcıda açılır (internet gerekmez; linkler için gerekir) |
| `KATALOG.md` | Aynı katalog düz metin: 36 kaynak, 9 kategori, her birinde link, ne işe yarar, örnek, kullanım adımları, ücret, dikkat | Herhangi bir Markdown görüntüleyici / GitHub |
| `hazir-promptlar.md` | 30 kopyala-yapıştır prompt (prompts.chat, CC0, birebir) + Fabric `extract_wisdom` tam metni + **Türkçe "tekte tam sistem prompt'u" ana şablonu** | Metin editörü |
| `harita.md` | Hedef → bağımlılık → A/B/C yolları → uygulama → test → teslim haritası | — |
| `calisma-gunlugu.md` | Ne yapıldı, hangi araç/komut, kaynaklar, kararlar, sorunlar, doğrulamalar | — |
| `dogrulama/` | Canlı link raporu, birebir alıntı raporu, tarayıcı testi, ekran görüntüleri | — |

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
python3 araclar/secki_cek.py      # CSV anlık görüntüsünden 30 prompt'u birebir çeker
python3 araclar/olustur.py        # KATALOG.md, hazir-promptlar.md, index.html
python3 araclar/dogrula.py        # şema + canlı link + birebirlik (çıkış kodu 0 = geçti)
NODE_PATH=$(npm root -g) node araclar/ekran-testi.mjs   # Chromium kullanıcı akışı + ekran görüntüleri
```

Lisans notu: prompts.chat prompt metinleri CC0; Fabric, The Agency, Superpowers, VoltAgent alıntıları
MIT lisanslı kaynaklardan kısa/tam alıntıdır ve kaynak linkleriyle verilmiştir. Diğer sitelerden yalnız
kısa alıntı veya tarif vardır.
