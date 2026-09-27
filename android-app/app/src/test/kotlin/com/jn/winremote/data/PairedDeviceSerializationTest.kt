package com.jn.winremote.data

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * [SecureStore] itself needs EncryptedSharedPreferences (Android framework),
 * so it isn't covered by a JVM unit test. What's fully testable — and
 * security-relevant — is that the record round-trips through the same JSON
 * codec [SecureStore] uses, and that its [toString] never leaks the secret.
 */
class PairedDeviceSerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    private fun sampleDevice() = PairedDevice(
        deviceId = "3f9c1234",
        deviceName = "Jake Pixel 8",
        host = "192.168.1.20",
        port = 8787,
        pinnedFingerprintHex = "a".repeat(64),
        deviceSecretB64 = "c3VwZXItc2VjcmV0LWRvLW5vdC1sb2c=",
        pairedAtEpochMs = 1_700_000_000_000L,
    )

    @Test
    fun `round trips through the same json codec SecureStore uses`() {
        val device = sampleDevice()
        val encoded = json.encodeToString(device)
        val decoded = json.decodeFromString<PairedDevice>(encoded)
        assertEquals(device, decoded)
    }

    @Test
    fun `list of devices round trips`() {
        val devices = listOf(sampleDevice(), sampleDevice().copy(deviceId = "other-id"))
        val encoded = json.encodeToString(devices)
        val decoded = json.decodeFromString<List<PairedDevice>>(encoded)
        assertEquals(devices, decoded)
    }

    @Test
    fun `toString never includes the raw device secret`() {
        val device = sampleDevice()
        val text = device.toString()
        assertFalse(text.contains(device.deviceSecretB64))
        assertTrue(text.contains("redacted"))
    }
}
