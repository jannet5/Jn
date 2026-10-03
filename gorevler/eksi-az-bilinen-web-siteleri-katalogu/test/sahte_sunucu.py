"""Ekşi başlık sayfası yapısını taklit eden yerel test sunucusu (içerik tamamen sahte)."""
import http.server, sys, urllib.parse

TOPLAM = 5
ENTRYLER = {
    1: ['<a rel="nofollow" class="url" href="https://eski-site.example/">https://eski-site.example/</a>'],
    2: ['düz metinle yazılmış: ornekaraclar.com ve (bkz: <a class="b" href="/?q=x">x</a>).'],
    3: ['<a class="url" href="http://www.tekrar.example.com/?utm_source=eksi">link</a>',
        'görsel: <a class="url" href="https://i.imgur.com/abc.png">resim</a>'],
    4: ['<a class="url" href="https://github.com/kisi/proje">github</a> ve '
        '<a class="url" href="https://github.com/kisi/baska">ikinci</a>',
        'aynı site tekrar: <a class="url" href="https://tekrar.example.com/alt/sayfa">alt</a>'],
    5: ['son sayfa: <a class="url" href="https://yeni-site.example/araç?ref=eksi">yeni</a> nokta ile biten metin.dev.'],
}

def sayfa_html(p):
    lis = ''.join(
        f'<li data-id="{p*100+i}" id="entry-item"><div class="content">{c}</div></li>'
        for i, c in enumerate(ENTRYLER.get(p, [])))
    return (f'<html><body><h1>test başlığı</h1><div class="pager" data-currentpage="{p}" '
            f'data-pagecount="{TOPLAM}"></div><ul id="entry-item-list">{lis}</ul></body></html>')

class H(http.server.BaseHTTPRequestHandler):
    def do_GET(self):
        u = urllib.parse.urlsplit(self.path)
        if not u.path.startswith('/test-basligi--123'):
            self.send_error(404); return
        p = int(urllib.parse.parse_qs(u.query).get('p', ['1'])[0])
        b = sayfa_html(p).encode()
        self.send_response(200); self.send_header('Content-Type', 'text/html; charset=utf-8')
        self.send_header('Content-Length', str(len(b))); self.end_headers(); self.wfile.write(b)
    def log_message(self, *a): pass

if __name__ == '__main__':
    http.server.ThreadingHTTPServer(('127.0.0.1', int(sys.argv[1])), H).serve_forever()
