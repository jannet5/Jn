"""Instagram kullanıcı adı avcısı.

AMAÇ: Instagram'da 3-7 harfli kullanıcı adlarını (her dilden kelimeden türetilmiş: Arapça, Türkçe, Rusça...)
sürekli kontrol etmek. Dolu bir isim boşalırsa (hesap silinir / isim değişir / yasak kalkar) kullanıcıya hemen
haber vermek ki ismi Instagram'dan kendisi alabilsin. Araç ismi kendisi ALMAZ, hesaba girmez, engel aşmaz.

ÇALIŞMAZSA: AMAC.md dosyasını oku. Tek komutla rapor: `python -m avci tani`. Ham cevap: `python -m avci probe isim`.
"""
__version__ = "1.1.0"
