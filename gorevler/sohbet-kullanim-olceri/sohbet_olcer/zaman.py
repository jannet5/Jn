"""Zaman ayrıştırma ve biçimlendirme yardımcıları."""

from datetime import datetime, timedelta, timezone

try:
    from zoneinfo import ZoneInfo, ZoneInfoNotFoundError
except ImportError:  # pragma: no cover
    ZoneInfo, ZoneInfoNotFoundError = None, Exception

# Windows'ta Python saat dilimi veritabanı (tzdata paketi) olmayabilir.
# Türkiye 2016'dan beri yaz saati uygulamadığı için sabit +03:00 doğrudur.
SABIT_DILIMLER = {"Europe/Istanbul": timezone(timedelta(hours=3), "TRT"), "UTC": timezone.utc}

AYLAR = ["Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz",
         "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"]


def iso_ayristir(metin):
    """ISO 8601 metnini UTC-farkında datetime'a çevirir; olmazsa None."""
    if not metin or not isinstance(metin, str):
        return None
    m = metin.strip()
    if m.endswith("Z"):
        m = m[:-1] + "+00:00"
    try:
        dt = datetime.fromisoformat(m)
    except ValueError:
        return None
    if dt.tzinfo is None:
        dt = dt.replace(tzinfo=timezone.utc)
    return dt.astimezone(timezone.utc)


def epoch_ayristir(deger):
    """Unix zamanını (saniye ya da milisaniye) datetime'a çevirir."""
    if deger is None or isinstance(deger, bool):
        return None
    try:
        sayi = float(deger)
    except (TypeError, ValueError):
        return iso_ayristir(deger) if isinstance(deger, str) else None
    if sayi > 1e12:  # milisaniye
        sayi /= 1000.0
    return datetime.fromtimestamp(sayi, tz=timezone.utc)


def dilim(tz):
    try:
        return ZoneInfo(tz)
    except (ZoneInfoNotFoundError, ValueError, TypeError):
        if tz in SABIT_DILIMLER:
            return SABIT_DILIMLER[tz]
        return datetime.now().astimezone().tzinfo  # sistemin yerel saati


def yerel(dt, tz):
    return dt.astimezone(dilim(tz))


def tarih_saat(dt, tz):
    """'3 Ekim 2026 19:30' biçimi."""
    y = yerel(dt, tz)
    return f"{y.day} {AYLAR[y.month - 1]} {y.year} {y:%H:%M}"


def saat(dt, tz):
    return f"{yerel(dt, tz):%H:%M:%S}"


def sure_metni(saniye):
    saniye = int(round(saniye))
    if saniye < 60:
        return f"{saniye} saniye"
    dakika, _ = divmod(saniye, 60)
    saat_, dakika = divmod(dakika, 60)
    gun, saat_ = divmod(saat_, 24)
    parcalar = []
    if gun:
        parcalar.append(f"{gun} gün")
    if saat_:
        parcalar.append(f"{saat_} saat")
    if dakika:
        parcalar.append(f"{dakika} dakika")
    return " ".join(parcalar) or "1 dakikadan az"
