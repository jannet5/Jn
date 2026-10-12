# Claude Code for Beginners — Build Your First App with AI (No Coding Required!) — Teacher's Tech — 13 dk — https://www.youtube.com/watch?v=s-Mc26Ytz10

## Tek paragraf: ne yapıyor, sonuç ne
Jamie, Claude Code'u Windows (winget) ve Mac'te (Homebrew) kurup terminalde ilk kurulum ekranlarını geçiyor, ardından plan mode'da tek bir düz İngilizce prompt'la masaüstünde klasör açtırıp **tek HTML dosyalık kişisel bookmark dashboard** yaptırıyor: kart grid, arama, kategori filtre butonları, localStorage kalıcılığı, örnek veri, mavi tema, silme, responsive. İkinci prompt'la tarayıcıda açtırıyor, üçüncüsüyle edit butonu ve dark mode toggle ekletiyor (Claude dosyayı baştan yazmıyor, hedefli değişiklik yapıyor). Sonda 6 pratik ipucu (spesifik ol, plan mode, iterasyon, hata yapıştırma, açıklama isteme, `/compact`). Temel seviye; fabrika için yeni bilgi az, ama prompt yapısı ve kurulum hataları işe yarar.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code (terminal) — Pro $20/ay ile başlanması öneriliyor; Max 5x/20x; ücretsiz planda yok.
- Linkler (açıklamadan): https://claude.ai/pricing , https://code.claude.com/docs , Git Bash for Windows: https://git-scm.com/downloads/win
- Kurulum komutları (açıklamadan aynen):
  ```
  brew install claude-code
  winget install Anthropic.ClaudeCode
  claude
  $env:CLAUDE_CODE_GIT_BASH_PATH = "C:\Program Files\Git\bin\bash.exe"
  ```
- Homebrew (Mac), Git for Windows/Git Bash, tarayıcı localStorage.

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:03:03] Windows: Terminal'i yönetici olarak aç → `winget install Anthropic.ClaudeCode`.
2. [00:03:42] Kırmızı hata (Git Bash bulunamıyor) → git-scm.com'dan Git for Windows indir/kur → tekrar `claude`. (Gerekirse `CLAUDE_CODE_GIT_BASH_PATH` env değişkeni.)
3. [00:04:03] Mac: önce Homebrew kur, sonra `brew install claude-code`, `claude`.
4. [00:04:51] İlk açılış: tema (dark mode) → abonelikle giriş (seçenek 1, tarayıcıda authorize) → güvenlik notları → klasöre güven (1) → effort level: high (1).
5. [00:06:02] `claude` başlat, Shift+Tab ile **plan mode**, prompt #1 yapıştır.
6. [00:07:55] Plan çıkıyor; onayda seçenek 2 "auto accept edits".
7. [00:08:37] Prompt #2 ile tarayıcıda açtırma; izin sorusunda "2" (bir daha sorma).
8. [00:09:17] Test: arama (CSS, free), kategori butonları, silme (X → onay), yeni bookmark ekleme (yeni kategori oluşuyor), refresh sonrası localStorage kalıcılığı.
9. [00:10:01] Prompt #3: edit butonu + dark mode toggle; Claude mevcut dosyayı okuyup hedefli ekliyor. Ay ikonu ile toggle, edit formu çalışıyor.
10. [00:11:45] İpuçları (aşağıda).

## Kullandığı prompt'lar (varsa aynen)
1. Uygulamayı kur (açıklamadaki metin):
```
Create a new folder on my Desktop called my-bookmarks-app, then build me a personal bookmark dashboard as a single HTML file inside it. Here's what it should do:

Let me save bookmarks with a title, URL, category, and optional notes
Display all bookmarks as nice-looking cards in a grid layout
Include a search bar that filters bookmarks by title or category
Let me filter by category using clickable buttons
Save everything to the browser's local storage so bookmarks persist
Include some sample bookmarks so it doesn't look empty when it first loads
Make it look modern and clean with a blue color scheme
Let me delete bookmarks I don't want anymore
Make it responsive so it works on mobile too
```
2. Aç:
```
Open the app in my browser so I can see it
```
3. Özellik ekle:
```
Can you add two more features to the bookmark app:

An edit button on each card so I can update the title, URL, or category
A dark mode toggle in the top right corner
```
4. İpucu kalıpları:
```
Make me a single-page website with a header, three sections, a contact form, and a blue color scheme.
The search bar isn't filtering correctly. Can you fix it?
Can you make the cards bigger?
I'm getting this error: [paste error here] — can you fix it?
Can you explain how the search feature works?
Walk me through what this code does.
/compact
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Özel bir tasarım tekniği yok; sadece prompt'ta "nice-looking cards in a grid layout", "modern and clean with a blue color scheme", "responsive" ve **örnek veriyle boş görünmeyi engelleme** ("Include some sample bookmarks so it doesn't look empty"). Dark mode toggle sağ üstte ay ikonu.

## Hatalar ve çözümleri
- Windows'ta winget sonrası kırmızı Git Bash hatası → Git for Windows kur; gerekirse `$env:CLAUDE_CODE_GIT_BASH_PATH = "C:\Program Files\Git\bin\bash.exe"`.
- İzin hataları → Terminal'i "Run as administrator".
- Diğer kurulum hataları → hata mesajını claude.ai'ye yapıştır.
- Plan mode ilk Shift+Tab'da açılmamıştı → tekrar Shift+Tab, mod göstergesini kontrol et.
- Uzun oturumda yavaşlama/bağlam kaybı → `/compact`.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Özellik listesi formatında prompt (madde madde "Let me...", "Display...", "Include...") — aşama prompt şablonlarımızda kullan.
- "Örnek/seed veri ile boş ekran gösterme" kuralını CLAUDE.md'ye ekle: her liste ekranı ilk açılışta demo veriyle dolu + gerçek empty state tasarımı.
- Plan mode zorunlu (Shift+Tab) + planı onaylarken "auto accept edits"; iterasyonda baştan yazdırma yok, hedefli değişiklik iste.
- Müşteri/ekip kurulum rehberine Windows Git Bash fix'ini ve `CLAUDE_CODE_GIT_BASH_PATH` satırını ekle.
- Dark mode toggle ve edit akışını standart bileşen setine dahil et (Expo'da `useColorScheme` + tema token'ları).
