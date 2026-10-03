# Sohbet kullanım ölçeri

Claude Code ile yapılan her çalışmanın sonunda iki ayrı şeyi gösterir:

1. **O sohbetin kullandığı token miktarı.** Claude Code'un kendi yazdığı
   transkriptten ölçülür, konuşma konuşma ayrılır.
2. **Hesabın kalan kotası.** 5 saatlik ve haftalık pencerede kullanılan ve
   kalan yüzde, bir de yenilenme zamanı. Bu değer hesap geneli bir sayıdır.

İkisi bilerek birbirine karıştırılmaz. Hesap genelindeki yüzde değişimi
asla "bu sohbet şu kadar harcadı" diye yazılmaz. Ölçülemeyen bir değer
için rapor "erişilemiyor" der. Tahmin yapılan yerde başına
**TAHMİN (ölçüm değil)** yazılır.

Ayrıca OpenAI **Codex CLI** için ayrı ve salt okunur bir adaptör içerir
(aşağıdaki "Codex" bölümüne bakın). Codex ve Claude kotaları birbirine
karışmaz.

Yalnızca Python 3.9+ standart kütüphanesini kullanır. Linux, macOS ve
Windows'ta çalışır. Windows'ta `tzdata` paketi gerekmez. Çıktı her zaman
UTF-8'dir. Bu önemli, çünkü Windows'ta yönlendirilmiş çıktı (kanca, boru,
dosya) normalde cp1252 olur ve Türkçe karakterde çökerdi.

## Hızlı başlangıç

```text
python olc.py oturumlar                 # transkriptteki konuşmalar ve token toplamları
python olc.py rapor                     # en son konuşmanın raporu
python olc.py rapor --oturum 3f2a       # belirli bir konuşma (kimlik ya da önek)
python olc.py rapor --json              # makinenin okuyacağı çıktı
python olc.py gunluk --oturum 3f2a      # kronolojik işlem günlüğü
```

Windows'ta `python` yerine `py` yazılabilir. Transkriptler varsayılan
olarak `~/.claude/projects` altından okunur; Windows'ta bu
`%USERPROFILE%\.claude\projects` demektir.

## Her çalışmanın sonunda otomatik rapor

Claude Code ayar dosyası (`~/.claude/settings.json`, Windows'ta
`%USERPROFILE%\.claude\settings.json`) içine şunlar eklenir:

```json
{
  "statusLine": {
    "type": "command",
    "command": "python C:\\araclar\\sohbet-kullanim-olceri\\olc.py kaydet"
  },
  "hooks": {
    "Stop": [
      { "hooks": [ { "type": "command",
                     "command": "python C:\\araclar\\sohbet-kullanim-olceri\\olc.py kanca" } ] }
    ]
  }
}
```

* `kaydet`: Claude Code'un statusline'a verdiği `rate_limits` bilgisini
  (resmî alan; Pro/Max aboneliklerinde ve oturumun ilk yanıtından sonra
  gelir) kilitli bir JSONL dosyasına ekler. Alt satırda da
  `kota 5s %23 · 7g %41` biçiminde kısa bir durum gösterir. Kullanılan başka
  bir statusline varsa onun yerine geçer. Windows'ta kayıt dosyasının
  yanında `kota.jsonl.kilit` adlı küçük bir kilit dosyası oluşur. Silinmesi
  güvenlidir.
* `kanca`: Her yanıt bittiğinde o konuşmanın raporunu
  `~/.claude/sohbet-olcer/raporlar/<oturum>.txt` dosyasına yazar. Kısa bir
  özeti de Claude Code ekranında bildirim olarak gösterir.

## Kota kaynakları

| Kaynak | Ne verir | Nasıl verilir |
|---|---|---|
| Claude Code statusline `rate_limits` | 5 saatlik ve haftalık kullanılan yüzde (0-100), yenilenme zamanı | `kaydet` komutu kendiliğinden toplar |
| `rate_limit_event` (Agent SDK, `claude -p --output-format stream-json --verbose`, bulut oturumu olay dökümü) | `unifiedWindows` içinde 0-1 kesir olarak kullanım ve yenilenme zamanı | `rapor --kota dosya.jsonl` |
| claude.ai **Ayarlar → Kullanım** ekranı | Ekranda görünen yüzde ve yenilenme zamanı | `elle --pencere seven_day --yuzde 41 --yenilenme 2026-01-05T09:00+03:00` |
| OpenTokenUsage yerel API'si (Windows tepsi uygulaması, kuruluysa) | `GET http://127.0.0.1:6736/v1/usage/claude` yanıtındaki Session/Weekly satırları | yanıt JSON'u kaydedilir, sonra `rapor --kota otu.json` |

