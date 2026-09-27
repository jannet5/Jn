package com.jn.winremote.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.ui.theme.AmberWarning
import com.jn.winremote.ui.theme.GreenOk
import com.jn.winremote.ui.theme.RedCritical

data class StatusVisual(val label: String, val color: Color)

fun ConnectionStatus.toVisual(): StatusVisual = when (this) {
    is ConnectionStatus.Connected -> StatusVisual("Bağlı", GreenOk)
    is ConnectionStatus.Connecting -> StatusVisual("Bağlanıyor", AmberWarning)
    is ConnectionStatus.Reconnecting -> StatusVisual("Yeniden bağlanıyor", AmberWarning)
    is ConnectionStatus.Offline -> StatusVisual("Çevrimdışı", Color.Gray)
    is ConnectionStatus.Unauthorized -> StatusVisual("Yetkisiz", RedCritical)
    is ConnectionStatus.Error -> StatusVisual("Hata", RedCritical)
}

@Composable
fun ConnectionStatusChip(status: ConnectionStatus, modifier: Modifier = Modifier) {
    val visual = status.toVisual()
    Row(
        modifier = modifier
            .background(visual.color.copy(alpha = 0.15f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(8.dp)
                .background(visual.color, CircleShape)
        )
        androidx.compose.foundation.layout.Spacer(Modifier.size(6.dp))
        Text(
            visual.label,
            style = MaterialTheme.typography.labelLarge,
            color = visual.color,
        )
    }
}
