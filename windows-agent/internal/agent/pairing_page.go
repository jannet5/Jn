package agent

import (
	"bytes"
	"encoding/base64"
	"html/template"
	"time"
)

// PairingPage is everything the customer-facing pairing page shows.
type PairingPage struct {
	Code        string
	Host        string
	Port        int
	Fingerprint string
	QRPNG       []byte
	ExpiresAt   time.Time
	OtherHosts  []string
	PlayURL     string
}

var pairingPageTmpl = template.Must(template.New("pair").Parse(`<!doctype html>
<html lang="tr"><head><meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>WinRemote Monitor - Telefonu eşleştir</title>
<style>
 body{font-family:"Segoe UI",Arial,sans-serif;background:#f3f5f9;color:#1d2433;margin:0}
 .wrap{max-width:860px;margin:32px auto;padding:0 20px;display:flex;gap:28px;flex-wrap:wrap;align-items:flex-start}
 .qr{background:#fff;border-radius:16px;padding:18px;box-shadow:0 2px 12px #0001;text-align:center;max-width:340px}
 .qr img{width:340px;height:340px;image-rendering:pixelated;display:block}
 .steps{flex:1;min-width:280px}
 h1{font-size:26px;margin:0 0 12px;color:#0f2a5c}
 ol{font-size:18px;line-height:1.6;padding-left:22px}
 .code{font-size:30px;font-weight:700;letter-spacing:2px;color:#0f2a5c}
 .timer{font-size:15px;color:#45506a;margin-top:6px}
 .expired{color:#b3261e;font-weight:700}
 details{margin-top:18px;font-size:14px;color:#45506a}
 code{background:#e7ebf3;padding:1px 5px;border-radius:4px;word-break:break-all}
 .tip{background:#fff7e8;border:1px solid #f1d49a;border-radius:10px;padding:10px 14px;font-size:15px;margin-top:14px}
</style></head><body>
<div class="wrap">
 <div class="qr">
  <img alt="Eşleştirme QR kodu" src="data:image/png;base64,{{.QRBase64}}">
  <div class="code">{{.Code}}</div>
  <div class="timer" id="timer"></div>
 </div>
 <div class="steps">
  <h1>Telefonunuzu bağlayın</h1>
  <ol>
   <li>Telefonda <b>WinRemote Monitor</b> uygulamasını açın{{if .PlayURL}} (yoksa <a href="{{.PlayURL}}">Google Play'den indirin</a>){{end}}.</li>
   <li><b>QR Tara</b>'ya dokunun ve telefonu bu ekrandaki koda tutun.</li>
   <li><b>Eşleştir</b>'e dokunun. Bu kadar.</li>
  </ol>
  <div class="tip">Telefon ve bu bilgisayar <b>aynı Wi-Fi ağında</b> olmalı.
   Kodun süresi dolarsa Başlat menüsünden <b>Telefonu Eşleştir</b>'i açın.</div>
  <details><summary>QR okunmuyorsa elle girin</summary>
   <p>Sunucu adresi: <code>{{.Host}}</code>{{if .OtherHosts}} (olmazsa: {{range $i, $h := .OtherHosts}}{{if $i}}, {{end}}<code>{{$h}}</code>{{end}}){{end}}<br>
   Port: <code>{{.Port}}</code><br>
   Eşleştirme kodu: <code>{{.Code}}</code><br>
   Sertifika parmak izi: <code>{{.Fingerprint}}</code></p>
  </details>
 </div>
</div>
<script>
 var end = {{.ExpiresUnixMs}};
 function tick(){
  var left = Math.round((end - Date.now())/1000), el = document.getElementById('timer');
  if (left <= 0) { el.className='timer expired'; el.textContent='Kodun süresi doldu. Başlat menüsünden "Telefonu Eşleştir"i yeniden açın.'; return; }
  el.textContent = 'Kod ' + Math.floor(left/60) + ':' + ('0'+left%60).slice(-2) + ' içinde geçerliliğini yitirir';
  setTimeout(tick, 1000);
 }
 tick();
</script>
</body></html>
`))

// RenderPairingPage returns a self-contained HTML page (QR embedded) that a
// non-technical user can follow to pair their phone.
func RenderPairingPage(p PairingPage) ([]byte, error) {
	var buf bytes.Buffer
	err := pairingPageTmpl.Execute(&buf, struct {
		PairingPage
		QRBase64      string
		ExpiresUnixMs int64
	}{p, base64.StdEncoding.EncodeToString(p.QRPNG), p.ExpiresAt.UnixMilli()})
	return buf.Bytes(), err
}
