# Araç adayları

Not: Kaynaklar WebSearch/WebFetch ile okundu; kurulum denenmedi.

## Tasarım sistemi (DESIGN.md)
### VoltAgent/awesome-claude-design (okundu)
https://github.com/VoltAgent/awesome-claude-design — 68 hazır DESIGN.md (Claude Design için). Kişiliğe uyanlar (listeden, yalnız tek satırlık açıklamalara dayanır; dosyaların içi açılmadı):
- Notion (sıcak minimalizm, serif başlık), Linear (ultra minimal), Things'e en yakın ruh: Apple (beyaz boşluk), Cal.com (nötr), Mintlify, Ollama (monokrom), Nike/Uber (siyah-beyaz keskin).
- Kaçın: mor gradyanlı olanlar (Stripe, Linear'ın mor vurgusu, Superhuman, Lovable) -> slop riski.
- Artı: hızlı başlangıç, ajan okur. Eksi: hepsi web markası; mobil liste için ölçü içermez, uyarlama gerekir.
### designmd.app (okundu)
https://designmd.app — 759 DESIGN.md (YAML front matter + açıklama), 42 gerçek marka; Claude Code, Cursor vb. ile uyumlu. Artı: çok seçenek. Eksi: kalite değişken, tarama zor; mobil odaklı değil. Tek dosya seçip elle sadeleştir.
Nokta için: büyük olasılıkla hazır dosya yerine kendi 1 sayfalık DESIGN.md (renk, tip, boşluk, hareket) yazmak daha iyi.

## Android + AI ajan
### Android CLI (Google, 1.0)
Kaynaklar: https://theregister.com/2026/04/20/google_previews_android_cli , https://infoq.com/news/2026/05/agent-friendly-android-cli , https://heise.de/-11301645 (yalnız arama özetleri; proandroiddev makalesi 403 verdi)
- `android` komutu: proje şablonu, SDK/emülatör yönetimi, SKILL.md biçiminde "Android skills"; Claude Code/Codex ile çalışır; Compose Preview render, statik analiz, bağımlılık sürümü sorgusu. Google %70 az token, 3x hız iddia ediyor (üretici iddiası).
- Artı: resmi, ajan odaklı. Eksi: Linux AMD64 destekli ama emülatör bulutta zor. Nokta: ÇOK UYGUN (proje/bağımlılık/preview).
### adb-mcp (iksnerd)
https://github.com/iksnerd/adb-mcp — MIT, 79 araç: emülatör aç, ekran görüntüsü, UI hiyerarşisi, dokunma/kaydırma, logcat, Gradle. Artı: ajan kendi UI'sini görüp dener. Eksi: emülatör/cihaz gerekir; bu bulut ortamında KVM olmayabilir. Nokta: yerel/cihazda uygun.
### Android UI Assist MCP (infiniV)
https://glama.ai/mcp/servers/@infiniV/Android-Ui-MCP — canlı ekran görüntüsü alıp ajana UI geri bildirimi verdirir. Cihaz gerekir; olgunluğu bilinmiyor.
### AVD MCP (jramalho)
https://glama.ai/mcp/servers/@jramalho/avd-mcp — emülatör başlat + ekran görüntüsü. Basit; küçük proje.

## Ekran görüntüsü testi
### Roborazzi (okundu)
https://github.com/takahirom/roborazzi — Robolectric ile JVM'de çalışır (emülatör GEREKMEZ), Compose @Preview'dan otomatik test, record/verify/compare Gradle görevleri, GIF kaydı, UI ağacı JSON'u (ajan için), isteğe bağlı Gemini/OpenAI anlamsal doğrulama. Artı: bulutta/CI'da çalışır, ajan PNG'yi okuyup UI'yi inceler. Eksi: Robolectric render'ı gerçek cihazdan biraz farklı. Nokta: EN UYGUN görsel doğrulama.
### Paparazzi (snippet)
Aynı iş (JVM render), Robolectric ile uyumsuz, etkileşim yok (https://proandroiddev.com/screenshot-testing-in-compose-f8a7389a7e6). Nokta: sürükleme/tıklama durumları için Roborazzi tercih.

## CI / dağıtım
- actions/upload-artifact@v4 ile APK/AAB'yi çalıştırma çıktısı olarak sakla (en basit); r0adkll/upload-google-play (Play iç test); fastlane (https://www.runway.team/blog/ci-cd-pipeline-android-app-fastlane-github-actions). Kaynak: arama özetleri, sayfalar tek tek okunmadı.
- Drive'a yükleme: bu oturumda Google Drive MCP aracı (create_file) var; CI'dan Drive'a yükleme için okuduğum hazır bir kaynak bulamadım -> doğrulanmadı. Pratik yol: Actions artifact + elle indir, ya da Drive API servis hesabı (denenmedi).
