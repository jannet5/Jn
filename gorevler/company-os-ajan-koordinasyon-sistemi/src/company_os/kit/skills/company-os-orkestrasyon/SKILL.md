---
name: company-os-orkestrasyon
description: Büyük bir işi uzman alt ajanlara bölme, Company OS MCP defteriyle lease/fencing token altında dağıtma, kanıtla kabul etme ve sonuçları toplama yöntemi. Çok adımlı, birden fazla uzmanlık isteyen görevlerde kullan.
---
# Company OS ile orkestrasyon

Ana oturum (sen) **planlayıcı ve toplayıcıdır**; işi alt ajanlar yapar, defter doğruluğu garanti eder.

## 1. Planla
1. Hedefi 3–8 bağımsız/az bağımlı göreve böl. Her göreve `role` ver:
   `arastirmaci`, `uygulayici`, `inceleyici`, `qa` (alt ajan adlarıyla eşleşir).
2. Her göreve **çalıştırılabilir kabul komutu** yaz (`acceptance: [{argv, refs}]`).
   Kanıtı olmayan görev tamamlanamaz.
3. Aynı dosyayı paralel düzenleyecek görevler oluşturma; birini diğerine bağımlı yap.
4. `mcp__company-os__plan_submit` ile planı yükle.

## 2. Dağıt
- Bağımsız görevler için uygun alt ajanları **aynı mesajda paralel** başlat
  (`cos-arastirmaci`, `cos-uygulayici`, `cos-inceleyici`, `cos-qa`).
- Alt ajana yalnız rolünü ve "task_claim ile görevini al" talimatını ver; görev
  ayrıntısı, skill'ler, bağımlılık çıktıları ve mesajlar `task_claim` yanıtında gelir.

## 3. İzle ve kurtar
- `mcp__company-os__status` ile durumu izle. Takılan alt ajan olursa
  `mcp__company-os__reconcile` süresi dolmuş lease'i geri alır; görevi yeni bir
  alt ajana ver. Eski ajanın geç yazmaları fencing token ile reddedilir.
- `blocked` görev: nedenini oku, planı düzelt, gerekirse `company-os retry`.

## 4. Topla
- `mcp__company-os__report` ile her görevin artifact hash'lerini ve kanıtlarını al.
- `mcp__company-os__artifact_read` ile yalnız `succeeded` görev çıktılarını oku ve
  kullanıcıya tek, kaynaklı sonuç sun. Başarısız/engellenen görevleri açıkça yaz.

## Kurallar
- Kanıt yoksa "bitti" deme. Kabul kapısının reddettiği nedenleri olduğu gibi aktar.
- Lease token'ı hiçbir zaman başka bir görevde kullanma; her alt ajan kendi token'ını taşır.
