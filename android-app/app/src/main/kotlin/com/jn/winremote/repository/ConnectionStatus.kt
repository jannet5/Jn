package com.jn.winremote.repository

/**
 * Mirrors the six connection states the product requires the UI to show
 * (Bağlı / Bağlanıyor / Yeniden bağlanıyor / Çevrimdışı / Yetkisiz / Hata).
 */
sealed class ConnectionStatus {
    /** No active device, or the user explicitly disconnected. */
    object Offline : ConnectionStatus()

    /** First attempt of a connection (not a retry after a drop). */
    object Connecting : ConnectionStatus()

    /** Authenticated and subscribed; [hostname] is the agent's reported name. */
    data class Connected(val hostname: String) : ConnectionStatus()

    /** Automatic retry after a transient drop. */
    data class Reconnecting(val attempt: Int, val nextDelayMillis: Long) : ConnectionStatus()

    /** auth_failed / pair_failed with a reason that won't resolve itself by retrying. */
    data class Unauthorized(val reasonCode: String) : ConnectionStatus()

    /** Anything else: cert pin mismatch, network failure, protocol/version error. */
    data class Error(val message: String, val isCertMismatch: Boolean = false) : ConnectionStatus()
}
