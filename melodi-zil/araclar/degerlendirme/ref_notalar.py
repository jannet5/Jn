# Vocadito'nun insan işaretli nota anotasyonlarını (notes_a1) <ad>.refnotes.csv olarak yazar: başlangıç, bitiş, Hz
import sys, os, csv, mirdata  # gerekli
data, *dirs = sys.argv[1:]
voc = mirdata.initialize("vocadito", data_home=os.path.join(data, "vocadito"))
for tid, tr in voc.load_tracks().items():
    if tr.notes_a1 is None: continue
    rows = [(round(iv[0], 4), round(iv[1], 4), round(p, 2)) for iv, p in zip(tr.notes_a1.intervals, tr.notes_a1.pitches)]
    for d in dirs:
        with open(os.path.join(d, f"voc_{tid}.refnotes.csv"), "w", newline="") as f: csv.writer(f).writerows(rows)
print("yazıldı")
