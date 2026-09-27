package com.jn.winremote.util

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class YuvRotateTest {

    /**
     * 2x3 (width=2, height=3) plane:
     *  1 2
     *  3 4
     *  5 6
     */
    private val data = byteArrayOf(1, 2, 3, 4, 5, 6)

    @Test
    fun `rotation 0 returns the same buffer and dimensions`() {
        val (out, w, h) = rotateLumaPlane(data, 2, 3, 0)
        assertArrayEquals(data, out)
        assertEquals(2, w)
        assertEquals(3, h)
    }

    @Test
    fun `rotation 90 clockwise transposes and flips`() {
        // 90 clockwise of
        //  1 2
        //  3 4
        //  5 6
        // is (width becomes 3, height becomes 2):
        //  5 3 1
        //  6 4 2
        val (out, w, h) = rotateLumaPlane(data, 2, 3, 90)
        assertEquals(3, w)
        assertEquals(2, h)
        assertArrayEquals(byteArrayOf(5, 3, 1, 6, 4, 2), out)
    }

    @Test
    fun `rotation 180 reverses the buffer, dimensions unchanged`() {
        val (out, w, h) = rotateLumaPlane(data, 2, 3, 180)
        assertEquals(2, w)
        assertEquals(3, h)
        assertArrayEquals(byteArrayOf(6, 5, 4, 3, 2, 1), out)
    }

    @Test
    fun `rotation 270 clockwise is the inverse of 90`() {
        val (out, w, h) = rotateLumaPlane(data, 2, 3, 270)
        assertEquals(3, w)
        assertEquals(2, h)
        assertArrayEquals(byteArrayOf(2, 4, 6, 1, 3, 5), out)
    }

    @Test
    fun `rotating 90 then 270 restores the original`() {
        val (once, w1, h1) = rotateLumaPlane(data, 2, 3, 90)
        val (twice, w2, h2) = rotateLumaPlane(once, w1, h1, 270)
        assertEquals(2, w2)
        assertEquals(3, h2)
        assertArrayEquals(data, twice)
    }

    @Test
    fun `negative rotation degrees normalize correctly`() {
        val fromPositive = rotateLumaPlane(data, 2, 3, 90).first
        val fromNegative = rotateLumaPlane(data, 2, 3, -270)
        assertArrayEquals(fromPositive, fromNegative.first)
    }
}
