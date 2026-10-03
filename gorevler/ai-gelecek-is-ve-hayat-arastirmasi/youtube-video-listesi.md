# YouTube Video Listesi ve Transkript Durumu

16 video, `yt-dlp "ytsearch6:<sorgu>"` ile 3 Ekim 2026'da gerçek YouTube aramasından
seçildi (başlık, kanal ve süre aramanın döndürdüğü bilgilerdir).

## Transkript durumu: ALINAMADI (gerçek erişim engeli)

Bu bulut ortamından üç yol denendi, üçü de YouTube tarafından engellendi:

| Yol | Araç | Sonuç |
|---|---|---|
| A | `youtube-transcript-api` (Python) | 16/16 video `IpBlocked` — YouTube bulut IP'sini engelliyor |
| B | `yt-dlp --write-auto-subs` | `HTTP 429 Too Many Requests`, ardından "Sign in to confirm you're not a bot" |
| C | Üçüncü taraf transkript siteleri ve Invidious/Piped aynaları | 403 ya da bot doğrulama sayfası |

Bu yüzden raporda video içerikleri **"izlenmiş gibi" anlatılmadı**. Yalnızca
videolardaki ana figürlerin (Hinton, Amodei, Huang) başka yayınlarda yazıya
dökülmüş ve doğrulanmış sözleri kullanıldı.

## Kullanıcının kendi transkript aracıyla tamamlama

Kullanıcının bilgisayarındaki YouTube transkript aracı (ev IP'si engellenmez) bu
listeyle çalıştırılabilir. Python ile örnek:

```bash
pip install youtube-transcript-api
python3 dogrulama/transkript_al.py   # bu klasördeki betik; transkript/ klasörüne yazar
```

Transkriptler alındıktan sonra her birinden: (1) iddia, (2) kanıt mı tahmin mi,
(3) konuşanın çıkarı çıkarılıp `kanit-matrisi.md` tablosuna `TOPLULUK` veya `TAHMIN`
satırı olarak eklenmelidir.

## Liste

| # | Video | Kanal | Dil | Neden seçildi |
|---|---|---|---|---|
| 1 | https://www.youtube.com/watch?v=fpHOCyFesxI — How will AI impact the jobs market? | BBC News | EN | Ana akım haber çerçevesi |
| 2 | https://www.youtube.com/watch?v=zju51INmW7U — AI company's CEO issues warning about mass unemployment | CNN | EN | Amodei uyarısı |
| 3 | https://www.youtube.com/watch?v=giT0ytynSqg — Godfather of AI: They Keep Silencing Me… | The Diary Of A CEO | EN | Hinton'un "tesisatçı ol" sözünün kaynağı |
| 4 | https://www.youtube.com/watch?v=GWtNUpksm6A — Electricians and Plumbers Will Win | Low Voltage Nation | EN | Huang alıntısı |
| 5 | https://www.youtube.com/watch?v=lmWFtWr9OBo — Why You Won't Find a Plumber in 2030 | Roger Wakefield Plumbing Education | EN | Bir tesisat ustasının bakışı |
| 6 | https://www.youtube.com/watch?v=fh8kYYFji1M — Why AI Can't Touch the Skilled Trades | Dream Select Realty | EN | Elektrikçi sohbeti |
| 7 | https://www.youtube.com/watch?v=PEFso88LkC4 — My Honest Thoughts on AI and the Job Market in 2026 | Tech With Tim | EN | Yazılımcı bakışı |
| 8 | https://www.youtube.com/watch?v=1PdgNyOy20w — Is Software Engineering Dying in 2026? | Tech With Tim | EN | Veriye dayalı yazılım tartışması |
| 9 | https://www.youtube.com/watch?v=Oq8TUJuButM — Will AI Replace Software Engineers? | SuperSimpleDev | EN | Eğitimci bakışı |
| 10 | https://www.youtube.com/watch?v=KQgATAU6ztU — AI Replacing Developers Has Officially Failed | Amigoscode | EN | Karşı görüş |
| 11 | https://www.youtube.com/watch?v=l6d_0PB0Pbg — Future of Work 2026 | Silicon Valley Girl | EN | Popüler kariyer içeriği |
| 12 | https://www.youtube.com/watch?v=_1nRNbSJjuA — Yapay zeka bazı meslekleri tabii ki yok edecek | Oksijen TV (Ayşegül İldeniz) | TR | Türk teknoloji yöneticisi |
| 13 | https://www.youtube.com/watch?v=0Wpna736wwE — Yapay Zeka Çağında Hayatta Kalma Rehberi | Barış Özcan | TR | Türkçe popüler anlatım |
| 14 | https://www.youtube.com/watch?v=NjnHbYtTZDc — Yapay Zekada Durum Ne? | Evrim Ağacı | TR | Bilim iletişimi |
| 15 | https://www.youtube.com/watch?v=sYn5TeTj9jg — En Çok Etkilenen 40 Meslek | Görkem Sakınmaz | TR | Meslek listesi |
| 16 | https://www.youtube.com/watch?v=xkke6xy-S8s — İşsiz Kalıyoruz!!!! İşte Yok Olacak Meslekler | Cüneyt Özdemir | TR | Türk ana akım yorum |
