package app.nokta.a

import app.nokta.a.model.ListState
import app.nokta.a.model.Removal
import org.junit.Assert.*
import org.junit.Test

class ListStateTest {
    private fun listOf3() = ListState().add("süt").add("ekmek").add("çay")
    private fun ListState.texts() = items.map { it.text }

    @Test fun addTrimsAndCollapsesWhitespace() {
        val s = ListState().add("  iki   kelime \n satır ")
        assertEquals(listOf("iki kelime satır"), s.texts())
    }

    @Test fun blankIsRejected() {
        val s = ListState().add("   ").add("")
        assertTrue(s.items.isEmpty())
        assertEquals(1L, s.nextId)
    }

    @Test fun longTextIsCapped() {
        assertEquals(500, ListState().add("a".repeat(900)).items[0].text.length)
    }

    @Test fun idsAreUniqueAndIncreasing() {
        val s = listOf3()
        assertEquals(listOf(1L, 2L, 3L), s.items.map { it.id })
        assertEquals(4L, s.nextId)
    }

    @Test fun toggleIsReversibleAndKeepsPosition() {
        val s = listOf3().toggle(2)
        assertTrue(s.items[1].done)
        assertEquals(listOf("süt", "ekmek", "çay"), s.texts())
        assertEquals(1, s.doneCount); assertEquals(2, s.pendingCount)
        assertFalse(s.toggle(2).items[1].done)
    }

    @Test fun toggleUnknownIdChangesNothing() {
        val s = listOf3()
        assertEquals(s.items, s.toggle(99).items)
    }

    @Test fun moveDownAndUp() {
        val s = listOf3()
        assertEquals(listOf("ekmek", "çay", "süt"), s.move(0, 2).texts())
        assertEquals(listOf("çay", "süt", "ekmek"), s.move(2, 0).texts())
    }

    @Test fun moveOutOfRangeOrSameIsNoop() {
        val s = listOf3()
        assertSame(s, s.move(1, 1))
        assertSame(s, s.move(-1, 1))
        assertSame(s, s.move(0, 3))
    }

    @Test fun removeThenRestoreReturnsToSamePosition() {
        val s = listOf3()
        val (after, removal) = s.remove(2)!!
        assertEquals(listOf("süt", "çay"), after.texts())
        assertEquals(s.items, after.restore(removal).items)
    }

    @Test fun restoreAfterLaterAddsKeepsNewItems() {
        val (after, removal) = listOf3().remove(1)!!
        val withNew = after.add("yeni")
        val restored = withNew.restore(removal)
        assertEquals(listOf("süt", "ekmek", "çay", "yeni"), restored.texts())
    }

    @Test fun restoreTwiceDoesNotDuplicate() {
        val (after, removal) = listOf3().remove(3)!!
        val once = after.restore(removal)
        assertEquals(3, once.restore(removal).items.size)
    }

    @Test fun removeUnknownReturnsNull() {
        assertNull(listOf3().remove(42))
    }

    @Test fun clearDoneRemovesOnlyDoneAndRestoresOrder() {
        val s = listOf3().toggle(1).toggle(3)
        val (after, removal) = s.clearDone()!!
        assertEquals(listOf("ekmek"), after.texts())
        assertEquals(Removal.Kind.CLEAR_DONE, removal.kind)
        assertEquals(2, removal.entries.size)
        val back = after.restore(removal)
        assertEquals(listOf("süt", "ekmek", "çay"), back.texts())
        assertTrue(back.items[0].done && !back.items[1].done && back.items[2].done)
    }

    @Test fun clearDoneWithNothingDoneIsNull() {
        assertNull(listOf3().clearDone())
    }

    @Test fun editKeepsDoneAndRejectsBlank() {
        val s = listOf3().toggle(1)
        assertEquals("süt 2", s.edit(1, " süt  2 ").items[0].text)
        assertTrue(s.edit(1, "x").items[0].done)
        assertEquals("süt", s.edit(1, "  ").items[0].text)
    }

    @Test fun idsAreNotReusedAfterDelete() {
        val (after, _) = listOf3().remove(3)!!
        assertEquals(4L, after.add("d").items.last().id)
    }
}
