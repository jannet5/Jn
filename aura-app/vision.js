// Kamera + MediaPipe FaceLandmarker. Görüntü cihazdan ÇIKMAZ, sadece sayılar işlenir.
const AuraVision=(()=>{
let lmk=null;
async function load(){
 if(lmk)return lmk;
 const m=await import('./vendor/vision_bundle.mjs');
 const fs={wasmLoaderPath:'./vendor/vision_wasm_internal.js',wasmBinaryPath:'./vendor/vision_wasm_internal.wasm'};
 lmk=await m.FaceLandmarker.createFromOptions(fs,{baseOptions:{modelAssetPath:'./vendor/face_landmarker.task'},runningMode:'VIDEO',numFaces:1});
 return lmk;}
function measure(res,w,h){
 const lm=res.faceLandmarks&&res.faceLandmarks[0];if(!lm)return null;
 return{ear:VisionCore.eyeAspect(lm),distance_m:VisionCore.distanceM(lm,w,h)};}
let stream=null,raf=0,counter=null,onData=null,video=null,lastTs=-1;
async function start(cb){
 onData=cb;counter=new VisionCore.BlinkCounter();
 const l=await load();
 stream=await navigator.mediaDevices.getUserMedia({video:{facingMode:'user',width:640,height:480},audio:false});
 video=document.createElement('video');video.playsInline=true;video.muted=true;video.srcObject=stream;await video.play();
 const loop=()=>{
  if(!stream)return;
  const ts=performance.now();
  if(video.readyState>=2&&ts!==lastTs){lastTs=ts;
   const m=measure(l.detectForVideo(video,ts),video.videoWidth,video.videoHeight);
   if(m){counter.push(m.ear,ts/1000);onData({face:true,ear:m.ear,distance_m:m.distance_m,blinks_per_min:counter.perMinute(ts/1000)})}
   else onData({face:false})}
  raf=requestAnimationFrame(loop)};loop();}
function stop(){cancelAnimationFrame(raf);if(stream)stream.getTracks().forEach(t=>t.stop());stream=null}
// Test için: tek bir resmi analiz et
async function analyzeImage(img){const l=await load();await l.setOptions({runningMode:'IMAGE'});
 const r=l.detect(img);await l.setOptions({runningMode:'VIDEO'});return measure(r,img.naturalWidth,img.naturalHeight)}
return{start,stop,analyzeImage};})();
