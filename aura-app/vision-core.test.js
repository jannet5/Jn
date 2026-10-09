const V=require('./vision-core.js');const assert=require('assert');
let ok=0;const t=(n,f)=>{f();ok++;console.log('✓',n)};
const eye=(open)=>[{x:0,y:0},{x:1,y:0},{x:.3,y:open/2},{x:.3,y:-open/2},{x:.7,y:open/2},{x:.7,y:-open/2}];
t('EAR açık göz ≈0.3',()=>assert.ok(Math.abs(V.ear(eye(.3))-.3)<1e-9));
t('EAR kapalı göz ≈0.05',()=>assert.ok(V.ear(eye(.05))<.18));
t('mesafe: IPD 63mm, 640px genişlikte 100px → ~0.32m',()=>{
 const lm=[];lm[468]={x:.4,y:.5};lm[473]={x:.4+100/640,y:.5};
 const d=V.distanceM(lm,640,480);assert.ok(Math.abs(d-0.785*640*0.063/100)<1e-9);assert.ok(d>.3&&d<.35)});
t('mesafe yok → null',()=>assert.strictEqual(V.distanceM([],640,480),null));
function run(seq,fps=30){const c=new V.BlinkCounter();seq.forEach((e,i)=>c.push(e,i/fps));return c}
const open=Array(30).fill(.3),blink=[.1,.05,.1];
t('1 kırpma sayılır',()=>assert.strictEqual(run([...open,...blink,...open]).times.length,1));
t('3 kırpma sayılır',()=>assert.strictEqual(run([...open,...blink,...open,...blink,...open,...blink,...open]).times.length,3));
t('uzun kapalı (>1sn, uyku) kırpma değil',()=>assert.strictEqual(run([...open,...Array(40).fill(.05),...open]).times.length,0));
t('açık-gürültü (0.19 civarı) sayılmaz',()=>assert.strictEqual(run([...open,.19,.2,.19,.3,...open]).times.length,0));
t('dakikada hız: 15sn’de 5 kırpma → 20/dk',()=>{
 const c=new V.BlinkCounter(),fps=30;let tt=0;
 for(let b=0;b<5;b++){for(let i=0;i<60;i++)c.push(.3,tt++/fps);for(const e of blink)c.push(e,tt++/fps)}
 for(let i=0;i<30;i++)c.push(.3,tt++/fps);
 const pm=c.perMinute(tt/fps);assert.ok(pm>=18&&pm<=22,pm)});
console.log(ok,'test geçti');
