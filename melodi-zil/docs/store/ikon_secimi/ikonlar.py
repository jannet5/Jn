# 6 özgün ikon konsepti: 108x108 adaptive ikon tuvali, güvenli alan merkez 54, yarıçap 33. Her biri SVG eleman listesi (Android vector'e çevrilebilir).
V="#5B3DF5"; W="#FFFFFF"
ICONS = {
 "A": ("Çan + nota (mevcut)", [
   ("path", dict(d="M54,26 C43,26 36,34 36,45 L36,58 L31,65 L77,65 L72,58 L72,45 C72,34 65,26 54,26 Z", fill=W)),
   ("path", dict(d="M48,69 A6,6 0 0 0 60,69 Z", fill=W)),
   ("path", dict(d="M51,22 L57,22 L57,28 L51,28 Z", fill=W)),
   ("path", dict(d="M58,38 L58,52.5 A4.5,4 0 1 1 55.5,49 L55.5,42 L50,43.5 L50,55.5 A4.5,4 0 1 1 47.5,52 L47.5,41 Z", fill=V))]),
 "B": ("Nota + çalan halkalar", [
   ("path", dict(d="M50,34 L53,34 L53,64 A7,6 0 1 1 50,59.2 Z", fill=W)),
   ("path", dict(d="M53,34 C58,36 62,40 61,47 C59,43 56,41 53,41 Z", fill=W)),
   ("path", dict(d="M64,46 A10,10 0 0 1 64,62", fill="none", stroke=W, sw=3.5)),
   ("path", dict(d="M69,41 A16,16 0 0 1 69,67", fill="none", stroke=W, sw=3.5)),
   ("path", dict(d="M38,46 A10,10 0 0 0 38,62", fill="none", stroke=W, sw=3.5, op=0.55))]),
 "C": ("Ses dalgası → nota", [
   ("path", dict(d="M30,50 L33,50 L33,58 L30,58 Z", fill=W)),
   ("path", dict(d="M36,44 L39,44 L39,64 L36,64 Z", fill=W)),
   ("path", dict(d="M42,40 L45,40 L45,68 L42,68 Z", fill=W)),
   ("path", dict(d="M48,46 L51,46 L51,62 L48,62 Z", fill=W, op=0.7)),
   ("path", dict(d="M65,34 L68,34 L68,64 A7,6 0 1 1 65,59.2 Z", fill=W)),
   ("path", dict(d="M68,34 C73,36 77,40 76,47 C74,43 71,41 68,41 Z", fill=W))]),
 "D": ("Piyano tuşlu çan", [
   ("path", dict(d="M54,26 C43,26 36,34 36,45 L36,58 L31,65 L77,65 L72,58 L72,45 C72,34 65,26 54,26 Z", fill=W)),
   ("path", dict(d="M48,69 A6,6 0 0 0 60,69 Z", fill=W)),
   ("path", dict(d="M44,40 L48,40 L48,56 L44,56 Z", fill=V)),
   ("path", dict(d="M52,40 L56,40 L56,56 L52,56 Z", fill=V)),
   ("path", dict(d="M60,40 L64,40 L64,56 L60,56 Z", fill=V))]),
 "E": ("Müzik kutusu", [
   ("path", dict(d="M34,52 L74,52 L74,72 Q74,76 70,76 L38,76 Q34,76 34,72 Z", fill=W)),
   ("path", dict(d="M34,47 L74,47 L74,50 L34,50 Z", fill=W, op=0.7)),
   ("path", dict(d="M40,58 L68,58 L68,61 L40,61 Z", fill=V, op=0.5)),
   ("path", dict(d="M53,26 L56,26 L56,40 A6,5 0 1 1 53,36 Z", fill=W)),
   ("path", dict(d="M56,26 C60,28 63,31 62,36 C60,33 58,32 56,32 Z", fill=W))]),
 "F": ("8-bit piksel nota", [
   ("path", dict(d="M54,30 h6 v6 h6 v6 h-6 v18 h-6 Z", fill=W)),
   ("path", dict(d="M42,60 h12 v12 h-12 Z M36,64 h6 v6 h-6 Z", fill=W)),
   ("path", dict(d="M70,50 h4 v4 h-4 Z M74,46 h4 v4 h-4 Z M74,58 h4 v4 h-4 Z M70,62 h4 v4 h-4 Z", fill=W, op=0.8))]),
}
def svg(key, size):
    name, els = ICONS[key]; parts=[]
    for _, a in els:
        st = f'fill="{a["fill"]}"' + (f' stroke="{a["stroke"]}" stroke-width="{a["sw"]}" stroke-linecap="round"' if "stroke" in a else "") + (f' opacity="{a["op"]}"' if "op" in a else "")
        parts.append(f'<path d="{a["d"]}" {st}/>')
    return f'<svg width="{size}" height="{size}" viewBox="18 18 72 72" xmlns="http://www.w3.org/2000/svg"><defs><clipPath id="c{key}{size}"><circle cx="54" cy="54" r="36"/></clipPath></defs><g clip-path="url(#c{key}{size})"><rect x="0" y="0" width="108" height="108" fill="{V}"/>{"".join(parts)}</g></svg>'
if __name__=="__main__":
    cells="".join(f'<div class="c"><div class="k">{k}</div>{svg(k,220)}<div class="row">{svg(k,48)}<span class="n">{ICONS[k][0]}</span></div></div>' for k in ICONS)
    html=f'<html><body style="margin:0;background:#F4F3F9;font-family:sans-serif"><div style="display:grid;grid-template-columns:repeat(3,300px);gap:16px;padding:20px">{cells}</div><style>.c{{background:#fff;border:1px solid #E3E1EC;border-radius:16px;padding:16px;text-align:center}}.k{{font-weight:700;font-size:22px;text-align:left}}.row{{display:flex;align-items:center;gap:12px;margin-top:12px;justify-content:center}}.n{{font-size:15px;color:#16151D}}</style></body></html>'
    open("varyasyonlar.html","w").write(html)
