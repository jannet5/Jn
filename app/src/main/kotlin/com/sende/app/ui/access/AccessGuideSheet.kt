package com.sende.app.ui.access

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sende.app.R
import com.sende.app.ui.theme.Tek

/**
 * Step-by-step visual guide for the one thing a new user has to do: flip the notification access
 * switch. Step 4 covers Android 13+'s "restricted setting" lock that appears when an app was
 * installed from outside Google Play.
 */
@Composable
fun AccessGuideSheet(onOpenSettings: () -> Unit, onOpenAppInfo: () -> Unit, onDismiss: () -> Unit) {
    val colors = Tek.colors
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = colors.background,
        contentColor = colors.text,
    ) {
        AccessGuideContent(onOpenSettings = onOpenSettings, onOpenAppInfo = onOpenAppInfo)
    }
}

/** The guide's content as a plain composable (also what the screenshot tests render). */
@Composable
fun AccessGuideContent(onOpenSettings: () -> Unit, onOpenAppInfo: () -> Unit) {
    val colors = Tek.colors
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).navigationBarsPadding().padding(horizontal = 20.dp).padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(stringResource(R.string.guide_title), style = MaterialTheme.typography.titleLarge, color = colors.text)
        Text(stringResource(R.string.guide_why), style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)

        Step(1, stringResource(R.string.guide_step1)) { MockButton(stringResource(R.string.access_open)) }
        Step(2, stringResource(R.string.guide_step2)) { MockListWithSwitch(stringResource(R.string.app_name)) }
        Step(3, stringResource(R.string.guide_step3)) { MockDialog(stringResource(R.string.guide_allow)) }
        Step(4, stringResource(R.string.guide_step4)) { MockGreySwitch(stringResource(R.string.app_name)) }

        Button(
            onClick = onOpenSettings,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Tek.shapes.button),
            colors = ButtonDefaults.buttonColors(containerColor = colors.accent, contentColor = colors.onAccent),
        ) { Text(stringResource(R.string.access_open)) }
        OutlinedButton(
            onClick = onOpenAppInfo,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Tek.shapes.button),
            border = BorderStroke(1.dp, colors.line),
        ) { Text(stringResource(R.string.guide_open_app_info), color = colors.text) }
    }
}

@Composable
private fun Step(number: Int, text: String, illustration: @Composable () -> Unit) {
    val colors = Tek.colors
    Surface(shape = RoundedCornerShape(Tek.shapes.card), color = colors.surface, border = BorderStroke(1.dp, colors.line)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(Modifier.size(26.dp).clip(CircleShape).background(colors.accent), contentAlignment = Alignment.Center) {
                    Text(number.toString(), style = MaterialTheme.typography.labelLarge, color = colors.onAccent)
                }
                Spacer(Modifier.width(12.dp))
                Text(text, style = MaterialTheme.typography.bodyMedium, color = colors.text, modifier = Modifier.weight(1f))
            }
            illustration()
        }
    }
}

@Composable
private fun MockFrame(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Tek.colors.surfaceHigh,
        modifier = Modifier.fillMaxWidth(),
    ) { Box(Modifier.padding(12.dp)) { content() } }
}

@Composable
private fun MockButton(label: String) = MockFrame {
    Box(
        Modifier.clip(RoundedCornerShape(Tek.shapes.button)).background(Tek.colors.accent).padding(horizontal = 18.dp, vertical = 10.dp),
    ) { Text(label, style = MaterialTheme.typography.labelLarge, color = Tek.colors.onAccent) }
}

@Composable
private fun Switch(on: Boolean, enabled: Boolean = true) {
    val colors = Tek.colors
    val track = if (!enabled) colors.line else if (on) colors.accent else colors.textSecondary.copy(alpha = 0.4f)
    Box(Modifier.size(width = 44.dp, height = 24.dp).clip(CircleShape).background(track)) {
        Box(
            Modifier
                .padding(3.dp)
                .size(18.dp)
                .align(if (on) Alignment.CenterEnd else Alignment.CenterStart)
                .clip(CircleShape)
                .background(if (enabled) colors.background else colors.textSecondary),
        )
    }
}

@Composable
private fun MockListWithSwitch(appName: String) = MockFrame {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(22.dp).clip(CircleShape).background(Tek.colors.line))
            Spacer(Modifier.width(10.dp))
            Box(Modifier.height(8.dp).weight(1f).clip(CircleShape).background(Tek.colors.line))
            Spacer(Modifier.width(10.dp))
            Switch(on = false)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(22.dp).clip(CircleShape).background(Tek.colors.accent))
            Spacer(Modifier.width(10.dp))
            Text(appName, style = MaterialTheme.typography.labelLarge, color = Tek.colors.text, modifier = Modifier.weight(1f))
            Switch(on = true)
        }
    }
}

@Composable
private fun MockDialog(allow: String) = MockFrame {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(Modifier.height(8.dp).fillMaxWidth(0.8f).clip(CircleShape).background(Tek.colors.line))
        Box(Modifier.height(8.dp).fillMaxWidth(0.6f).clip(CircleShape).background(Tek.colors.line))
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Text(allow, style = MaterialTheme.typography.labelLarge, color = Tek.colors.accent)
        }
    }
}

@Composable
private fun MockGreySwitch(appName: String) = MockFrame {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(22.dp).clip(CircleShape).background(Tek.colors.line))
        Spacer(Modifier.width(10.dp))
        Text(appName, style = MaterialTheme.typography.labelLarge, color = Tek.colors.textSecondary, modifier = Modifier.weight(1f))
        Switch(on = false, enabled = false)
    }
}
