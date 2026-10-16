import sys, time, os, traceback, json
os.environ["OMP_NUM_THREADS"]="2"
import numpy as np, torch
torch.set_num_threads(2)
sys.path.insert(0, "/home/user/Jn/melodi-zil/arastirma/ortak")
sr=16000; dur=5.0; t=np.arange(int(sr*dur))/sr
f_inst = 220*2**(np.where(t<2.5, 0, 7)/12)*(1+0.01*np.sin(2*np.pi*5*t))
ph = 2*np.pi*np.cumsum(f_inst)/sr
y = sum(np.sin(k*ph)/k for k in range(1,6)).astype(np.float32); y/=np.abs(y).max(); y*=0.5
y += (0.01*np.random.RandomState(0).randn(len(y))).astype(np.float32)
import soundfile as sf
sf.write("/tmp/melodi_duman/sine.wav", y, sr)
res={}
def run(name, fn):
    t0=time.time()
    try:
        out=fn(); res[name]=(True, round(time.time()-t0,2), out)
    except Exception as e:
        res[name]=(False, round(time.time()-t0,2), repr(e)[:300]); traceback.print_exc()
    print(name, res[name], flush=True)
def med_hz(f0):
    f0=np.asarray(f0).ravel(); v=f0[f0>0]
    return (len(f0), float(np.median(v[:len(v)//3])) if len(v) else None, float(np.median(v[-len(v)//3:])) if len(v) else None)
def rmvpe_pt():
    from rmvpe_model import RMVPE
    m=RMVPE("/home/user/models/rmvpe/rmvpe.pt"); return med_hz(m.infer_from_audio(y))
def rmvpe_onnx():
    from rmvpe_model import RMVPE
    m=RMVPE("/home/user/models/rmvpe/rmvpe.onnx")
    return (m.onnx_in, m.onnx_out, med_hz(m.infer_from_audio(y)))
def crepe():
    import torchcrepe
    a=torch.from_numpy(y)[None]
    p=torchcrepe.predict(a, sr, 160, 50, 1000, model="tiny", batch_size=512, device="cpu", return_periodicity=False)
    return med_hz(p.numpy())
def fcpe():
    from torchfcpe import spawn_bundled_infer_model
    m=spawn_bundled_infer_model(device="cpu")
    a=torch.from_numpy(y)[None,:,None]
    f0=m.infer(a, sr=sr, decoder_mode="local_argmax", threshold=0.006)
    return med_hz(f0.numpy())
def bp():
    from basic_pitch.inference import predict, Model
    from basic_pitch import ICASSP_2022_MODEL_PATH
    mo, md, notes = predict("/tmp/melodi_duman/sine.wav", Model(ICASSP_2022_MODEL_PATH))
    return (str(ICASSP_2022_MODEL_PATH), [(round(n[0],2),round(n[1],2),n[2]) for n in notes][:6])
def demucs_t():
    from demucs.pretrained import get_model
    from demucs.apply import apply_model
    m=get_model("htdemucs"); m.eval()
    x=torch.from_numpy(np.stack([y,y]).astype(np.float32))[None]
    # htdemucs 44.1k: 5 sn'lik 16k sesi 44.1k'ya tekrar ornekle
    import torchaudio
    x=torchaudio.functional.resample(x, 16000, 44100)
    with torch.no_grad(): o=apply_model(m, x, device="cpu", num_workers=0, split=True, segment=None)
    return (m.sources, tuple(o.shape))
def ytdlp():
    import subprocess
    v=subprocess.run(["/home/user/venv-ml/bin/yt-dlp","--version"],capture_output=True,text=True).stdout.strip()
    r=subprocess.run(["/home/user/venv-ml/bin/yt-dlp","--simulate","--print","%(title)s|%(duration)s","https://www.youtube.com/watch?v=jNQXAC9IVRw"],capture_output=True,text=True,timeout=60)
    return (v, r.stdout.strip()[:100], r.stderr.strip()[-200:])
def libs():
    import librosa, mir_eval, pretty_midi, scipy, onnx, onnxruntime
    p=librosa.yin(y, fmin=80, fmax=800, sr=sr)
    return dict(librosa=librosa.__version__, mir_eval=mir_eval.__version__, pretty_midi=pretty_midi.__version__, onnx=onnx.__version__, ort=onnxruntime.__version__, torch=torch.__version__, yin_med=float(np.median(p)))
which=sys.argv[1:] or ["libs","rmvpe_pt","rmvpe_onnx","crepe","fcpe","bp","demucs","ytdlp"]
fns=dict(libs=libs,rmvpe_pt=rmvpe_pt,rmvpe_onnx=rmvpe_onnx,crepe=crepe,fcpe=fcpe,bp=bp,demucs=demucs_t,ytdlp=ytdlp)
for w in which: run(w, fns[w])
