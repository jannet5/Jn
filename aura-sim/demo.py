from aura import *
print("Saat  Mesafe  Kırpma  -> Cam(D)  Karartma  Mavi  Uyarı")
for h, d, b, room, scr in [(10,.5,16,400,300),(14,.3,5,300,300),(21,.45,12,10,250),(23.5,.6,6,5,200)]:
    a = step(Sensors(t=100, distance_m=d, room_lux=room, screen_lux=scr, blinks_last_min=b, hour=h))
    print(f"{h:>4}  {d:>5}m  {b:>4}   -> {a.lens_add_d:>5.2f}   {a.tint:>6.2f}   {a.blue_cut:>4.2f}  {', '.join(a.warnings) or '-'}")
