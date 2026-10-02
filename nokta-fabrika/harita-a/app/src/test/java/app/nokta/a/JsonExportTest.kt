package app.nokta.a

import app.nokta.a.model.*
import org.junit.Assert.*
import org.junit.Test

class JsonExportTest {
    @Test fun roundTripPreservesEverythingIncludingTurkishAndQuotes() {
        val s = ListState().add("çğıöşü İĞ \"tırnak\" \\ ters").add("ikinci").toggle(2)
        val back = ListCodec.decode(ListCodec.encode(s))!!
        assertEquals(s, back)
    }

    @Test fun emptyListRoundTrip() {
        assertEquals(ListState(), ListCodec.decode(ListCodec.encode(ListState())))
    }

    @Test fun garbageReturnsNull() {
        assertNull(ListCodec.decode("not json"))
        assertNull(ListCodec.decode("{}"))
        assertNull(ListCodec.decode(""))
        assertNull(ListCodec.decode(null))
    }

    @Test fun badEntriesAreSkippedAndDuplicatesDropped() {
        val j = """{"v":1,"next":1,"items":[{"id":1,"t":"a","d":true},{"id":1,"t":"dup"},{"id":2,"t":"  "},{"t":"idsiz"},"x",{"id":5,"t":"b"}]}"""
        val s = ListCodec.decode(j)!!
        assertEquals(listOf("a", "b"), s.items.map { it.text })
        assertTrue(s.items[0].done)
        assertEquals(6L, s.nextId) // next, en büyük id + 1'e yükseltilir
    }

    @Test fun exportFormat() {
        val s = ListState().add("süt").add("ekmek").toggle(2)
        assertEquals("[ ] süt\n[x] ekmek", Export.toText(s))
    }

    @Test fun exportThenImportIsLossless() {
        val s = ListState().add("a b").add("c").toggle(1)
        val parsed = Export.fromText(Export.toText(s))
        assertEquals(listOf("a b" to true, "c" to false), parsed)
    }

    @Test fun emptyExport() {
        assertEquals("", Export.toText(ListState()))
        assertTrue(Export.fromText("").isEmpty())
    }
}
