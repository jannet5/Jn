"""Tüm ayarlar ortam değişkenlerinden okunur; sır değerleri koda yazılmaz."""
import os
from dataclasses import dataclass, field
from pathlib import Path


def _int(name: str, default: int) -> int:
    try:
        return int(os.environ.get(name, default))
    except ValueError:
        return default


@dataclass
class Settings:
    data_dir: Path = field(default_factory=lambda: Path(os.environ.get("MEDPDF_DATA_DIR", "./data")))
    embed_model: str = os.environ.get(
        "MEDPDF_EMBED_MODEL", "sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2"
    )
    embed_cache: str | None = os.environ.get("MEDPDF_EMBED_CACHE") or None
    max_upload_mb: int = _int("MEDPDF_MAX_UPLOAD_MB", 40)
    max_pages: int = _int("MEDPDF_MAX_PAGES", 1500)

    # Kota: herkese eşit aylık ücretsiz hak + iş ortasında kesmeyen esneme payı
    monthly_free_credits: int = _int("MEDPDF_MONTHLY_FREE", 60)
    grace_credits: int = _int("MEDPDF_GRACE", 6)
    cost_question: int = _int("MEDPDF_COST_QUESTION", 1)
    cost_question_ai: int = _int("MEDPDF_COST_QUESTION_AI", 3)
    cost_upload: int = _int("MEDPDF_COST_UPLOAD", 0)  # katkı ücretsiz
    new_devices_per_ip_per_day: int = _int("MEDPDF_NEW_DEVICES_PER_IP", 5)
    reports_to_hide: int = _int("MEDPDF_REPORTS_TO_HIDE", 3)

    # Bağış / destek bağlantısı (Ko-fi, Open Collective, GitHub Sponsors vb.) — isteğe bağlı
    donate_url: str = os.environ.get("MEDPDF_DONATE_URL", "")
    admin_token: str = os.environ.get("MEDPDF_ADMIN_TOKEN", "")

    # İsteğe bağlı üretken yanıt: yalnız açıkça bu değişken verilirse kullanılır
    anthropic_key: str = os.environ.get("MEDPDF_ANTHROPIC_API_KEY", "")
    anthropic_model: str = os.environ.get("MEDPDF_ANTHROPIC_MODEL", "claude-sonnet-5-5")

    @property
    def ai_enabled(self) -> bool:
        return bool(self.anthropic_key)


settings = Settings()
