# WinRemote Monitor — Android client

Android client for the WinRemoteMonitor Windows agent. Implements the wire
protocol in `../docs/PROTOCOL.md` exactly (pairing, challenge-response
re-auth, the full message catalogue, critical-process/allow-list UI hints,
file-event/alert shapes). Kotlin, Jetpack Compose, Material 3, MVVM.

## Architecture

```
app/src/main/kotlin/com/jn/winremote/
  protocol/     Wire messages (kotlinx.serialization sealed classes, 1:1 with PROTOCOL.md §4)
  crypto/       HMAC challenge-response math + certificate-fingerprint pinning (TrustManager)
  data/         PairedDevice model + EncryptedSharedPreferences-backed SecureStore (multi-PC)
  pairing/      QR payload parsing (camera-independent, pure function)
  repository/   WinRemoteRepository: the one persistent, auto-reconnecting OkHttp WebSocket
                session (connect -> hello -> auth -> subscribe -> requests/pushes), plus
                PairingClient (a separate, ephemeral, one-shot pairing connection) and
                BackoffPolicy (reconnect backoff sequencing).
  util/         Formatting, Turkish reason-code text, file-event filter predicates,
                critical-process UI-hint rules, YUV rotation math for the QR scanner.
  ui/           One package per screen (pairing, dashboard, processes, apps, fileactivity,
                alerts, history, settings), each with a ViewModel (StateFlow) + Composable.
                ui/components holds shared pieces: state screens (Loading/Empty/Error/
                Offline/Unauthorized/Reconnecting), the connection status chip, the
                confirm dialog, and the custom Canvas sparkline.
  ui/nav/       Navigation drawer + NavHost wiring all 8 screens together.
```

**MVVM + single connection owner.** `WinRemoteRepository` is a process-wide singleton
(held by `WinRemoteApplication`) that owns the one live WebSocket connection to the
*active* paired PC. Every screen's ViewModel reads `repository.connectionStatus` /
`repository.metrics` / `repository.liveFileEvents` / `repository.liveAlerts` (all
`StateFlow`/`SharedFlow`) and issues request/response calls (`listProcesses`,
`killProcess`, `launchApp`, `listFileEvents`, `topGrowth`, `listAlerts`, `listHistory`)
that suspend until the matching `request_id` reply arrives or time out. Nothing in the
UI ever touches OkHttp directly.

**No general-purpose command execution.** The only two mutating client actions are
`kill_process` and `launch_app`; `launch_app` only ever sends an `app_id` from the
server's own `allowed_apps` list — there is no text field anywhere that lets a user
type an arbitrary path or command.

**Security**
- TLS trust is a real pinned-fingerprint check, not a stub: `crypto/CertPinning.kt`
  implements a custom `X509TrustManager` that computes SHA-256 of the server's leaf
  certificate's DER-encoded public key (matching PROTOCOL.md §1's exact definition —
  see "Spec interpretation notes" below) and compares it to the fingerprint pinned at
  pairing time. A mismatch throws `CertificatePinningException`, which the repository
  turns into a hard-fail `ConnectionStatus.Error(isCertMismatch = true)` — surfaced in
  the UI as "sunucu kimliği doğrulanamadı" — with **no automatic retry** (retrying
  against a possibly-hostile endpoint teaches nothing). Hostname verification is
  intentionally bypassed (see code comment in `CertPinning.kt`): trust comes from the
  pin, not from CN/SAN matching, since users pair by raw IP to a self-signed cert.
- `device_secret` is stored only inside `EncryptedSharedPreferences`
  (`androidx.security:security-crypto`, Android Keystore-backed AES-256), one record
  per paired PC. It is never sent back over the wire after pairing — only used to
  compute `HMAC_SHA256(secret, nonce || ":" || device_id)` for each reconnect
  (`crypto/HmacAuth.kt`, unit-tested against RFC 4231 and an independently-computed
  vector — see below).
- `PairedDevice.toString()` is overridden to redact the secret, and there is no
  `Log.*`/`println` call anywhere that logs a device, a secret, an HMAC or a nonce
  (verified by grepping the whole `main/kotlin` tree — see Testing below).
- Reconnect is automatic exponential backoff (1s, 2s, 5s, 10s, capped, ±20% jitter —
  `repository/BackoffPolicy.kt`) re-running the full challenge-response with the
  stored secret; no user interaction needed after a transient drop. `auth_failed`
  with a non-recoverable reason (`bad_hmac`/`unknown_device`/`revoked`) stops
  auto-retry and surfaces "Yetkisiz" instead, since retrying a wrong/revoked secret
  cannot succeed and only adds to the server's lockout counter.

## Spec interpretation notes (things PROTOCOL.md left implicit)

I did not edit `docs/PROTOCOL.md`. Two places where I had to pick a concrete
interpretation, flagged here since the Windows agent is being built concurrently by a
different agent against the same document:

