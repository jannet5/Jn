package com.cepgozcu.app.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom
import java.util.Base64

data class AgentSession(
    val host: String,
    val port: Int,
    val certSha256: String,
    val deviceId: String,
    val sessionToken: String,
    val refreshToken: String,
)

/**
 * Everything here is per-device-pairing secret material (the bearer token in particular is
 * equivalent to a password for controlling the PC), so it lives in EncryptedSharedPreferences
 * (AES-256-GCM, key in the Android Keystore) rather than plain SharedPreferences, is excluded
 * from backups (see AndroidManifest / data_extraction_rules.xml), and is never logged.
 */
class CredentialStore(context: Context) {

    private val prefs: SharedPreferences = run {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "cepgozcu_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    fun saveSession(session: AgentSession) {
        prefs.edit()
            .putString(KEY_HOST, session.host)
            .putInt(KEY_PORT, session.port)
            .putString(KEY_CERT_SHA256, session.certSha256)
            .putString(KEY_DEVICE_ID, session.deviceId)
            .putString(KEY_SESSION_TOKEN, session.sessionToken)
            .putString(KEY_REFRESH_TOKEN, session.refreshToken)
            .apply()
    }

    fun loadSession(): AgentSession? {
        val host = prefs.getString(KEY_HOST, null) ?: return null
        val port = prefs.getInt(KEY_PORT, -1).takeIf { it > 0 } ?: return null
        val fp = prefs.getString(KEY_CERT_SHA256, null) ?: return null
        val deviceId = prefs.getString(KEY_DEVICE_ID, null) ?: return null
        val token = prefs.getString(KEY_SESSION_TOKEN, null) ?: return null
        val refresh = prefs.getString(KEY_REFRESH_TOKEN, null) ?: return null
        return AgentSession(host, port, fp, deviceId, token, refresh)
    }

    fun updateTokens(sessionToken: String, refreshToken: String) {
        prefs.edit()
            .putString(KEY_SESSION_TOKEN, sessionToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    /** A stable, random per-install identifier sent at pairing time so the PC's admin page can tell devices apart. Not used for cryptographic authentication in this version — see README security model. */
    fun getOrCreateInstallIdentifier(): String {
        prefs.getString(KEY_INSTALL_ID, null)?.let { return it }
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        val id = Base64.getEncoder().withoutPadding().encodeToString(bytes)
        prefs.edit().putString(KEY_INSTALL_ID, id).apply()
        return id
    }

    companion object {
        private const val KEY_HOST = "host"
        private const val KEY_PORT = "port"
        private const val KEY_CERT_SHA256 = "cert_sha256"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_SESSION_TOKEN = "session_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_INSTALL_ID = "install_id"
    }
}
