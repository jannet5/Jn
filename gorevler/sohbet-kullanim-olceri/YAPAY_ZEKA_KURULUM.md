# Yapay zeka için kurulum talimatı

Bu dosya, bu aracı kullanıcının bilgisayarına kuracak yapay zeka asistanı
(Claude Code, Codex vb.) içindir. Adımları sırayla uygula. Her adımın
sonucunu kullanıcıya kısa ve anlaşılır Türkçeyle bildir.

## Araç ne yapar

Claude Code'da her yanıtın sonunda iki şey gösterir: o sohbetin harcadığı
token miktarı ve hesabın kalan 5 saatlik ve haftalık kotası (yenilenme
zamanıyla birlikte). Codex CLI kayıtları varsa onları da ayrıca raporlar.
Yalnızca Python standart kütüphanesini kullanır; `pip install` gerekmez.

## Kurallar (çiğneme)

- Kimlik bilgisi dosyalarını açma, okuma, kopyalama: `~/.claude/.credentials.json`,
  `~/.codex/auth.json`, anahtarlar, tokenler.
- `settings.json` dosyasını elle düzenleme; yalnızca `olc.py kur` ve
  `olc.py kaldir` komutlarını kullan. Bu komutlar önce yedek alır ve mevcut
  ayarlara dokunmaz.
- Kullanıcının mevcut başka bir statusline'ı varsa onu değiştirmeden önce
  kullanıcıya sor.
- Hiçbir veriyi internete gönderme. Bu araç ağ kullanmaz.
- Bir adım başarısız olursa sonraki adıma geçme; hatayı olduğu gibi kullanıcıya göster.

## Adımlar

1. **Python'u kontrol et.** Windows'ta `py -3 --version`, olmazsa
   `python --version` (macOS ve Linux'ta `python3 --version`) çalıştır.
   Sürüm 3.9 veya üstü olmalı. Python yoksa kullanıcıdan python.org ya da
   Microsoft Store üzerinden kurmasını iste ve dur.

2. **Kalıcı bir klasöre çıkar.** Ayar dosyası bu klasörün tam yolunu
   kullanır; klasör sonradan taşınır ya da silinirse rapor çalışmaz.
   Önerilen yerler:
   - Windows: `%USERPROFILE%\sohbet-kullanim-olceri`
   - macOS/Linux: `~/sohbet-kullanim-olceri`

   Bu klasör ZIP'teki `sohbet-kullanim-olceri` klasörünün içeriği olmalı:
   `olc.py`, `sohbet_olcer\`, `testler\`, `KUR.bat` ve diğerleri.
   İndirilenler (Downloads) ya da geçici klasörler kullanma.

3. **Testleri çalıştır** (bu klasörün içinde):
   `py -3 -m unittest discover -s testler`
   (macOS/Linux'ta `python3 -m unittest discover -s testler`).
   Beklenen son satır `OK` (bir test "gerçek transkript yok" diye atlanabilir).
   `FAILED` görürsen dur, çıktıyı kullanıcıya göster.

4. **Kur:** `py -3 olc.py kur`
   - Çıktıda "Başka bir statusline zaten var; değiştirilmedi" yazıyorsa
     kullanıcıya sor. Kabul ederse `py -3 olc.py kur --statusline-degistir`
     çalıştır (eskisi yedekte kalır). Kabul etmezse kota "erişilemiyor"
     görünür, token ölçümü yine çalışır.
   - Çıktıdaki yedek dosyanın yolunu kullanıcıya bildir.

5. **Doğrula:** `py -3 olc.py dogrula-yerel`
   Çıktıda şunlar görünmeli: bulunan Claude konuşmaları ve en son
   konuşmanın token raporu, Codex kaydı (varsa) ve "Kurulum: Stop kancası
   kurulu, statusline kurulu". Rapor ayrıca
   `~/.claude/sohbet-olcer/yerel_dogrulama.txt` dosyasına yazılır.

6. **Kullanıcıya söyle:** "Claude Code'u kapatıp yeniden açın. Bir mesaj
   gönderin; yanıt bitince ekranda 'Bu sohbet: … token' özeti görünecek.
   Kota yüzdesi statusline'ın ilk kaydından sonra, yani bir sonraki
   yanıttan itibaren görünür."

7. **İsteğe bağlı kontrol.** Kullanıcı bir mesaj gönderip Claude Code'u
   yeniden açtıktan sonra `py -3 olc.py rapor` çalıştır ve kota satırlarının
   dolduğunu doğrula. Kota hâlâ "erişilemiyor" görünüyorsa olası nedenler:
   hesap Pro/Max değildir, statusline kurulmamıştır ya da henüz hiç yanıt
   alınmamıştır.

## Kaldırma

`py -3 olc.py kaldir`: yalnızca bu aracın eklediği girdileri siler, önce yedek alır.
Sonra klasör silinebilir.

## Bilinen sınırlar

- claude.ai web ve mobil sohbetlerinin token sayısı ölçülemez; resmî bir
  kaynak yok.
- Kota yüzdesi hesap genelidir; araç bunu tek bir sohbetin tüketimi diye
  göstermez.
- Gerçek bir Windows bilgisayarda ilk kez bu kurulumla çalıştırılacak.
  Önceki doğrulama Wine altında, gerçek Windows CPython 3.12.7 ile yapıldı.
