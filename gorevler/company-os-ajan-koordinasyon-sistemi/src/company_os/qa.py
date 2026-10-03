"""Kabul kapısı: kanıtı, kontrol edilen anlık görüntüye (snapshot) bağlar.

Denetim bulgusu 4 (qa_runner.py:33): eski ``acceptance_gate`` çağıranın verdiği
iki hash haritasının eşitliğine bakıyor, kanıtın ``artifact_refs``'ini bunlara
bağlamıyordu; boş haritalar ve ilgisiz artifact'ı anlatan haritalar geçiyordu.

Burada snapshot çağırandan alınmaz; defterden ve diskten üretilir:

* Görevin *mevcut* denemesine ait en az bir artifact olmalı (boş snapshot red).
* Her artifact'ın diskteki SHA-256'sı kayıtla aynı olmalı (sonradan değişiklik red).
* En az bir kanıt olmalı; tüm kanıtlar mevcut deneme + mevcut fencing token'a ait,
  çıkış kodu 0 olmalı (eski görev/deneme kanıtı red).
* Her kanıt referansı (yol, sha256) mevcut snapshot'ta birebir bulunmalı.
* Her artifact en az bir başarılı kanıt tarafından kapsanmalı.
"""
from __future__ import annotations


def acceptance_gate(ledger, task_id: str, *, conn=None) -> dict:
    c = conn or ledger.conn
    row = c.execute("SELECT state, attempt, lease_token FROM tasks WHERE id=?", (task_id,)).fetchone()
    reasons: list[str] = []
    if row is None:
        return {"passed": False, "reasons": [f"görev yok: {task_id}"], "snapshot": {}}
    attempt, token = row["attempt"], row["lease_token"]
    arts = ledger.artifacts(task_id, attempt, conn=c)
    snapshot = {a["relpath"]: a["sha256"] for a in arts}
    if not snapshot:
        reasons.append("boş snapshot: mevcut denemede artifact yok")
    for a in arts:
        if a["token"] != token:
            reasons.append(f"{a['relpath']}: başka bir lease token'ı ile yazılmış ({a['token']} ≠ {token})")
        try:
            disk = ledger.store.sha256(f"{ledger.attempt_dir(task_id, attempt)}/{a['relpath']}")
        except Exception as e:  # dosya silinmiş / symlink'e çevrilmiş
            reasons.append(f"{a['relpath']}: diskten güvenli okunamadı ({e})")
            continue
        if disk != a["sha256"]:
            reasons.append(f"{a['relpath']}: diskteki hash kayıtla uyuşmuyor")
    evs = ledger.evidence(task_id, attempt, conn=c)
    if not evs:
        reasons.append("kanıt yok: mevcut denemede çalıştırılmış kabul komutu yok")
    covered: set[str] = set()
    for e in evs:
        if e["token"] != token:
            reasons.append(f"kanıt #{e['id']}: eski lease token'ı ({e['token']} ≠ {token})")
            continue
        if e["exit_code"] != 0:
            reasons.append(f"kanıt #{e['id']}: çıkış kodu {e['exit_code']} ({' '.join(e['argv'])})")
            continue
        if not e["artifact_refs"]:
            reasons.append(f"kanıt #{e['id']}: artifact referansı yok")
        for ref in e["artifact_refs"]:
            if snapshot.get(ref["path"]) != ref["sha256"]:
                reasons.append(f"kanıt #{e['id']}: {ref['path']} referansı snapshot ile eşleşmiyor")
            else:
                covered.add(ref["path"])
    for path in snapshot:
        if path not in covered:
            reasons.append(f"{path}: hiçbir başarılı kanıt bu artifact'ı kapsamıyor")
    return {
        "passed": not reasons,
        "reasons": reasons,
        "attempt": attempt,
        "token": token,
        "snapshot": snapshot,
        "evidence_ids": [e["id"] for e in evs],
    }
