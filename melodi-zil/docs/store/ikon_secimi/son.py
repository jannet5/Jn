# Seçilen C ikonunun iyileştirilmiş hali (değerlendirici önerileri uygulandı) + Android vector XML + mağaza görselleri için SVG
V="#5B3DF5"; W="#FFFFFF"
EL=[ # (pathData, renk, opaklık, çizgi kalınlığı veya None)
 ("M26,50 L30.5,50 L30.5,58 L26,58 Z",W,1,None),          # dalga çubuğu 1 (kalınlaştırıldı)
 ("M32.5,44 L37,44 L37,64 L32.5,64 Z",W,1,None),          # dalga çubuğu 2
 ("M39,40 L43.5,40 L43.5,68 L39,68 Z",W,1,None),          # dalga çubuğu 3
 ("M45.5,46 L50,46 L50,62 L45.5,62 Z",W,0.7,None),        # dalga çubuğu 4 (sönük: dönüşüm)
 ("M62,33 L66,33 L66,64 A7.5,6.5 0 1 1 62,58.8 Z",W,1,None), # nota sapı + başı (boşluk açıldı, sola alındı)
 ("M66,33 C71.5,35 75.5,39.5 74.5,47 C72.5,43 69.5,41 66,41 Z",W,1,None), # nota bayrağı
 ("M77.5,52 A6,6 0 0 1 77.5,62","none",0.6,3),            # zil sesi halkası
]
def svg(size, clip=True, bg=True):
    ps="".join((f'<path d="{d}" fill="none" stroke="{W}" stroke-width="{sw}" stroke-linecap="round" opacity="{o}"/>' if sw else f'<path d="{d}" fill="{c}" opacity="{o}"/>') for d,c,o,sw in EL)
    clipdef=f'<defs><clipPath id="c{size}"><circle cx="54" cy="54" r="36"/></clipPath></defs>' if clip else ""
    g=f'<g clip-path="url(#c{size})">' if clip else "<g>"
    rect = '<rect width="108" height="108" fill="%s"/>' % V if bg else ""  # arka plan
    return f'<svg width="{size}" height="{size}" viewBox="18 18 72 72" xmlns="http://www.w3.org/2000/svg">{clipdef}{g}{rect}{ps}</g></svg>'
def vector_xml():
    items=[]
    for d,c,o,sw in EL:
        if sw: items.append(f'    <path android:pathData="{d}" android:strokeColor="#FFFFFF" android:strokeWidth="{sw}" android:strokeLineCap="round" android:strokeAlpha="{o}" />')
        else: items.append(f'    <path android:pathData="{d}" android:fillColor="#FFFFFF" android:fillAlpha="{o}" />')
    return '<?xml version="1.0" encoding="utf-8"?>\n<!-- Uygulama ikonu ön planı: "ses dalgası → nota" (6 varyasyon arasından bağımsız değerlendirici seçti, bkz. fabrika/IKON_SECIMI.md). 108dp tuval, güvenli alan 66dp. -->\n<vector xmlns:android="http://schemas.android.com/apk/res/android"\n    android:width="108dp" android:height="108dp" android:viewportWidth="108" android:viewportHeight="108">\n'+"\n".join(items)+"\n</vector>\n"
if __name__=="__main__":
    open("ic_launcher_foreground.xml","w").write(vector_xml())
    open("onizleme.html","w").write(f'<html><body style="margin:0;background:#F4F3F9;display:flex;gap:24px;align-items:center;padding:20px">{svg(220)}{svg(96)}{svg(48)}<div style="background:#121218;padding:12px;border-radius:12px">{svg(48)}</div></body></html>')
    open("ikon512.html","w").write(f'<html><body style="margin:0">{svg(512,clip=False)}</body></html>')
    fg=f'<html><body style="margin:0;width:1024px;height:500px;background:{V};display:flex;align-items:center;font-family:Roboto,sans-serif"><div style="margin-left:60px">{svg(380,clip=False)}</div><div style="color:#fff;margin-left:30px"><div style="font-size:76px;font-weight:700">Melodi Zil</div><div style="font-size:36px;color:#ECE8FF;margin-top:12px">Şarkıyı melodiye çevir,<br>zil sesi yap.</div></div></body></html>'
    open("onecikan.html","w").write(fg)
