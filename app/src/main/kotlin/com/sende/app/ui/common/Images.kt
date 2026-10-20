package com.sende.app.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sende.app.data.apps.AppIconLoader
import com.sende.app.data.media.MediaLoader
import com.sende.app.ui.theme.Tek

/** Loaders are provided once at the root (from Koin in the app, from fakes in screenshot tests). */
val LocalAppIconLoader = compositionLocalOf<AppIconLoader> { error("AppIconLoader not provided") }
val LocalMediaLoader = compositionLocalOf<MediaLoader> { error("MediaLoader not provided") }

/** Decoded picture from app-private storage, or null while loading / when [path] is null. */
@Composable
fun rememberStoredImage(path: String?, maxPx: Int): State<ImageBitmap?> {
    val loader = LocalMediaLoader.current
    return produceState(initialValue = path?.let { loader.cached(it, maxPx) }, path, maxPx) {
        value = if (path == null) null else loader.load(path, maxPx)
    }
}

/** The *real* launcher icon of an installed app (WhatsApp looks like WhatsApp), as a round chip. */
@Composable
fun AppIcon(packageName: String, size: Dp, modifier: Modifier = Modifier) {
    val loader = LocalAppIconLoader.current
    val icon by produceState(initialValue = loader.cached(packageName), packageName) {
        value = loader.load(packageName)
    }
    val bitmap = icon
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier.size(size).clip(CircleShape),
        )
    } else {
        Box(modifier.size(size).clip(CircleShape).background(Tek.colors.surfaceHigh))
    }
}

/** Profile photo when one was captured, otherwise a stable coloured circle with initials. */
@Composable
fun Avatar(name: String, photoPath: String?, size: Dp, modifier: Modifier = Modifier) {
    val photo by rememberStoredImage(photoPath, maxPx = 192)
    val bitmap = photo
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.size(size).clip(CircleShape),
        )
    } else {
        val dark = Tek.colors.isDark
        val tint = remember(name, dark) { initialsColor(name, dark) }
        Box(modifier.size(size).clip(CircleShape).background(tint), contentAlignment = Alignment.Center) {
            Text(
                text = initialsOf(name),
                color = if (dark) Color(0xFFF2F0EA) else Color(0xFF1B1B1B),
                fontSize = (size.value * 0.38f).sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

internal fun initialsOf(name: String): String {
    val words = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    return when {
        words.isEmpty() -> "?"
        words.size == 1 -> words[0].take(1).uppercase()
        else -> (words[0].take(1) + words[1].take(1)).uppercase()
    }
}

internal fun initialsColor(name: String, dark: Boolean): Color {
    val hue = ((name.lowercase().hashCode() % 360) + 360) % 360
    return Color.hsv(hue.toFloat(), if (dark) 0.38f else 0.30f, if (dark) 0.50f else 0.88f)
}
