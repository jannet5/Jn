# Checkpoint ve devam komutu

Durum (2026-10-03, ikinci tur): Bağımsız incelemenin istediği düzeltmeler yapıldı. Betik artık önbellek konumunu kanıta dayanarak belirliyor ve klasörü sınırlı tarıyor; 34 testin hepsi geçiyor. Shadow ve Windows 365 fiyatları canlı sayfalardan yeniden doğrulandı. Değişiklikler `claude/wizardly-bohr-coy8w8` dalına push edildi. Ürün klasöründen oluşan ZIP ve SHA-256 değeri özel scratchpad'de.

Açık kalan maddeler (yapılmış sayılmaz): Betiğin gerçek bir Windows bilgisayarda, kurulu Drive for desktop ile çalıştırılması (T5). Hesap açma, ödeme ve gecikme ölçümü de yapılmadı. Bu adımları kullanıcının kendisi yapmalı.

Oturum kesilirse yeni bir Claude Code oturumunda şunu yaz:

```
jannet5/Jn deposunda claude/wizardly-bohr-coy8w8 dalını aç. gorevler/internetten-ram-ve-bulut-bilgisayar-secenekleri/ klasöründeki harita.md ve calisma-gunlugu.md dosyalarını oku. Testleri çalıştır (pwsh -File araclar/testler/tani-test.ps1 ve araclar/kaynak-kontrol.sh). Fiyatlar 30 günden eskiyse resmi sayfalardan yeniden doğrula, sonra günlüğe ekle ve aynı dala push et. Başka klasörlere dokunma.
```
