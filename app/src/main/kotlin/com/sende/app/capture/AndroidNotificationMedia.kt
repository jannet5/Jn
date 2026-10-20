package com.sende.app.capture

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream
import kotlin.math.max

/**
 * Reads the images a notification carries: the sender's profile photo and attached pictures.
 *
 * Everything is best-effort. Whether a picture is readable at all depends on the sending app
 * (it must expose it in the notification) and on Android granting this listener access to the
 * content URI; a failure to read is never an error, the message is simply shown without it.
 */
class AndroidNotificationMedia(
    private val context: Context,
    private val sbn: StatusBarNotification,
) : NotificationMedia {

    private val notification: Notification get() = sbn.notification

    private val messagingStyle: NotificationCompat.MessagingStyle? by lazy {
        try {
            NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(notification)
        } catch (_: Exception) {
            null
        }
    }

    override fun avatar(senderName: String?): ByteArray? = runCatching {
        val personIcon = messagingStyle?.messages.orEmpty()
            .firstOrNull { it.person?.name?.toString() == senderName && it.person?.icon != null }
            ?.person?.icon?.toIcon(context)
        val icon = personIcon ?: notification.getLargeIcon()
        icon?.let { encode(it.loadDrawable(context), AVATAR_MAX_PX, quality = 90) }
    }.getOrNull()

    override fun picture(lineIndex: Int?): ByteArray? = runCatching {
        if (lineIndex != null) {
            val message = messagingStyle?.messages?.getOrNull(lineIndex) ?: return@runCatching null
            val uri = message.dataUri ?: return@runCatching null
            if (message.dataMimeType?.startsWith("image/") != true) return@runCatching null
            context.contentResolver.openInputStream(uri)?.use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream) ?: return@use null
                encode(BitmapDrawable(context.resources, bitmap), PICTURE_MAX_PX, quality = 82)
            }
        } else {
            val extras = notification.extras
            @Suppress("DEPRECATION")
            val bitmap = extras.getParcelable<Bitmap>(Notification.EXTRA_PICTURE)
            when {
                bitmap != null -> encode(BitmapDrawable(context.resources, bitmap), PICTURE_MAX_PX, quality = 82)
                Build.VERSION.SDK_INT >= 31 -> {
                    @Suppress("DEPRECATION")
                    val icon = extras.getParcelable<Icon>("android.pictureIcon")
                    icon?.let { encode(it.loadDrawable(context), PICTURE_MAX_PX, quality = 82) }
                }
                else -> null
            }
        }
    }.getOrNull()

    private fun encode(drawable: Drawable?, maxPx: Int, quality: Int): ByteArray? {
        drawable ?: return null
        val srcW = drawable.intrinsicWidth.takeIf { it > 0 } ?: return null
        val srcH = drawable.intrinsicHeight.takeIf { it > 0 } ?: return null
        val scale = minOf(1f, maxPx.toFloat() / max(srcW, srcH))
        val bitmap = drawable.toBitmap((srcW * scale).toInt().coerceAtLeast(1), (srcH * scale).toInt().coerceAtLeast(1))
        val out = ByteArrayOutputStream()
        val format = if (Build.VERSION.SDK_INT >= 30) Bitmap.CompressFormat.WEBP_LOSSY else {
            @Suppress("DEPRECATION")
            Bitmap.CompressFormat.WEBP
        }
        return if (bitmap.compress(format, quality, out)) out.toByteArray() else null
    }

    private companion object {
        const val AVATAR_MAX_PX = 192
        const val PICTURE_MAX_PX = 1280
    }
}