1. **Fingerprint scope.** §1 calls it "the certificate's SHA-256 fingerprint" in prose
   but then precisely defines it as "the DER-encoded **public key**, hex-encoded
   lowercase, no separators" — i.e. an SPKI pin, not a whole-certificate hash. I
   implemented exactly the precise definition (`crypto/CertPinning.kt:publicKeyFingerprintHex`),
   verified against an `openssl x509 -pubkey | openssl pkey -pubin -outform DER |
   openssl dgst -sha256` reference value in `CertPinningTest`. If the Windows agent
   instead pins/prints a whole-certificate SHA-256, pairing will report a fingerprint
   mismatch — worth a quick cross-check once both sides run against each other.
2. **QR payload schema.** §2/§3 require the pairing code and fingerprint to come
   out-of-band via "QR code or manual entry" but never define the QR's JSON shape.
   `pairing/QrPairingPayload.kt` assumes `{"host","port","fingerprint","pairing_code",
   "agent_name"?}` (tolerating `cert_fingerprint`/`fp` and `code` as synonyms for the
   two security-critical fields). The **manual-entry form is fully independent and
   equally capable** — every pairing field it needs is typeable by hand — so a schema
   mismatch degrades to "use manual entry", never to a broken pairing flow.
3. **`processes` push topic.** §4.4 says the server "allows subscribing to it for a
   lighter periodic snapshot (every 5s)" but the message catalogue never shows what a
   push-mode `process_list` looks like (whether it carries a `request_id` at all, and
   if so, which one). To avoid guessing at undocumented framing, the Süreçler screen
   instead polls with an explicit `list_processes` request every 5 seconds while
   visible — same effective behavior, using only the documented request/response
   shape.

## Building

Requires: JDK 17+ (JDK 21 used here), no other local tools — the wrapper fetches its
own Gradle. This repo checks in `gradlew`/`gradlew.bat` and `keystore/dev-release.jks`
so a clean checkout builds both APKs with nothing else to set up besides the Android
SDK below.

1. **Android SDK.** If `$ANDROID_HOME` isn't already set up with `platform-tools`,
   `platforms;android-34` and `build-tools;34.0.0`, get the command-line tools and
   install them:
   ```bash
   curl -o cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
   unzip cmdline-tools.zip -d /path/to/android-sdk/cmdline-tools-extract
   mkdir -p /path/to/android-sdk/cmdline-tools
   mv /path/to/android-sdk/cmdline-tools-extract/cmdline-tools /path/to/android-sdk/cmdline-tools/latest
   export ANDROID_HOME=/path/to/android-sdk
   yes | $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_HOME --licenses
   $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_HOME \
       "platform-tools" "platforms;android-34" "build-tools;34.0.0"
   echo "sdk.dir=$ANDROID_HOME" > local.properties
   ```
2. **Tests:**
   ```bash
   ./gradlew testDebugUnitTest
   ```
3. **Debug APK:**
   ```bash
   ./gradlew assembleDebug
   # -> app/build/outputs/apk/debug/app-debug.apk
   ```
4. **Signed release APK:** already configured to sign with the checked-in
   `keystore/dev-release.jks` via `keystore.properties` at the project root.
   ```bash
   ./gradlew assembleRelease
   # -> app/build/outputs/apk/release/app-release.apk
   ```

Both APKs, plus their SHA-256 hashes, are also copied into `dist/` —
see `dist/SHA256SUMS.txt`.

### About the signing key

`keystore/dev-release.jks` is a throwaway, self-signed **local/dev** identity
generated once with:
```bash
keytool -genkeypair -v -keystore keystore/dev-release.jks -alias winremote-dev \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass winremote-dev-only \
  -keypass winremote-dev-only -dname "CN=WinRemote Monitor Dev, OU=Dev, O=Local Build, L=NA, ST=NA, C=US"
```
It is **not** a production/Play Store signing key, and the password is deliberately
not secret (`winremote-dev-only`, in `keystore.properties`). It exists only so
`assembleRelease` produces a real, verifiably-signed APK from a clean checkout.
Replace it with a real key (and stop checking in `keystore.properties`) before any
real distribution.

## Installing on a phone

**Sideload, USB + adb:**
```bash
adb install -r dist/app-debug.apk        # or app-release.apk
```

**Sideload, no computer:** copy the APK to the phone (email/Drive/USB transfer),
open it from Files, and allow "install unknown apps" for that source when prompted.
Since `app-release.apk` is signed with the dev key above, Android will treat it as an
ordinary (non-Play-Store) app — no special flags needed beyond the unknown-sources
permission.

## Pairing with the Windows agent

1. On the Windows PC, start the agent and open its pairing UI (QR code) or run its
   `agent devices` CLI to get: host/IP, port (default `8787`), the certificate's
   SHA-256 public-key fingerprint, and a pairing code (valid 5 minutes, single-use).
2. On the phone, open **Eşleştirme** (shown automatically on first launch, or via
   **Ayarlar → Yeni cihaz eşleştir**).
3. **QR Tara**: point the camera at the agent's QR code. On a recognized payload the
   app switches to the **Elle Gir** tab with the fields pre-filled so you can review
   before submitting.
4. **Elle Gir** (fully independent path — works with no camera at all): type the
   host, port, the 64-character fingerprint, the pairing code, and a name for this
   phone, then **Eşleştir**.
