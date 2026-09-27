package com.jn.winremote.util

import com.jn.winremote.protocol.FileEventData
import com.jn.winremote.protocol.FileOp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FileEventFilterTest {

    private fun event(
        ts: Long = 100,
        op: String = FileOp.CREATED,
        path: String = "C:\\Users\\jake\\Downloads\\big.zip",
        sizeBytes: Long = 1000,
    ) = FileEventData(ts = ts, op = op, path = path, oldPath = null, sizeBytes = sizeBytes, isDir = false)

    @Test
    fun `no filter matches everything`() {
        assertTrue(FileEventFilter().matches(event()))
    }

    @Test
    fun `fromTs excludes earlier events`() {
        val filter = FileEventFilter(fromTsSeconds = 200)
        assertFalse(filter.matches(event(ts = 100)))
        assertTrue(filter.matches(event(ts = 200)))
        assertTrue(filter.matches(event(ts = 300)))
    }

    @Test
    fun `toTs excludes later events`() {
        val filter = FileEventFilter(toTsSeconds = 200)
        assertTrue(filter.matches(event(ts = 200)))
        assertFalse(filter.matches(event(ts = 201)))
    }

    @Test
    fun `op filter matches only the selected operation, case-insensitively`() {
        val filter = FileEventFilter(op = FileOp.DELETED)
        assertFalse(filter.matches(event(op = FileOp.CREATED)))
        assertTrue(filter.matches(event(op = FileOp.DELETED)))
        assertTrue(filter.matches(event(op = "DELETED")))
    }

    @Test
    fun `minSizeBytes excludes smaller files`() {
        val filter = FileEventFilter(minSizeBytes = 500)
        assertFalse(filter.matches(event(sizeBytes = 499)))
        assertTrue(filter.matches(event(sizeBytes = 500)))
        assertTrue(filter.matches(event(sizeBytes = 501)))
    }

    @Test
    fun `pathPrefix is case-insensitive and requires a real prefix match`() {
        val filter = FileEventFilter(pathPrefix = "c:\\users\\jake\\downloads")
        assertTrue(filter.matches(event(path = "C:\\Users\\jake\\Downloads\\big.zip")))
        assertFalse(filter.matches(event(path = "C:\\Users\\jake\\Documents\\big.zip")))
    }

    @Test
    fun `blank pathPrefix is treated as no filter`() {
        val filter = FileEventFilter(pathPrefix = "   ")
        assertTrue(filter.matches(event(path = "D:\\anything.txt")))
    }

    @Test
    fun `all filters combine with AND semantics`() {
        val filter = FileEventFilter(
            fromTsSeconds = 100,
            toTsSeconds = 500,
            op = FileOp.MODIFIED,
            minSizeBytes = 1_000_000,
            pathPrefix = "C:\\Users",
        )
        val matching = event(ts = 200, op = FileOp.MODIFIED, path = "C:\\Users\\jake\\a.bin", sizeBytes = 2_000_000)
        val wrongOp = matching.copy(op = FileOp.CREATED)
        val tooSmall = matching.copy(sizeBytes = 10)
        val wrongPath = matching.copy(path = "D:\\a.bin")

        assertTrue(filter.matches(matching))
        assertFalse(filter.matches(wrongOp))
        assertFalse(filter.matches(tooSmall))
        assertFalse(filter.matches(wrongPath))
    }

    @Test
    fun `applyFilter on a list keeps only matching events`() {
        val events = listOf(
            event(op = FileOp.CREATED),
            event(op = FileOp.DELETED),
            event(op = FileOp.MODIFIED),
        )
        val result = events.applyFilter(FileEventFilter(op = FileOp.DELETED))
        assertEquals(1, result.size)
        assertEquals(FileOp.DELETED, result[0].op)
    }
}
