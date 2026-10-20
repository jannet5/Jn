package com.sende.app.data.catalog

/**
 * Package names Sende recognises as places where customers write to a business. This is
 * *only* a hint: every launchable app on the phone can be picked as a channel; these just
 * decide which apps are pre-selected on the very first launch and which rank on top of the
 * picker before the user has any message history.
 */
object KnownApps {

    /** Pre-selected on first launch when installed: the usual customer-facing chat apps. */
    val DEFAULT_ON: Set<String> = setOf(
        "com.whatsapp",
        "com.whatsapp.w4b",
        "com.instagram.android",
        "com.facebook.orca",
        "org.telegram.messenger",
        "org.telegram.messenger.web",
        "com.zhiliaoapp.musically",
        "com.ss.android.ugc.trill",
        "com.twitter.android",
        "com.google.android.apps.messaging",
        "com.samsung.android.messaging",
        "com.android.mms",
        "com.miui.mms",
    )

    /** Popular chat/mail apps: not pre-selected, but ranked above the alphabetical tail. */
    val POPULAR: Set<String> = DEFAULT_ON + setOf(
        "com.facebook.katana",
        "org.thoughtcrime.securesms",
        "com.viber.voip",
        "jp.naver.line.android",
        "com.discord",
        "com.snapchat.android",
        "com.google.android.gm",
        "com.microsoft.office.outlook",
        "com.slack",
        "com.microsoft.teams",
        "com.skype.raider",
        "com.google.android.apps.dynamite",
        "im.vector.app",
        "com.linkedin.android",
    )

    /**
     * Engagement notifications from social apps often use the app's own name as the "sender"
     * (title = "Instagram") instead of a real username, because they are not a direct message.
     */
    val GENERIC_SENDER_NAMES: Map<String, Set<String>> = mapOf(
        "com.instagram.android" to setOf("instagram"),
        "com.zhiliaoapp.musically" to setOf("tiktok"),
        "com.ss.android.ugc.trill" to setOf("tiktok"),
        "com.twitter.android" to setOf("x", "twitter"),
        "com.facebook.katana" to setOf("facebook"),
    )
}
