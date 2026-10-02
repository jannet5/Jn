package app.nokta.a.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.nokta.a.R
import app.nokta.a.model.Item
import app.nokta.a.model.ListState
import app.nokta.a.model.Removal
import app.nokta.a.ui.theme.LocalNokta
import app.nokta.a.ui.theme.NoktaType
import kotlinx.coroutines.delay
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

class ScreenActions(
    val onAdd: (String) -> Unit = {},
    val onToggle: (Long) -> Unit = {},
    val onDelete: (Long) -> Unit = {},
    val onMove: (Int, Int) -> Unit = { _, _ -> },
    val onUndo: () -> Unit = {},
    val onDismissUndo: () -> Unit = {},
    val onClearDone: () -> Unit = {},
    val onExport: () -> Unit = {},
    val onToggleBubble: () -> Unit = {},
    val onGrantOverlay: () -> Unit = {},
)

@Composable
fun NoktaScreen(
    ui: UiState,
    bubbleOn: Boolean,
    overlayGranted: Boolean,
    actions: ScreenActions,
    autoFocus: Boolean = true,
) {
    val c = LocalNokta.current
    Column(
        Modifier
            .fillMaxSize()
            .background(c.bg)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
    ) {
        TopBar(ui.list, bubbleOn, actions)
        if (!overlayGranted && bubbleOn) PermissionRow(actions.onGrantOverlay)
        Box(Modifier.weight(1f)) {
            ItemList(ui.list.items, actions)
            if (ui.list.items.isEmpty()) {
                Text(
                    stringResource(R.string.empty),
                    style = NoktaType.row, color = c.inkSoft,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                )
            }
            UndoBar(ui, actions, Modifier.align(Alignment.BottomCenter))
        }
        AddField(actions.onAdd, autoFocus)
    }
}

@Composable
private fun TopBar(list: ListState, bubbleOn: Boolean, a: ScreenActions) {
    val c = LocalNokta.current
    var menu by remember { mutableStateOf(false) }
    Row(
        Modifier.fillMaxWidth().height(56.dp).padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(stringResource(R.string.app_name), style = NoktaType.wordmark, color = c.ink)
        Spacer(Modifier.weight(1f))
        if (list.items.isNotEmpty()) {
            Text(
                stringResource(R.string.counts, list.pendingCount, list.doneCount),
                style = NoktaType.meta, color = c.inkSoft,
            )
        }
        Box {
            Box(
                Modifier.size(56.dp).clickable(role = Role.Button, onClick = { menu = true }),
                contentAlignment = Alignment.Center,
            ) { MoreIcon(c.ink) }
            DropdownMenu(
                expanded = menu, onDismissRequest = { menu = false },
                containerColor = c.bg, shape = RoundedCornerShape(8.dp), shadowElevation = 4.dp,
            ) {
                if (list.doneCount > 0) MenuItem(stringResource(R.string.clear_done, list.doneCount)) { menu = false; a.onClearDone() }
                MenuItem(stringResource(R.string.export_list), enabled = list.items.isNotEmpty()) { menu = false; a.onExport() }
                MenuItem(stringResource(if (bubbleOn) R.string.bubble_off else R.string.bubble_on)) { menu = false; a.onToggleBubble() }
            }
        }
    }
}

@Composable
private fun MenuItem(label: String, enabled: Boolean = true, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(label, style = NoktaType.menu, color = LocalNokta.current.ink.copy(alpha = if (enabled) 1f else 0.4f)) },
        onClick = onClick, enabled = enabled,
    )
}

@Composable
private fun PermissionRow(onGrant: () -> Unit) {
    val c = LocalNokta.current
    Row(
        Modifier.fillMaxWidth().padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(stringResource(R.string.overlay_needed), style = NoktaType.meta, color = c.inkSoft, modifier = Modifier.weight(1f))
        Box(Modifier.heightIn(min = 48.dp).clickable(role = Role.Button, onClick = onGrant).padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.grant), style = NoktaType.snack, color = c.dot)
        }
    }
}

