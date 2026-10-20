package com.sende.app.ui.channels

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sende.app.R
import com.sende.app.domain.model.Channel
import com.sende.app.ui.common.AppIcon
import com.sende.app.ui.theme.Tek
import org.koin.androidx.compose.koinViewModel

/**
 * The picker and nothing but the picker: every app on the phone with its real icon and a
 * checkbox, a select-all row on top, a search field, and a "Tamam" button within thumb reach.
 */
@Composable
fun ChannelPickerSheet(onDismiss: () -> Unit, viewModel: ChannelPickerViewModel = koinViewModel()) {
    val colors = Tek.colors
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) { viewModel.onOpen() }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = colors.background,
        contentColor = colors.text,
    ) {
        ChannelPickerContent(
            state = state,
            onQuery = viewModel::setQuery,
            onToggle = viewModel::toggle,
            onSetAllVisible = viewModel::setAllVisible,
            onDone = onDismiss,
        )
    }
}

/** The sheet's content as a pure function of state (also what the screenshot tests render). */
@Composable
fun ChannelPickerContent(
    state: PickerUiState,
    onQuery: (String) -> Unit,
    onToggle: (Channel) -> Unit,
    onSetAllVisible: (Boolean) -> Unit,
    onDone: () -> Unit,
) {
    val colors = Tek.colors
    Column(Modifier.navigationBarsPadding().padding(horizontal = 16.dp)) {
        Text(stringResource(R.string.picker_title), style = MaterialTheme.typography.titleLarge, color = colors.text)
        Spacer(Modifier.padding(top = 10.dp))
        OutlinedTextField(
            value = state.query,
            onValueChange = onQuery,
            singleLine = true,
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = colors.textSecondary) },
            placeholder = { Text(stringResource(R.string.picker_search_hint), color = colors.textSecondary) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Tek.shapes.button),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.accent,
                unfocusedBorderColor = colors.line,
                focusedTextColor = colors.text,
                unfocusedTextColor = colors.text,
                cursorColor = colors.accent,
            ),
        )
        Spacer(Modifier.padding(top = 6.dp))
        SelectAllRow(
            checked = state.allVisibleSelected,
            onToggle = { onSetAllVisible(!state.allVisibleSelected) },
        )
        if (state.items.isEmpty() && !state.loading) {
            Text(
                stringResource(R.string.picker_none_found),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary,
                modifier = Modifier.padding(vertical = 24.dp),
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f, fill = false).heightIn(max = 520.dp),
            contentPadding = PaddingValues(vertical = 4.dp),
        ) {
            items(state.items, key = { it.packageName }) { channel ->
                ChannelRow(channel = channel, onToggle = { onToggle(channel) }, modifier = Modifier.animateItem())
            }
        }
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            shape = RoundedCornerShape(Tek.shapes.button),
            colors = ButtonDefaults.buttonColors(containerColor = colors.accent, contentColor = colors.onAccent),
        ) { Text(stringResource(R.string.picker_done) + " (" + state.selectedCount + ")") }
    }
}

@Composable
private fun SelectAllRow(checked: Boolean, onToggle: () -> Unit) {
    val colors = Tek.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onToggle)
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = checked, onCheckedChange = { onToggle() }, colors = checkboxColors())
        Text(
            text = stringResource(R.string.filter_all),
            style = MaterialTheme.typography.titleSmall,
            color = colors.text,
            modifier = Modifier.padding(start = 6.dp),
        )
    }
}

@Composable
private fun ChannelRow(channel: Channel, onToggle: () -> Unit, modifier: Modifier = Modifier) {
    val colors = Tek.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onToggle)
            .heightIn(min = 52.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = channel.enabled, onCheckedChange = { onToggle() }, colors = checkboxColors())
        Spacer(Modifier.width(4.dp))
        AppIcon(packageName = channel.packageName, size = 32.dp)
        Spacer(Modifier.width(12.dp))
        Text(
            text = channel.label,
            style = MaterialTheme.typography.bodyLarge,
            color = colors.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        if (channel.totalMessages > 0) {
            Text(
                text = channel.totalMessages.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = colors.textSecondary,
                modifier = Modifier.padding(end = 8.dp),
            )
        }
    }
}

@Composable
private fun checkboxColors() = CheckboxDefaults.colors(
    checkedColor = Tek.colors.accent,
    checkmarkColor = Tek.colors.onAccent,
    uncheckedColor = Tek.colors.textSecondary,
)
