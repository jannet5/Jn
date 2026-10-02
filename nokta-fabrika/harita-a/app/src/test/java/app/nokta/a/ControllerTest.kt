package app.nokta.a

import app.nokta.a.model.*
import org.junit.Assert.*
import org.junit.Test

class FakeStorage(var raw: String? = null) : Storage {
    var saves = 0
    val corrupt = mutableListOf<String>()
    override fun load() = raw
    override fun save(json: String) { raw = json; saves++ }
    override fun backupCorrupt(json: String) { corrupt += json }
}

class ControllerTest {
    @Test fun deleteThenUndoRestoresAndPersists() {
        val st = FakeStorage()
        val c = NoktaController(st)
        c.add("a"); c.add("b"); c.add("c")
        c.remove(2)
        assertEquals(listOf("a", "c"), c.state.items.map { it.text })
        assertNotNull(c.undo)
        c.undoLast()
        assertEquals(listOf("a", "b", "c"), c.state.items.map { it.text })
        assertNull(c.undo)
        // diskteki hal bellekle aynı
        assertEquals(c.state, ListCodec.decode(st.raw))
    }

    @Test fun undoIsSingleLevelNewerActionReplacesOlder() {
        val c = NoktaController(FakeStorage())
        c.add("a"); c.add("b")
        c.remove(1)
        c.remove(2)
        c.undoLast()
        assertEquals(listOf("b"), c.state.items.map { it.text })
        c.undoLast() // ikinci geri al etkisiz
        assertEquals(1, c.state.items.size)
    }

    @Test fun clearDoneUndo() {
        val c = NoktaController(FakeStorage())
        c.add("a"); c.add("b"); c.toggle(1)
        c.clearDone()
        assertEquals(1, c.state.items.size)
        assertEquals(Removal.Kind.CLEAR_DONE, c.undo!!.kind)
        c.undoLast()
        assertEquals(2, c.state.items.size)
        assertTrue(c.state.items[0].done)
    }

    @Test fun blankAddDoesNotSave() {
        val st = FakeStorage()
        val c = NoktaController(st)
        c.add("  ")
        assertEquals(0, st.saves)
    }

    @Test fun reloadFromStorageKeepsOrderAndDoneFlags() {
        val st = FakeStorage()
        val c = NoktaController(st)
        c.add("x"); c.add("y"); c.add("z"); c.toggle(2); c.move(0, 2)
        val c2 = NoktaController(st)
        assertEquals(c.state, c2.state)
        assertEquals(listOf("y", "z", "x"), c2.state.items.map { it.text })
    }

    @Test fun corruptFileIsBackedUpAndAppStartsEmpty() {
        val st = FakeStorage("{bozuk json")
        val c = NoktaController(st)
        assertTrue(c.state.items.isEmpty())
        assertTrue(c.recoveredFromCorrupt)
        assertEquals(listOf("{bozuk json"), st.corrupt)
    }

    @Test fun missingFileIsNotCorrupt() {
        val c = NoktaController(FakeStorage(null))
        assertFalse(c.recoveredFromCorrupt)
    }

    @Test fun importAddsLinesWithDoneFlag() {
        val c = NoktaController(FakeStorage())
        c.add("var")
        c.importLines(Export.fromText("[ ] süt\n[x] ekmek\n\nçay"))
        assertEquals(listOf("var", "süt", "ekmek", "çay"), c.state.items.map { it.text })
        assertEquals(listOf(false, false, true, false), c.state.items.map { it.done })
    }
}
