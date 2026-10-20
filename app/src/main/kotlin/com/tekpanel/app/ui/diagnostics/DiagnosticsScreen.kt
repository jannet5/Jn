package com.tekpanel.app.ui.diagnostics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tekpanel.app.ui.theme.Tek
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.koinViewModel

@Composable
fun DiagnosticsScreen(viewModel: DiagnosticsViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Tek.colors.background,
        topBar = {
            TopAppBar(
                title = { Text("Tanılama", color = Tek.colors.text) },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(containerColor = Tek.colors.background),
            )
        },
    ) { padding ->
        val rows = buildList {
            add("Bildirim erişimi" to if (state.snapshot.notificationAccessGranted) "Açık" else "Kapalı")
            add("Dinleyici bağlı mı" to if (state.snapshot.listenerConnected) "Bağlı" else "Bağlı değil")
            add("Son bağlanma" to (state.snapshot.lastConnectedAt?.toString() ?: "-"))
            add("Son görülen paket" to (state.snapshot.lastSeenPackage ?: "-"))
            add("Son karar" to (state.snapshot.lastDecisionReason?.name ?: "-"))
            add("Son karar zamanı" to (state.snapshot.lastDecisionAt?.toString() ?: "-"))
            add("Son kabul zamanı" to (state.snapshot.lastAcceptedAt?.toString() ?: "-"))
            add("Toplam kabul edilen" to state.snapshot.totalAccepted.toString())
            add("Duplicate olarak elenen" to state.snapshot.totalDuplicatesSuppressed.toString())
            add("Room kayıt sayısı" to state.roomRowCount.toString())
            state.snapshot.rejectedByReason.forEach { (reason, count) ->
                add("Red: ${reason.name}" to count.toString())
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(rows) { (label, value) ->
                Column(modifier = Modifier.padding(vertical = 10.dp)) {
                    Text(text = label, style = MaterialTheme.typography.labelMedium, color = Tek.colors.textSecondary)
                    Text(text = value, style = MaterialTheme.typography.bodyLarge, color = Tek.colors.text)
                }
            }
        }
    }
}
