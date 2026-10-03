"""Sentetik Codex rollout örneklerini üretir (gerçek hesap verisi DEĞİLDİR).

Şema openai/codex codex-rs/protocol/src/protocol.rs (RolloutLine,
SessionMeta, TokenCountEvent, RateLimitSnapshot, RateLimitWindow) ile
uyumludur. Çalıştırma: python codex_uret.py  → ./codex/sessions/... yazar."""
import json
import os
from datetime import datetime

KOK = os.path.join(os.path.dirname(os.path.abspath(__file__)), "codex", "sessions")
R5 = 1767261600      # 2026-01-01T10:00:00Z
R7 = 1767600000      # 2026-01-05T08:00:00Z
R7_YENI = 1768204800  # 2026-01-12T08:00:00Z


def meta(kimlik, ts, **ek):
    p = {"id": kimlik, "session_id": kimlik, "timestamp": ts, "cwd": "C:\\\\proje",
         "originator": "codex_cli_rs", "cli_version": "0.99.0", "source": "cli"}
    p.update(ek)
    return {"timestamp": ts, "type": "session_meta", "payload": p}


def usage(girdi, onbellek, cikti, akil):
    return {"input_tokens": girdi, "cached_input_tokens": onbellek, "output_tokens": cikti,
            "reasoning_output_tokens": akil, "total_tokens": girdi + cikti}


def tc(ts, toplam=None, son=None, p5=None, p7=None, eski=False, r7=R7, plan="plus"):
    info = None if toplam is None else {"total_token_usage": usage(*toplam),
                                        "last_token_usage": usage(*son),
                                        "model_context_window": 272000}
    rl = None
    if p5 is not None:
        def w(u, dk, r):
            # eski sürüm: olay anından yenilenmeye kalan saniye
            kalan = r - int(datetime.fromisoformat(ts.replace("Z", "+00:00")).timestamp())
            return ({"used_percent": u, "window_minutes": dk, "resets_in_seconds": kalan}
                    if eski else {"used_percent": u, "window_minutes": dk, "resets_at": r})
        rl = {"limit_id": "codex", "limit_name": None, "primary": w(p5, 300, R5),
              "secondary": w(p7, 10080, r7), "credits": None, "plan_type": plan}
    return {"timestamp": ts, "type": "event_msg",
            "payload": {"type": "token_count", "info": info, "rate_limits": rl}}


def yaz(gun, ad, satirlar, yarim=None):
    d = os.path.join(KOK, "2026", "01", gun)
    os.makedirs(d, exist_ok=True)
    with open(os.path.join(d, ad), "w", encoding="utf-8", newline="\n") as f:
        for s in satirlar:
            f.write(json.dumps(s, ensure_ascii=False) + "\n")
        if yarim:
            f.write(yarim)


A = "aaaaaaaa-0000-4000-8000-000000000001"
B = "bbbbbbbb-0000-4000-8000-000000000002"
C = "cccccccc-0000-4000-8000-000000000003"
D = "dddddddd-0000-4000-8000-000000000004"
F = "ffffffff-0000-4000-8000-000000000006"

a1 = tc("2026-01-01T06:00:05.000Z", None, None, 10.0, 40.0)
a2 = tc("2026-01-01T06:01:00.000Z", (800, 500, 200, 50), (800, 500, 200, 50), 12.0, 41.0)
a3 = tc("2026-01-01T06:05:00.000Z", (2000, 1500, 500, 120), (1200, 1000, 300, 70), 15.0, 42.0)
a4 = tc("2026-01-01T06:05:01.000Z", (2000, 1500, 500, 120), (1200, 1000, 300, 70), 16.0, 42.0)
yaz("01", f"rollout-2026-01-01T06-00-00-{A}.jsonl",
    [meta(A, "2026-01-01T06:00:00.000Z"), a1, a2, a2, a3, a4])

# B: A ile eşzamanlı; eski sürüm (resets_in_seconds) ve eskimiş, daha düşük kota
yaz("01", f"rollout-2026-01-01T06-02-00-{B}.jsonl",
    [meta(B, "2026-01-01T06:02:00.000Z"),
     tc("2026-01-01T06:02:30.000Z", (2500, 0, 500, 0), (2500, 0, 500, 0), 13.0, 41.0, eski=True),
     tc("2026-01-01T06:06:00.000Z", (3300, 0, 700, 0), (800, 0, 200, 0), 11.0, 40.0, eski=True)])

# C: A'dan çatallanmış; A'nın satırları birebir kopya + yeni bir tur
yaz("01", f"rollout-2026-01-01T07-00-00-{C}.jsonl",
    [meta(C, "2026-01-01T07:00:00.000Z", forked_from_id=A), a1, a2, a3,
     tc("2026-01-01T07:01:00.000Z", (2400, 1700, 700, 150), (400, 200, 200, 30), 18.0, 43.0)])

# D: sayaç geriye düşüyor, haftalık pencere yenileniyor, son satır yarım
yaz("02", f"rollout-2026-01-02T09-00-00-{D}.jsonl",
    [meta(D, "2026-01-02T09:00:00.000Z"),
     tc("2026-01-02T09:01:00.000Z", (4000, 0, 1000, 0), (4000, 0, 1000, 0), 5.0, 96.0),
     tc("2026-01-02T09:02:00.000Z", (600, 0, 100, 0), (600, 0, 100, 0), 6.0, 2.0, r7=R7_YENI)],
    yarim='{"timestamp":"2026-01-02T09:03:00.000Z","type":"event_msg","payl')

# F: kota bilgisi hiç yok (rate_limits null), yalnız token
yaz("03", f"rollout-2026-01-03T09-00-00-{F}.jsonl",
    [meta(F, "2026-01-03T09:00:00.000Z"),
     tc("2026-01-03T09:01:00.000Z", (100, 0, 10, 0), (100, 0, 10, 0))])
