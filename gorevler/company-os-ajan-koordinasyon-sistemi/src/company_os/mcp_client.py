"""Bağımlılıksız, asgari MCP stdio istemcisi (JSON-RPC 2.0, satır ayrımlı).

Orkestratörün ``mcp`` yürütücüsü bununla herhangi bir stdio MCP sunucusundaki
aracı çağırıp sonucu artifact olarak geri getirir.
"""
from __future__ import annotations

import json
import subprocess
import threading
import queue

CLIENT_PROTOCOL = "2025-06-18"


class MCPError(RuntimeError):
    pass


class StdioMCPClient:
    def __init__(self, argv: list[str], *, cwd: str | None = None, env: dict | None = None,
                 timeout: float = 60):
        self.timeout = timeout
        self.proc = subprocess.Popen(argv, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                     stderr=subprocess.PIPE, cwd=cwd, env=env, text=True,
                                     encoding="utf-8", bufsize=1)
        self._id = 0
        self._q: "queue.Queue[dict | None]" = queue.Queue()
        threading.Thread(target=self._reader, daemon=True).start()
        self.server_info: dict = {}

    def _reader(self):
        for line in self.proc.stdout:
            line = line.strip()
            if not line:
                continue
            try:
                self._q.put(json.loads(line))
            except json.JSONDecodeError:
                continue
        self._q.put(None)

    def _send(self, msg: dict):
        self.proc.stdin.write(json.dumps(msg, ensure_ascii=False) + "\n")
        self.proc.stdin.flush()

    def request(self, method: str, params: dict | None = None) -> dict:
        self._id += 1
        rid = self._id
        self._send({"jsonrpc": "2.0", "id": rid, "method": method, "params": params or {}})
        while True:
            try:
                msg = self._q.get(timeout=self.timeout)
            except queue.Empty as e:
                raise MCPError(f"{method}: zaman aşımı") from e
            if msg is None:
                err = self.proc.stderr.read() if self.proc.stderr else ""
                raise MCPError(f"sunucu kapandı: {err[-2000:]}")
            if msg.get("id") != rid:
                continue  # bildirim ya da sunucu isteği; asgari istemci yok sayar
            if "error" in msg:
                raise MCPError(f"{method}: {msg['error']}")
            return msg.get("result", {})

    def initialize(self) -> dict:
        res = self.request("initialize", {
            "protocolVersion": CLIENT_PROTOCOL,
            "capabilities": {},
            "clientInfo": {"name": "company-os", "version": "1.0.0"},
        })
        self.server_info = res
        self._send({"jsonrpc": "2.0", "method": "notifications/initialized"})
        return res

    def list_tools(self) -> list[dict]:
        tools, cursor = [], None
        while True:
            res = self.request("tools/list", {"cursor": cursor} if cursor else {})
            tools += res.get("tools", [])
            cursor = res.get("nextCursor")
            if not cursor:
                return tools

    def call_tool(self, name: str, arguments: dict | None = None) -> dict:
        return self.request("tools/call", {"name": name, "arguments": arguments or {}})

    def close(self):
        try:
            self.proc.stdin.close()
        except OSError:
            pass
        try:
            self.proc.wait(timeout=5)
        except subprocess.TimeoutExpired:
            self.proc.kill()

    def __enter__(self):
        self.initialize()
        return self

    def __exit__(self, *exc):
        self.close()
