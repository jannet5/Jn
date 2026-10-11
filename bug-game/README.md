# Böcek Ringi (çalışma adı)

Android için hikâyeli, hafızaya dayalı böcek kapıştırma oyunu.

## Oyun mantığı
1. **Hikâye girişi:** Maskot yeni bir mekana gelir, böcek kapıştıran rakiple konuşur (diyalog sahneleri).
2. **Bak:** Rakip 5 slota 5 böcek koyar, oyuncu birkaç saniye görür.
3. **Yaprak kapanır:** Böcekler yaprağın altında kalır.
4. **Karşı böcek:** Oyuncu her slota, hatırladığı böceği yenecek böceği koyar. Doğru eşleşme puan.
5. **İki oyuncu sırayla** oynar, yüksek puan kazanır.
6. Arka planda sis/duman içinde "HEY HEY!" diye tezahürat yapan seyirci böcekler.

## 20 böcek ve kim kimi yener (taslak)
Böcekler `concepts/concepts.json` sırasıyla bir çember gibi dizilir:
her böcek kendinden sonraki 9 böceği yener, önceki 9'una yenilir, tam karşısındaki ile berabere kalır.
Dengeli olur, ezberlenecek kadar mantıklı nedenler (ör. "Peygamber Devesi sinek avlar") sonra eklenecek.

## Konseptler
`concepts/` klasöründe 20 konsept kartı var (maskot, mekan, rakip, hikâye).
- `node concepts/generate.js` → SVG kartlar
- `NODE_PATH=$(npm root -g) node concepts/render.js` → PNG kartlar
- `node concepts/build-gallery.js` → `gallery.html`
