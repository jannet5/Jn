package com.jn.melodizil.ui.components // Ortak bileşenler

import androidx.compose.foundation.background // Arka plan
import androidx.compose.foundation.layout.Column // Sütun
import androidx.compose.foundation.layout.ColumnScope // Sütun kapsamı
import androidx.compose.foundation.layout.PaddingValues // Padding
import androidx.compose.foundation.layout.WindowInsets // Insets
import androidx.compose.foundation.layout.fillMaxSize // Tam boyut
import androidx.compose.foundation.layout.padding // Padding
import androidx.compose.foundation.layout.safeDrawing // Güvenli alan
import androidx.compose.foundation.layout.windowInsetsPadding // Insets padding
import androidx.compose.foundation.rememberScrollState // Kaydırma durumu
import androidx.compose.foundation.verticalScroll // Dikey kaydırma
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.runtime.Composable // Composable
import androidx.compose.ui.Modifier // Modifier
import com.jn.melodizil.ui.theme.Space // Boşluk token'ları

/**
 * Her ekranı saran kök bileşen: arka plan + güvenli alan + standart yatay kenar boşluğu (16) + isteğe bağlı kaydırma.
 * Ekranlar kendi padding'ini uydurmaz; hepsi buradan geçer.
 */
@Composable
fun Screen(
    modifier: Modifier = Modifier, // Dış modifier
    scrollable: Boolean = true, // İçerik kaydırılabilir mi
    contentPadding: PaddingValues = PaddingValues(horizontal = Space.lg), // Standart kenar
    content: @Composable ColumnScope.() -> Unit, // İçerik
) {
    val base = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).windowInsetsPadding(WindowInsets.safeDrawing) // Arka plan + güvenli alan
    val body = if (scrollable) base.verticalScroll(rememberScrollState()) else base // Kaydırma
    Column(modifier = body.padding(contentPadding), content = content) // İçerik sütunu
}
