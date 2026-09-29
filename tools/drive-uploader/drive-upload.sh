#!/usr/bin/env bash
# Bir dosyayı Apps Script yükleyicisi (Code.gs) üzerinden Google Drive'a gönderir ve
# Drive'ın döndürdüğü MD5'i yerel dosyayla karşılaştırarak doğrular.
#
# Kullanım: tools/drive-uploader/drive-upload.sh <dosya> [Drive içindeki ad]
# Gerekli ortam değişkenleri (ortam ayarlarından verilir, asla koda/sohbete yazılmaz):
#   DRIVE_UPLOAD_URL     Apps Script web uygulamasının /exec adresi
#   DRIVE_UPLOAD_SECRET  setupSecret() ile üretilen gizli anahtar
set -euo pipefail

file="${1:?Kullanım: drive-upload.sh <dosya> [Drive içindeki ad]}"
name="${2:-$(basename "$file")}"
: "${DRIVE_UPLOAD_URL:?DRIVE_UPLOAD_URL ortam değişkeni tanımlı değil}"
: "${DRIVE_UPLOAD_SECRET:?DRIVE_UPLOAD_SECRET ortam değişkeni tanımlı değil}"
[[ -f "$file" ]] || { echo "Dosya bulunamadı: $file" >&2; exit 1; }

case "$file" in
  *.apk) mime="application/vnd.android.package-archive" ;;
  *.zip) mime="application/zip" ;;
  *.exe) mime="application/vnd.microsoft.portable-executable" ;;
  *.pdf) mime="application/pdf" ;;
  *)     mime="application/octet-stream" ;;
esac

payload="$(mktemp)"
response="$(mktemp)"
trap 'rm -f "$payload" "$response"' EXIT

# Gizli anahtar komut satırına değil, yalnızca geçici dosyaya yazılır (ps çıktısında görünmez).
FILE="$file" NAME="$name" MIME="$mime" python3 - > "$payload" <<'PY'
import base64, json, os, sys
with open(os.environ["FILE"], "rb") as f:
    data = base64.b64encode(f.read()).decode("ascii")
json.dump({"secret": os.environ["DRIVE_UPLOAD_SECRET"], "name": os.environ["NAME"],
           "mimeType": os.environ["MIME"], "dataBase64": data}, sys.stdout)
PY

# Apps Script POST'u /exec'te işler, sonucu 302 ile yönlendirir; -L sonucu GET ile alır.
curl -sS -L --fail --max-time 300 -H "Content-Type: application/json" \
  --data-binary "@$payload" "$DRIVE_UPLOAD_URL" -o "$response"

local_md5="$(md5sum "$file" | cut -d' ' -f1)"
LOCAL_MD5="$local_md5" python3 - "$response" <<'PY'
import json, os, sys
try:
    r = json.load(open(sys.argv[1]))
except ValueError:
    sys.exit("Yükleyiciden JSON dışı yanıt geldi (adres yanlış olabilir ya da dağıtım 'Herkes' erişimine açık değil).")
if not r.get("ok"):
    sys.exit(f"Yükleme reddedildi: {r.get('error')}")
if r["md5"] != os.environ["LOCAL_MD5"]:
    sys.exit(f"MD5 uyuşmuyor! yerel={os.environ['LOCAL_MD5']} drive={r['md5']} (dosya id: {r['id']})")
print(f"Yüklendi ve doğrulandı: {r['name']} ({r['size']} bayt, md5 {r['md5']})")
print(f"Drive id: {r['id']}")
print(f"Link: {r['url']}")
PY
