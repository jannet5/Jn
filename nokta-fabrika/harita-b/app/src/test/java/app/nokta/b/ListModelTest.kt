package app.nokta.b

import app.nokta.b.core.ListModel
import app.nokta.b.core.Undo
import org.junit.Assert.*
import org.junit.Test

class ListModelTest {
    private fun model(vararg t: String) = ListModel().also { m -> t.reversed().forEach { m.add(it) } }
    private fun ListModel.texts() = items.map { it.text }

    @Test fun addTrimsAndRejectsBlank() {
        val m = ListModel()
        assertNull(m.add("   "))
        assertEquals("süt ekmek", m.add("  süt   ekmek ")!!.text)
        assertEquals(1, m.items.size)
    }

    @Test fun addPutsNewItemOnTopAndReindexes() {
        val m = model("a", "b")
        m.add("c")
        assertEquals(listOf("c", "a", "b"), m.texts())
        assertEquals(listOf(0, 1, 2), m.items.map { it.position })
    }

    @Test fun idsAreUnique() {
        val m = model("a", "b", "c")
        m.delete(m.items[0].id)
        m.add("d")
        assertEquals(m.items.size, m.items.map { it.id }.toSet().size)
    }

    @Test fun toggleKeepsPlaceAndCounts() {
        val m = model("a", "b", "c")
        val id = m.items[1].id
        assertTrue(m.toggle(id))
        assertEquals(listOf("a", "b", "c"), m.texts())
        assertTrue(m.items[1].done)
        assertEquals(2, m.remaining); assertEquals(1, m.doneCount)
        m.toggle(id)
        assertFalse(m.items[1].done)
        assertFalse(m.toggle(9999))
    }

    @Test fun deleteThenUndoRestoresPosition() {
        val m = model("a", "b", "c")
        val b = m.items[1]
        assertTrue(m.delete(b.id))
        assertEquals(listOf("a", "c"), m.texts())
        assertTrue(m.undo is Undo.Deleted)
        assertTrue(m.undoLast())
        assertEquals(listOf("a", "b", "c"), m.texts())
        assertEquals(listOf(0, 1, 2), m.items.map { it.position })
        assertNull(m.undo)
        assertFalse(m.undoLast())
    }

    @Test fun deleteLastItemAndUndo() {
        val m = model("a", "b")
        m.delete(m.items[1].id)
        m.undoLast()
        assertEquals(listOf("a", "b"), m.texts())
    }

    @Test fun secondDeleteReplacesUndoSlot() {
        val m = model("a", "b", "c")
        m.delete(m.items[0].id)
        m.delete(m.items[0].id)
        assertEquals(listOf("c"), m.texts())
        m.undoLast()  // yalniz son silinen (b) geri gelir; ilk silinen (a) geri alinamaz
        assertEquals(listOf("b", "c"), m.texts())
    }

    @Test fun clearDoneRemovesOnlyDoneAndUndoRestoresOrder() {
        val m = model("a", "b", "c", "d", "e")
        m.toggle(m.items[1].id); m.toggle(m.items[2].id); m.toggle(m.items[4].id)
        assertEquals(3, m.clearDone())
        assertEquals(listOf("a", "d"), m.texts())
        assertTrue(m.undo is Undo.ClearedDone)
        m.undoLast()
        assertEquals(listOf("a", "b", "c", "d", "e"), m.texts())
        assertEquals(listOf(false, true, true, false, true), m.items.map { it.done })
    }

    @Test fun clearDoneWithNothingDoneKeepsUndo() {
        val m = model("a")
        assertEquals(0, m.clearDone())
        assertNull(m.undo)
    }

    @Test fun moveReordersAndReindexes() {
        val m = model("a", "b", "c", "d")
        assertTrue(m.move(0, 2))
        assertEquals(listOf("b", "c", "a", "d"), m.texts())
        assertTrue(m.move(3, 0))
        assertEquals(listOf("d", "b", "c", "a"), m.texts())
        assertEquals(listOf(0, 1, 2, 3), m.items.map { it.position })
    }

    @Test fun moveRejectsBadIndices() {
        val m = model("a", "b")
        assertFalse(m.move(0, 0)); assertFalse(m.move(-1, 1)); assertFalse(m.move(0, 2))
        assertEquals(listOf("a", "b"), m.texts())
    }

    @Test fun dismissUndoDisablesIt() {
        val m = model("a"); m.delete(m.items[0].id)
        m.dismissUndo()
        assertFalse(m.undoLast())
        assertTrue(m.items.isEmpty())
    }

    @Test fun exportFormat() {
        val m = model("süt", "ekmek"); m.toggle(m.items[1].id)
        assertEquals("[ ] süt\n[x] ekmek", m.export())
    }

    @Test fun importAppendsParsedLines() {
        val m = model("a")
        val n = m.importText("[x] b\n\n  c  \n[ ] d\n[x]  ")
        assertEquals(3, n)
        assertEquals(listOf("a", "b", "c", "d"), m.texts())
        assertEquals(listOf(false, true, false, false), m.items.map { it.done })
    }

    @Test fun exportImportRoundTrip() {
        val a = model("x", "y", "z"); a.toggle(a.items[0].id)
        val b = ListModel(); b.importText(a.export())
        assertEquals(a.texts(), b.texts())
        assertEquals(a.items.map { it.done }, b.items.map { it.done })
    }

    @Test fun veryLongTextIsCapped() {
        val m = ListModel()
        assertEquals(ListModel.MAX_TEXT, m.add("x".repeat(500))!!.text.length)
    }
}
