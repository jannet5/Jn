# CREPE "tiny" perde modelini (Kim ve ark. 2018; ağırlıklar torchcrepe, MIT) TFLite'a çevirir ve doğrular.
# 1) pt ortamında:  python crepe_tiny_tflite.py agirlik <cikti.npz>      → torch ağırlıkları + test kareleri + torch çıktıları
# 2) sp ortamında:  python crepe_tiny_tflite.py tflite <girdi.npz> <crepe_tiny.tflite>  → Keras modeli kurulur, ağırlıklar yüklenir, TFLite'a çevrilir, torch ile karşılaştırılır
# Girdi: [N, 1024] kare (16 kHz, her kare ortalaması çıkarılmış ve standart sapmaya bölünmüş); çıktı: [N, 360] perde olasılığı (sigmoid, 20 cent adım, 0. kutu = 31.7 Hz).
import sys, numpy as np

def agirlik(out):
    import torch, torchcrepe
    m = torchcrepe.Crepe("tiny"); m.load_state_dict(torch.load(torchcrepe.__path__[0] + "/assets/tiny.pth", map_location="cpu", weights_only=True)); m.eval()
    w = {k: v.numpy() for k, v in m.state_dict().items()}  # tüm ağırlıklar
    rng = np.random.default_rng(1); x = rng.standard_normal((64, 1024)).astype(np.float32)  # rastgele test kareleri
    t = np.arange(1024) / 16000; x[:8] = np.stack([np.sin(2 * np.pi * f * t) for f in (110, 220, 330, 440, 550, 660, 880, 1000)])  # + saf sinüsler
    x = (x - x.mean(1, keepdims=True)) / np.maximum(x.std(1, ddof=1, keepdims=True), 1e-10)  # torchcrepe normalizasyonu
    with torch.no_grad(): y = m(torch.from_numpy(x)).numpy()  # referans çıktı
    np.savez(out, x=x, y=y, **{k.replace(".", "__"): v for k, v in w.items()})

def tflite(inp, out):
    import tensorflow as tf
    d = np.load(inp); L = tf.keras.layers
    i = L.Input((1024,), batch_size=None); h = L.Reshape((1024, 1))(i)  # [N, 1024, 1]
    for k, name in enumerate(["conv1", "conv2", "conv3", "conv4", "conv5", "conv6"]):
        w = d[name + "__weight"][:, :, :, 0]  # torch [out, in, k]
        h = L.ZeroPadding1D((254, 254) if k == 0 else (31, 32))(h)  # torchcrepe dolgusu
        conv = L.Conv1D(w.shape[0], w.shape[2], strides=4 if k == 0 else 1, activation="relu"); h = conv(h)  # evrişim + ReLU (torchcrepe: conv → relu → BN)
        conv.set_weights([np.transpose(w, (2, 1, 0)), d[name + "__bias"]])  # [k, in, out]
        bn = L.BatchNormalization(epsilon=0.0010000000474974513); h = bn(h)
        bn.set_weights([d[name + "_BN__weight"], d[name + "_BN__bias"], d[name + "_BN__running_mean"], d[name + "_BN__running_var"]])
        h = L.MaxPool1D(2)(h)  # (2,1) havuz
    h = L.Flatten()(h)  # [N, 4*64]: torch permute(0,2,1,3) sırasıyla aynı (zaman önce, kanal sonra)
    fc = L.Dense(360, activation="sigmoid"); y = fc(h); fc.set_weights([d["classifier__weight"].T, d["classifier__bias"]])
    model = tf.keras.Model(i, y)
    yk = model.predict(d["x"], verbose=0); print("keras-torch en büyük fark:", float(np.abs(yk - d["y"]).max()))
    conv_ = tf.lite.TFLiteConverter.from_keras_model(model); tfl = conv_.convert(); open(out, "wb").write(tfl)  # float32 (2 MB): küçük model, hassasiyet korunur
    it = tf.lite.Interpreter(model_path=out); it.resize_tensor_input(0, [len(d["x"]), 1024]); it.allocate_tensors()
    it.set_tensor(it.get_input_details()[0]["index"], d["x"]); it.invoke(); yt = it.get_tensor(it.get_output_details()[0]["index"])
    print("tflite-torch en büyük fark:", float(np.abs(yt - d["y"]).max()), "argmax uyumu:", float((yt.argmax(1) == d["y"].argmax(1)).mean()), "boyut:", len(tfl))

if __name__ == "__main__":
    {"agirlik": lambda: agirlik(sys.argv[2]), "tflite": lambda: tflite(sys.argv[2], sys.argv[3])}[sys.argv[1]]()
