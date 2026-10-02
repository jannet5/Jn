package app.nokta.c

import org.junit.Assert.*
import org.junit.Test

class NoktaJsonTest {
    @Test fun roundTrip() {
        val items = listOf(Item(1, "süt", false), Item(2, "ekmek", true), Item(5, "çay", false))
        assertEquals(items, NoktaJson.decode(NoktaJson.encode(items)))
    }
    @Test fun roundTripSpecialCharacters() {
        val items = listOf(Item(1, "tırnak \" ve \\ ters\nsatır\ttab"), Item(2, "emoji 🙂 ğüşiöç İ"), Item(3, "{\"a\":[1,2]}"))
        assertEquals(items, NoktaJson.decode(NoktaJson.encode(items)))
    }
    @Test fun emptyList() {
        assertEquals(emptyList<Item>(), NoktaJson.decode(NoktaJson.encode(emptyList<Item>())))
    }
    @Test fun nullBlankAndGarbageReturnNull() {
        assertNull(NoktaJson.decode(null)); assertNull(NoktaJson.decode("  "))
        assertNull(NoktaJson.decode("not json")); assertNull(NoktaJson.decode("{\"v\":1}"))
        assertNull(NoktaJson.decode("{\"items\":"))
    }
    @Test fun skipsInvalidAndDuplicateItems() {
        val s = """{"v":1,"items":[{"id":1,"t":"a"},{"id":1,"t":"dup"},{"t":"no id"},{"id":2,"t":""},"x",{"id":3,"t":"ok","d":true}]}"""
        assertEquals(listOf(Item(1, "a"), Item(3, "ok", true)), NoktaJson.decode(s))
    }
    @Test fun missingDoneDefaultsFalse() {
        assertFalse(NoktaJson.decode("""{"items":[{"id":1,"t":"a"}]}""")!![0].done)
    }
    @Test fun longTextSurvives() {
        val t = "u".repeat(NoktaList.MAX_TEXT)
        assertEquals(t, NoktaJson.decode(NoktaJson.encode(listOf(Item(1, t))))!![0].text)
    }
    @Test fun modelSurvivesSaveLoadCycle() {
        val l = NoktaList(); val a = l.add("a")!!; l.add("b"); l.toggle(a.id)
        val l2 = NoktaList(NoktaJson.decode(NoktaJson.encode(l.items))!!)
        assertEquals(l.items, l2.items); assertEquals(3L, l2.add("c")!!.id)
    }
}
