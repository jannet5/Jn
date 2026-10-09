// aura-sim/aura.py ile aynı mantık (JS portu)
const AURA=(()=>{
const EYE=0.5,MAX=2.5,BLINK_MIN=8,REST_EVERY=1200,REST_LEN=20,CLOSE=0.35;
const lensPower=d=>{if(d<=0)throw new Error('mesafe');return Math.max(0,Math.min(MAX,1/d-EYE))};
const tintLevel=(r,s)=>{const q=(s+1)/(r+1);return q<=3?0:Math.min(0.6,(q-3)/20)};
const blueCut=h=>(h>=6&&h<19)?0:(h>=19&&h<22)?(h-19)/3:1;
function step(s){
 const w=[],phase=s.t%REST_EVERY;
 const resting=phase>=REST_EVERY-REST_LEN&&s.t>=REST_EVERY-REST_LEN;
 let add=lensPower(s.distance_m);
 if(resting){const k=(phase-(REST_EVERY-REST_LEN))/REST_LEN;add*=Math.abs(1-2*k)}
 if(s.blinks_last_min<BLINK_MIN)w.push('Az göz kırpıyorsun');
 if(s.distance_m<CLOSE)w.push('Ekrana çok yakınsın');
 if(s.pwm_hz>0&&s.pwm_hz<1000)w.push(`Ekran titriyor (${Math.round(s.pwm_hz)} Hz)`);
 return{lens_add_d:add,tint:tintLevel(s.room_lux,s.screen_lux),blue_cut:blueCut(s.hour),resting,warnings:w};}
return{step,lensPower,tintLevel,blueCut}})();
if(typeof module!=='undefined')module.exports=AURA;
