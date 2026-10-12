"""Bulunan ismi duyurma: ekran + ses + dosya + (isteğe bağlı) Telegram / Discord."""
import os
import sys
import threading
import time

import requests

CLAIM_URL = "https://www.instagram.com/accounts/edit/"


class Notifier:
    def __init__(self, found_file: str = "bulunanlar.txt", bell: bool = True,
                 telegram_token: str = None, telegram_chat: str = None,
                 discord_webhook: str = None):
        self.found_file = found_file
        self.bell = bell
        self.tg_token = telegram_token or os.environ.get("TG_BOT_TOKEN")
        self.tg_chat = telegram_chat or os.environ.get("TG_CHAT_ID")
        self.discord = discord_webhook or os.environ.get("DISCORD_WEBHOOK")
        self.tg_base = os.environ.get("TG_API_BASE", "https://api.telegram.org")
        self.errors = []

    def found(self, name: str, detail: str = ""):
        text = f"✅ BULUNDU: @{name}  ({len(name)} harf)\nHemen al: {CLAIM_URL}  → Kullanıcı adı: {name}"
        banner = "=" * 56
        print(f"\n{banner}\n{text}\n{banner}\n", flush=True)
        if self.bell:
            sys.stdout.write("\a\a\a")
            sys.stdout.flush()
        try:
            with open(self.found_file, "a", encoding="utf-8") as f:
                f.write(f"{time.strftime('%Y-%m-%d %H:%M:%S')}\t{name}\t{detail}\n")
        except OSError as e:
            self.errors.append(f"dosya: {e}")
        # Ağ bildirimleri yavaş olabilir: ana akışı bekletmesin
        threading.Thread(target=self._remote, args=(text,), daemon=True).start()

    def _remote(self, text: str):
        if self.tg_token and self.tg_chat:
            try:
                requests.post(f"{self.tg_base}/bot{self.tg_token}/sendMessage",
                              json={"chat_id": self.tg_chat, "text": text}, timeout=10)
            except requests.RequestException as e:
                self.errors.append(f"telegram: {e}")
        if self.discord:
            try:
                requests.post(self.discord, json={"content": text}, timeout=10)
            except requests.RequestException as e:
                self.errors.append(f"discord: {e}")
