package app.nokta.list

import org.junit.Assert.*
import org.junit.Test

class TaskListTest {
    private fun sample() = TaskList().apply { add("a"); add("b"); add("c") }

    @Test fun addTrimsAndRejectsBlank() {
        val l = TaskList()
        assertNull(l.add("   "))
        assertEquals("x", l.add("  x ")!!.text)
        assertEquals(1, l.all.size)
    }

    @Test fun toggleStrikesWithoutRemoving() {
        val l = sample()
        l.toggle(1)
        assertTrue(l.all[1].done)
        assertEquals(3, l.all.size)
        assertEquals(2, l.remaining)
        l.toggle(1)
        assertFalse(l.all[1].done)
    }

    @Test fun removeDeletes() {
        val l = sample()
        assertEquals("b", l.remove(1)!!.text)
        assertEquals(listOf("a", "c"), l.all.map { it.text })
        assertNull(l.remove(9))
    }

    @Test fun moveReorders() {
        val l = sample()
        l.move(0, 2)
        assertEquals(listOf("b", "c", "a"), l.all.map { it.text })
        l.move(2, 0)
        assertEquals(listOf("a", "b", "c"), l.all.map { it.text })
        l.move(0, 7) // out of range: no-op
        assertEquals(listOf("a", "b", "c"), l.all.map { it.text })
    }

    @Test fun jsonRoundTripKeepsOrderStateAndIds() {
        val l = sample(); l.toggle(0); l.move(2, 0)
        val r = TaskList.fromJson(l.toJson())
        assertEquals(l.all, r.all)
        assertEquals(4L, r.add("d")!!.id)
    }

    @Test fun corruptJsonYieldsEmpty() {
        assertTrue(TaskList.fromJson("{not json").all.isEmpty())
        assertTrue(TaskList.fromJson(null).all.isEmpty())
    }
}
