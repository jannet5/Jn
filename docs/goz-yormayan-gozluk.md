# 👓 AURA — Göz Yormayan Akıllı Gözlük (İcat Konsepti)

## 1. Göz neden yoruluyor? (Bilimsel temel)

| Sorun | Ne oluyor | Etkisi |
|---|---|---|
| **Odak kası (akomodasyon)** | Ekran 50 cm → göz sürekli ~2 diyoptri odaklanır, siliyer kas kasılı kalır | Ağırlık, bulanıklık, baş ağrısı |
| **Göz kırpma düşüşü** | Normal 15–20/dk → ekranda 5–7/dk | Kuruluk, yanma, kızarıklık |
| **Yakınsama (vergence)** | İki göz sürekli içe döner | Göz arkası ağrı, çift görme hissi |
| **Parlaklık uyumsuzluğu** | Ekran parlak, oda karanlık (veya tersi) | Kamaşma, kısma |
| **Yansıma / parlama** | Lamba, pencere ekrana/cama yansır | Kontrast kaybı, kasılma |
| **Titreşim (PWM flicker)** | Ucuz ekran/LED saniyede yüzlerce kez yanıp söner | Hassas kişilerde baş ağrısı |
| **Mavi ışık** | Göz yorgunluğuna etkisi **zayıf**, ama gece **uyku hormonunu** baskılar | Uykusuzluk → ertesi gün yorgun göz |
| **Kötü duruş / mesafe** | Ekrana fazla yakın, uzun süre ara vermeden | Hepsini katlar |

➡️ **Sonuç:** Tek bir filtre çözmez. Asıl düşman **odak kası + kuruluk**. Gözlük hepsine ayrı katmanla saldırmalı.

## 2. Buluş: 7 katmanlı sistem

| # | Katman | Nasıl çalışır | Mevcut teknoloji? |
|---|---|---|---|
| 1 | **Adaptif odak camı** ⭐ | Sıvı kristal cam + ToF mesafe sensörü. Ekran 50 cm ise cama otomatik +1.5 D ekler → göz ekranı "uzaktaymış" gibi görür, **odak kası gevşer** | Var (Morrow, LC lensler) — mesafeye bağlı otomatik kullanım yeni |
| 2 | **Mikro-dinlenme modu** | Her 20 dk'da 20 sn odağı yavaşça kaydırır → kas "uzağa bakmış" gibi esner (20-20-20 kuralını otomatikleştirir) | **Yeni fikir** |
| 3 | **Kırpma sensörü + nem odası** | Kızılötesi sensör kırpmayı sayar; düşerse çerçevede hafif titreşim. Yan ince kalkanlar gözün etrafında nemi tutar | Parçalar var, birleşim yeni |
| 4 | **Akıllı karartma** | Elektrokromik cam: odadaki ışık ↔ ekran parlaklığını ölçer, aradaki farkı kapatacak kadar kararır | Var (otomobil aynası teknolojisi) |
| 5 | **Taban-içe ayarlı prizma** | Aynı LC cam küçük prizma etkisi verir → gözlerin içe dönme yükü azalır | LC prizma araştırma aşamasında |
| 6 | **Gündüz/Gece spektrum** | Gündüz şeffaf (renk bozmaz), akşam 415–455 nm mavi bandı kademeli keser | Var (notch filtre) |
| 7 | **Titreşim + duruş dedektörü** | Fotodiyot ekran PWM'ini algılar, uyarır; ivme sensörü + mesafe ile "çok yakınsın / boynun eğik" der | Parçalar var |

**Kaplamalar:** Çift yüzlü yansıma önleyici (AR) + leke tutmaz. **Polarize YOK** (LCD ekranları karartır).

## 3. Donanım (Prototip)

| Parça | Örnek | ~Fiyat |
|---|---|---|
| Mesafe sensörü | VL53L1X ToF | 5 $ |
| Kırpma sensörü | IR LED + fototransistör (×2) | 2 $ |
| Işık sensörü | TSL2591 / VEML7700 | 4 $ |
| Flicker fotodiyot | BPW34 + hızlı ADC | 3 $ |
| İşlemci + BLE | nRF52840 / ESP32-C3 | 6 $ |
| Odak camı | LC shutter/lens (ilk prototipte: 2 sabit cam arası mekanik geçiş) | 20–80 $ |
| Karartma | Elektrokromik film | 15 $ |
| Pil | 150 mAh LiPo (~1 gün) | 4 $ |
| **Toplam prototip** | | **~60–120 $** |

## 4. Yazılım mantığı (kısa)

```
her 100 ms:
  mesafe = ToF()            → cam_gücü = 1/mesafe − 0.5 D (göz ~0.5 D çalışsın)
  ışık_farkı = ekran − oda  → karartma seviyesi ayarla
  kırpma_hızı < 8/dk        → hafif titreşim
  süre % 20 dk == 0         → 20 sn odak kaydırma (mikro-dinlenme)
  saat > gün batımı         → mavi filtre kademeli aç
  PWM tespit                → telefona bildirim
```

## 5. Yol haritası

| Aşama | Ne yapılır | Süre |
|---|---|---|
| **V0** | Sensörlü çerçeve + telefon uygulaması (sadece ölçer, uyarır) | 2–4 hafta |
| **V1** | + elektrokromik karartma + 2 kademeli odak (mekanik) | 2–3 ay |
| **V2** | Tam LC adaptif odak + mikro-dinlenme | 6–12 ay |
| **V3** | Göz doktoru ile klinik test (kırpma, kuruluk, yorgunluk anketi) | 6 ay |

## 6. Dürüst sınırlar

- ❗ **"Hiç yormayan" %100 mümkün değil**; hedef yorgunluğu **büyük ölçüde** azaltmak.
- Odak camı kişinin **numarasına** göre kalibre edilmeli (göz muayenesi şart).
- Mavi ışık filtresi tek başına göz yorgunluğunu çözmez — pazarlama abartısı.
- Kuru göz hastalığı varsa gözlük + doktor tedavisi birlikte gerekir.

## 7. Farkı ne? (Patent fikri)

> **Ekran mesafesine göre otomatik odak ekleyen + periyodik "odak esnetme" yapan + kırpmayı izleyen** tek gözlük. Piyasadaki ürünler (mavi filtre, sabit "bilgisayar camı") bunlardan sadece birini yapıyor.
