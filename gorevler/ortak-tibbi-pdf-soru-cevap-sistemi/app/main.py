"""Ortak Tıbbi PDF Soru-Cevap — FastAPI sunucusu (web + mobil PWA)."""
import hashlib
import secrets
from datetime import timedelta
from pathlib import Path

from fastapi import Depends, FastAPI, File, Form, Header, HTTPException, Request, UploadFile
from fastapi.responses import FileResponse, JSONResponse
from fastapi.staticfiles import StaticFiles
from pydantic import BaseModel, Field

from . import quota
from .config import settings
from .db import DB
from .embed import embed
from .pdfproc import chunk_pages, detect_language, extract_pages
from . import translate
from .search import LANG_NAMES, Index, ai_answer, extractive_answer, retrieve, translated_answer

STATIC = Path(__file__).resolve().parent.parent / "static"


def create_app() -> FastAPI:
    db = DB(settings.data_dir / "medpdf.sqlite3")
    index = Index(db)
    app = FastAPI(title="Ortak Tıbbi PDF Soru-Cevap", version="1.0.0")
    app.state.db = db
    app.state.index = index

    def ip_hash(request: Request) -> str:
        ip = request.headers.get("x-forwarded-for", "").split(",")[0].strip() or (
            request.client.host if request.client else "?"
        )
        return hashlib.sha256(ip.encode()).hexdigest()[:16]

    def device(x_device_id: str = Header(default="")) -> str:
        if not x_device_id or not db.one("SELECT 1 FROM devices WHERE id=?", (x_device_id,)):
            raise HTTPException(401, "Cihaz kimliği yok; sayfayı yenileyin.")
        return x_device_id

    def admin(x_admin_token: str = Header(default="")):
        if not settings.admin_token or not secrets.compare_digest(x_admin_token, settings.admin_token):
            raise HTTPException(403, "Yönetici yetkisi gerekli.")

    @app.exception_handler(quota.QuotaExceeded)
    async def _quota(_, exc: quota.QuotaExceeded):
        return JSONResponse(status_code=402, content={"error": "quota", "quota": exc.status})

    # --- Giriş yok: anonim cihaz kimliği -------------------------------------------------
    @app.post("/api/device")
    def new_device(request: Request):
        ih = ip_hash(request)
        since = (quota.now() - timedelta(days=1)).isoformat()
        n = db.one("SELECT COUNT(*) n FROM devices WHERE ip_hash=? AND created_at>?", (ih, since))["n"]
        if n >= settings.new_devices_per_ip_per_day:
            raise HTTPException(429, "Bu ağdan bugün çok fazla yeni cihaz açıldı; mevcut cihazı kullanın.")
        did = secrets.token_urlsafe(18)
        db.run(
            "INSERT INTO devices(id, created_at, ip_hash) VALUES (?,?,?)", (did, quota.now().isoformat(), ih)
        )
        return {"device_id": did, "quota": quota.status(db, did)}

    @app.get("/api/quota")
    def get_quota(dev: str = Depends(device)):
        return quota.status(db, dev)

    @app.post("/api/redeem")
    def redeem(code: str = Form(...), dev: str = Depends(device)):
        try:
            added = quota.redeem(db, dev, code)
        except ValueError as e:
            raise HTTPException(400, str(e))
        return {"added": added, "quota": quota.status(db, dev)}

    # --- Ortak kütüphane -------------------------------------------------------------------
    @app.get("/api/documents")
    def documents(q: str = ""):
        rows = db.q(
            "SELECT id, title, language, subject, pages, chunks, created_at FROM documents"
            " WHERE hidden=0 AND (title LIKE ? OR subject LIKE ?) ORDER BY id DESC LIMIT 500",
            (f"%{q}%", f"%{q}%"),
        )
        return [{**dict(r), "language_name": LANG_NAMES.get(r["language"], r["language"])} for r in rows]

    @app.post("/api/documents")
    async def upload(
        file: UploadFile = File(...),
        title: str = Form(""),
        subject: str = Form(""),
        rights: bool = Form(False),
        dev: str = Depends(device),
    ):
        if not rights:
            raise HTTPException(400, "Paylaşım hakkınız olduğunu onaylamanız gerekir.")
        data = await file.read(settings.max_upload_mb * 1024 * 1024 + 1)
        if len(data) > settings.max_upload_mb * 1024 * 1024:
            raise HTTPException(413, f"Dosya {settings.max_upload_mb} MB sınırını aşıyor.")
        if not data.startswith(b"%PDF"):
            raise HTTPException(400, "Yalnız PDF dosyası yüklenebilir.")
        sha = hashlib.sha256(data).hexdigest()
        existing = db.one("SELECT id, title FROM documents WHERE sha256=?", (sha,))
        if existing:
            return {"id": existing["id"], "title": existing["title"], "duplicate": True}
        try:
            pages, meta_title = extract_pages(data, settings.max_pages)
        except ValueError as e:
            raise HTTPException(400, str(e))
        except Exception:
            raise HTTPException(400, "PDF okunamadı.")
        chunks = chunk_pages(pages)
        if not chunks:
            raise HTTPException(
                400, "PDF'te seçilebilir metin bulunamadı (taranmış görüntü olabilir; OCR'li sürüm yükleyin)."
            )
        # Katkı varsayılan olarak ücretsiz; bedel varsa (cost_upload>0) önce düşülür.
        quota.charge(db, dev, "upload", settings.cost_upload)
        vecs = embed([t for _, t in chunks])
        full = " ".join(t for _, t in pages[:20])
        name = (title or meta_title or Path(file.filename or "belge.pdf").stem).strip()[:200]
        with db.lock:
            cur = db.conn.execute(
                "INSERT INTO documents(sha256,title,language,subject,pages,chunks,uploader,created_at)"
                " VALUES (?,?,?,?,?,?,?,?)",
                (sha, name, detect_language(full), subject.strip()[:100], len(pages), len(chunks), dev,
                 quota.now().isoformat()),
            )
            doc_id = cur.lastrowid
            db.conn.executemany(
                "INSERT INTO chunks(doc_id,page,text,vec) VALUES (?,?,?,?)",
                [(doc_id, p, t, v.tobytes()) for (p, t), v in zip(chunks, vecs)],
            )
            db.conn.commit()
        index.invalidate()
        return {"id": doc_id, "title": name, "pages": len(pages), "chunks": len(chunks), "duplicate": False,
                "quota": quota.status(db, dev)}

    class ReportIn(BaseModel):
        reason: str = Field(pattern="^(copyright|wrong|spam|other)$")
        note: str = Field(default="", max_length=500)

    @app.post("/api/documents/{doc_id}/report")
    def report(doc_id: int, body: ReportIn, dev: str = Depends(device)):
        if not db.one("SELECT 1 FROM documents WHERE id=?", (doc_id,)):
            raise HTTPException(404, "Belge yok.")
        try:
            db.run(
                "INSERT INTO reports(doc_id,device_id,reason,note,created_at) VALUES (?,?,?,?,?)",
                (doc_id, dev, body.reason, body.note, quota.now().isoformat()),
            )
        except Exception:
            return {"ok": True, "already": True}
        db.run("UPDATE documents SET reports=reports+1 WHERE id=?", (doc_id,))
        db.run(
            "UPDATE documents SET hidden=1 WHERE id=? AND (reports>=? OR ?='copyright' AND reports>=1)",
            (doc_id, settings.reports_to_hide, body.reason),
        )
        index.invalidate()
        return {"ok": True}

    # --- Soru-cevap ------------------------------------------------------------------------
    class AskIn(BaseModel):
        question: str = Field(min_length=3, max_length=1000)
        doc_ids: list[int] | None = None
        thread_id: str | None = None
        use_ai: bool = False
        ui_language: str | None = Field(default=None, pattern="^[a-z]{2}$")

    @app.post("/api/ask")
    def ask(body: AskIn, dev: str = Depends(device)):
        use_ai = body.use_ai and settings.ai_enabled
        cost = settings.cost_question_ai if use_ai else settings.cost_question
        tid = body.thread_id
        if not tid or not db.one("SELECT 1 FROM threads WHERE id=? AND device_id=?", (tid, dev)):
            tid = quota.open_thread(db, dev)
        st = quota.charge(db, dev, "ask_ai" if use_ai else "ask", cost, tid)
        hits = retrieve(db, index, body.question, body.doc_ids or None, k=5)
        ans = extractive_answer(body.question, hits)
        qlang = ans["question_language"]
        if qlang == "unknown" and body.ui_language:
            qlang = ans["question_language"] = body.ui_language
        if hits and hits[0]["language"] != qlang and not use_ai:
            if translate.supports(hits[0]["language"], qlang):
                try:
                    ans = translated_answer(body.question, hits, qlang)
                except Exception as e:  # model yüklenemezse alıntıya düş ve bunu açıkça söyle
                    ans["translation_error"] = type(e).__name__
            elif hits[0]["language"] in translate.SOURCE_LANGS and qlang in translate.TARGET_TAGS:
                ans["translation_error"] = "mt_unavailable"  # dil çifti destekli ama model dosyası yok
            else:
                ans["translation_error"] = "unsupported_pair"
        if use_ai and hits:
            try:
                ans = ai_answer(body.question, hits)
            except Exception as e:  # AI çökerse yine kaynaklı alıntı yanıtı döner
                ans["ai_error"] = type(e).__name__
        return {"thread_id": tid, "cost": cost, "answer": ans, "hits": hits, "quota": st}

    # --- Yönetim (bağış karşılığı destek kodu, gizleme) ------------------------------------
    class CodeIn(BaseModel):
        credits: int = Field(gt=0, le=100000)
        note: str = ""

    @app.post("/api/admin/codes", dependencies=[Depends(admin)])
    def make_code(body: CodeIn):
        return {"code": quota.create_code(db, body.credits, body.note), "credits": body.credits}

    @app.get("/api/admin/reports", dependencies=[Depends(admin)])
    def list_reports():
        return [dict(r) for r in db.q(
            "SELECT r.*, d.title, d.hidden FROM reports r JOIN documents d ON d.id=r.doc_id ORDER BY r.id DESC"
        )]

    @app.post("/api/admin/documents/{doc_id}/visibility", dependencies=[Depends(admin)])
    def visibility(doc_id: int, hidden: bool = Form(...)):
        db.run("UPDATE documents SET hidden=? WHERE id=?", (int(hidden), doc_id))
        index.invalidate()
        return {"id": doc_id, "hidden": hidden}

    @app.get("/api/health")
    def health():
        n = db.one("SELECT COUNT(*) n FROM documents WHERE hidden=0")["n"]
        return {"ok": True, "documents": n, "ai_enabled": settings.ai_enabled, "model": settings.embed_model,
                "local_translation": translate.available()}

    @app.get("/")
    def root():
        return FileResponse(STATIC / "index.html")

    app.mount("/", StaticFiles(directory=STATIC), name="static")
    return app


app = create_app()
