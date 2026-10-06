package app.nokta.list

import app.nokta.list.reminder.ReminderPlan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class ReminderPlanTest {
    private val tz = TimeZone.getTimeZone("Europe/Istanbul")

    private fun at(y: Int, mo: Int, d: Int, h: Int, mi: Int): Long = Calendar.getInstance(tz).apply {
        clear(); set(y, mo - 1, d, h, mi, 0)
    }.timeInMillis

    @Test fun encodeDecodeRoundTrip() {
        val p = ReminderPlan(everyMin = 10, atMillis = 1_700_000_000_000)
        assertEquals(p, ReminderPlan.decode(p.encode()))
        assertEquals(ReminderPlan(everyMin = 5), ReminderPlan.decode(ReminderPlan(everyMin = 5).encode()))
        assertEquals(ReminderPlan(atMillis = 42), ReminderPlan.decode(ReminderPlan(atMillis = 42).encode()))
    }

    @Test fun decodeRejectsEmptyAndGarbage() {
        assertNull(ReminderPlan.decode(null))
        assertNull(ReminderPlan.decode(""))
        assertNull(ReminderPlan.decode("every=;at="))
        assertNull(ReminderPlan.decode("every=abc;at=-5"))
        assertNull(ReminderPlan.decode("every=0"))
    }

    @Test fun parseMinutesBounds() {
        assertEquals(10, ReminderPlan.parseMinutes("10"))
        assertEquals(5, ReminderPlan.parseMinutes(" 5 "))
        assertEquals(1, ReminderPlan.parseMinutes("1"))
        assertEquals(1440, ReminderPlan.parseMinutes("1440"))
        assertNull(ReminderPlan.parseMinutes("0"))
        assertNull(ReminderPlan.parseMinutes("1441"))
        assertNull(ReminderPlan.parseMinutes(""))
        assertNull(ReminderPlan.parseMinutes("on"))
    }

    @Test fun everyTenMinutesMeansTenMinutesLater() {
        assertEquals(1_000L + 600_000L, ReminderPlan.nextEvery(1_000L, 10))
    }

    @Test fun alarmLaterTodayStaysToday() {
        val now = at(2026, 10, 6, 10, 0)
        val next = ReminderPlan.nextAt(now, 18, 30, tz)
        assertEquals(at(2026, 10, 6, 18, 30), next)
        assertTrue(ReminderPlan.isToday(now, next, tz))
    }

    @Test fun alarmTimeAlreadyPassedGoesToTomorrow() {
        val now = at(2026, 10, 6, 19, 0)
        val next = ReminderPlan.nextAt(now, 18, 30, tz)
        assertEquals(at(2026, 10, 7, 18, 30), next)
        assertFalse(ReminderPlan.isToday(now, next, tz))
    }

    @Test fun alarmExactlyNowGoesToTomorrow() {
        val now = at(2026, 10, 6, 18, 30)
        assertEquals(at(2026, 10, 7, 18, 30), ReminderPlan.nextAt(now, 18, 30, tz))
    }

    @Test fun emptyPlan() {
        assertTrue(ReminderPlan().isEmpty)
        assertFalse(ReminderPlan(everyMin = 3).isEmpty)
    }
}
