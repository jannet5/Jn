package app.nokta.c

import org.junit.Assert.*
import org.junit.Test

class NoktaListTest {
    private fun texts(l: NoktaList) = l.items.map { it.text }

    @Test fun addPutsNewItemOnTop() {
        val l = NoktaList(); l.add("a"); l.add("b")
        assertEquals(listOf("b", "a"), texts(l))
    }
    @Test fun addTrimsAndRejectsBlank() {
        val l = NoktaList()
        assertNull(l.add("   ")); assertNull(l.add(""))
        assertEquals("x", l.add("  x \n")!!.text)
        assertEquals(1, l.items.size)
    }
    @Test fun addCapsLength() {
        val l = NoktaList(); assertEquals(NoktaList.MAX_TEXT, l.add("a".repeat(5000))!!.text.length)
    }
    @Test fun idsAreUniqueEvenAfterDelete() {
        val l = NoktaList(); val a = l.add("a")!!; l.delete(a.id); val b = l.add("b")!!
        assertNotEquals(a.id, b.id)
    }
    @Test fun idsContinueFromLoaded() {
        val l = NoktaList(listOf(Item(7, "x"))); assertEquals(8L, l.add("y")!!.id)
    }
    @Test fun toggleFlipsDone() {
        val l = NoktaList(); val a = l.add("a")!!
        assertTrue(l.toggle(a.id)); assertTrue(l.items[0].done)
        assertTrue(l.toggle(a.id)); assertFalse(l.items[0].done)
        assertFalse(l.toggle(999))
    }
    @Test fun counts() {
        val l = NoktaList(); val a = l.add("a")!!; l.add("b"); l.toggle(a.id)
        assertEquals(1, l.doneCount); assertEquals(1, l.openCount)
    }
    @Test fun deleteRemoves() {
        val l = NoktaList(); val a = l.add("a")!!; l.add("b")
        assertTrue(l.delete(a.id)); assertEquals(listOf("b"), texts(l)); assertFalse(l.delete(a.id))
    }
    @Test fun clearDoneRemovesOnlyDone() {
        val l = NoktaList(); val a = l.add("a")!!; l.add("b"); val c = l.add("c")!!
        l.toggle(a.id); l.toggle(c.id)
        assertEquals(2, l.clearDone()); assertEquals(listOf("b"), texts(l))
        assertEquals(0, l.clearDone())
    }
    @Test fun moveReorders() {
        val l = NoktaList(); l.add("c"); l.add("b"); l.add("a") // a b c
        assertTrue(l.move(0, 2)); assertEquals(listOf("b", "c", "a"), texts(l))
        assertTrue(l.move(2, 0)); assertEquals(listOf("a", "b", "c"), texts(l))
    }
    @Test fun moveRejectsBadIndexesAndNoop() {
        val l = NoktaList(); l.add("a"); l.add("b")
        assertFalse(l.move(0, 0)); assertFalse(l.move(-1, 0)); assertFalse(l.move(0, 2))
    }
    @Test fun undoDelete() {
        val l = NoktaList(); val a = l.add("a")!!; l.add("b")
        l.delete(a.id); assertEquals(listOf("b"), texts(l))
        assertTrue(l.undo()); assertEquals(listOf("b", "a"), texts(l))
    }
    @Test fun undoClearDoneRestoresAll() {
        val l = NoktaList(); val a = l.add("a")!!; val b = l.add("b")!!
        l.toggle(a.id); l.toggle(b.id); l.clearDone()
        assertTrue(l.items.isEmpty()); l.undo()
        assertEquals(2, l.items.size); assertEquals(2, l.doneCount)
    }
    @Test fun undoMoveAndToggle() {
        val l = NoktaList(); l.add("b"); l.add("a")
        l.move(0, 1); l.undo(); assertEquals(listOf("a", "b"), texts(l))
        l.toggle(l.items[0].id); l.undo(); assertEquals(0, l.doneCount)
    }
    @Test fun multiLevelUndo() {
        val l = NoktaList(); l.add("a"); l.add("b"); l.add("c")
        l.undo(); l.undo(); assertEquals(listOf("a"), texts(l))
        l.undo(); assertTrue(l.items.isEmpty())
        assertFalse(l.undo())
    }
    @Test fun undoOnEmptyHistoryIsFalse() { assertFalse(NoktaList().undo()) }
    @Test fun failedOpsDoNotPushUndo() {
        val l = NoktaList(); l.add(" "); l.delete(5); l.toggle(5); l.clearDone()
        assertFalse(l.canUndo)
    }
    @Test fun undoStackIsCapped() {
        val l = NoktaList(); repeat(NoktaList.MAX_UNDO + 10) { l.add("x$it") }
        var n = 0; while (l.undo()) n++
        assertEquals(NoktaList.MAX_UNDO, n)
    }
    @Test fun exportTextFormat() {
        val l = NoktaList(); val a = l.add("süt")!!; l.add("ekmek"); l.toggle(a.id)
        assertEquals("[ ] ekmek\n[x] süt", l.exportText())
        assertEquals("", NoktaList().exportText())
    }
}
