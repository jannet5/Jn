#!/usr/bin/env bash
# Yerel çeviri modellerini indirip CTranslate2 int8 biçimine dönüştürür (tek seferlik, ~5 dk, ~315 MB çıktı).
# Dönüştürme araçları (torch/transformers) GEÇİCİ bir sanal ortamda kurulur; çalışma anı yalnız
# ctranslate2 + sentencepiece kullanır. Kullanım: scripts/fetch_mt_models.sh [hedef_klasör]
set -euo pipefail
OUT="${1:-$(dirname "$0")/../models/mt}"
VENV="${MT_CONVERT_VENV:-$(mktemp -d)/venv}"
# Sabitlenmiş model sürümleri (Hugging Face commit) — ikisi de Apache-2.0
BIBLE_REPO=Helsinki-NLP/opus-mt-tc-bible-big-trk-deu_eng_fra_por_spa
BIBLE_REV=f1cda35b5be3f5a6466e9edd892a7c489e7a001f
OPUS_REPO=Helsinki-NLP/opus-mt-tr-es
OPUS_REV=7157c6839c828146930b4b0573f93a79cfce7863

python3 -m venv "$VENV"
"$VENV/bin/pip" install -q --index-url https://download.pytorch.org/whl/cpu torch
"$VENV/bin/pip" install -q "transformers>=4.40" sentencepiece ctranslate2==4.8.2 protobuf
mkdir -p "$OUT"
convert() {  # $1 repo  $2 rev  $3 çıktı adı
  local snap
  snap=$("$VENV/bin/python" -c "from huggingface_hub import snapshot_download as s; print(s('$1', revision='$2', allow_patterns=['*.json','*.spm','pytorch_model.bin']))")
  "$VENV/bin/ct2-transformers-converter" --model "$snap" --output_dir "$OUT/$3" \
    --quantization int8 --copy_files source.spm target.spm --force
}
convert "$BIBLE_REPO" "$BIBLE_REV" bible-trk
convert "$OPUS_REPO" "$OPUS_REV" opus-tr-es
echo "Modeller hazır: $OUT"; du -sh "$OUT"/*
