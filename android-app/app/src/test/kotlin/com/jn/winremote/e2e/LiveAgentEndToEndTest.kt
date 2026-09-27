package com.jn.winremote.e2e

import com.jn.winremote.data.ActiveDeviceProvider
import com.jn.winremote.data.PairedDevice
import com.jn.winremote.protocol.FileEventData
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.repository.PairingClient
import com.jn.winremote.repository.PairingOutcome
import com.jn.winremote.repository.WinRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

/**
 * A genuine end-to-end interoperability test: the Android app's REAL
 * protocol/crypto/repository code (PairingClient, WinRemoteRepository,
 * Protocol.kt, HmacAuth, CertPinning - the exact classes shipped in the
 * app) talking, over a real TLS WebSocket socket, to a REAL running
 * instance of the windows-agent server built from windows-agent/. No part
 * of this test is mocked or simulated: the agent process is a real OS
 * process, the metrics are its real host's real CPU/RAM/disk, the killed/
 * launched processes are real OS processes, and the file events come from
 * a real fsnotify watch on a real directory.
 *
 * This exists because unit tests on each side, run in isolation, cannot
 * catch real wire-format/behavior mismatches between two independently
 * built implementations of the same protocol (and in fact this test did
 * catch one - see PROTOCOL.md §10's QR field name and the pairing
 * cross-process persistence fix in windows-agent).
 *
 * It needs a live agent process and is therefore NOT part of the normal
 * hermetic test run: it self-skips (JUnit "assumption failed", reported
 * as ignored/skipped, not failed) unless E2E_AGENT_HOST is set. To run it
 * for real:
 *
 *   1. Start a real windows-agent instance (built for this machine's OS,
 *      e.g. `go build -o /tmp/e2e-agent ./cmd/agent` from windows-agent/)
 *      pointed at a scratch config/data-dir with a watched_roots entry
 *      and an allowed_apps.json entry.
 *   2. Run `agent --pair` (a separate process/invocation) against the
 *      same --data-dir/--config while the server from step 1 is running,
 *      and note the printed pairing code + cert fingerprint.
 *   3. Run this test with:
 *      E2E_AGENT_HOST=127.0.0.1 E2E_AGENT_PORT=8797 \
 *      E2E_AGENT_FINGERPRINT=<fingerprint> E2E_PAIRING_CODE=<code> \
 *      E2E_WATCH_DIR=<the watched_roots directory> \
 *      E2E_ALLOWED_APP_ID=<an allowed_apps.json app_id> \
 *      ./gradlew testDebugUnitTest --tests '*LiveAgentEndToEndTest*'
 */
class LiveAgentEndToEndTest {

    private fun env(name: String): String? = System.getenv(name)?.takeIf { it.isNotBlank() }
    private fun requiredEnv(name: String): String =
        checkNotNull(env(name)) { "$name must be set to run this live-agent E2E test" }

