"""Kullanıcı adı kontrolcüleri.

Her kontrolcü bir isim alır, şu sonuçlardan birini döndürür:
  FREE    -> bu isimde hesap yok (alınabilir adayı)
  TAKEN   -> hesap var
  LIMITED -> Instagram yavaşla dedi (429/giriş duvarı); motor otomatik bekler
  ERROR   -> ağ/beklenmeyen cevap; sonra tekrar denenir
"""
import random
import threading
import time
from dataclasses import dataclass
from typing import Dict, Optional

import requests

FREE, TAKEN, LIMITED, ERROR = "free", "taken", "limited", "error"


@dataclass
class Result:
    status: str
    detail: str = ""
    retry_after: Optional[float] = None


class Checker:
    name = "base"

    def check(self, username: str) -> Result:  # pragma: no cover
        raise NotImplementedError


USER_AGENTS = [
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Safari/605.1.15",
    "Mozilla/5.0 (X11; Linux x86_64; rv:125.0) Gecko/20100101 Firefox/125.0",
]


class InstagramProfileChecker(Checker):
    """Tarayıcının profil açarken yaptığı herkese açık sorgu (web_profile_info).

    404 -> hesap yok. 200 + kullanıcı verisi -> hesap var. 429/401/yönlendirme -> yavaşla.
    NOT: 'hesap yok' = 'kesin alınabilir' demek değildir (Instagram yeni silinen/yasaklı isimleri
    bir süre tutabilir). Bu yüzden FREE sadece ADAY'dır; son kararı Instagram'daki isim değiştirme
    ekranı verir.
    """
    name = "instagram"
    URL = "https://www.instagram.com/api/v1/users/web_profile_info/"
    APP_ID = "936619743392459"

    HOME = "https://www.instagram.com/"

    def __init__(self, timeout: float = 15.0, url: Optional[str] = None, home: Optional[str] = HOME):
        """url/home yalnızca test içindir (yerel sahte sunucu). home=None: çerez ısınması yapma."""
        self.timeout = timeout
        self.url = url or self.URL
        self.home = home
        self.local = threading.local()

    def _session(self) -> requests.Session:
        s = getattr(self.local, "s", None)
        if s is None:
            s = requests.Session()
            s.headers.update({
                "User-Agent": random.choice(USER_AGENTS),
                "Accept-Language": "en-US,en;q=0.9",
                "Accept": "*/*",
            })
            if self.home:
                try:  # çerez ısınması (tarayıcı gibi önce ana sayfa)
                    s.get(self.home, timeout=self.timeout)
                except requests.RequestException:
                    pass
            self.local.s = s
        return s

    def _get(self, username: str) -> requests.Response:
        s = self._session()
        headers = {
            "X-IG-App-ID": self.APP_ID,
            "X-Requested-With": "XMLHttpRequest",
            "Referer": f"https://www.instagram.com/{username}/",
        }
        csrf = s.cookies.get("csrftoken")
        if csrf:
            headers["X-CSRFToken"] = csrf
        return s.get(self.url, params={"username": username}, headers=headers,
                     timeout=self.timeout, allow_redirects=False)

    def check(self, username: str) -> Result:
        try:
            r = self._get(username)
        except requests.RequestException as e:
            return Result(ERROR, f"ağ hatası: {type(e).__name__}")
        return self.interpret(r.status_code, r.headers, r.text)

    def raw(self, username: str) -> dict:
        """Tanı için: Instagram'ın ham cevabı ve aracın bunu nasıl yorumladığı (1 istek)."""
        try:
            r = self._get(username)
        except requests.RequestException as e:
            return {"error": f"{type(e).__name__}: {e}"}
        res = self.interpret(r.status_code, r.headers, r.text)
        keep = ("content-type", "retry-after", "location", "x-ig-origin-region", "server", "date")
        return {"http": r.status_code, "headers": {k: v for k, v in r.headers.items() if k.lower() in keep},
                "body_start": r.text[:400], "decision": res.status, "decision_detail": res.detail}

    @staticmethod
    def interpret(code: int, headers, body: str) -> Result:
        if code == 404:
            return Result(FREE, "404 hesap yok")
        if code == 200:
            try:
                import json
                data = json.loads(body)
            except ValueError:
                return Result(LIMITED, "200 ama JSON değil (giriş duvarı?)")
            user = (data.get("data") or {}).get("user")
            if user:
                return Result(TAKEN, "hesap var")
            if data.get("status") == "ok":
                return Result(FREE, "200 ama kullanıcı boş")
            return Result(ERROR, "beklenmeyen JSON")
        if code in (429, 401, 403, 302, 301):
            ra = None
            try:
                ra = float(headers.get("Retry-After")) if headers.get("Retry-After") else None
            except (TypeError, ValueError):
                pass
            return Result(LIMITED, f"HTTP {code}", retry_after=ra)
        return Result(ERROR, f"HTTP {code}")


class MockChecker(Checker):
    """Sahte Instagram: test ve demo için. Gerçek ağ kullanmaz.

    taken: dolu isimler. release_at: {isim: saniye} -> o saniyeden sonra isim 'boşalır'.
    limit_every: her N. istekte 429 simüle eder (motorun yavaşlamasını test etmek için).
    """
    name = "mock"

    def __init__(self, taken=(), release_at: Optional[Dict[str, float]] = None,
                 limit_every: int = 0, flaky: Optional[set] = None):
        self.taken = set(taken)
        self.release_at = dict(release_at or {})
        self.limit_every = limit_every
        self.flaky = set(flaky or ())  # bir kez FREE, sonra TAKEN görünen isimler
        self._flaky_seen = set()
        self.t0 = time.monotonic()
        self.calls = 0
        self.lock = threading.Lock()

    def check(self, username: str) -> Result:
        with self.lock:
            self.calls += 1
            n = self.calls
            elapsed = time.monotonic() - self.t0
        if self.limit_every and n % self.limit_every == 0:
            return Result(LIMITED, "simüle 429", retry_after=0.2)
        if username in self.flaky:
            with self.lock:
                if username not in self._flaky_seen:
                    self._flaky_seen.add(username)
                    return Result(FREE, "geçici boş görünüm")
            return Result(TAKEN, "meğer doluymuş")
        if username in self.release_at and elapsed >= self.release_at[username]:
            return Result(FREE, "serbest kaldı")
        return Result(TAKEN, "dolu") if username in self.taken else Result(FREE, "boş")
