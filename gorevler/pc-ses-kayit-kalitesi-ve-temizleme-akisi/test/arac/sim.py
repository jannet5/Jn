"""Kötü/dahili PC mikrofonu benzetimi (sentetik test girdisi üretir).
Temiz konuşma: Microsoft DNS-Challenge test seti (clean_fileid_*.wav, 16 kHz).
Her 'kayıt' = 3 sn oda sesi + 3 cümle; yankı, fan/uğultu gürültüsü, dar bant mikrofon tepkisi, düşük seviye."""
import numpy as np, soundfile as sf
from scipy import signal
SR=48000; rng=np.random.default_rng(42)
def load(i):
    x,sr=sf.read(f'clean_{i}.wav'); return signal.resample_poly(x,3,1)  # 16k->48k
def room_ir(rt60, sr=SR, drr_db=0):
    n=int(rt60*sr); t=np.arange(n)/sr
    tail=rng.standard_normal(n)*np.exp(-6.9*t/rt60)
    tail[:int(0.004*sr)]=0  # ilk yansıma gecikmesi
    b,a=signal.butter(2,4000/(sr/2)); tail=signal.lfilter(b,a,tail)
    tail/=np.sqrt(np.sum(tail**2)); ir=tail*10**(-drr_db/20); ir[0]+=1.0; return ir
def colored_noise(n, slope):  # slope: 1=pembe, 2=kahverengi
    f=np.fft.rfftfreq(n,1/SR); s=rng.standard_normal(len(f))+1j*rng.standard_normal(len(f))
    s[1:]/=f[1:]**(slope/2); s[0]=0; x=np.fft.irfft(s,n); return x/np.std(x)
def take(ids, rt60, drr, snr_db, hum_db, level_db, name):
    sp=[load(i) for i in ids]; gap=np.zeros(int(0.7*SR))
    speech=np.concatenate([np.zeros(3*SR)]+[np.concatenate([s,gap]) for s in sp])
    clean=speech.copy()
    rev=signal.fftconvolve(speech,room_ir(rt60,drr_db=drr))[:len(speech)]
    # mikrofon tepkisi: 150 Hz alt, 7 kHz üst kesim + 2.5 kHz tepe (ucuz kapsül)
    b,a=signal.butter(2,[150/(SR/2),7000/(SR/2)],'band'); m=signal.lfilter(b,a,rev)
    b2,a2=signal.iirpeak(2500/(SR/2),2.0); m=m+0.5*signal.lfilter(b2,a2,m)
    act=np.abs(clean)>0.01*np.max(np.abs(clean)); p_sp=np.mean(m[act]**2)
    n=len(m); noise=0.7*colored_noise(n,1)+0.3*colored_noise(n,2)
    noise+= 0.4*rng.standard_normal(n)  # elektronik hışırtı
    noise=noise/np.sqrt(np.mean(noise**2))*np.sqrt(p_sp/10**(snr_db/10))
    t=np.arange(n)/SR; hum=sum(np.sin(2*np.pi*50*k*t)/k for k in (1,2,3))
    hum=hum/np.sqrt(np.mean(hum**2))*np.sqrt(p_sp/10**(-hum_db/10)) if hum_db is not None else 0
    y=m+noise+hum
    y=y/np.max(np.abs(y))*10**(level_db/20)
    sf.write(f'work/{name}_ham.wav',y.astype(np.float32),SR,subtype='PCM_24')
    sf.write(f'work/{name}_referans_temiz.wav',(clean/np.max(np.abs(clean))*0.5).astype(np.float32),SR,subtype='PCM_24')
    print(name,'len',n/SR)
take([0,2,3],rt60=0.35,drr=4,snr_db=15,hum_db=None,level_db=-14,name='kayit1')
take([4,5,6],rt60=0.5,drr=1,snr_db=10,hum_db=-26,level_db=-20,name='kayit2')
