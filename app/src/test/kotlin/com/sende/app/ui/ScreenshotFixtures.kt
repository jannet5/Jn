package com.sende.app.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import com.sende.app.data.apps.AppIconLoader
import com.sende.app.data.media.MediaLoader
import com.sende.app.domain.model.CaptureMethod
import com.sende.app.domain.model.Channel
import com.sende.app.domain.model.Conversation
import com.sende.app.domain.model.InboxMessage
import com.sende.app.domain.model.MessageState
import com.sende.app.ui.main.BarItem
import com.sende.app.ui.main.MainUiState
import java.io.File
import java.time.Instant

/** Fake-but-realistic data for the screenshot tests: Turkish small-business chats across several apps. */
object ScreenshotFixtures {

    const val WHATSAPP = "com.whatsapp"
    const val WHATSAPP_BUSINESS = "com.whatsapp.w4b"
    const val INSTAGRAM = "com.instagram.android"
    const val TELEGRAM = "org.telegram.messenger"

    private val iconColors = mapOf(
        WHATSAPP to 0xFF25D366.toInt(),
        WHATSAPP_BUSINESS to 0xFF128C7E.toInt(),
        INSTAGRAM to 0xFFD62976.toInt(),
        TELEGRAM to 0xFF2AABEE.toInt(),
        "com.some.shop.app" to 0xFFF2B544.toInt(),
        "com.android.chrome" to 0xFF4285F4.toInt(),
    )

    private fun iconBitmap(color: Int, letter: String): Bitmap {
        val bitmap = Bitmap.createBitmap(144, 144, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(color)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = 0xFFFFFFFF.toInt()
            textSize = 72f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
        canvas.drawText(letter, 72f, 100f, paint)
        return bitmap
    }

    /** Wraps [content] in a Koin whose icon/media loaders are pre-filled with stand-in icons (real ones come from the phone). */
    @androidx.compose.runtime.Composable
    fun WithIcons(context: Context, extraIcons: Map<String, Int> = emptyMap(), content: @androidx.compose.runtime.Composable () -> Unit) {
        val icons = androidx.compose.runtime.remember {
            AppIconLoader(context).also { loader ->
                (iconColors + extraIcons).forEach { (pkg, color) ->
                    loader.prime(pkg, iconBitmap(color, pkg.substringAfterLast('.').take(1).uppercase()).asImageBitmap())
                }
            }
        }
        androidx.compose.runtime.CompositionLocalProvider(
            com.sende.app.ui.common.LocalAppIconLoader provides icons,
            com.sende.app.ui.common.LocalMediaLoader provides MediaLoader(),
        ) { content() }
    }

    fun pictureFile(dir: File): String {
        val bitmap = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(0xFF3A4A5E.toInt())
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFF2B544.toInt() }
        canvas.drawCircle(480f, 140f, 70f, paint)
        paint.color = 0xFF22303F.toInt()
        canvas.drawRect(0f, 330f, 640f, 480f, paint)
        val file = File(dir, "photo.png")
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        return file.absolutePath
    }

    private var counter = 0
    private fun message(
        pkg: String, conversation: String, sender: String, text: String, minutesAgo: Long,
        group: Boolean = false, picture: String? = null,
    ): InboxMessage {
        val at = Instant.now().minusSeconds(minutesAgo * 60)
        counter++
        return InboxMessage(
            id = "m$counter", notificationKey = "k", fingerprint = "f$counter", sourcePackage = pkg,
            conversationKey = "$pkg|$conversation", conversationTitle = conversation, isGroupConversation = group,
            senderName = sender, messageText = text, receivedAt = at, capturedAt = at,
            captureMethod = CaptureMethod.NOTIFICATION, state = MessageState.UNREAD, avatarPath = null, picturePath = picture,
        )
    }

    fun conversations(pictureDir: File): List<Conversation> {
        counter = 0
        fun conv(messages: List<InboxMessage>) = Conversation(
            key = messages.first().conversationKey, sourcePackage = messages.first().sourcePackage,
            title = messages.first().conversationTitle, isGroup = messages.first().isGroupConversation,
            avatarPath = null, messages = messages,
        )
        val picture = pictureFile(pictureDir)
        return listOf(
            conv(
                listOf(
                    message(WHATSAPP, "Ayşe Yılmaz", "Ayşe Yılmaz", "Merhaba, randevu almak istiyorum", 12),
                    message(WHATSAPP, "Ayşe Yılmaz", "Ayşe Yılmaz", "Cumartesi saat 14:00 uygun mu? Saçımı kestirip boyatmak istiyorum, yanımda kızım da gelecek, ona da küçük bir bakım yapabilir misiniz?", 4),
                    message(WHATSAPP, "Ayşe Yılmaz", "Ayşe Yılmaz", "Fiyatı da öğrenebilir miyim?", 1),
                ),
            ),
            conv(
                listOf(
                    message(WHATSAPP_BUSINESS, "Güzellik Salonu Müşterileri", "Zeynep", "Yarın açık mısınız?", 25, group = true),
                    message(WHATSAPP_BUSINESS, "Güzellik Salonu Müşterileri", "Can", "Ben de öğleden sonra gelebilirim", 22, group = true),
                ),
            ),
            conv(listOf(message(INSTAGRAM, "mehmet_demir", "mehmet_demir", "Bu modelin lacivert rengi var mı?", 41, picture = picture))),
            conv(
                listOf(
                    message(TELEGRAM, "Selin Aksoy", "Selin Aksoy", "Siparişim ne zaman kargoya verilir?", 90),
                    message(TELEGRAM, "Selin Aksoy", "Selin Aksoy", "Takip numarası gelmedi hâlâ", 70),
                ),
            ),
        )
    }

