"""Test/demo için ÖZGÜN, kısa eğitim metinlerinden PDF üretir (telifli içerik değildir).

Bunlar gerçek kullanıcı yüklemesi değil, kabul testinin girdisidir; içerik genel
ders kitabı bilgisinin kendi cümlelerimizle özetidir ve klinik kaynak yerine geçmez.
"""
import sys
from pathlib import Path

import pymupdf

FONT = "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"

TR_PAGES = [
    ("Akut Miyokard Enfarktüsü",
     "Akut miyokard enfarktüsü, koroner arterin aniden tıkanması sonucu kalp kasının beslenememesidir. "
     "Tipik belirti göğüste baskı tarzında, sol kola ve çeneye yayılan ağrıdır; terleme ve bulantı eşlik edebilir. "
     "Tanıda on iki derivasyonlu EKG ilk on dakika içinde çekilmeli ve troponin düzeyi ölçülmelidir. "
     "Başlangıç tedavisinde aspirin çiğnetilir, ST yükselmeli enfarktüste en kısa sürede reperfüzyon sağlanır: "
     "tercih edilen yöntem perkütan koroner girişimdir, uygun değilse trombolitik tedavi verilir. "
     "Oksijen yalnızca satürasyon düşükse eklenir."),
    ("Tip 2 Diyabet",
     "Tip 2 diyabet, insülin direnci ve göreceli insülin eksikliği ile seyreden kronik bir metabolik hastalıktır. "
     "Tanı ölçütleri arasında açlık plazma glukozunun 126 mg/dL ve üzerinde olması ile HbA1c değerinin yüzde 6,5 ve üzerinde olması yer alır. "
     "Çoğu erişkin için HbA1c hedefi yüzde 7'nin altıdır. İlk basamak ilaç genellikle metformindir; "
     "yaşam tarzı değişikliği, kilo kaybı ve düzenli egzersiz tedavinin temelidir."),
    ("Anafilaksi",
     "Anafilaksi, dakikalar içinde gelişen ve hayatı tehdit eden sistemik bir alerjik reaksiyondur. "
     "İlk ve en önemli tedavi uyluk dış yüzüne kas içi adrenalin uygulanmasıdır; erişkinde doz 0,5 mg'dır ve gerekirse beş dakika sonra tekrarlanır. "
     "Hasta sırtüstü yatırılır, bacakları kaldırılır, damar yolu açılarak sıvı verilir. Antihistaminik ve steroid ikinci basamaktır."),
]

EN_PAGES = [
    ("Iron Deficiency Anaemia",
     "Iron deficiency anaemia is the most common anaemia worldwide. Laboratory findings include low serum ferritin, "
     "microcytic hypochromic red cells and a raised red cell distribution width. Oral iron is first-line treatment; "
     "the underlying cause, such as gastrointestinal blood loss, must be investigated."),
]


def build(pages, path: Path, title: str):
    doc = pymupdf.open()
    for head, body in pages:
        page = doc.new_page()
        page.insert_font(fontname="dv", fontfile=FONT)
        page.insert_textbox(pymupdf.Rect(60, 60, 540, 110), head, fontname="dv", fontsize=18)
        page.insert_textbox(pymupdf.Rect(60, 120, 540, 780), body, fontname="dv", fontsize=11)
    doc.set_metadata({"title": title})
    doc.save(path)
    doc.close()


def main(out: str = "samples"):
    d = Path(out)
    d.mkdir(parents=True, exist_ok=True)
    build(TR_PAGES, d / "acil-dahiliye-ozet-tr.pdf", "Acil ve Dahiliye Özet Notları")
    build(EN_PAGES, d / "haematology-notes-en.pdf", "Haematology Notes")
    print("written to", d.resolve())


if __name__ == "__main__":
    main(*sys.argv[1:])
