package com.jn.winremote.ui.pairing

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jn.winremote.util.Links

/**
 * First thing a new user sees: the phone app is useless until the Windows
 * program is installed, and that download has to reach a PC. Sharing the link
 * (WhatsApp, e-mail, ...) is the least technical way to get it there.
 */
@Composable
fun GettingStartedCard(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Nasıl başlarım?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("1. Windows bilgisayarınıza WinRemote Monitor programını kurun (ücretsiz, 1 dakika).")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { shareDownloadLink(context) }) { Text("Bağlantıyı gönder") }
                OutlinedButton(onClick = { copyDownloadLink(context) }) { Text("Kopyala") }
            }
            Text(
                Links.WINDOWS_DOWNLOAD_DISPLAY,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Text("2. Kurulum bitince bilgisayar ekranında bir QR kod açılır.")
            Text("3. Telefonu o QR koda tutun. Telefon ve bilgisayar aynı Wi-Fi'da olmalı.")
        }
    }
}

private fun shareDownloadLink(context: Context) {
    val text = "WinRemote Monitor Windows programı (bilgisayarda açın ve kurun):\n${Links.WINDOWS_DOWNLOAD}"
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "WinRemote Monitor - Windows programı")
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(send, "Bağlantıyı bilgisayara gönder"))
}

private fun copyDownloadLink(context: Context) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText("WinRemote Monitor", Links.WINDOWS_DOWNLOAD))
    Toast.makeText(context, "Bağlantı kopyalandı", Toast.LENGTH_SHORT).show()
}
