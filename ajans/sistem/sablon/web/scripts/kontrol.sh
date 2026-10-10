#!/usr/bin/env bash
# Stop kancası: oturum "bitti" demeden önce build + kullanıcı testi. Hata varsa çıkış kodu 2 → Claude devam eder.
cd "$(dirname "$0")/.." || exit 0
[ -f package.json ] || exit 0                      # henüz proje yoksa engelleme
if ! npm run build --silent >/tmp/kontrol-build.log 2>&1; then
  echo "BUILD HATASI — düzeltmeden bitirme:" >&2; tail -20 /tmp/kontrol-build.log >&2; exit 2
fi
if [ -f scripts/kullanici-testi.mjs ] && curl -s -o /dev/null http://localhost:4321/; then
  node scripts/kullanici-testi.mjs >/tmp/kontrol-test.log 2>&1 || { echo "KULLANICI TESTİ BAŞARISIZ:" >&2; tail -20 /tmp/kontrol-test.log >&2; exit 2; }
fi
exit 0
