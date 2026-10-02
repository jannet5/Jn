package com.notivo.app

import com.notivo.app.data.Logic
import com.notivo.app.data.Rule
import org.junit.Assert.*
import org.junit.Test

class LogicTest {
    @Test fun deletedNoticeDetected() {
        assertTrue(Logic.isDeletedNotice("This message was deleted"))
        assertTrue(Logic.isDeletedNotice("  Bu mesaj silindi "))
        assertEquals("indirim", Logic.norm("İNDİRİM"))
        assertEquals(Logic.norm("ILIK"), Logic.norm("ılık"))
        assertTrue(Logic.isDeletedNotice("🚫 This message was deleted"))
        assertFalse(Logic.isDeletedNotice("Merhaba, bu mesaj silindi mi?"))
    }
    @Test fun rulesMatchByAppAndKeyword() {
        val rules = listOf(
            Rule(1, "com.a", "indirim", "BLOCK"),
            Rule(2, "", "kod", "MUTE"),
            Rule(3, "com.b", "", "MUTE", enabled = false),
        )
        assertEquals(1L, Logic.matchRule(rules, "com.a", "Büyük İNDİRİM", "")?.id)
        assertNull(Logic.matchRule(rules, "com.c", "indirim", "x"))
        assertEquals(2L, Logic.matchRule(rules, "com.c", "Doğrulama", "kodunuz 123")?.id)
        assertNull(Logic.matchRule(rules, "com.b", "any", "any"))   // disabled rule
    }
    @Test fun dayBuckets() {
        val day = 86_400_000L
        val now = 10 * day + 12 * 3_600_000L
        assertEquals(0, Logic.dayBucket(now, now - 1000, 0))
        assertEquals(1, Logic.dayBucket(now, now - day, 0))
        assertEquals(2, Logic.dayBucket(now, now - 3 * day, 0))
        assertEquals(3, Logic.dayBucket(now, now - 9 * day, 0))
    }
}
