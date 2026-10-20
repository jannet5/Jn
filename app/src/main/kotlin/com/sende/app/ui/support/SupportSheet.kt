package com.sende.app.ui.support

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sende.app.R
import com.sende.app.monetization.SupportPlan
import com.sende.app.ui.theme.Tek

/**
 * Support plans. Nothing in the app is ever locked behind this sheet in the default build; it is a
 * thank-you path. [trialEnded] only changes the wording for the two A/B test builds.
 */
@Composable
fun SupportSheet(
    plans: List<SupportPlan>,
    supporter: Boolean,
    trialEnded: Boolean,
    onBuy: (planId: String) -> Unit,
    onDismiss: () -> Unit,
    thanked: Boolean = false,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Tek.colors.background,
        contentColor = Tek.colors.text,
    ) {
        SupportContent(plans, supporter, trialEnded, onBuy, onDismiss, thanked)
    }
}

@Composable
fun SupportContent(
    plans: List<SupportPlan>,
    supporter: Boolean,
    trialEnded: Boolean,
    onBuy: (planId: String) -> Unit,
    onDismiss: () -> Unit,
    thanked: Boolean = false,
) {
    val colors = Tek.colors
    val subscriptions = plans.filter { !it.isOneTime }
    val tips = plans.filter { it.isOneTime }
    var selected by remember(plans) { mutableStateOf(subscriptions.firstOrNull { it.months == 12 }?.id ?: subscriptions.firstOrNull()?.id) }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).navigationBarsPadding().padding(horizontal = 20.dp).padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(
                if (supporter || thanked) R.string.support_thanks_title else if (trialEnded) R.string.support_trial_over_title else R.string.support_title,
            ),
            style = MaterialTheme.typography.titleLarge,
            color = colors.text,
        )
        Text(
            text = stringResource(if (trialEnded && !supporter) R.string.support_trial_over_body else R.string.support_body),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary,
        )
        if (plans.isEmpty()) {
            Text(stringResource(R.string.support_unavailable), style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
        }
        if (subscriptions.isNotEmpty()) {
            Text(stringResource(R.string.support_recurring_header), style = MaterialTheme.typography.labelLarge, color = colors.textSecondary)
        }
        subscriptions.forEach { plan ->
            PlanRow(plan = plan, selected = plan.id == selected, onSelect = { selected = plan.id })
        }
        if (subscriptions.isNotEmpty()) {
            Button(
                onClick = { selected?.let(onBuy) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Tek.shapes.button),
                colors = ButtonDefaults.buttonColors(containerColor = colors.accent, contentColor = colors.onAccent),
            ) { Text(stringResource(R.string.support_action)) }
            Text(stringResource(R.string.support_fine_print), style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
        }
        if (tips.isNotEmpty()) {
            Text(stringResource(R.string.support_tip_header), style = MaterialTheme.typography.labelLarge, color = colors.textSecondary)
            tips.forEach { tip ->
                Surface(
                    onClick = { onBuy(tip.id) },
                    shape = RoundedCornerShape(Tek.shapes.card),
                    color = colors.surface,
                    border = BorderStroke(1.dp, colors.line),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(tipLabel(tip.id), style = MaterialTheme.typography.titleSmall, color = colors.text, modifier = Modifier.weight(1f))
                        Text(tip.priceText, style = MaterialTheme.typography.titleSmall, color = colors.text)
                    }
                }
            }
        }
        TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(if (trialEnded && !supporter) R.string.support_not_now else R.string.action_close), color = colors.text)
        }
    }
}

@Composable
private fun PlanRow(plan: SupportPlan, selected: Boolean, onSelect: () -> Unit) {
    val colors = Tek.colors
    Surface(
        shape = RoundedCornerShape(Tek.shapes.card),
        color = if (selected) colors.accent.copy(alpha = 0.12f) else colors.surface,
        border = BorderStroke(1.dp, if (selected) colors.accent else colors.line),
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(Tek.shapes.card)).clickable(onClick = onSelect),
    ) {
        Row(Modifier.padding(horizontal = 8.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = colors.accent, unselectedColor = colors.textSecondary),
            )
            Spacer(Modifier.width(4.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = pluralStringResource(R.plurals.support_months, plan.months, plan.months),
                    style = MaterialTheme.typography.titleSmall,
                    color = colors.text,
                )
                plan.savingsPercent?.let {
                    Text(
                        text = stringResource(R.string.support_savings, it),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.success,
                    )
                }
            }
            Text(plan.priceText, style = MaterialTheme.typography.titleSmall, color = colors.text, modifier = Modifier.padding(end = 8.dp))
        }
    }
}

@Composable
private fun tipLabel(id: String): String = stringResource(
    when (id) {
        "tip_small" -> R.string.support_tip_small
        "tip_medium" -> R.string.support_tip_medium
        else -> R.string.support_tip_large
    },
)
