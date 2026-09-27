# WinRemoteMonitor Protocol Specification v1

This document is the single source of truth for both implementations:
- Windows agent: `windows-agent/` (Go)
- Android app: `android-app/` (Kotlin)

Both sides MUST implement exactly this protocol. If either side needs to
deviate, update this file first.

## 1. Transport

- The agent listens on TCP with **TLS 1.2+**, serving a WebSocket endpoint at
  `wss://<host>:<port>/ws`. Default port: `8787`.
- The TLS certificate is **self-signed**, generated once on first run and
  persisted at `%ProgramData%\WinRemoteMonitor\agent-cert.pem` /
  `agent-key.pem`. Validity: 10 years (this is a pinned-fingerprint model,
  not a CA-trust model — see Security below).
- The client (Android) does **not** rely on the system CA trust store. It
  pins the certificate's SHA-256 fingerprint (of the DER-encoded public key,
  hex-encoded lowercase, no separators) obtained out-of-band during pairing
  (QR code or manual entry). Every subsequent connection re-validates the
  live fingerprint against the pinned one; a mismatch is a hard failure
  surfaced to the user (never silently trusted), because it means either the
  cert was regenerated or a MITM is in progress.
- Message framing: each WebSocket **text** frame is exactly one JSON object
  (UTF-8). No batching multiple JSON objects into one frame.
- Idle keepalive: server sends a WS ping every 20s; client must pong within
  10s or the server closes the connection. Client also treats 2 missed
  server pushes on subscribed topics + failed ping as a liveness signal to
  proactively reconnect.

## 2. Security model

- **No general-purpose command execution** is ever exposed over the
  protocol. There is no "run arbitrary command" message type. The only
  mutating actions are `kill_process` (subject to the critical-process
  guard, §5) and `launch_app` (subject to the server-side allow-list, §6).
- **Pairing is local-network, human-present, out-of-band**: the pairing code
  and the cert fingerprint must both be obtained by the user directly from
  the Windows machine (QR code shown on screen, or the `agent devices` CLI
  output). Knowing the IP:port alone is insufficient to pair.
- Pairing codes are single-use, expire after 5 minutes, and the agent
  locks out an offering connection after 5 failed `pair_request` attempts
  (closes the socket; the underlying code is invalidated).
- Every paired device gets a random 256-bit secret, returned to the client
  exactly once at pairing time and never sent over the wire again. The
  client stores its copy in the Android Keystore via
  `EncryptedSharedPreferences`. The agent's copy is never stored in
  plaintext either: it is kept under **authenticated encryption
  (AES-256-GCM)**, keyed by a random 256-bit server-local key that is
  generated once, persisted alongside the TLS key material, and never
  transmitted. This (not a one-way hash) is required because §4.3's
  challenge-response needs the agent to recompute
  `HMAC_SHA256(device_secret, ...)` itself, which is only possible if the
  raw secret can be recovered server-side — a one-way hash could not
  satisfy that and is not used.
- Re-authentication uses a **challenge-response** (server nonce + HMAC),
  so the device secret itself is never sent over the wire again after
  pairing, even though the channel is already TLS-encrypted (defense in
  depth against a future TLS downgrade/bug).
- Auth failures are rate-limited per device_id and per source IP
  (exponential lockout, capped). Unauthenticated connections cannot access
  `metrics`, `processes`, `file_events`, `alerts`, or `history` — the
  server only accepts `hello`, `pair_request`, `auth_request`,
  `auth_response` before `auth_success`.
- File monitoring never transmits file **contents** — only path, name,
  size, timestamps, and event type metadata.

## 3. Connection lifecycle

```
Client                                   Server (agent)
  |--- TLS connect, verify pinned fp ------->|
  |--- {"type":"hello", ...} --------------->|
  |<-- {"type":"hello_ack", ...} ------------|
  |--- {"type":"auth_request", ...} -------->|   (paired devices)
  |<-- {"type":"auth_challenge", ...} -------|
  |--- {"type":"auth_response", ...} ------->|
  |<-- {"type":"auth_success"} --------------|
  |--- {"type":"subscribe", ...} ----------->|
  |<-- {"type":"metrics", ...} (periodic) ---|
  |<-- {"type":"file_event", ...} (as they happen)
  |<-- {"type":"alert", ...} (as triggered) -|
  | ... request/response messages ...        |
```

First-time pairing replaces the `auth_request`/`auth_response` step with
`pair_request`/`pair_success`.

## 4. Message catalogue

