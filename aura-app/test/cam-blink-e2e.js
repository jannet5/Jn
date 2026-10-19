const {chromium}=require('/opt/node22/lib/node_modules/playwright');
(async()=>{
 const b=await chromium.launch({executablePath:'/opt/pw-browsers/chromium',args:['--use-fake-ui-for-media-stream','--use-fake-device-for-media-stream','--use-file-for-fake-video-capture='+process.argv[2]]});
 const ctx=await b.newContext({viewport:{width:390,height:844},permissions:['camera']});
 const p=await ctx.newPage();const errs=[];p.on('pageerror',e=>errs.push(e.message));
 await p.goto('http://localhost:8080/index.html');
 await p.click('#cam');
 await p.waitForFunction(()=>document.getElementById('camst').textContent.startsWith('Canlı'),null,{timeout:60000});
 const seen=[];
 for(let i=0;i<12;i++){await p.waitForTimeout(1500);seen.push(await p.textContent('#blinkv'))}
 console.log('KIRPMA/dk zaman içinde:',seen.join(' '));
 console.log('DURUM',await p.textContent('#camst'));
 console.log('UYARI',(await p.textContent('#warns')).trim());
 console.log('HATA',JSON.stringify(errs));await b.close();})();
