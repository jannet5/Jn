"""Ölçüm: DNSMOS P.835 (SIG/BAK/OVRL, referanssız), STOI ve SI-SDR (temiz referansa göre),
EBU R128 tümleşik ses yüksekliği (LUFS), gerçek tepe, ilk 3 sn oda sesinin gürültü tabanı."""
import sys, json, subprocess, numpy as np, soundfile as sf, onnxruntime as ort
from scipy import signal
from pystoi import stoi
SESS=ort.InferenceSession('sig_bak_ovr.onnx')
PO=np.poly1d([-0.06766283,1.11546468,0.04602535]);PS=np.poly1d([-0.08397278,1.22083953,0.0052439]);PB=np.poly1d([-0.13166888,1.60915514,-0.39604546])
def dnsmos(x16):
    L=int(9.01*16000); a=x16
    while len(a)<L: a=np.append(a,a)
    hops=int(np.floor(len(a)/16000)-9.01)+1; r=[]
    for i in range(hops):
        seg=a[i*16000:i*16000+L]
        if len(seg)<L: continue
        s,b,o=SESS.run(None,{'input_1':seg.astype('float32')[None,:]})[0][0]; r.append((PS(s),PB(b),PO(o)))
    return np.mean(r,axis=0)
def r128(path):
    out=subprocess.run(['ffmpeg','-hide_banner','-nostats','-i',path,'-af','ebur128=peak=true','-f','null','-'],capture_output=True,text=True).stderr
    tail=out[out.rfind('Summary:'):]
    I=float(tail.split('I:')[1].split('LUFS')[0]); TP=float(tail.split('Peak:')[1].split('dBFS')[0]); return I,TP
def align(ref,x):
    n=min(len(ref),len(x)); c=signal.correlate(x[:n],ref[:n],'full',method='fft'); lag=np.argmax(np.abs(c))-(n-1)
    lag=int(np.clip(lag,-4800,4800))
    if lag>0: x=x[lag:]
    elif lag<0: x=np.concatenate([np.zeros(-lag),x])
    n=min(len(ref),len(x)); return ref[:n],x[:n],lag
def sisdr(ref,x):
    a=np.dot(x,ref)/np.dot(ref,ref); t=a*ref; e=x-t; return 10*np.log10(np.sum(t**2)/np.sum(e**2))
def measure(path, ref_path=None):
    x,sr=sf.read(path)
    if x.ndim>1: x=x.mean(1)
    x16=signal.resample_poly(x,16000,sr) if sr!=16000 else x
    sp=x16[3*16000:]  # oda sesi hariç konuşma bölümü
    sp=sp*(10**(-25/20)/(np.sqrt(np.mean(sp**2))+1e-12))  # seviye etkisini kaldır: DNSMOS girişi -25 dBFS RMS'e ölçeklenir
    sig,bak,ovr=dnsmos(sp)
    nf=20*np.log10(np.sqrt(np.mean(x[int(0.5*sr):int(2.5*sr)]**2))+1e-12)
    I,TP=r128(path); d=dict(dosya=path.split('/')[-1],SIG=sig,BAK=bak,OVRL=ovr,LUFS=I,TP=TP,gurultu_tabani_dBFS=nf)
    if ref_path:
        ref,rsr=sf.read(ref_path); r16=signal.resample_poly(ref,16000,rsr)
        r,y,lag=align(r16,x16); d['STOI']=stoi(r,y,16000,extended=False); d['SI_SDR']=sisdr(r,y); d['gecikme_ms']=lag/16
    return d
if __name__=='__main__':
    ref=sys.argv[2] if len(sys.argv)>2 else None
    print(json.dumps({k:(round(v,3) if isinstance(v,float) else v) for k,v in measure(sys.argv[1],ref).items()},ensure_ascii=False))
