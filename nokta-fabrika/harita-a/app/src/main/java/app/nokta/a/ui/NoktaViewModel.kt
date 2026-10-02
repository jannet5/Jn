package app.nokta.a.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import app.nokta.a.data.FileStorage
import app.nokta.a.model.ListState
import app.nokta.a.model.NoktaController
import app.nokta.a.model.Removal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class UiState(val list: ListState, val undo: Removal?, val undoToken: Long)

class NoktaViewModel(app: Application) : AndroidViewModel(app) {
    private val c = NoktaController(FileStorage(app.filesDir))
    private val _ui = MutableStateFlow(snapshot(0))
    val ui: StateFlow<UiState> = _ui
    private var token = 0L

    private fun snapshot(t: Long) = UiState(c.state, c.undo, t)
    private fun publish(newUndo: Boolean = false) {
        if (newUndo) token++
        _ui.value = snapshot(token)
    }

    fun add(text: String) { c.add(text); publish() }
    fun toggle(id: Long) { c.toggle(id); publish() }
    fun move(from: Int, to: Int) { c.move(from, to); publish() }
    fun remove(id: Long) { c.remove(id); publish(true) }
    fun clearDone() { c.clearDone(); publish(true) }
    fun undo() { c.undoLast(); publish() }
    fun dismissUndo() { c.dismissUndo(); publish() }
    fun exportText(): String = app.nokta.a.model.Export.toText(c.state)
}
