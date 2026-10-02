package app.nokta.list

import app.nokta.list.core.AddFlow
import app.nokta.list.core.ListModel
import app.nokta.list.core.Undo
import org.junit.Assert.*
import org.junit.Test

/** Hizli art arda ekleme, odak karari, toplu temizleme + geri al ve disa aktarma akislari. */
class QuickAddTest {
    @Test fun addFlowKeepsFocusAndClearsField() {
        val f = AddFlow.after(true)
        assertTrue(f.keepFocus); assertTrue(f.clearField); assertTrue(f.scrollToTop)
    }

    @Test fun blankAddKeepsFocusButDoesNotClearOrScroll() {
        val f = AddFlow.after(false)
        assertTrue(f.keepFocus); assertFalse(f.clearField); assertFalse(f.scrollToTop)
    }

    @Test fun rapidConsecutiveAddsAllLandNewestOnTop() {
        val m = ListModel()
        listOf("a", "b", "c", "", "d", "e").forEach { m.add(it) }
        assertEquals(listOf("e", "d", "c", "b", "a"), m.items.map { it.text })
        assertEquals(5, m.items.map { it.id }.toSet().size)
    }

    @Test fun clearDoneThenUndoThenClearAgain() {
        val m = ListModel()
        listOf("a", "b", "c", "d").forEach { m.add(it) } // d c b a
        m.items.filter { it.text in setOf("d", "b") }.forEach { m.toggle(it.id) }
        assertEquals(2, m.clearDone())
        assertEquals(listOf("c", "a"), m.items.map { it.text })
        assertTrue(m.undo is Undo.ClearedDone)
        assertTrue(m.undoLast())
        assertEquals(listOf("d", "c", "b", "a"), m.items.map { it.text })
        assertNull(m.undo)
        assertEquals(2, m.clearDone())
    }

    @Test fun deleteAfterClearReplacesUndoSlot() {
        val m = ListModel()
        listOf("a", "b").forEach { m.add(it) }
        m.toggle(m.items[0].id); m.clearDone()
        m.delete(m.items[0].id)
        assertTrue(m.undo is Undo.Deleted)
        m.undoLast()
        assertEquals(listOf("a"), m.items.map { it.text })
    }

    @Test fun exportKeepsDoneStateAndOrderForSharing() {
        val m = ListModel()
        listOf("süt", "ekmek").forEach { m.add(it) }
        m.toggle(m.items[0].id)
        assertEquals("[x] ekmek\n[ ] süt", m.export())
        assertEquals("", ListModel().export())
    }

    @Test fun importDoesNotLoseExistingOnEmptyInput() {
        val m = ListModel(); m.add("a")
        assertEquals(0, m.importText("  \n \n"))
        assertEquals(1, m.items.size)
    }
}
