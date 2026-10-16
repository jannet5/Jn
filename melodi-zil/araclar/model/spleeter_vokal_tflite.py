# Spleeter 2stems (Deezer, MIT) checkpoint'inden yalnızca VOKAL U-Net'ini TFLite'a aktarır.
# Girdi: [1, 512, 1024, 2] karışım genlik spektrogramı (44.1 kHz, STFT 4096/1024, 0-11 kHz, stereo)
# Çıktı: [1, 512, 1024, 2] tahmini vokal genliği (= sigmoid maske × girdi). Telefonda maske = çıktı / girdi.
import sys, numpy as np, tensorflow as tf  # TF 2.12 (TF1 uyumluluk modu)
from spleeter.model.functions.unet import apply_unet  # Spleeter'ın kendi ağ tanımı
ckpt, out_path = sys.argv[1], sys.argv[2]
tf.compat.v1.disable_eager_execution()  # TF1 grafiği
g = tf.Graph()
with g.as_default():
    inp = tf.compat.v1.placeholder(tf.float32, [1, 512, 1024, 2], name="mix_mag")  # sabit boyutlu girdi
    out = apply_unet(inp, "vocals_spectrogram", params={})  # vokal ağı (checkpoint'teki ilk ağ: aynı katman adları)
    out = tf.identity(out, name="vocal_mag")
    names = {v.op.name for v in tf.compat.v1.global_variables()}
    ck = dict(tf.train.list_variables(ckpt))
    missing = [n for n in names if n not in ck]; assert not missing, missing  # tüm ağırlıklar checkpoint'te olmalı
    saver = tf.compat.v1.train.Saver(var_list=tf.compat.v1.global_variables())
    with tf.compat.v1.Session(graph=g) as sess:
        tf.compat.v1.keras.backend.set_learning_phase(0)  # çıkarım modu (BatchNorm/Dropout)
        saver.restore(sess, ckpt)  # ağırlıkları yükle
        conv = tf.compat.v1.lite.TFLiteConverter.from_session(sess, [inp], [out])
        conv.optimizations = [tf.lite.Optimize.DEFAULT]; conv.target_spec.supported_types = [tf.float16]  # yarım hassasiyet (~20 MB)
        open(out_path, "wb").write(conv.convert())
        x = np.abs(np.random.default_rng(0).standard_normal((1, 512, 1024, 2))).astype(np.float32)
        ref = sess.run(out, {inp: x})  # doğrulama için TF çıktısı
it = tf.lite.Interpreter(model_path=out_path); it.allocate_tensors()
it.set_tensor(it.get_input_details()[0]["index"], x); it.invoke(); y = it.get_tensor(it.get_output_details()[0]["index"])
print("TFLite boyut MB:", round(len(open(out_path, "rb").read()) / 1e6, 1), "| TF↔TFLite en büyük fark:", float(np.abs(y - ref).max()), "| ort:", float(np.abs(y - ref).mean()))
