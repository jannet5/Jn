package com.sende.app.capture

import com.google.common.truth.Truth.assertThat
import com.sende.app.domain.model.FilterReason
import org.junit.Test

class MessageFilterEngineTest {

    private val engine = MessageFilterEngine(ownPackage = "com.sende.app")

    private fun content(sender: String = "Ali Veli", text: String = "Merhaba, siparişim hakkında bilgi alabilir miyim?", picture: Boolean = false) =
        NormalizedNotification(sender, false, listOf(NormalizedMessage(sender, text, null, null, picture)))

    @Test
    fun `never reads its own notifications`() {
        val reason = engine.decide(payloadOf(packageName = "com.sende.app"), content(), channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.UNSUPPORTED_PACKAGE)
    }

    @Test
    fun `never reads the android system itself`() {
        val reason = engine.decide(payloadOf(packageName = "android"), content(), channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.UNSUPPORTED_PACKAGE)
    }

    @Test
    fun `rejects when the user did not pick that app`() {
        val reason = engine.decide(payloadOf(), content(), channelEnabled = false)
        assertThat(reason).isEqualTo(FilterReason.CHANNEL_DISABLED)
    }

    @Test
    fun `accepts any app the user picked, not only well-known chat apps`() {
        val reason = engine.decide(payloadOf(packageName = "com.some.shop.app"), content(), channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.ACCEPTED)
    }

    @Test
    fun `rejects when there is no usable content`() {
        assertThat(engine.decide(payloadOf(), normalized = null, channelEnabled = true)).isEqualTo(FilterReason.EMPTY_CONTENT)
    }

    @Test
    fun `keeps a picture-only message`() {
        val reason = engine.decide(payloadOf(), content(text = "", picture = true), channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.ACCEPTED)
    }

    @Test
    fun `rejects a group summary notification`() {
        val reason = engine.decide(payloadOf(isGroupSummary = true), content(), channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.GROUP_SUMMARY)
    }

    @Test
    fun `rejects an ongoing notification such as an active call`() {
        val reason = engine.decide(payloadOf(isOngoing = true), content(), channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.ONGOING_SERVICE_CATEGORY)
    }

    @Test
    fun `rejects a known non-message category such as a missed call`() {
        val reason = engine.decide(payloadOf(category = "call"), content(), channelEnabled = true)
        assertThat(reason).isEqualTo(FilterReason.ONGOING_SERVICE_CATEGORY)
    }

    @Test
    fun `rejects an Instagram engagement notification whose sender is just the app name`() {
        val reason = engine.decide(
            payloadOf(packageName = "com.instagram.android"),
            content(sender = "Instagram", text = "yeni takipçin var"),
            channelEnabled = true,
        )
        assertThat(reason).isEqualTo(FilterReason.LIVE_OR_PROMOTIONAL_HEURISTIC)
    }

    @Test
    fun `does not drop a real customer message that happens to mention a filtered keyword`() {
        val reason = engine.decide(
            payloadOf(),
            content(sender = "Deniz Kaya", text = "Kargo ile gönderim yapıyor musunuz, ne kadar sürer?"),
            channelEnabled = true,
        )
        assertThat(reason).isEqualTo(FilterReason.ACCEPTED)
    }

    @Test
    fun `rejects a short live-broadcast alert`() {
        val reason = engine.decide(
            payloadOf(packageName = "com.instagram.android"),
            content(sender = "ali_veli", text = "ali_veli canlı yayında."),
            channelEnabled = true,
        )
        assertThat(reason).isEqualTo(FilterReason.LIVE_OR_PROMOTIONAL_HEURISTIC)
    }

    @Test
    fun `keeps a customer message that merely contains the words canli yayinda`() {
        val reason = engine.decide(
            payloadOf(packageName = "com.instagram.android"),
            content(sender = "Deniz Kaya", text = "Dün canlı yayında gördüğüm elbisenin fiyatı nedir?"),
            channelEnabled = true,
        )
        assertThat(reason).isEqualTo(FilterReason.ACCEPTED)
    }

    @Test
    fun `keeps a message in a chat whose title contains the words canli yayinda`() {
        val reason = engine.decide(
            payloadOf(packageName = "com.instagram.android", title = "Canlı Yayında Satış Grubu"),
            content(sender = "Deniz Kaya", text = "Sipariş vermek istiyorum"),
            channelEnabled = true,
        )
        assertThat(reason).isEqualTo(FilterReason.ACCEPTED)
    }
}
