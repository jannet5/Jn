#!/bin/bash
# iki geçişli loudnorm -16 LUFS / -1.5 dBTP, 48k 24-bit
in="$1"; out="$2"; pre="$3"; [ -n "$pre" ] && pre="$pre,"
j=$(ffmpeg -hide_banner -nostats -i "$in" -af "${pre}loudnorm=I=-16:TP=-1.5:LRA=11:print_format=json" -f null - 2>&1 | sed -n '/^{/,/^}/p')
g(){ echo "$j" | python3 -c "import json,sys;print(json.load(sys.stdin)['$1'])"; }
ffmpeg -loglevel error -y -i "$in" -af "${pre}loudnorm=I=-16:TP=-1.5:LRA=11:measured_I=$(g input_i):measured_TP=$(g input_tp):measured_LRA=$(g input_lra):measured_thresh=$(g input_thresh):offset=$(g target_offset):linear=true" -ar 48000 -c:a pcm_s24le "$out"
