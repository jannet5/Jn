"""Resmi MCP Python SDK istemcisi (pip: mcp) ile Company OS sunucusunun birlikte çalışma testi.

Kullanım: <mcp kurulu python> kabul/sdk_birlikte_calisma.py
"""
import asyncio
import json
import os
import sys
import tempfile

from mcp import ClientSession, StdioServerParameters
from mcp.client.stdio import stdio_client

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


async def main():
    d = tempfile.mkdtemp()
    params = StdioServerParameters(
        command=sys.executable,
        args=["-m", "company_os", "mcp-serve", "--db", os.path.join(d, "l.db"),
              "--skills", os.path.join(ROOT, "ornekler", "skills")],
        env={**os.environ, "PYTHONPATH": os.path.join(ROOT, "src")},
    )
    async with stdio_client(params) as (r, w):
        async with ClientSession(r, w) as s:
            init = await s.initialize()
            print("protocolVersion:", init.protocolVersion, "server:", init.serverInfo.name)
            tools = await s.list_tools()
            print("araç sayısı:", len(tools.tools))
            await s.call_tool("plan_submit", {"plan": {"tasks": [{"id": "t", "title": "t", "role": "qa"}]}})
            c = (await s.call_tool("task_claim", {"worker": "sdk"})).structuredContent
            tok = c["lease"]["token"]
            await s.call_tool("artifact_write", {"task_id": "t", "token": tok, "path": "a.txt", "content": "x"})
            ev = (await s.call_tool("evidence_run", {"task_id": "t", "token": tok,
                  "argv": [sys.executable, "-c", "open('a.txt').read()"], "refs": ["a.txt"]})).structuredContent
            done = (await s.call_tool("task_complete", {"task_id": "t", "token": tok})).structuredContent
            stale = await s.call_tool("artifact_write", {"task_id": "t", "token": tok, "path": "b", "content": "y"})
            print("kanıt çıkış:", ev["exit_code"], "tamamlandı:", done["succeeded"],
                  "eski token reddi:", stale.isError)
            assert ev["exit_code"] == 0 and done["succeeded"] and stale.isError
            print("SDK BİRLİKTE ÇALIŞMA: GEÇTİ")


asyncio.run(main())
