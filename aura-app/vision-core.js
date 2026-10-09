// Kamera ölçümünün saf mantığı (DOM yok, düğüm/tarayıcıda aynı çalışır)
const VisionCore=(()=>{
const EAR_CLOSE=0.18,EAR_OPEN=0.22,IPD_M=0.063,FOCAL_RATIO=0.785; // f ≈ 0.785 × görüntü genişliği (≈65° yatay görüş)
const dist=(a,b)=>Math.hypot(a.x-b.x,a.y-b.y);
// Göz açıklık oranı: (dikey1+dikey2)/(2*yatay). p=[dış,iç,üst1,alt1,üst2,alt2]
function ear(p){return (dist(p[2],p[3])+dist(p[4],p[5]))/(2*dist(p[0],p[1]))}
const L=[33,133,160,144,158,153], R=[362,263,385,380,387,373];
function eyeAspect(lm){const g=ix=>ix.map(i=>lm[i]);return (ear(g(L))+ear(g(R)))/2}
// Ekran mesafesi (m): gözbebekleri arası piksel → metre
function distanceM(lm,w,h){const a=lm[468],b=lm[473];if(!a||!b)return null;
 const ipdPx=Math.hypot((a.x-b.x)*w,(a.y-b.y)*h);if(ipdPx<1)return null;return FOCAL_RATIO*w*IPD_M/ipdPx}
// Kırpma sayacı: histerezisli; en az 2 kare kapalı kalmalı (gürültü değil)
class BlinkCounter{
 constructor(){this.closed=false;this.n=0;this.times=[];this.t0=null}
 push(earV,tSec){
  if(this.t0===null)this.t0=tSec;
  if(!this.closed&&earV<EAR_CLOSE){this.closed=true;this.cs=tSec}
  else if(this.closed&&earV>EAR_OPEN){this.closed=false;if(tSec-this.cs<1.0&&tSec-this.cs>0.03)this.times.push(tSec)}
  this.times=this.times.filter(x=>tSec-x<=60)}
 perMinute(tSec){const win=Math.min(60,Math.max(15,tSec-this.t0));return Math.round(this.times.length*60/win)}
}
return{ear,eyeAspect,distanceM,BlinkCounter,EAR_CLOSE,EAR_OPEN};})();
if(typeof module!=='undefined')module.exports=VisionCore;