@Composable
private fun ItemList(items: List<Item>, a: ScreenActions) {
    val c = LocalNokta.current
    val listState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current
    val reorder = rememberReorderableLazyListState(listState) { from, to ->
        a.onMove(from.index, to.index)
        haptic.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }
    // yeni öğe eklenince en alta kay
    var lastCount by remember { mutableIntStateOf(items.size) }
    LaunchedEffect(items.size) {
        if (items.size > lastCount && items.isNotEmpty()) listState.animateScrollToItem(items.lastIndex)
        lastCount = items.size
    }
    LazyColumn(Modifier.fillMaxSize(), state = listState, contentPadding = PaddingValues(bottom = 72.dp)) {
        items(items, key = { it.id }) { item ->
            ReorderableItem(reorder, key = item.id) { dragging ->
                val scale by animateFloatAsState(if (dragging) 1.03f else 1f, tween(150), label = "scale")
                val elev by animateDpAsState(if (dragging) 6.dp else 0.dp, tween(150), label = "elev")
                Column(
                    Modifier
                        .zIndex(if (dragging) 1f else 0f)
                        .scale(scale)
                        .shadow(elev)
                        .background(c.bg)
                ) {
                    ItemRow(
                        item, a,
                        gripModifier = Modifier.draggableHandle(
                            onDragStarted = { haptic.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate) },
                            onDragStopped = { haptic.performHapticFeedback(HapticFeedbackType.GestureEnd) },
                        ),
                        rowModifier = Modifier.longPressDraggableHandle(
                            onDragStarted = { haptic.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate) },
                            onDragStopped = { haptic.performHapticFeedback(HapticFeedbackType.GestureEnd) },
                        ),
                    )
                    Box(Modifier.padding(start = 16.dp).fillMaxWidth().height(1.dp).background(c.line))
                }
            }
        }
    }
}

@Composable
private fun ItemRow(item: Item, a: ScreenActions, gripModifier: Modifier, rowModifier: Modifier) {
    val c = LocalNokta.current
    val textColor by animateColorAsState(if (item.done) c.inkSoft else c.ink, tween(200), label = "text")
    Row(Modifier.fillMaxWidth().heightIn(min = 56.dp), verticalAlignment = Alignment.CenterVertically) {
        // tutamaç: 48x56
        Box(gripModifier.size(48.dp, 56.dp), contentAlignment = Alignment.Center) { GripIcon(c.inkSoft) }
        // satır gövdesi: dokun = alındı, uzun bas = sürükle
        Row(
            rowModifier
                .weight(1f)
                .heightIn(min = 56.dp)
                .clickable(role = Role.Checkbox, onClick = { a.onToggle(item.id) })
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DoneDot(item.done)
            Spacer(Modifier.width(12.dp))
            Text(
                item.text, style = NoktaType.row, color = textColor,
                textDecoration = if (item.done) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 3, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }
        Box(
            Modifier.size(48.dp).clickable(role = Role.Button, onClick = { a.onDelete(item.id) }),
            contentAlignment = Alignment.Center,
        ) { CrossIcon(c.inkSoft) }
        Spacer(Modifier.width(4.dp))
    }
}

@Composable
private fun UndoBar(ui: UiState, a: ScreenActions, modifier: Modifier) {
    val c = LocalNokta.current
    val removal: Removal? = ui.undo
    // son gösterilen mesajı çıkış animasyonu boyunca koru
    var shown by remember { mutableStateOf(removal) }
    if (removal != null) shown = removal
    LaunchedEffect(ui.undoToken, removal != null) {
        if (removal != null) { delay(5000); a.onDismissUndo() }
    }
    AnimatedVisibility(
        visible = removal != null,
        enter = fadeIn(tween(150)) + slideInVertically(tween(150)) { it / 2 },
        exit = fadeOut(tween(150)) + slideOutVertically(tween(150)) { it / 2 },
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        val n = shown?.entries?.size ?: 0
        val msg = if (shown?.kind == Removal.Kind.CLEAR_DONE)
            pluralStringResource(R.plurals.cleared, n, n) else stringResource(R.string.deleted)
        Row(
            Modifier.fillMaxWidth().heightIn(min = 48.dp)
                .background(c.inverse, RoundedCornerShape(8.dp))
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(msg, style = NoktaType.snack, color = c.onInverse, modifier = Modifier.weight(1f))
            Box(
                Modifier.heightIn(min = 48.dp).clickable(role = Role.Button, onClick = a.onUndo).padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) { Text(stringResource(R.string.undo), style = NoktaType.snack, color = if (c.bg == app.nokta.a.ui.theme.DarkColors.bg) app.nokta.a.ui.theme.LightColors.dot else c.dot) }
        }
    }
}

@Composable
private fun AddField(onAdd: (String) -> Unit, autoFocus: Boolean) {
    val c = LocalNokta.current
    var text by remember { mutableStateOf("") }
    val focus = remember { FocusRequester() }
    val kb = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        if (autoFocus) { delay(150); focus.requestFocus(); kb?.show() }
    }
    fun submit() { if (text.isNotBlank()) { onAdd(text); text = "" } }
    Column(Modifier.fillMaxWidth().background(c.bg).windowInsetsPadding(WindowInsets.ime.union(WindowInsets.navigationBars))) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(c.line))
        Box(Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
            BasicTextField(
                value = text, onValueChange = { text = it },
                textStyle = NoktaType.row.copy(color = c.ink),
                cursorBrush = SolidColor(c.dot),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                modifier = Modifier.fillMaxWidth().focusRequester(focus),
                decorationBox = { inner ->
                    Box(Modifier.padding(vertical = 16.dp)) {
                        if (text.isEmpty()) Text(stringResource(R.string.hint), style = NoktaType.row, color = c.inkSoft)
                        inner()
                    }
                },
            )
        }
    }
}
