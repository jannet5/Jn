package com.jn.yazikart.ui // arayüz paketi

import androidx.compose.foundation.layout.Column // alt alta
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.navigationBarsPadding // alt çubuk boşluğu
import androidx.compose.foundation.layout.padding // iç boşluk
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.material3.ExperimentalMaterial3Api // alt pencere deneysel
import androidx.compose.material3.ModalBottomSheet // alttan pencere
import androidx.compose.material3.OutlinedTextField // yazı kutusu
import androidx.compose.material3.Text // yazı
import androidx.compose.material3.rememberModalBottomSheetState // alt pencere durumu
import androidx.compose.runtime.Composable // Compose bileşeni
import androidx.compose.runtime.LaunchedEffect // açılınca odak
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.focus.FocusRequester // odak isteyici
import androidx.compose.ui.focus.focusRequester // odak bağlayıcı
import androidx.compose.ui.unit.dp // ölçü birimi
import kotlinx.coroutines.delay // kısa bekleme (pencere yerleşsin diye)

// Önizlemeye dokununca açılır: klavye hemen gelir, yazdıkça önizleme (arkada) değişir
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextEditSheet(text: String, onText: (String) -> Unit, onDismiss: () -> Unit) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true) // tam açılsın (klavye için yer)
    val focus = remember { FocusRequester() } // yazı kutusunun odağı
    LaunchedEffect(Unit) { delay(150); focus.requestFocus() } // pencere yerleşince kutuya odak → klavye açılır
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = state) { // alt pencere
        Column(Modifier.navigationBarsPadding().padding(horizontal = 16.dp).padding(bottom = 24.dp)) { // içerik
            Text("Yazın (önizleme arkada anında değişir)", style = androidx.compose.material3.MaterialTheme.typography.labelLarge) // başlık
            OutlinedTextField( // yazı kutusu
                value = text, // güncel yazı
                onValueChange = onText, // her harfte güncellenir
                modifier = Modifier.fillMaxWidth().focusRequester(focus).padding(top = 8.dp), // tam genişlik, odak
                placeholder = { Text("Yazını buraya yaz…") }, // ipucu
                minLines = 2, // en az iki satır
                maxLines = 6, // en çok altı satır
                shape = RoundedCornerShape(12.dp), // yuvarlak köşe
            )
        }
    }
}
