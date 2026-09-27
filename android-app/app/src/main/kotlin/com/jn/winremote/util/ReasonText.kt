package com.jn.winremote.util

/**
 * Turkish user-facing text for every `reason` code PROTOCOL.md defines.
 * Kept as pure string mapping so it's unit-testable and centralizes the
 * copy instead of scattering `when` blocks with Turkish literals across
 * every screen.
 */
object ReasonText {

    // pair_failed (§4.2) / auth_failed (§4.3)
    private val map: Map<String, String> = mapOf(
        "invalid_code" to "Eşleştirme kodu geçersiz.",
        "expired_code" to "Eşleştirme kodunun süresi doldu. Windows bilgisayarında yeni bir kod alın.",
        "locked_out" to "Çok fazla başarısız deneme yapıldı; bu bağlantı bir süre kilitlendi.",
        "bad_hmac" to "Kimlik doğrulama başarısız (cihaz anahtarı eşleşmedi).",
        "unknown_device" to "Bu cihaz sunucuda tanınmıyor. Yeniden eşleştirmeniz gerekebilir.",
        "revoked" to "Bu cihazın erişimi sunucu tarafından iptal edildi.",
        // kill_process action_result failures (§4.5)
        "critical_process_protected" to "Bu süreç korumalı olduğu için sonlandırılamaz.",
        "not_found" to "Süreç bulunamadı (zaten kapanmış olabilir).",
        "access_denied" to "Erişim reddedildi.",
        // launch_app action_result failures (§4.6)
        "not_allow_listed" to "Bu uygulama izin listesinde değil.",
        "launch_failed" to "Uygulama başlatılamadı.",
        // error (§4.9)
        "unauthenticated" to "Bu işlem için kimlik doğrulaması gerekiyor.",
        "bad_request" to "Geçersiz istek.",
        "internal_error" to "Sunucuda beklenmeyen bir hata oluştu.",
    )

    fun forCode(reasonCode: String?): String {
        if (reasonCode == null) return "Bilinmeyen hata."
        return map[reasonCode] ?: "Bilinmeyen hata ($reasonCode)."
    }
}
