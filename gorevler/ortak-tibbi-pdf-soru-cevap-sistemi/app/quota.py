"""Şeffaf, adil ve iş ortasında kesmeyen kota.

Kurallar (kaynak sohbetten):
- Herkese eşit aylık ücretsiz kredi (kart/giriş istenmez).
- Kalan kredi her zaman görünür; her işlemin bedeli işlemden ÖNCE gösterilir.
- Kredisi varken başlatılmış bir soru zinciri (thread), kredi bitse bile küçük bir
  "esneme payı" ile tamamlanabilir; kesinti yalnız YENİ zincirde olur.
- Çok kullanan, gönüllü destek koduyla ek kredi alabilir; az kullanan hiç ödeme görmez.
"""
import secrets
from datetime import datetime, timezone

from .config import settings
from .db import DB


class QuotaExceeded(Exception):
    def __init__(self, status: dict):
        super().__init__("quota")
        self.status = status


def now() -> datetime:
    return datetime.now(timezone.utc)


def period_of(t: datetime | None = None) -> str:
    return (t or now()).strftime("%Y-%m")


def reset_at(t: datetime | None = None) -> str:
    t = t or now()
    y, m = (t.year + 1, 1) if t.month == 12 else (t.year, t.month + 1)
    return datetime(y, m, 1, tzinfo=timezone.utc).isoformat()


def _sum(db: DB, device: str, period: str, source: str) -> int:
    r = db.one(
        "SELECT COALESCE(SUM(cost),0) s FROM usage WHERE device_id=? AND period=? AND kind LIKE ?",
        (device, period, f"%:{source}"),
    )
    return int(r["s"])


def status(db: DB, device: str) -> dict:
    p = period_of()
    free_total = settings.monthly_free_credits
    free_used = _sum(db, device, p, "free")
    grace_used = _sum(db, device, p, "grace")
    dev = db.one("SELECT bonus_credits FROM devices WHERE id=?", (device,))
    bonus_left = int(dev["bonus_credits"]) if dev else 0
    free_left = max(0, free_total - free_used)
    available = free_left + bonus_left
    ratio = free_left / free_total if free_total else 0
    if available <= 0:
        level = "grace" if grace_used < settings.grace_credits else "blocked"
    elif bonus_left == 0 and ratio <= 0.1:
        level = "low"
    elif bonus_left == 0 and ratio <= 0.25:
        level = "warn"
    else:
        level = "ok"
    history = [
        dict(r)
        for r in db.q(
            "SELECT kind, cost, created_at FROM usage WHERE device_id=? ORDER BY id DESC LIMIT 12",
            (device,),
        )
    ]
    return {
        "period": p,
        "free_total": free_total,
        "free_used": free_used,
        "free_left": free_left,
        "bonus_left": bonus_left,
        "available": available,
        "grace_total": settings.grace_credits,
        "grace_used": grace_used,
        "grace_left": max(0, settings.grace_credits - grace_used),
        "level": level,
        "reset_at": reset_at(),
        "costs": {
            "question": settings.cost_question,
            "question_ai": settings.cost_question_ai,
            "upload": settings.cost_upload,
        },
        "ai_enabled": settings.ai_enabled,
        "donate_url": settings.donate_url,
        "history": history,
    }


def open_thread(db: DB, device: str) -> str:
    st = status(db, device)
    tid = secrets.token_urlsafe(9)
    db.run(
        "INSERT INTO threads(id, device_id, started_with_credit, created_at) VALUES (?,?,?,?)",
        (tid, device, 1 if st["available"] > 0 else 0, now().isoformat()),
    )
    return tid


def charge(db: DB, device: str, kind: str, cost: int, thread_id: str | None = None) -> dict:
    """Bedeli düşer; yetmiyorsa ve zincir kredili başladıysa esneme payını kullanır."""
    if cost <= 0:
        return status(db, device)
    with db.lock:
        st = status(db, device)
        take_free = min(cost, st["free_left"])
        take_bonus = min(cost - take_free, st["bonus_left"])
        shortfall = cost - take_free - take_bonus
        take_grace = 0
        if shortfall:
            thread_ok = False
            if thread_id:
                t = db.one(
                    "SELECT started_with_credit FROM threads WHERE id=? AND device_id=?",
                    (thread_id, device),
                )
                thread_ok = bool(t and t["started_with_credit"])
            # Kredisi biraz kalan kişi işlemini yarıda bırakmaz; ya da kredili başlamış zincir sürer.
            if (take_free + take_bonus > 0 or thread_ok) and st["grace_left"] >= shortfall:
                take_grace = shortfall
            else:
                raise QuotaExceeded(st)
        ts = now().isoformat()
        p = period_of()
        for source, amount in (("free", take_free), ("bonus", take_bonus), ("grace", take_grace)):
            if amount:
                db.conn.execute(
                    "INSERT INTO usage(device_id, period, kind, cost, thread_id, grace, created_at)"
                    " VALUES (?,?,?,?,?,?,?)",
                    (device, p, f"{kind}:{source}", amount, thread_id, int(source == "grace"), ts),
                )
        if take_bonus:
            db.conn.execute(
                "UPDATE devices SET bonus_credits = bonus_credits - ? WHERE id=?", (take_bonus, device)
            )
        db.conn.commit()
        return status(db, device)


def redeem(db: DB, device: str, code: str) -> int:
    with db.lock:
        r = db.one("SELECT credits, redeemed_by FROM codes WHERE code=?", (code.strip(),))
        if not r or r["redeemed_by"]:
            raise ValueError("Kod geçersiz ya da kullanılmış.")
        db.conn.execute(
            "UPDATE codes SET redeemed_by=?, redeemed_at=? WHERE code=?",
            (device, now().isoformat(), code.strip()),
        )
        db.conn.execute(
            "UPDATE devices SET bonus_credits = bonus_credits + ? WHERE id=?", (r["credits"], device)
        )
        db.conn.commit()
        return int(r["credits"])


def create_code(db: DB, credits: int, note: str = "") -> str:
    code = "DESTEK-" + secrets.token_hex(4).upper()
    db.run("INSERT INTO codes(code, credits, note) VALUES (?,?,?)", (code, credits, note))
    return code
