package com.jn.winremote.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Persists paired-device records (device_id, device_secret, pinned cert
 * fingerprint, host/port) in [EncryptedSharedPreferences] backed by the
 * Android Keystore. Supports multiple paired PCs; one of them is marked
 * "active" (the one the Dashboard etc. connect to).
 *
 * Nothing here ever calls Log.* or println with a [PairedDevice] — see its
 * redacted [PairedDevice.toString].
 */
class SecureStore(context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    private val prefs: SharedPreferences = run {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            PREFS_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    @Synchronized
    fun listDevices(): List<PairedDevice> {
        val raw = prefs.getString(KEY_DEVICES, null) ?: return emptyList()
        return try {
            json.decodeFromString<List<PairedDevice>>(raw)
        } catch (_: Exception) {
            emptyList()
        }
    }

    @Synchronized
    fun getDevice(deviceId: String): PairedDevice? =
        listDevices().firstOrNull { it.deviceId == deviceId }

    @Synchronized
    fun getActiveDevice(): PairedDevice? {
        val activeId = prefs.getString(KEY_ACTIVE_DEVICE_ID, null) ?: return listDevices().firstOrNull()
        return getDevice(activeId) ?: listDevices().firstOrNull()
    }

    @Synchronized
    fun setActiveDevice(deviceId: String) {
        prefs.edit().putString(KEY_ACTIVE_DEVICE_ID, deviceId).apply()
    }

    @Synchronized
    fun upsertDevice(device: PairedDevice) {
        val updated = listDevices().filterNot { it.deviceId == device.deviceId } + device
        saveDevices(updated)
        if (prefs.getString(KEY_ACTIVE_DEVICE_ID, null) == null) {
            setActiveDevice(device.deviceId)
        }
    }

    /** Forgets a paired PC (used by the "unpair" / "forget" action in Ayarlar). */
    @Synchronized
    fun removeDevice(deviceId: String) {
        val remaining = listDevices().filterNot { it.deviceId == deviceId }
        saveDevices(remaining)
        if (prefs.getString(KEY_ACTIVE_DEVICE_ID, null) == deviceId) {
            prefs.edit().apply {
                if (remaining.isEmpty()) remove(KEY_ACTIVE_DEVICE_ID)
                else putString(KEY_ACTIVE_DEVICE_ID, remaining.first().deviceId)
                apply()
            }
        }
    }

    /** Updates the pinned fingerprint for a device after an explicit user-approved re-pair. */
    @Synchronized
    fun updatePinnedFingerprint(deviceId: String, newFingerprintHex: String) {
        val device = getDevice(deviceId) ?: return
        upsertDevice(device.copy(pinnedFingerprintHex = newFingerprintHex))
    }

    private fun saveDevices(devices: List<PairedDevice>) {
        prefs.edit().putString(KEY_DEVICES, json.encodeToString(devices)).apply()
    }

    companion object {
        private const val PREFS_FILE_NAME = "winremote_secure_prefs"
        private const val KEY_DEVICES = "paired_devices_json"
        private const val KEY_ACTIVE_DEVICE_ID = "active_device_id"
    }
}