All messages are JSON objects with a required `type` field. Request
messages that expect a direct reply include a client-generated `request_id`
(UUIDv4 string); the server echoes it in the reply.

### 4.1 Handshake

```jsonc
// C->S
{"type":"hello","proto_version":1,"client":"android","app_version":"1.0.0"}
// S->C
{"type":"hello_ack","proto_version":1,"agent_version":"1.0.0","hostname":"DESKTOP-ABC"}
```

### 4.2 Pairing (unauthenticated connection only)

```jsonc
// C->S
{"type":"pair_request","request_id":"...","pairing_code":"7F3K-9QRT","device_name":"Jake Pixel 8"}
// S->C (success)
{"type":"pair_success","request_id":"...","device_id":"3f9c...","device_secret_b64":"base64(32 bytes)"}
// S->C (failure)
{"type":"pair_failed","request_id":"...","reason":"invalid_code|expired_code|locked_out"}
```

### 4.3 Auth (paired devices, every reconnect)

```jsonc
// C->S
{"type":"auth_request","request_id":"...","device_id":"3f9c..."}
// S->C
{"type":"auth_challenge","request_id":"...","nonce":"base64(16 bytes)"}
// C->S   hmac = base64(HMAC_SHA256(device_secret, nonce_bytes || ":" || device_id))
{"type":"auth_response","request_id":"...","hmac_b64":"..."}
// S->C
{"type":"auth_success","request_id":"..."}
// or
{"type":"auth_failed","request_id":"...","reason":"bad_hmac|unknown_device|revoked|locked_out"}
```

### 4.4 Subscriptions (push topics)

```jsonc
// C->S
{"type":"subscribe","topics":["metrics","file_events","alerts"]}
```

Server pushes, unprompted, once subscribed:

```jsonc
{"type":"metrics","ts":1234567890,"cpu_percent":23.4,
 "ram":{"total_bytes":..,"used_bytes":..},
 "disks":[{"volume":"C:\\","total_bytes":..,"used_bytes":..,"free_bytes":..}]}

{"type":"file_event","ts":..,"op":"created|modified|deleted|renamed|moved",
 "path":"C:\\Users\\jake\\Downloads\\big.zip","old_path":null,
 "size_bytes":..,"is_dir":false}

{"type":"alert","ts":..,"id":"...","kind":"large_file|fast_growth|disk_fill_rate|low_free_space",
 "severity":"warning|critical","message":"...","context":{...}}
```

`metrics` push interval: 2s while at least one client is subscribed.
`processes` topic is **not** a push topic — it's polled via `list_processes`
to avoid needless load; the agent still allows subscribing to it for a
lighter periodic snapshot (every 5s) if a client wants a live view.

### 4.5 Processes

```jsonc
// C->S
{"type":"list_processes","request_id":"...","query":"chrome"}
// S->C
{"type":"process_list","request_id":"...","items":[
  {"pid":1234,"name":"chrome.exe","exe":"C:\\...\\chrome.exe","user":"DESKTOP\\jake",
   "cpu_percent":4.2,"ram_bytes":184320000,"protected":false}
]}

// C->S
{"type":"kill_process","request_id":"...","pid":1234}
// S->C
{"type":"action_result","request_id":"...","action":"kill_process","success":true}
{"type":"action_result","request_id":"...","action":"kill_process","success":false,
 "reason":"critical_process_protected|not_found|access_denied"}
```

### 4.6 App launch (allow-list only)

```jsonc
{"type":"list_allowed_apps","request_id":"..."}
{"type":"allowed_apps","request_id":"...","items":[{"app_id":"notepad","label":"Not Defteri"}]}

{"type":"launch_app","request_id":"...","app_id":"notepad"}
{"type":"action_result","request_id":"...","action":"launch_app","success":true,"pid":5678}
{"type":"action_result","request_id":"...","action":"launch_app","success":false,"reason":"not_allow_listed|launch_failed"}
```

### 4.7 File events / growth queries

```jsonc
{"type":"list_file_events","request_id":"...","from_ts":..,"to_ts":..,
 "op":"created|modified|deleted|renamed|moved|null","min_size_bytes":0,
 "path_prefix":"C:\\Users\\jake\\Downloads","limit":100,"offset":0}
{"type":"file_events","request_id":"...","total":42,"items":[ ...file_event objects... ]}

{"type":"top_growth","request_id":"...","window":"1h|24h|7d","limit":20}
{"type":"top_growth_result","request_id":"...","window":"1h","items":[
  {"path":"C:\\Users\\jake\\Videos","is_dir":true,"delta_bytes":1073741824,
   "size_before":.., "size_after":..}
]}
```