5. On success the phone stores its `device_id`/`device_secret` and connects
   immediately; the Panel screen should show **Bağlı**. On failure the screen shows
   the specific Turkish reason (invalid/expired code, lockout, or "sunucu kimliği
   doğrulanamadı" for a fingerprint mismatch).

To pair a second PC, repeat from **Ayarlar**; switch which one is active from the
same screen (only one connection is held at a time).

## Testing done in this environment

- `./gradlew testDebugUnitTest`: **79 tests, 0 failures, 0 errors, 0 skipped.**
  Covers: full protocol JSON round-trips against literal PROTOCOL.md examples
  (`ProtocolCodecTest`), HMAC challenge-response math against an RFC 4231 vector and
  an independently Python-computed vector (`HmacAuthTest`), certificate-pinning
  fingerprint computation and trust/reject behavior against a real OpenSSL-generated
  X.509 certificate (`CertPinningTest`), reconnect backoff sequencing and jitter
  bounds (`BackoffPolicyTest`), file-event filter predicates (`FileEventFilterTest`),
  critical-process UI-hint rules mirroring PROTOCOL.md §5 (`CriticalProcessRulesTest`),
  QR-payload parsing (`QrPairingPayloadTest`), YUV-plane rotation math for the QR
  analyzer (`YuvRotateTest`), byte/percent/timestamp formatting (`FormattingTest`),
  Turkish reason-code text coverage (`ReasonTextTest`), and `PairedDevice`
  JSON round-trip + secret-redaction (`PairedDeviceSerializationTest`).
- `./gradlew assembleDebug` and `./gradlew assembleRelease`: both succeed; both
  output files verified to be real, valid APK/zip archives (`file`, `unzip -l`) and
  correctly signed (`apksigner verify --print-certs`, confirming the release APK
  carries the dev keystore's certificate and the debug APK carries the standard
  Android debug certificate). SHA-256 of both recorded in `dist/SHA256SUMS.txt`.
- Manually grepped the entire `app/src/main/kotlin` tree for `Log.` / `println(` to
  confirm no call ever logs a secret, HMAC, or nonce value.

## What is NOT verified (no device, emulator, or live Windows agent in this environment)

This container has the Android SDK's build tools but **no emulator and no physical
device**, and the Windows agent is a separate binary being built concurrently — not a
live server reachable here. So the following are implemented per-spec and compile/
unit-test cleanly, but their actual runtime behavior on a device has **not** been
observed:

- **QR camera scanning.** `ui/pairing/QrAnalyzer.kt` (CameraX `ImageAnalysis.Analyzer`
  + ZXing's `QRCodeReader` run directly against the Y/luminance plane) has never
  decoded a real camera frame. Only its pure geometry helper (`util/YuvRotate.kt`,
  handling sensor-vs-portrait rotation) is unit-tested. Actual decode reliability in
  good/bad lighting, at an angle, or with a low-end camera sensor is unverified.
  The manual-entry pairing path does not depend on this at all.
- **Real TLS handshake / WebSocket connection.** No live agent to connect to here, so
  the actual `wss://` handshake, the real pinned-fingerprint check against a live
  self-signed cert, and the full `hello`/`auth_challenge`/`auth_response` exchange
  have only been exercised by unit tests against literal JSON and a static test
  certificate — never against a running Go agent process.
- **Reconnect-after-real-network-loss.** The backoff *sequencing* is unit-tested;
  actually killing Wi-Fi/mobile data mid-session and observing the phone silently
  recover (or the liveness watchdog in `WinRemoteRepository` correctly detecting a
  half-open connection) has not been observed.
- **On-device rendering/layout.** No emulator or device screen was available to
  actually render any Compose screen. Layouts were written and reviewed for 360dp and
  420dp widths (comfortable touch targets, no fixed-width elements, scrollable
  content, Material 3 defaults), but nothing has been screenshotted or visually
  confirmed. `@Preview` composables were intentionally *not* added with fake data per
  the "no demo data in the runtime path" constraint on non-test code, so there's
  currently no visual preview at all — only compiled, logically-tested UI code.
- **Background/battery behavior.** The app holds its WebSocket connection only while
  the process is alive (no foreground service, no WorkManager keep-alive); how
  Android's battery/Doze management actually treats this connection over a real
  multi-hour idle period is unverified.
- **EncryptedSharedPreferences / Android Keystore.** Compiles against the real
  `androidx.security:security-crypto` API, but `SecureStore` itself needs a real
  Android Keystore and so has no JVM unit test; only the plain-JSON shape it
  serializes (`PairedDevice`) is round-trip tested.
- **CameraX permission flow, dropdown/menu interactions, snackbars, dialogs** — all
  implemented with standard Compose/Material 3 APIs but never interacted with on a
  real touchscreen.

In short: **the protocol layer, crypto, backoff, filters, and formatting are real and
verified by tests; the Android-framework-dependent runtime behavior (camera, live
TLS/WebSocket I/O against a real agent, on-screen rendering, background lifecycle) is
implemented per-spec but unverified in this environment.**
