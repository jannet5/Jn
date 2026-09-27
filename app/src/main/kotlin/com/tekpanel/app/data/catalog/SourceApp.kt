package com.tekpanel.app.data.catalog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.tekpanel.app.R

/**
 * A single supported message channel (spec CAP-02).
 *
 * [packageNames] can list more than one package for the same brand (e.g. TikTok ships two
 * package ids across regions/builds). [id] is the stable identifier stored in Room/DataStore;
 * it must never change once shipped, even if package names are added later.
 */
data class SourceApp(
    val id: String,
    val packageNames: List<String>,
    @StringRes val displayNameRes: Int,
    @DrawableRes val logoRes: Int,
    val accentColor: Color,
)

/**
 * The known-good catalog of supported messaging/social apps (spec CAP-02).
 *
 * This is a closed catalog by design: TekPanel never queries "all installed apps" (Play
 * policy + spec 3.5). Adding a new supported app means adding an entry here AND a matching
 * <package> entry in AndroidManifest.xml's <queries> block.
 */
object SourceAppCatalog {

    val WHATSAPP = SourceApp(
        id = "whatsapp",
        packageNames = listOf("com.whatsapp"),
        displayNameRes = R.string.channel_whatsapp,
        logoRes = R.drawable.logo_whatsapp,
        accentColor = Color(0xFF25D366),
    )

    val WHATSAPP_BUSINESS = SourceApp(
        id = "whatsapp_business",
        packageNames = listOf("com.whatsapp.w4b"),
        displayNameRes = R.string.channel_whatsapp_business,
        logoRes = R.drawable.logo_whatsapp_business,
        accentColor = Color(0xFF25D366),
    )

    val INSTAGRAM = SourceApp(
        id = "instagram",
        packageNames = listOf("com.instagram.android"),
        displayNameRes = R.string.channel_instagram,
        logoRes = R.drawable.logo_instagram,
        accentColor = Color(0xFFE1306C),
    )

    val TIKTOK = SourceApp(
        id = "tiktok",
        packageNames = listOf("com.zhiliaoapp.musically", "com.ss.android.ugc.trill"),
        displayNameRes = R.string.channel_tiktok,
        logoRes = R.drawable.logo_tiktok,
        accentColor = Color(0xFF00F2EA),
    )

    val X_TWITTER = SourceApp(
        id = "x_twitter",
        packageNames = listOf("com.twitter.android"),
        displayNameRes = R.string.channel_x,
        logoRes = R.drawable.logo_x,
        accentColor = Color(0xFF1D9BF0),
    )

    val MESSENGER = SourceApp(
        id = "messenger",
        packageNames = listOf("com.facebook.orca"),
        displayNameRes = R.string.channel_messenger,
        logoRes = R.drawable.logo_messenger,
        accentColor = Color(0xFF0084FF),
    )

    val FACEBOOK = SourceApp(
        id = "facebook",
        packageNames = listOf("com.facebook.katana"),
        displayNameRes = R.string.channel_facebook,
        logoRes = R.drawable.logo_facebook,
        accentColor = Color(0xFF1877F2),
    )

    val TELEGRAM = SourceApp(
        id = "telegram",
        packageNames = listOf("org.telegram.messenger", "org.telegram.messenger.web"),
        displayNameRes = R.string.channel_telegram,
        logoRes = R.drawable.logo_telegram,
        accentColor = Color(0xFF29B6F6),
    )

    val GOOGLE_MESSAGES = SourceApp(
        id = "google_messages",
        packageNames = listOf("com.google.android.apps.messaging"),
        displayNameRes = R.string.channel_google_messages,
        logoRes = R.drawable.logo_google_messages,
        accentColor = Color(0xFF1A73E8),
    )

    val SAMSUNG_MESSAGES = SourceApp(
        id = "samsung_messages",
        packageNames = listOf("com.samsung.android.messaging"),
        displayNameRes = R.string.channel_samsung_messages,
        logoRes = R.drawable.logo_samsung_messages,
        accentColor = Color(0xFF1428A0),
    )

    val AOSP_SMS = SourceApp(
        id = "aosp_sms",
        packageNames = listOf("com.android.mms"),
        displayNameRes = R.string.channel_sms,
        logoRes = R.drawable.logo_sms,
        accentColor = Color(0xFF8AB4F8),
    )

    val XIAOMI_MESSAGES = SourceApp(
        id = "xiaomi_messages",
        packageNames = listOf("com.miui.mms"),
        displayNameRes = R.string.channel_xiaomi_messages,
        logoRes = R.drawable.logo_xiaomi_messages,
        accentColor = Color(0xFFFF6900),
    )

    val ALL: List<SourceApp> = listOf(
        WHATSAPP,
        WHATSAPP_BUSINESS,
        INSTAGRAM,
        TIKTOK,
        X_TWITTER,
        MESSENGER,
        FACEBOOK,
        TELEGRAM,
        GOOGLE_MESSAGES,
        SAMSUNG_MESSAGES,
        AOSP_SMS,
        XIAOMI_MESSAGES,
    )

    private val packageToApp: Map<String, SourceApp> =
        ALL.flatMap { app -> app.packageNames.map { pkg -> pkg to app } }.toMap()

    fun byId(id: String): SourceApp? = ALL.find { it.id == id }

    fun byPackageName(packageName: String): SourceApp? = packageToApp[packageName]

    fun isSupportedPackage(packageName: String): Boolean = packageToApp.containsKey(packageName)
}