### 4.8 Alerts & history

```jsonc
{"type":"list_alerts","request_id":"...","limit":50}
{"type":"alerts","request_id":"...","items":[ ...alert objects... ]}

{"type":"list_history","request_id":"...","from_ts":..,"to_ts":..,"limit":50,"offset":0}
{"type":"history","request_id":"...","total":..,"items":[
  {"ts":..,"device_id":"...","device_name":"...","action":"kill_process|launch_app|pair|unpair",
   "success":true,"detail":"chrome.exe (pid 1234)","reason":null}
]}
```

### 4.9 Errors

```jsonc
{"type":"error","request_id":"...","reason":"unauthenticated|bad_request|internal_error","message":"..."}
```

## 5. Critical-process protection (must match on both sides for UI hints)

The agent is the sole enforcement point (client-side hints are cosmetic
only). A process is **protected** (kill refused) if any of:

1. `pid <= 8` (covers PID 0 "System Idle" and PID 4 "System").
2. `pid == agent's own pid`.
3. Process owner is `NT AUTHORITY\SYSTEM`, `NT AUTHORITY\LOCAL SERVICE`, or
   `NT AUTHORITY\NETWORK SERVICE`.
4. Executable base name (case-insensitive) is in the hardcoded set:
   `system, smss.exe, csrss.exe, wininit.exe, winlogon.exe, services.exe,
   lsass.exe, lsaiso.exe, svchost.exe, fontdrvhost.exe, dwm.exe,
   explorer.exe, registry, memory compression, wudfhost.exe,
   sihost.exe, ctfmon.exe`.

The `process_list` response includes `protected:true/false` per item so the
client can grey out the kill action pre-emptively, but the server re-checks
on every `kill_process` regardless of what the client sends.

## 6. Allowed-app launch list

Configured by the PC owner only, on the Windows side, in
`%ProgramData%\WinRemoteMonitor\allowed_apps.json`:

```json
[
  {"app_id": "notepad", "label": "Not Defteri", "path": "C:\\Windows\\System32\\notepad.exe"},
  {"app_id": "calc", "label": "Hesap Makinesi", "path": "C:\\Windows\\System32\\calc.exe"}
]
```

There is no protocol message to add/modify this list remotely — it is
edited on the PC, by design, so the phone can never expand its own launch
permissions.

## 7. Noise filtering for file monitoring

Default-ignored path globs (configurable, additive; `**` matches any
number of path segments, `*`/`?` match within a segment, and a bare
pattern like `*.tmp` matches by basename anywhere in the tree, not only at
a watch root):
`**\node_modules\**`, `**\.git\**`, `**\AppData\Local\Temp\**`,
`**\$Recycle.Bin\**`, `**\System Volume Information\**`, `*.tmp`, `*.log`,
`**\AppData\Local\Packages\**\TempState\**`.

Independently of the ignore list, the agent debounces repeated events for
the same path within a 500ms window (e.g. a file being written in small
chunks) and emits a single coalesced `file_event` rather than one per
underlying OS notification.

## 8. Alert thresholds (defaults, configurable via `config.json`)

Each rule below has one `severity`-crossing threshold and fires as
`"warning"`; the agent additionally fires `"critical"` once the measured
value reaches **2x** that threshold (e.g. a file at 500 MiB is a warning,
one at 1 GiB is critical). This 2x multiplier is also configurable.

- `large_file_bytes`: 500 MiB — single new/grown file crossing this size.
- `fast_growth`: a directory gaining > 200 MiB within a rolling 10-minute
  window.
- `disk_fill_rate`: a volume losing > 5% of total free space within 15
  minutes.
- `low_free_space_percent`: 10% — free space on any volume drops below.

## 9. Version negotiation

`proto_version` is an integer, currently `1`. The server rejects a `hello`
with an unknown/future major version with `error{reason:"bad_request"}` and
closes the connection.

## 10. QR pairing payload

The QR code the agent displays/writes to an image on the Windows machine
encodes a single JSON object as UTF-8 text (this is the exact schema the
Windows agent implements; the Android client's QR scanner must decode this
same shape):

```json
{
  "host": "192.168.1.42",
  "port": 8787,
  "pairing_code": "7F3K-9QRT",
  "fingerprint_sha256": "3f9c...64 hex chars..."
}
```

`fingerprint_sha256` is the lowercase hex SHA-256 of the server's
certificate's DER-encoded public key (§1). The manual-entry fallback form
must accept the same four fields typed in by hand.
