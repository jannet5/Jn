package com.sende.app.capture

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UntimedLineTrackerTest {

    private val tracker = UntimedLineTracker()

    @Test
    fun `the first delivery counts every line as new`() {
        val diff = tracker.diff("k", listOf("a", "b"))
        assertThat(diff.newIndices).containsExactly(0, 1).inOrder()
        assertThat(diff.firstSighting).isTrue()
    }

    @Test
    fun `an update that appends one line yields only that line`() {
        tracker.diff("k", listOf("merhaba"))
        val diff = tracker.diff("k", listOf("merhaba", "fiyat?"))
        assertThat(diff.newIndices).containsExactly(1)
        assertThat(diff.firstSighting).isFalse()
    }

    @Test
    fun `the very same lines delivered again yield nothing`() {
        tracker.diff("k", listOf("evet"))
        assertThat(tracker.diff("k", listOf("evet")).newIndices).isEmpty()
    }

    @Test
    fun `the same word sent again after another line is a new message`() {
        tracker.diff("k", listOf("evet"))
        tracker.diff("k", listOf("evet", "tamam"))
        assertThat(tracker.diff("k", listOf("evet", "tamam", "evet")).newIndices).containsExactly(2)
    }

    @Test
    fun `a dismissed notification starts fresh so the same word again is accepted`() {
        tracker.diff("k", listOf("evet"))
        tracker.forget("k")
        assertThat(tracker.diff("k", listOf("evet")).newIndices).containsExactly(0)
    }

    @Test
    fun `lines scrolling off the top of the notification are not new`() {
        tracker.diff("k", listOf("a", "b", "c"))
        assertThat(tracker.diff("k", listOf("b", "c")).newIndices).isEmpty()
    }

    @Test
    fun `scrolled lines plus a new one yield only the new one`() {
        tracker.diff("k", listOf("a", "b", "c"))
        assertThat(tracker.diff("k", listOf("b", "c", "d")).newIndices).containsExactly(2)
    }

    @Test
    fun `different notifications never influence each other`() {
        tracker.diff("one", listOf("evet"))
        assertThat(tracker.diff("two", listOf("evet")).newIndices).containsExactly(0)
    }

    @Test
    fun `the oldest notification is forgotten beyond the capacity`() {
        val small = UntimedLineTracker(capacity = 2)
        small.diff("1", listOf("x"))
        small.diff("2", listOf("y"))
        small.diff("3", listOf("z"))
        assertThat(small.diff("1", listOf("x")).firstSighting).isTrue()
    }
}