**Yuvarlama.** Değerler yuvarlanmış gelir. Örneğin `rate_limit_event`
kesri iki ondalık taşır (örneğin 0,23), bu da 1 puanlık çözünürlük demektir.
Araç, gelen değerlerdeki ondalık basamak sayısına bakarak çözünürlüğü
kendisi bulur. İki ölçüm arasındaki fark `d`, çözünürlük `r` ise gerçek
fark `[d-r, d+r]` aralığında yazılır. Bu yüzden 0,6 puanlık bir iş "0 puan
değişti, gerçek değer 0 ile 1 arasında olabilir" diye raporlanır.

**Eşzamanlı oturumlar.** Her oturum, kendi son API yanıtına gelen hesap
durumunu taşır. Bu yüzden aynı anda açık başka bir oturum daha eski, daha
düşük bir değer gösterebilir (anthropics/claude-code#75408). Bir yenilenme
dönemi içinde kota azalmaz. Araç bu nedenle dönem içindeki en yüksek değeri
esas alır ve daha düşük değerleri "eskimiş anlık görüntü" diye not eder.
Ölçüm aralığında bu bilgisayarda başka oturumlar da istek gönderdiyse
bunları adlarıyla listeler.

**Pencere yenilenmesi.** İki ölçüm arasında `resets_at` değiştiyse fark
hesaplanmaz, rapor bunu açıkça söyler. Yenilenme zamanı geçmiş bir değer
"bayat" diye işaretlenir.

## Token sayımının doğruluğu

* Claude Code tek bir API yanıtını, içerik bloğu başına ayrı bir satır olarak
  yazar ve her satıra aynı `usage` bilgisini koyar (anthropics/claude-code#6805,
  #41346, #87303). Satırları düz toplamak bu yüzden fazla sayar. Araç
  istekleri `message.id` ile tekilleştirir.
* Devam ettirilen bir oturum, eski mesajları yeni dosyaya kopyalayabilir.
  Böyle bir istek yalnızca bir kez sayılır.
* Aynı oturum kimliğiyle yazılmış ama farklı proje dizinlerinde duran
  transkriptler ayrı konuşma sayılır (`<kimlik>@<dizin>`). Aynı dosyada aynı
  kimlikle iç içe yazılmış bağımsız konuşmalar `parentUuid` zincirinin
  köküne göre ayrılır (`<kimlik>#1`, `#2`). Sıkıştırma (compact) sınırı
  `logicalParentUuid` ile önceki zincire bağlı olduğundan konuşmayı bölmez.
* Alt ajan transkriptleri (`<oturum>/subagents/*.jsonl`) ana konuşmaya
  eklenir ve raporda ayrıca belirtilir.
* O anda yazılmakta olan yarım son satır ve bozuk satırlar atlanır,
  sayıları raporda yazılır.
* `uzlastir` komutu, `claude -p --output-format stream-json --verbose`
  çıktısındaki API `result.usage` toplamını transkriptten hesaplanan toplamla
  mesaj kimliği üzerinden karşılaştırır.

## Ölçülemeyenler

* **claude.ai web, masaüstü ve mobil sohbetlerinin token sayısı.** Resmî
  bir sayaç ya da API yok. Bu araç o sohbetleri ölçmez.
* **Tek bir sohbetin kota yüzdesindeki gerçek payı.** Anthropic oturum
  başına pay vermiyor (anthropics/claude-code#29721). Rapor yalnızca
  hesap genelindeki değişimi gösterir. İsteğe bağlı olarak, bu
  bilgisayardaki oturumlar arasında token oranına göre bir TAHMİN de verir.
* **Token türlerinin kotadaki ağırlığı** (örneğin önbellekten okuma)
  yayımlanmıyor. Token toplamı bu yüzden yüzdeye çevrilmez.

## Alternatiflerle karşılaştırma

| Araç | Hesap kotası | Konuşma başına token | Not |
|---|---|---|---|
| Claude Code `/usage` | Var (resmî, canlı) | Yok | Ekranda görülür, dosyaya yazılmaz |
| Claude Code statusline `rate_limits` | Var (resmî) | Yok | Oturumun son yanıtına göre; bu araç kaydeder |
| Claude Code `/cost`, statusline `cost` | Yok | Oturum maliyeti (liste fiyatıyla tahmini USD) | Abonelik kotasıyla aynı şey değil |
| ccusage | Yok | Günlük ve oturum toplamları | Bilinen tekilleştirme ve alt ajan eksikleri var |
| OpenTokenUsage (Windows) | Var | Yok (ccusage'ın günlük toplamı) | Kotayı Claude Code'un OAuth kimlik bilgisini okuyup belgelenmemiş `/api/oauth/usage` uç noktasından alır; bu araç o yolu kullanmaz, yalnızca yerel API çıktısını okuyabilir |

## Codex

```text
python olc.py codex --liste                 # Codex konuşmaları ve token toplamları
python olc.py codex                         # en son Codex konuşmasının raporu
python olc.py codex --konusma 019a2b        # belirli bir konuşma (thread kimliği ya da öneki)
python olc.py codex --kok D:\yedek\sessions # farklı bir rollout dizini
```

Ne okur: Codex CLI'nin kendi yazdığı oturum kayıtlarını, yani
`%USERPROFILE%\.codex\sessions\YYYY\MM\DD\rollout-*.jsonl` dosyalarını
(`CODEX_HOME` tanımlıysa onun altındakileri). `auth.json` ve benzeri
kimlik bilgisi dosyalarını okumaz. Hiçbir dosyaya yazmaz; bu bir testle
denetlenir. Biçim, openai/codex deposundaki
`codex-rs/protocol/src/protocol.rs` dosyasından alındı.

Ne gösterir:

* **Konuşma başına token.** Değer `token_count` olaylarındaki birikimli
  `total_token_usage` sayacının artışlarından hesaplanır. Girdi, önbellekten
  gelen girdi, çıktı ve akıl yürütme tokenleri ayrı yazılır.
* **Hesap kotası.** Kayıttaki `rate_limits.primary` ve `secondary`
  pencereleri gösterilir; pencere süresi `window_minutes` alanından okunur
  (300 dakika 5 saatlik, 10080 dakika haftalık demektir). Her pencere için
  kullanılan ve kalan yüzde, yenilenme zamanı ve **kaynağın tarihi**, yani o
  kotanın okunduğu kaydın zaman damgası yazılır. Kayıt eskiyse ya da
  yenilenme zamanı geçtiyse rapor bunu açıkça söyler. Eski Codex
  sürümlerindeki `resets_in_seconds` alanı da desteklenir.
* **Doğruluk kuralları.** Birebir tekrar eden olaylar, çatallanmış (fork)
  konuşmaya kopyalanan eski olaylar ve sayaç değişmeden yazılan olaylar
  yeniden sayılmaz. Sayaç geriye düşerse yalnızca o turun değeri eklenir ve
  raporda belirtilir. Aynı anda açık başka bir oturumun eskimiş, daha düşük
  kota değeri elenir.

Doğrulama durumu: parser, şemaya uygun **sentetik** örnek dosyalarla test
edildi (`testler/ornekler/codex_uret.py`). Gerçek bir Codex hesabının
rollout kayıtlarıyla ve gerçek bir Windows makinesinde **denenmedi**.

## Testler

```text
python -m unittest discover -s testler -v
```

Testler şunları kapsar: tekilleştirme, konuşmaların ayrılması (iç içe
geçmiş, aynı kimlikli, alt ajanlı), yarım satırlar, yuvarlama aralıkları,
pencere yenilenmesi, kota verisinin hiç olmaması, eskimiş eşzamanlı anlık
görüntüler, 8 sürecin aynı anda kayıt yazması, Windows saat dilimi yedeği,
komut satırının uçtan uca çalışması ve Codex adaptörü.

Test paketinin tamamı, Wine 9.0 altında gerçek Windows CPython 3.12.7
(python.org gömülü dağıtımı) ile de çalıştırıldı. Gerçek transkript
gerektiren test o ortamda transkript olmadığı için atlandı; geri kalanların
hepsi geçti. Bu çalıştırma iki gerçek Windows hatasını ortaya çıkardı, ikisi
de düzeltildi: cp1252 çıktı çökmesi ve msvcrt kilidinin açılırken hata
vermesi. Gerçek bir Windows makinesinde ise henüz çalıştırılmadı.
