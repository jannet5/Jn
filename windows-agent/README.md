# WinRemoteMonitor — Windows Agent

The Windows-side half of WinRemoteMonitor: a Go program that runs on the PC
being monitored, exposes a TLS WebSocket endpoint (`wss://<host>:8787/ws`)
implementing [`docs/PROTOCOL.md`](../docs/PROTOCOL.md), and lets a paired
Android phone live-monitor CPU/RAM/disk/processes/files and perform exactly
two mutating actions: killing a non-critical process, and launching an
allow-listed application. There is no general command-execution path.

This program was developed and tested **on Linux, cross-compiled for
Windows**. See "What has and hasn't been verified" at the bottom — read it
before relying on this in production.

## What it is

- `internal/core/` — pure, OS-agnostic protocol logic (message types,
  critical-process protection rules, allow-list validation, pairing/auth
  primitives, growth analysis, alert thresholds, noise-filter globs). No
  gopsutil/fsnotify/sqlite imports; fully unit-tested on any platform.
- `internal/agent/` — the wiring layer: real system metrics via
  [gopsutil](https://github.com/shirou/gopsutil), real recursive file
  watching via [fsnotify](https://github.com/fsnotify/fsnotify) (with
  hand-rolled recursive-watch management, since fsnotify itself only
  watches one directory non-recursively), persistent storage via
  [modernc.org/sqlite](https://gitlab.com/cznic/sqlite) (pure Go, no cgo),
  the TLS/WebSocket server via
  [gorilla/websocket](https://github.com/gorilla/websocket), and QR
  rendering via [go-qrcode](https://github.com/skip2/go-qrcode).
- `cmd/agent/main.go` — the CLI entrypoint (see Usage below).

No file content is ever read or transmitted — only metadata (name, path,
size, timestamps, event type). No mocked data is used anywhere in the
production code path; the only test doubles anywhere in the codebase are
tiny hand-built structs inside `_test.go` files used to drive pure
predicate logic (e.g. a `core.ProcessInfo{...}` literal to test the
critical-process rule) — every code path that talks to gopsutil, fsnotify,
sqlite or the OS is exercised against the real thing, both in production
and in the test suite.

## Building

Requires Go 1.24+ (the module currently pins `go 1.25.0` because
`modernc.org/sqlite`'s latest release requires it; `go build`/`go test`
will auto-download that toolchain via `GOTOOLCHAIN=auto` if your installed
Go is older, as long as `proxy.golang.org` is reachable).

Native build (whatever OS you're on):

```sh
go build -o agent ./cmd/agent
```

Cross-compile for Windows (what `build.sh` does):

```sh
GOOS=windows GOARCH=amd64 CGO_ENABLED=0 go build -o dist/winremotemonitor-agent.exe ./cmd/agent
```

Run everything (vet + full test suite + cross-compile + checksum) with:

```sh
./build.sh
```

## Running on a real Windows machine

1. Copy `dist/winremotemonitor-agent.exe` to the Windows machine.
2. (Optional) Copy `config.example.json` to `config.json` and
   `allowed_apps.example.json` to `allowed_apps.json` next to the exe (or
   anywhere; point `--config`/`--allowed-apps` at them), and edit them —
   see "Configuring" below.
3. Run it from a console:

   ```
   winremotemonitor-agent.exe --config config.json
   ```

   On first run it generates and persists (under `%ProgramData%\WinRemoteMonitor`
   by default — see Known limitations if that path isn't writable for your
   user):
   - A self-signed TLS certificate + key (`agent-cert.pem`/`agent-key.pem`,
     10-year validity, per PROTOCOL.md §1).
   - A server-local key (`server.key`) used to protect paired devices'
     secrets at rest.
   - A sqlite database (`agent.db`) holding paired devices, file-event
     history, directory-size snapshots, alerts, and the
     kill/launch/pair/unpair history log.

   It logs the port it's listening on and the cert's SHA-256 fingerprint,
   and it keeps running (foreground console process — see "No Windows
   service wrapper" below) until you Ctrl+C it or send it SIGTERM/Ctrl+Break.

4. There is no installer and no Windows service wrapper in this build (see
   Known limitations). If you want it to survive logoff/reboot, run it via
   Task Scheduler ("run whether user is logged on or not") or wrap it with
   a service manager like [NSSM](https://nssm.cc/) — this was not built or
   tested here.

## Pairing from the phone

On the Windows machine, with the agent **already running** (pairing codes
are generated against the running server's in-memory pairing manager —
run this in a second console, or briefly stop the server, generate the
code, and restart it before the phone connects... in practice, the
cleanest flow is to run the agent normally and use `--pair` from a second
terminal, since pairing state is per-process; see Known limitations):

```
winremotemonitor-agent.exe --pair
```

This prints:
- A pairing code (`XXXX-XXXX`, expires in 5 minutes, single-use).
- The machine's best-guess LAN IP and the configured port.
- The TLS certificate's SHA-256 fingerprint (pinned by the phone
  out-of-band, per PROTOCOL.md §1 — never trust-on-first-use over the
  network itself).
- A QR code PNG (`pairing-qr.png` by default; `--qr-out` to change the
  path) encoding `{"host","port","pairing_code","fingerprint_sha256"}` —
  **this exact QR payload schema is this implementation's own choice, not
  specified by PROTOCOL.md; confirm the Android side reads the same
  shape**, or fall back to typing the code and fingerprint in by hand.

Five wrong pairing-code attempts on one connection locks it out and closes
the socket, per PROTOCOL.md §2.

## Managing paired devices

```
winremotemonitor-agent.exe devices list
winremotemonitor-agent.exe devices revoke <device_id>
```

## Configuring allow-listed apps

Edit `allowed_apps.json` (see `allowed_apps.example.json`) — a JSON array
of `{"app_id", "label", "path"}`. This file is only ever read from local
disk; there is no protocol message that lets the phone add or change an
entry, by design (PROTOCOL.md §6).

## Configuring watched folders / thresholds

Edit `config.json` (see `config.example.json`):
- `watched_roots`: absolute Windows paths to recursively watch. If empty,
  file monitoring (file events, growth analysis, `large_file`/
  `fast_growth` alerts) is disabled entirely — metrics/process/launch
  functionality still works.
- `ignore_globs`: **additive** to the built-in defaults from PROTOCOL.md
  §7 (`node_modules`, `.git`, temp dirs, `*.tmp`, `*.log`, etc.) — you
  cannot remove a default glob, only add more.
- `alert_thresholds`: overrides for `large_file_bytes`, `fast_growth_bytes`
  + `fast_growth_window_sec`, `disk_fill_rate_percent` +
  `disk_fill_rate_window_sec`, `low_free_space_percent`. Defaults exactly
  match PROTOCOL.md §8.
- `port`, `metrics_interval_sec`, `processes_interval_sec`,
  `snapshot_interval_sec`, `data_dir`.

## Spec ambiguities and flaws found (flagged, not silently resolved)

`docs/PROTOCOL.md` was treated as unchangeable, per instructions — where it
was ambiguous or self-contradictory, this implementation made an explicit,
documented choice (in code comments at the relevant spot) rather than
editing the spec. Summary, for the person reconciling this with the
Android side:

1. **Device-secret storage vs. challenge-response is contradictory as
   literally written** (`internal/core/auth.go`). PROTOCOL.md §2 says the
   device secret is "stored on the agent hashed (HMAC-SHA256 with a
   server-local key)" and never in plaintext; §4.3's challenge-response
   requires the agent to compute `HMAC_SHA256(device_secret, nonce||":"||
   device_id)` itself, which needs the *raw* secret, not a one-way hash of
   it. You cannot do both literally. This implementation stores the
   secret under **authenticated symmetric encryption (AES-256-GCM)** keyed
   by the same server-local key, reversible only by the agent, which
   preserves the spirit ("never plaintext at rest, never leaves the
   machine") while actually letting challenge-response work. If the
   Android side (or a future reviewer) assumed the literal one-way-hash
   reading, that assumption needs to be revisited — challenge-response
   cannot work with a one-way hash.
2. **Severity mapping (warning vs. critical) is unspecified** for all four
   alert kinds in §8 — PROTOCOL.md only gives trigger thresholds, not a
   severity rule. This implementation's choice (`internal/core/alerts.go`):
   crossing the threshold is `warning`; crossing 2x the threshold (or, for
   `low_free_space`, dropping to half the threshold's distance from 100%)
   is `critical`. Confirm this matches whatever severity-based UI (e.g.
   color) the Android app assumes.
3. **§7's noise-filtering glob semantics aren't fully specified.** This
   implementation's reading (`internal/core/noise.go`): `**` spans any
   number of path segments; a single `*` doesn't cross a path separator; a
   pattern with no separator (like `*.tmp`) matches the file's *base name*
   anywhere in the tree (not just a file named exactly that at the watch
   root) — the only reading under which `*.tmp` is a useful global ignore
   rule.
4. **The sentence "`*.tmp`, `*.log` churn is throttled (see §8)" doesn't
   parse against §8**, which defines alert thresholds, not a throttle
   mechanism. This implementation both (a) treats `*.tmp`/`*.log` as
   ordinary additive ignore-globs, and (b) separately debounces/batches
   rapid repeated fsnotify events for the same path
   (`internal/agent/watcher.go`, 500ms window) as a defensive throttle
   independent of the glob list — but this is a best-effort guess at what
   was intended, not a literal implementation of a spec'd mechanism.
5. **The QR pairing payload schema is not specified** by PROTOCOL.md at
   all (only that a QR code is shown). This implementation's chosen JSON
   shape is in `internal/agent/pairing_qr.go` and printed above under
   "Pairing from the phone" — the Android side needs to read the same
   shape, or the two apps need to agree on a different one.
6. **An expired/missing auth challenge has no dedicated `auth_failed`
   reason** in §4.3's enumerated list (`bad_hmac|unknown_device|revoked|
   locked_out`). This implementation reports it as `bad_hmac`
   (`internal/agent/server.go`), which is defensible but not literally
   what happened.

None of these were "fixed" by editing `docs/PROTOCOL.md`, per instructions.

## Known limitations / simplifications

- **No Windows service wrapper.** This is a plain console/background
  process, not an installed Windows service with autostart. Given the time
  available, this was a deliberate scope cut. Wiring it up as a proper
  service (`golang.org/x/sys/windows/svc`, or wrapping with NSSM/Task
  Scheduler) is future work, not done here.
- **Pairing is per-process, in-memory.** `PairingManager` (issued codes,
  per-connection attempt counters) lives only in the running server
  process's memory — it is not persisted to sqlite. Restarting the agent
  invalidates any outstanding (not-yet-used) pairing code. This matches
  the "codes expire after 5 minutes anyway" spirit of the spec but means
  `--pair` must talk to an *already-running* agent process (see the
  pairing section above) rather than being a fully standalone one-shot
  command; a cleaner design would run `--pair` as a client call to the
  live agent over a local control channel, which wasn't built here.
- **Alert severity thresholds, glob semantics, QR payload, and the
  device-secret storage mechanism are this implementation's own
  documented interpretations** of underspecified/contradictory parts of
  the spec — see the section above. Anthropic-flagged, not spec-changed.
- **IPv4-only** for the printed "connect to" LAN IP and cert SANs;
  IPv6-only networks aren't specifically handled.
- **No rate limiting on non-auth message types** beyond the
  authentication gate itself — e.g. nothing stops an authenticated device
  from spamming `list_processes`. Not called out as a requirement in
  PROTOCOL.md, so left out.
- **Windows drive-letter enumeration for the `disks` metrics field is not
  implemented as "enumerate all drives automatically."** `MetricsSampler.Volumes`
  is configured, defaulting to `/` on non-Windows for tests; a production
  Windows deployment should set this to the real drive letters (e.g.
  `C:\`, `D:\`) via config — automatic enumeration via
  `GetLogicalDrives`/gopsutil's partition listing wasn't wired up, though
  gopsutil supports it and adding it is straightforward follow-up work.

## What has and hasn't been verified (read this before trusting this build)

This was built entirely on a **Linux container** with no access to a real
Windows machine. Verified vs. not verified, explicitly:

**Verified here, on this Linux machine:**
- `go vet ./...` is clean and `go test ./...` passes for every package,
  with zero skips (`./build.sh` runs both).
- Cross-compilation to `windows/amd64` with `CGO_ENABLED=0` succeeds and
  produces a file `file(1)` identifies as a genuine Windows PE32+ (or
  PE32) executable — see `./build.sh`'s output.
- The **entire protocol dispatch/auth/pairing/kill/launch/history state
  machine** (`internal/agent/server_test.go`) runs end-to-end over a real
  TLS listener and a real `gorilla/websocket` client connection (not a
  mock) — hello handshake, pairing, challenge-response auth (including
  wrong-secret rejection and lockout-after-5-failed-attempts with actual
  socket closure), `list_processes`/`kill_process` (including refusing to
  kill the test process's own pid, exercising the *real*
  `core.EvaluateProtection` path against a *real* gopsutil-listed
  process), `launch_app` (allow-listed and rejected), and the
  history/alerts sqlite store, all against this actual Linux test machine's
  real CPU/RAM/disk/process data (see `internal/agent/metrics_test.go`,
  `process_test.go`).
- The **unauthenticated-rejection requirement** has an explicit passing
  test (`TestServer_UnauthenticatedConnectionRejectedForProtectedTypes`,
  `TestServer_KillProcessRejectedWhenUnauthenticated`) covering every
  gated message type.
- The recursive file watcher (`internal/agent/watcher_test.go`) is
  exercised against the real Linux filesystem and real fsnotify events:
  create/modify detection, glob-based ignoring, automatic recursive
  watch-adding on new subdirectories, and event debouncing.
- The critical-process predicate, allow-list validation, pairing/lockout
  state machines, growth-delta math, and alert-threshold math are all
  exhaustively table-tested as pure functions (`internal/core/*_test.go`).

**NOT verified — genuinely unknown until this runs on a real Windows box:**
- **Real Windows process-owner detection.** `core.EvaluateProtection`'s
  owner-account check (`NT AUTHORITY\SYSTEM` etc.) is fed by gopsutil's
  `Process.Username()`, which on Windows wraps `OpenProcessToken` +
  `LookupAccountSid`. That WinAPI path has never actually executed in this
  environment — only gopsutil's Linux implementation has run here, which
  returns a totally different string shape. Whether the exact strings
  gopsutil returns on a real Windows box match this code's
  `"nt authority\\system"` etc. comparisons (case/format) is **unverified**
  and is the single highest-risk gap in this deliverable.
- **Real Windows critical-process names encountered in practice.** The
  hardcoded name set is transcribed exactly from PROTOCOL.md §5, but
  whether gopsutil's `Process.Name()` returns exactly `"explorer.exe"`
  (vs., say, a different case or a full path) on a real Windows install
  has not been observed.
- **The actual TLS handshake against a real Android client**, including
  whether the Android side's certificate-pinning logic (computing the
  same "SHA-256 of DER-encoded public key, hex, lowercase, no separators"
  fingerprint this agent prints) actually matches byte-for-byte — this was
  only tested against Go's own TLS client in this repo's own tests.
- **Windows Defender / Windows Firewall interaction.** Whether a fresh
  Windows install prompts to allow the exe through the firewall for
  inbound WS connections, or whether Defender SmartScreen flags an
  unsigned/unnotarized exe, is unknown and unhandled (no code signing was
  done or attempted).
- **Whether admin/elevated rights are actually required for any
  operation.** Writing to `%ProgramData%` typically needs at least
  standard-user write access to that specific subfolder (created here on
  first run) but not necessarily admin; killing arbitrary *other users'*
  processes typically does need elevation on Windows. Neither was tested
  against real Windows permission boundaries.
- **Service/autostart behavior** — moot, since no service wrapper was
  built (see Known limitations).
- **Real directory-size scan performance on a large, real NTFS volume**
  (millions of files, deep trees, Windows path length limits, junction
  points/symlink loops). The scanner and watcher were only exercised
  against small synthetic directory trees on this Linux machine's
  filesystem.
- **fsnotify's actual Windows backend** (ReadDirectoryChangesW). fsnotify
  ran here on its Linux (inotify) backend only; its Windows backend's
  event shapes/timing/edge cases (e.g. rename semantics, buffer overflow
  under heavy churn) were not exercised.

In short: the *logic* (protocol state machine, security gating, alert
math, protection rules) is genuinely tested end-to-end against real
dependencies on this machine. The *Windows-specific runtime behavior* of
those same dependencies (gopsutil's WinAPI calls, fsnotify's
ReadDirectoryChangesW backend, real Windows account-name strings, real
firewall/Defender/UAC behavior) is unverified because no Windows machine
was available during development, and should be the focus of manual
testing before this is trusted on a real PC.
