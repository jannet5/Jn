package com.tekpanel.app.capture

import com.google.common.truth.Truth.assertThat
import com.tekpanel.app.domain.model.FilterReason
import org.junit.Test

class MessageFilterEngineTest {

    private val engine = MessageFilterEngine()

    private fun payload(
        packageName: String = "com.whatsapp",
        isGroupSummary: Boolean = false,
        isOngoing: Boolean = false,
        category: String? = null,
    ) = RawNotificationPayload(
        notificationKey = "key",
        packageName = packageName,
        postTimeEpochMillis = 0L,
        title = "Ali Veli",
        text = "Merhaba, siparişim hakkında bilgi alabilir miyim?",
        bigText = null,
        textLines = emptyList(),
        conversationTitle = null,
        messagingStyleMessages = emptyList(),
        category = category,
        isGroupSummary = isGroupSummary,
        isOngoing = isOngoing,
        hasContentIntent = true,
    )

    private val content = NormalizedContent(senderName = "Ali Veli", messageText = "Merhaba, siparişim hakkında bilgi alabilir miyim?")

    @Test
    fun `rejects a notification from an unsupported package before anything else`() {
        val reason = engine.decide(payload(packageName = "com.some.random.game"), content, channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.UNSUPPORTED_PACKAGE)
    }

    @Test
    fun `rejects when the channel is disabled even if the package is supported`() {
        val reason = engine.decide(payload(), content, channelEnabled = false)
        assertThat(reason).isEqualTo(FilterReason.CHANNEL_DISABLED)
    }

    @Test
    fun `rejects when there is no usable normalized content`() {
        val reason = engine.decide(payload(), normalized = null, channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.EMPTY_CONTENT)
    }

    @Test
    fun `rejects a group summary notification`() {
        val reason = engine.decide(payload(isGroupSummary = true), content, channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.GROUP_SUMMARY)
    }

    @Test
    fun `rejects an ongoing notification such as an active call`() {
        val reason = engine.decide(payload(isOngoing = true), content, channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.ONGOING_SERVICE_CATEGORY)
    }

    @Test
    fun `rejects a known non-message category such as a missed call`() {
        val reason = engine.decide(payload(category = "call"), content, channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.ONGOING_SERVICE_CATEGORY)
    }

    @Test
    fun `rejects an Instagram engagement notification whose sender is just the app name`() {
        val instagramPayload = payload(packageName = "com.instagram.android")
        val engagementContent = NormalizedContent(senderName = "Instagram", messageText = "yeni takipçin var")

        val reason = engine.decide(instagramPayload, engagementContent, channelEnabled = true)

        assertThat(reason).isEqualTo(FilterReason.LIVE_OR_PROMOTIONAL_HEURISTIC)
    }

    @Test
    fun `accepts a genuine direct message from a real customer`() {
        val reason = engine.decide(payload(), content, channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.ACCEPTED)
    }

    @Test
    fun `does not drop a real customer message that happens to mention a filtered keyword`() {
        val mentionsShipping = NormalizedContent(
            senderName = "Deniz Kaya",
            messageText = "Kargo ile gönderim yapıyor musunuz, ne kadar sürer?",
        )

        val reason = engine.decide(payload(), mentionsShipping, channelEnabled = true)

        assertThat(reason).isEqualTo(FilterReason.ACCEPTED)
    }
}