    @Test
    fun `real pairing, auth, metrics, process kill, app launch, and file events against a live agent`() =
        runBlocking {
            assumeTrue(
                "Skipping live-agent E2E test: E2E_AGENT_HOST not set (no live windows-agent instance configured for this run). See class doc for how to run it for real.",
                env("E2E_AGENT_HOST") != null,
            )
            val host = requiredEnv("E2E_AGENT_HOST")
            val port = env("E2E_AGENT_PORT")?.toIntOrNull() ?: 8797
            val fingerprint = requiredEnv("E2E_AGENT_FINGERPRINT")
            val pairingCode = requiredEnv("E2E_PAIRING_CODE")
            val watchDir = File(requiredEnv("E2E_WATCH_DIR"))
            val allowedAppId = requiredEnv("E2E_ALLOWED_APP_ID")

            // --- 1. Real pairing handshake against the live agent ---
            val pairing = PairingClient().pair(
                host = host,
                port = port,
                pinnedFingerprintHex = fingerprint,
                pairingCode = pairingCode,
                deviceName = "jvm-e2e-test-device",
            )
            if (pairing !is PairingOutcome.Success) {
                fail("real pairing against the live agent failed: $pairing")
                return@runBlocking
            }
            val paired = pairing.device
            assertTrue("device_id should be non-empty", paired.deviceId.isNotBlank())
            assertTrue("device_secret should be non-empty", paired.deviceSecretB64.isNotBlank())

            // --- 2. Connect + real challenge-response re-auth over a fresh socket ---
            val repo = WinRemoteRepository(object : ActiveDeviceProvider {
                override fun getActiveDevice(): PairedDevice? = null
            })
            val statusLog = CopyOnWriteArrayList<ConnectionStatus>()
            val statusJob: Job = launch(Dispatchers.IO) {
                repo.connectionStatus.collect { statusLog.add(it) }
            }
            try {
                repo.connect(paired)
                val connected = waitUntil(15_000) { repo.connectionStatus.value is ConnectionStatus.Connected }
                assertTrue(
                    "expected Connected within 15s, last status seen: ${statusLog.lastOrNull()}",
                    connected,
                )

                // --- 3. Real live metrics push from the real host ---
                val metrics = waitForValue(5_000) { repo.metrics.value }
                if (metrics == null) {
                    fail("no real metrics push received within 5s")
                    return@runBlocking
                }
                assertTrue("cpu_percent should be in [0,100], got ${metrics.cpuPercent}", metrics.cpuPercent in 0.0..100.0)
                assertTrue("ram total_bytes should be > 0 (this host's real RAM)", metrics.ram.totalBytes > 0)
                assertTrue("ram used_bytes should be <= total_bytes", metrics.ram.usedBytes <= metrics.ram.totalBytes)

                // --- 4. Real process list from the real host OS ---
                val processes = repo.listProcesses().getOrThrow()
                assertTrue("expected at least one real running process", processes.items.isNotEmpty())
                assertTrue(processes.items.all { it.pid > 0 })

                // --- 5. Kill a real process we spawn ourselves ---
                // (Process.pid()/ProcessHandle aren't available against the
                // Android SDK compile classpath used for this module's unit
                // tests, so the real pid is obtained the portable way: the
                // shell prints its own $$ before exec'ing into the real
                // long-running command we want the agent to kill.)
                val victim = ProcessBuilder("sh", "-c", "echo \$\$; exec sleep 300").start()
                val victimPid = victim.inputStream.bufferedReader().readLine().trim().toInt()
                try {
                    // Give the agent's process lister a moment to be able to see it
                    // (it re-queries the live OS process table on demand, no caching
                    // delay expected, but be generous for a loaded CI-like box).
                    delay(500)
                    val killResult = repo.killProcess(victimPid).getOrThrow()
                    assertTrue("kill_process should succeed for a normal, unprotected process we own: ${killResult.reason}", killResult.success)
                    val actuallyDied = withTimeoutOrNull(5_000) {
                        while (victim.isAlive) delay(50)
                        true
                    } ?: false
                    assertTrue("the real OS process must actually be dead after a successful kill_process", actuallyDied)
                } finally {
                    if (victim.isAlive) victim.destroyForcibly()
                }

                // --- 6. Allow-listed app launch (real process start) + cleanup kill ---
                val allowedApps = repo.listAllowedApps().getOrThrow()
                assertTrue(
                    "expected app_id '$allowedAppId' in the agent's real allow-list, got: ${allowedApps.items.map { it.appId }}",
                    allowedApps.items.any { it.appId == allowedAppId },
                )
                val launchResult = repo.launchApp(allowedAppId).getOrThrow()
                assertTrue("launch_app should succeed for an allow-listed app: ${launchResult.reason}", launchResult.success)
                val launchedPid = checkNotNull(launchResult.pid) { "launch_app succeeded but returned no pid" }
                // Portable "does this pid really exist" check (ProcessHandle
                // isn't available on this module's Android SDK compile
                // classpath) - `kill -0` sends no signal, it only checks
                // whether the process exists and is signalable.
                val launchedIsRunning = ProcessBuilder("kill", "-0", launchedPid.toString()).start().waitFor() == 0
                assertTrue(
                    "the launched app should be a real, currently-running OS process",
                    launchedIsRunning,
                )
                val cleanupKill = repo.killProcess(launchedPid).getOrThrow()
                assertTrue("cleanup kill of the launched app should succeed", cleanupKill.success)

                // --- 7. Real file-system events from a real fsnotify watch ---
                val fileEvents = CopyOnWriteArrayList<FileEventData>()
                val fileEventsJob = launch(Dispatchers.IO) {
                    repo.liveFileEvents.collect { fileEvents.add(it) }
                }
                delay(300) // let the collector actually attach before we act
                val testFile = File(watchDir, "e2e-test-file-${System.currentTimeMillis()}.txt")
                testFile.writeText("hello from the real end-to-end test")
                val sawCreateOrModify = waitUntil(5_000) {
                    fileEvents.any { it.path == testFile.absolutePath && (it.op == "created" || it.op == "modified") }
                }
                assertTrue(
                    "expected a real created/modified file_event for ${testFile.absolutePath} within 5s, got: $fileEvents",
                    sawCreateOrModify,
                )
                testFile.delete()
                val sawDelete = waitUntil(5_000) {
                    fileEvents.any { it.path == testFile.absolutePath && it.op == "deleted" }
                }
                assertTrue(
                    "expected a real deleted file_event for ${testFile.absolutePath} within 5s, got: $fileEvents",
                    sawDelete,
                )
                fileEventsJob.cancel()

                // --- 8. Growth/alerts/history read paths ---
                repo.topGrowth("1h").getOrThrow()
                repo.listAlerts().getOrThrow()
                val history = repo.listHistory().getOrThrow()
                assertTrue(
                    "expected at least the pair/kill/launch actions performed above in the real history log, got total=${history.total}",
                    history.total >= 3,
                )
            } finally {
                repo.disconnect()
                statusJob.cancel()
            }
        }

    private suspend fun waitUntil(timeoutMillis: Long, condition: () -> Boolean): Boolean {
        return withTimeoutOrNull(timeoutMillis) {
            while (!condition()) delay(50)
            true
        } ?: false
    }

    private suspend fun <T> waitForValue(timeoutMillis: Long, supplier: () -> T?): T? {
        return withTimeoutOrNull(timeoutMillis) {
            var v = supplier()
            while (v == null) {
                delay(50)
                v = supplier()
            }
            v
        }
    }
}
