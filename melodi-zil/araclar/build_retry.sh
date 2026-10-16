#!/usr/bin/env bash
# Maven 429/404 dalgalanmasi icin tekrarli derleme. Kullanim: build_retry.sh <proje_klasoru> <gorev...>
source /home/user/android-env.sh
cd "$1"; shift
for i in 1 2 3 4 5 6 7 8; do
  echo "=== deneme $i $(date -u +%T)"
  nice -n 5 ./gradlew "$@" --no-daemon --max-workers=2 && { echo "BASARILI $(date -u +%T)"; exit 0; }
  rm -rf ~/.gradle/caches/modules-2/metadata-* ; sleep 15
done
echo "BASARISIZ"; exit 1
