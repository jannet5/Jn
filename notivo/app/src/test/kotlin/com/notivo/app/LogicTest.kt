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

    private fun n(pkg: String, app: String, time: Long, read: Boolean = false) =
        com.notivo.app.data.Notif(sbnKey = "k", pkg = pkg, appName = app, title = "t", text = "x$time", time = time, category = "", isMessage = true, read = read)

    @Test fun groupsByAppNewestGroupFirstAndCountsUnread() {
        val items = listOf(n("wa", "WhatsApp", 100), n("mail", "Gmail", 300), n("wa", "WhatsApp", 200, read = true), n("wa", "WhatsApp", 50))
        val g = Logic.groupByApp(items)
        assertEquals(listOf("mail", "wa"), g.map { it.pkg })            // Gmail has the newest notification
        val wa = g.last()
        assertEquals("WhatsApp", wa.appName)
        assertEquals(listOf(200L, 100L, 50L), wa.items.map { it.time })  // newest first inside a group
        assertEquals(2, wa.unread)
        assertTrue(Logic.groupByApp(emptyList()).isEmpty())
    }

    @Test fun collapsedGroupShowsOnlyNewestThree() {
        val wa = Logic.groupByApp((1..5).map { n("wa", "WhatsApp", it * 10L) }).single()
        assertEquals(listOf(50L, 40L, 30L), Logic.visible(wa, expanded = false).map { it.time })
        assertEquals(5, Logic.visible(wa, expanded = true).size)
        val few = Logic.groupByApp(listOf(n("m", "Mail", 1))).single()
        assertEquals(1, Logic.visible(few, expanded = false).size)
        // eski → yeni sıralamada da önizleme EN YENİ 3'tür; sıra korunur
        val asc = Logic.groupByApp((1..5).map { n("wa", "WhatsApp", it * 10L) }, newestFirst = false).single()
        assertEquals(listOf(10L, 20L, 30L, 40L, 50L), asc.items.map { it.time })
        assertEquals(listOf(30L, 40L, 50L), Logic.visible(asc, expanded = false).map { it.time })
    }

    @Test fun dateRangesUseCalendarDaysAndLocalZone() {
        val day = 86_400_000L
        val now = 100 * day + 12 * 3_600_000L
        fun r(range: Int, ago: Long, off: Long = 0) = Logic.inRange(range, now, now - ago * day, off)
        assertTrue(r(Logic.RANGE_TODAY, 0)); assertFalse(r(Logic.RANGE_TODAY, 1))
        assertTrue(r(Logic.RANGE_YESTERDAY, 1)); assertFalse(r(Logic.RANGE_YESTERDAY, 0)); assertFalse(r(Logic.RANGE_YESTERDAY, 2))
        assertTrue(r(Logic.RANGE_7_DAYS, 6)); assertFalse(r(Logic.RANGE_7_DAYS, 7))
        assertTrue(r(Logic.RANGE_30_DAYS, 29)); assertFalse(r(Logic.RANGE_30_DAYS, 30))
        assertTrue(r(Logic.RANGE_ALL, 400))
        assertTrue(Logic.inRange(Logic.RANGE_TODAY, now, now + day, 0))   // gelecek tarihli kayıt bugün sayılır
        // yerel saat dilimi: UTC'de dün 23:00 olan kayıt, UTC+3'te bugün 02:00'dir
        val nowEarly = 100 * day + 3_600_000L
        val t = 100 * day - 3_600_000L
        assertTrue(Logic.inRange(Logic.RANGE_TODAY, nowEarly, t, 3 * 3_600_000L))
        assertFalse(Logic.inRange(Logic.RANGE_TODAY, nowEarly, t, 0))
    }

    @Test fun sortingAndGroupingHonourDirection() {
        val items = listOf(n("a", "A", 10), n("b", "B", 30), n("a", "A", 20), n("b", "B", 5))
        assertEquals(listOf(30L, 20L, 10L, 5L), Logic.sorted(items, true).map { it.time })
        assertEquals(listOf(5L, 10L, 20L, 30L), Logic.sorted(items, false).map { it.time })
        val newest = Logic.groupByApp(items, true)
        assertEquals(listOf("b", "a"), newest.map { it.pkg })                    // b'nin en yenisi 30
        assertEquals(listOf(30L, 5L), newest.first().items.map { it.time })
        val oldest = Logic.groupByApp(items, false)
        assertEquals(listOf("a", "b"), oldest.map { it.pkg })                    // a'nın en yenisi 20 < 30
        assertEquals(listOf(5L, 30L), oldest.last().items.map { it.time })
        assertEquals(30L, oldest.last().latest)                                  // latest = en yeni, yön ne olursa olsun
    }
}
