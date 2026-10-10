#!/usr/bin/env bash
# Parametre taraması: her ayar için değerlendirme setini çalıştırıp puanları yazar. Kullanım: tara.sh "voicing=0.2" "voicing=0.5" ...
S=${S:?}; cd /home/user/Jn/melodi-zil; export ANDROID_HOME=/home/user/Jn/android-sdk MELODI_EVAL_DIR=$S/eval
for prm in "$@"; do
  MELODI_PARAMS="$prm" /opt/gradle-8.14.3/bin/gradle --no-daemon -q :app:testDebugUnitTest --tests "com.jn.melodizil.core.EvalHarnessTest" --rerun-tasks -i 2>&1 | grep -E "EVAL_SURE|^e:" | tr '\n' ' '
  echo "[$prm] $($S/ev/bin/python -I araclar/degerlendirme/puanla.py $S/eval 2>/dev/null | grep 'voc   bizim')"
done
