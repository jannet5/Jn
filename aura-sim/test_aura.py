import pytest
from aura import *

def S(**k):
    d = dict(t=100, distance_m=0.5, room_lux=300, screen_lux=300, blinks_last_min=15, hour=14)
    d.update(k); return Sensors(**d)

def test_lens_50cm():           assert lens_power(0.5) == pytest.approx(1.5)
def test_lens_far_is_zero():    assert lens_power(3.0) == 0.0
def test_lens_capped():         assert lens_power(0.1) == MAX_ADD_D
def test_bad_distance():
    with pytest.raises(ValueError): lens_power(0)
def test_tint_balanced():       assert tint_level(300, 300) == 0
def test_tint_dark_room():      assert 0 < tint_level(5, 300) <= 0.6
def test_blue_day():            assert blue_cut(12) == 0
def test_blue_evening():        assert 0 < blue_cut(20) < 1
def test_blue_night():          assert blue_cut(23) == 1 and blue_cut(3) == 1
def test_blink_warning():
    a = step(S(blinks_last_min=4)); assert a.vibrate and "Az göz kırpıyorsun" in a.warnings
def test_blink_ok():            assert not step(S()).vibrate
def test_too_close():           assert "Ekrana çok yakınsın" in step(S(distance_m=0.3)).warnings
def test_pwm():                 assert any("titriyor" in w for w in step(S(pwm_hz=240)).warnings)
def test_no_pwm_no_warning():   assert not step(S(pwm_hz=0)).warnings
def test_rest_triggers():
    a = step(S(t=REST_EVERY_S - 10)); assert a.resting
    assert a.lens_add_d < lens_power(0.5) + 1e-9
def test_no_rest_early():       assert not step(S(t=60)).resting
def test_rest_mid_reaches_zero():
    assert step(S(t=REST_EVERY_S - 10)).lens_add_d == pytest.approx(0, abs=0.01)
def test_rest_ends():           assert not step(S(t=REST_EVERY_S + 5)).resting
def test_full_day_no_crash():
    for t in range(0, 8*3600, 7):
        step(S(t=t, distance_m=0.3 + (t % 50) / 100, hour=9 + t/3600))
