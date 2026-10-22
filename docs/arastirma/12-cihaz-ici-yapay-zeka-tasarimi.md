# Cihaz içi yapay zekâ: Sende'nin YZ'li ayrı sürümü için seçenekler ve karar

Araştırma kolu: cihaz-ici-yz (kendim, 22 Ekim 2026; alt-ajan kotası dolu olduğu için doğrudan
WebSearch/WebFetch ile)

Etiketler: [resmi] üreticinin kendi sayfası, [ölçüm] sayfadaki ölçüm tablosu, [çıkarım] benim
çıkarımım, [doğrulanamadı] bulamadım. Hiçbir şey gerçek cihazda denenmedi.

## İstenen

- Ayrı uygulama (yan yana kurulur), mesajları internete çıkmadan telefonda kategorilere ayırsın
  (sipariş, randevu, soru, şikayet, ödeme, reklam/gereksiz).
- Her mesajın altında küçük yuvarlak YZ simgesi; dokununca o mesajla sohbet açılsın, cevap önersin,
  "şöyle yaz" denince yeniden yazsın.

## Seçenek 1: Gemini Nano (ML Kit GenAI Prompt API, AICore)

- Bağımlılık: `com.google.mlkit:genai-prompt:1.0.0-beta4`. Android API 26+. Kilidi açık bootloader'da
  desteklenmiyor. Model, sistemin AICore uygulamasındadır; uygulama durumu sorar (AVAILABLE,
  DOWNLOADABLE, DOWNLOADING, UNAVAILABLE) ve gerekirse indirmeyi başlatır. [resmi]
  https://developers.google.com/ml-kit/genai/prompt/android/get-started
- Girdi 4000 token altında olmalı; uzun çıktılar (4K token üstü) önerilmiyor; uygulama başına çıkarım
  kotası var, sayısı yazılmamış. [resmi, aynı sayfa]
- Dil: sayfada dil listesi yok. Arama sonucunda Korece sayfa "İngilizce ve Korece doğrulandı" diyor;
  Türkçe geçmiyor. [doğrulanamadı] Türkçe kalitesi bilinmiyor.
- Cihaz kapsamı dar (belirli üst segment telefonlar). Resmi liste:
  https://developers.google.com/ml-kit/genai (üretici listesi) [resmi]
- Artı: model uygulamaya girmez, APK küçük kalır, pil/ısı yönetimini sistem yapar.
- Eksi: Türkçe güvencesi yok, cihaz kapsamı dar, model indirmesi AICore'un işi (internet gerekir,
  uygulamanın değil); kota.

## Seçenek 2: Gemma 3 1B (MediaPipe LLM Inference / LiteRT-LM)

- MediaPipe LLM Inference artık yalnız bakım modunda; yeni işler LiteRT-LM'de. [resmi]
  https://developers.google.com/edge/mediapipe/solutions/genai/llm_inference
- Model dosyası `gemma3-1b-it-int4.litertlm` (veya `.task`): 529 MB. Samsung S24 Ultra'da CPU
  int4: çözme ≈ 47–55 token/sn, ilk token 2–3 sn, RAM ≈ 1,0–1,2 GB; GPU ile ön doldurma çok hızlı. [ölçüm]
  https://huggingface.co/litert-community/Gemma3-1B-IT
- Lisans: Gemma şartları; dosyaya erişmek için Hugging Face'te oturum açıp şartları kabul etmek
  gerekiyor. [resmi] Yani CI veya ben bu dosyayı hesapsız indiremem; kurucu kabul edip bir kez
  indirmeli. [çıkarım]
- Türkçe: sayfada dil listesi yok. [doğrulanamadı] (Gemma ailesi çok dilli diye bilinir; 1B
  ölçeğinde Türkçe sohbet kalitesi ölçülmedi.)
- Dağıtım: 529 MB APK'ya girmez. Play'de varlık paketi (asset pack) olarak (Play kendi indirir,
  uygulamada INTERNET izni gerekmez) ya da kullanıcı dosyayı kendi seçer (SAF). [çıkarım]

## Seçenek 3: Küçük sınıflandırıcı (TFLite)

- Hazır yol: TensorFlow Lite Task Library `NLClassifier` / `BertNLClassifier`, Model Maker ile eğitim;
  MobileBERT veya ortalama kelime vektörü. [resmi]
  https://developers.google.com/edge/litert/libraries/modify/text_classification
- Türkçe için hazır, ölçülmüş bir niyet sınıflandırıcı bulamadım. [doğrulanamadı] Etiketli Türkçe
  veri üretmek gerekir.

## Karar (bunu ben seçtim)

Hibrit, katmanlı, her katman tek başına çalışır:

1. **Kategorizasyon: kural + sözlük, tamamen cihazda, model yok.** Türkçe (ve İngilizce) anahtar
   kalıplar, yazım hatası/ekler için kök eşleme, emoji ve sayı ipuçları (saat, tutar, IBAN).
   Milisaniye altı, APK'ya ek yük yok, her telefonda çalışır, Türkçe test derlemiyle ölçülür.
   Neden: ölçülmemiş bir modele güvenmek yerine ölçülebilir ve açıklanabilir bir çekirdek.
2. **Cevap taslağı: şablon motoru.** Kategoriye göre kısa, kibar Türkçe taslaklar; müşterinin
   adı ve kullanıcının düzenleyebildiği iş bilgileri ("Çalışma saatlerimiz…") yerleştirilir.
   "Şöyle yaz" için ton seçenekleri (resmi, samimi, kısa).
3. **İsteğe bağlı üretken katman (arka uç arayüzü):** `LocalLlm` arayüzü. Cihaz destekliyorsa
   Gemini Nano (ML Kit Prompt API) "deneysel" etiketiyle; ileride kullanıcı kendi `.litertlm`
   dosyasını seçerse LiteRT-LM. Bu katman yoksa uygulama aynen çalışır.
4. Hepsi yan yana kurulan ayrı uygulamada (`com.sende.ai`), internet izni yok, veri çıkmaz.

## Riskler ve dürüst sınırlar

- Türkçe üretken model kalitesi hiçbir yerde ölçülmedi; bu yüzden üretken katman varsayılan değil.
- Gemini Nano yalnız belirli cihazlarda var; Türkçe için resmi güvence yok.
- Gemma dosyası kurucunun lisans onayı olmadan indirilemez.
- Kural tabanlı kategorizasyon argo/yeni ifadelerde hata yapar; kullanıcı kategoriyi tek dokunuşla
  düzeltebilmeli (ve düzeltmeler yalnız cihazda saklanmalı).
