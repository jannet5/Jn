"""AURA gözlük kontrol mantığı (simülasyon). Birimler: metre, diyoptri (D), saniye."""
from dataclasses import dataclass

EYE_COMFORT_D = 0.5      # gözün rahat çalışacağı kalan odak yükü
MAX_ADD_D = 2.5          # camın ekleyebileceği en fazla güç
BLINK_MIN_PER_MIN = 8    # altı = uyarı
REST_EVERY_S = 20 * 60   # 20-20-20
REST_LEN_S = 20
TOO_CLOSE_M = 0.35

@dataclass
class Sensors:
    t: float                 # sn
    distance_m: float        # ekrana mesafe (ToF)
    room_lux: float
    screen_lux: float
    blinks_last_min: int
    hour: float              # 0-24
    pwm_hz: float = 0.0      # 0 = titreşim yok

@dataclass
class Actions:
    lens_add_d: float
    tint: float              # 0 (şeffaf) .. 1 (koyu)
    blue_cut: float          # 0..1
    vibrate: bool
    resting: bool
    warnings: list

def lens_power(distance_m: float) -> float:
    """Ekran mesafesindeki odak talebi (1/d) eksi rahat pay; 0..MAX arası."""
    if distance_m <= 0:
        raise ValueError("mesafe pozitif olmalı")
    return max(0.0, min(MAX_ADD_D, 1.0 / distance_m - EYE_COMFORT_D))

def tint_level(room_lux: float, screen_lux: float) -> float:
    """Ekran odadan çok parlaksa kararır. Oran 1:3'e kadar sorun yok."""
    ratio = (screen_lux + 1) / (room_lux + 1)
    if ratio <= 3:
        return 0.0
    return min(0.6, (ratio - 3) / 20)

def blue_cut(hour: float) -> float:
    """19:00'dan sonra kademeli artar, 22:00'de tam, 06:00'da biter."""
    if 6 <= hour < 19:
        return 0.0
    if 19 <= hour < 22:
        return (hour - 19) / 3
    return 1.0

def step(s: Sensors) -> Actions:
    warnings = []
    phase = s.t % REST_EVERY_S
    resting = phase >= REST_EVERY_S - REST_LEN_S and s.t >= REST_EVERY_S - REST_LEN_S
    add = lens_power(s.distance_m)
    if resting:
        # kas uzağa bakmış gibi esnesin: gücü yumuşakça 0'a indir
        k = (phase - (REST_EVERY_S - REST_LEN_S)) / REST_LEN_S
        add *= abs(1 - 2 * k) if k <= 1 else 1  # 0 yönüne in, geri çık
    vibrate = s.blinks_last_min < BLINK_MIN_PER_MIN
    if vibrate:
        warnings.append("Az göz kırpıyorsun")
    if s.distance_m < TOO_CLOSE_M:
        warnings.append("Ekrana çok yakınsın")
    if 0 < s.pwm_hz < 1000:
        warnings.append(f"Ekran titriyor ({s.pwm_hz:.0f} Hz)")
    return Actions(add, tint_level(s.room_lux, s.screen_lux), blue_cut(s.hour),
                   vibrate, resting, warnings)