    val labels = mapOf(
        WHATSAPP to "WhatsApp",
        WHATSAPP_BUSINESS to "WhatsApp Business",
        INSTAGRAM to "Instagram",
        TELEGRAM to "Telegram",
    )

    val bar = listOf(
        BarItem(WHATSAPP, "WhatsApp", 3),
        BarItem(WHATSAPP_BUSINESS, "WhatsApp Business", 2),
        BarItem(INSTAGRAM, "Instagram", 1),
        BarItem(TELEGRAM, "Telegram", 2),
    )

    fun state(pictureDir: File, selected: String? = null, accessGranted: Boolean = true, empty: Boolean = false): MainUiState {
        val all = if (empty) emptyList() else conversations(pictureDir)
        val shown = if (selected == null) all else all.filter { it.sourcePackage == selected }
        return MainUiState(
            loaded = true, accessGranted = accessGranted, bar = bar, selectedPackage = selected,
            totalUnread = bar.sumOf { it.unread }, conversations = shown, labels = labels, hasEnabledChannels = true,
        )
    }

    fun pickerChannels(): List<Channel> = listOf(
        Channel(WHATSAPP, "WhatsApp", true, 148, 3, 0, true),
        Channel(WHATSAPP_BUSINESS, "WhatsApp Business", true, 61, 2, 0, true),
        Channel(INSTAGRAM, "Instagram", true, 23, 1, 0, true),
        Channel(TELEGRAM, "Telegram", false, 9, 0, 0, true),
        Channel("com.some.shop.app", "Mağaza Paneli", false, 0, 0, 0, false),
        Channel("com.android.chrome", "Chrome", false, 0, 0, 0, false),
    )

    // ---- store graphics: the same screens with invented channel names, so no real app brand is shown ----

    private val storePackages = mapOf(
        WHATSAPP to "store.mesajlar",
        WHATSAPP_BUSINESS to "store.siparis",
        INSTAGRAM to "store.vitrin",
        TELEGRAM to "store.takip",
    )
    val storeIconColors = mapOf(
        "store.mesajlar" to 0xFF2F7D6B.toInt(),
        "store.siparis" to 0xFFB05A2F.toInt(),
        "store.vitrin" to 0xFF7A4E9C.toInt(),
        "store.takip" to 0xFF3B6EA5.toInt(),
        "store.kasa" to 0xFFC49A2C.toInt(),
        "store.rehber" to 0xFF5F6B73.toInt(),
    )
    private val storeLabels = mapOf(
        "store.mesajlar" to "Mesajlar", "store.siparis" to "Siparişler", "store.vitrin" to "Vitrin", "store.takip" to "Takip",
    )

    fun storeState(pictureDir: File, selected: String? = null): MainUiState {
        val all = conversations(pictureDir).map { conversation ->
            val pkg = storePackages.getValue(conversation.sourcePackage)
            conversation.copy(
                sourcePackage = pkg,
                key = conversation.key.replace(conversation.sourcePackage, pkg),
                messages = conversation.messages.map { it.copy(sourcePackage = pkg, conversationKey = it.conversationKey.replace(it.sourcePackage, pkg)) },
            )
        }
        val shown = if (selected == null) all else all.filter { it.sourcePackage == selected }
        val storeBar = bar.map { BarItem(storePackages.getValue(it.packageName), storeLabels.getValue(storePackages.getValue(it.packageName)), it.unread) }
        return MainUiState(
            loaded = true, accessGranted = true, bar = storeBar, selectedPackage = selected,
            totalUnread = storeBar.sumOf { it.unread }, conversations = shown, labels = storeLabels, hasEnabledChannels = true,
        )
    }

    fun storePickerChannels(): List<Channel> = listOf(
        Channel("store.mesajlar", "Mesajlar", true, 148, 3, 0, true),
        Channel("store.siparis", "Siparişler", true, 61, 2, 0, true),
        Channel("store.vitrin", "Vitrin", true, 23, 1, 0, true),
        Channel("store.takip", "Takip", false, 9, 0, 0, true),
        Channel("store.kasa", "Kasa", false, 0, 0, 0, false),
        Channel("store.rehber", "Rehber", false, 0, 0, 0, false),
    )
}
