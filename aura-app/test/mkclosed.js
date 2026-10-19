const {chromium}=require('/opt/node22/lib/node_modules/playwright');const fs=require('fs');
(async()=>{const b=await chromium.launch({executablePath:'/opt/pw-browsers/chromium'});
const p=await b.newPage();await p.goto('http://localhost:8080/index.html');
const res=await p.evaluate(async(b64)=>{
 const img=new Image();img.src='data:image/jpeg;base64,'+b64;await img.decode();
 const W=640,H=480;
 const m=await import('./vendor/vision_bundle.mjs');
 const fs_={wasmLoaderPath:'./vendor/vision_wasm_internal.js',wasmBinaryPath:'./vendor/vision_wasm_internal.wasm'};
 const l=await m.FaceLandmarker.createFromOptions(fs_,{baseOptions:{modelAssetPath:'./vendor/face_landmarker.task'},runningMode:'IMAGE',numFaces:1});
 // 1) tam resimde yüzü bul, yüzü ortalayan 4:3 kadraj kes (webcam gibi)
 const full=l.detect(img).faceLandmarks[0];
 const xs=full.map(q=>q.x*img.naturalWidth),ys=full.map(q=>q.y*img.naturalHeight);
 const fx=(Math.min(...xs)+Math.max(...xs))/2,fy=(Math.min(...ys)+Math.max(...ys))/2,fw=Math.max(...xs)-Math.min(...xs);
 const cw=fw*2.2,ch=cw*0.75;
 const c=document.createElement('canvas');c.width=W;c.height=H;const x=c.getContext('2d',{willReadFrequently:true});
 x.drawImage(img,fx-cw/2,fy-ch/2,cw,ch,0,0,W,H);
 let lmA=null;
 const open=c.toDataURL('image/png');
 const im0=new Image();im0.src=open;await im0.decode();const r0=l.detect(im0);const lm=r0.faceLandmarks[0];
 const ear0=VisionCore.eyeAspect(lm);
 // gözleri kapat: göz konturunu üst göz kapağı rengiyle doldur, ortaya kirpik çizgisi çek
 const eyes=[[33,246,161,160,159,158,157,173,133,155,154,153,145,144,163,7],[362,398,384,385,386,387,388,466,263,249,390,373,374,380,381,382]];
 const P=i=>[lm[i].x*W,lm[i].y*H];
 for(const e of eyes){
  const pts=e.map(P);const cx=pts.reduce((a,q)=>a+q[0],0)/pts.length,cy=pts.reduce((a,q)=>a+q[1],0)/pts.length;
  const top=Math.min(...pts.map(q=>q[1]));const sx=Math.round(cx),sy=Math.round(top-10);
  const d=x.getImageData(sx-2,sy-2,5,5).data;let r=0,g=0,bb=0;for(let i=0;i<d.length;i+=4){r+=d[i];g+=d[i+1];bb+=d[i+2]}
  const n=d.length/4;const col=`rgb(${r/n|0},${g/n|0},${bb/n|0})`;
  x.fillStyle=col;x.beginPath();pts.forEach((q,i)=>i?x.lineTo(q[0],q[1]):x.moveTo(q[0],q[1]));x.closePath();x.fill();
  x.strokeStyle='rgba(40,25,20,.9)';x.lineWidth=2;x.beginPath();x.moveTo(pts[0][0],cy);x.lineTo(pts[8][0],cy);x.stroke();
 }
 const closed=c.toDataURL('image/png');
 const im2=new Image();im2.src=closed;await im2.decode();const r1=l.detect(im2);const ear1=r1.faceLandmarks[0]?VisionCore.eyeAspect(r1.faceLandmarks[0]):null;
 return{open,closed,ear0,ear1,dist:VisionCore.distanceM(lm,W,H)};
},fs.readFileSync(process.argv[2]).toString('base64'));
fs.writeFileSync(process.argv[3]+'/open.png',Buffer.from(res.open.split(',')[1],'base64'));
fs.writeFileSync(process.argv[3]+'/closed.png',Buffer.from(res.closed.split(',')[1],'base64'));
console.log(JSON.stringify({ear_open:res.ear0,ear_closed:res.ear1,dist_m:res.dist,thr_close:0.18,thr_open:0.22}));await b.close()})();
