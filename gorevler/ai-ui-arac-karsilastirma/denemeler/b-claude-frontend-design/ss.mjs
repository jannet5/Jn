import { chromium } from 'playwright';
const out = process.argv[2] || 'tur';
const b = await chromium.launch({proxy: process.env.HTTPS_PROXY?{server:process.env.HTTPS_PROXY}:undefined});
for (const [w,h] of [[1440,900],[390,844]]) {
  const p = await b.newPage({viewport:{width:w,height:h}, deviceScaleFactor:1, ignoreHTTPSErrors:true});
  const errs=[]; p.on('pageerror',e=>errs.push(e.message)); p.on('console',m=>m.type()==='error'&&errs.push(m.text()));
  for (let k=0;k<4;k++){ await p.goto('file://'+process.cwd()+'/dist/index.html'); await p.evaluate(()=>document.fonts.ready); await p.waitForTimeout(1500);
    const n = await p.evaluate(()=>[...document.fonts].filter(f=>f.status==='loaded').length); if(n>=6) break; }
  const fonts = await p.evaluate(()=>[...document.fonts].filter(f=>f.status==='loaded').map(f=>f.family+f.weight));
  const ov = await p.evaluate(()=>document.documentElement.scrollWidth);
  console.log(w, 'fonts', [...new Set(fonts)].join(','), 'scrollW', ov, errs);
  await p.screenshot({path:`${out}-${w}.png`, fullPage:true});
  if (w===390){ await p.click('.menu-dugme'); await p.screenshot({path:`${out}-${w}-menu.png`}); }
  else { for(let i=0;i<8;i++) await p.keyboard.press('Tab'); await p.keyboard.press('ArrowLeft'); await p.keyboard.press('ArrowLeft'); await p.screenshot({path:`${out}-${w}-focus.png`});
    await p.click('label[for=r-30]'); await p.waitForTimeout(700); await p.screenshot({path:`${out}-${w}-30.png`}); }
}
await b.close();
