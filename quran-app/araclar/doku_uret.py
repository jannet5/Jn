# Kağıt dokusu üreten betik: kenarları dikişsiz bağlanan, lif ve tane içeren yarı saydam (siyah + alfa) PNG
import numpy as np  # sayısal işlemler için
from PIL import Image, ImageDraw, ImageFilter  # görüntü çizmek ve bulanıklaştırmak için
import sys  # çıktı yolu için

N = 512  # karo boyutu (piksel)
rng = np.random.default_rng(7)  # her seferinde aynı doku çıksın diye sabit tohum

def periyodik_gurultu(olcek):  # FFT ile kenarları dikişsiz (periyodik) gürültü üretir
    f = np.fft.fftfreq(N)  # frekans ekseni
    fx, fy = np.meshgrid(f, f)  # 2B frekans ızgarası
    r = np.sqrt(fx**2 + fy**2) + 1e-6  # frekans büyüklüğü
    spektrum = np.exp(-(r * olcek) ** 2)  # alçak geçiren filtre: olcek büyüdükçe daha geniş lekeler
    faz = np.exp(2j * np.pi * rng.random((N, N)))  # rastgele faz
    g = np.real(np.fft.ifft2(spektrum * faz))  # uzaya geri dönüş
    return (g - g.mean()) / (g.std() + 1e-9)  # sıfır ortalama, birim sapma

leke = periyodik_gurultu(60)  # geniş, yumuşak lekelenme (kağıdın bulutlu görünümü)
orta = periyodik_gurultu(14)  # orta ölçek dalgalanma
tane = rng.standard_normal((N, N))  # ince tane (kağıt pürüzü)
L = 0.958 + 0.014 * leke + 0.008 * orta + 0.018 * tane  # açıklık değeri (1 = beyaz, çarpma ile renk değişmez)

lif = Image.new("L", (N, N), 0)  # lifler için boş katman
d = ImageDraw.Draw(lif)  # çizim aracı
for _ in range(900):  # 900 kısa lif
    x, y = rng.random() * N, rng.random() * N  # başlangıç noktası
    a = rng.random() * np.pi  # yön
    u = 6 + rng.random() * 26  # uzunluk
    renk = int(25 + rng.random() * 55)  # koyuluk
    for dx in (-N, 0, N):  # dikişsiz olsun diye kenar komşularına da çiziliyor
        for dy in (-N, 0, N):
            x0, y0 = x + dx, y + dy  # kaydırılmış başlangıç
            pts = [(x0 + np.cos(a) * t + np.sin(t * 0.3) * 1.5, y0 + np.sin(a) * t) for t in np.linspace(0, u, 8)]  # hafif kıvrık çizgi
            d.line(pts, fill=renk, width=1)  # lif çiziliyor
lif = np.asarray(lif.filter(ImageFilter.GaussianBlur(0.6)), dtype=np.float32) / 255.0  # yumuşatılıp 0-1'e çevriliyor
L = L - 0.10 * lif  # lifler kağıdı hafif koyulaştırıyor
L = np.clip(L, 0.80, 1.0)  # aşırı koyu nokta kalmasın
alfa = np.clip((1.0 - L) * 1.15, 0, 1)  # koyuluk → saydamlık: kağıt rengini her Android sürümünde aynı şekilde koyulaştırır (karışım modu gerekmez)
rgba = np.zeros((N, N, 4), dtype=np.uint8)  # siyah renk + saydamlık katmanı
rgba[..., 3] = (alfa * 255).astype(np.uint8)  # sadece alfa kanalı dolu
Image.fromarray(rgba, "RGBA").save(sys.argv[1], optimize=True)  # PNG olarak kaydediliyor
print("ortalama", L.mean())  # kontrol için ortalama açıklık
