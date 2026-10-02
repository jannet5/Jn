package app.nokta.b

import app.nokta.b.bubble.BubbleMath
import org.junit.Assert.*
import org.junit.Test

class BubbleMathTest {
    @Test fun edgeIsRightAlignedWithGap() = assertEquals(1080 - 140 - 12, BubbleMath.edgeX(1080, 140, 12))
    @Test fun clampXKeepsInside() {
        assertEquals(0, BubbleMath.clampX(-50, 1080, 140))
        assertEquals(940, BubbleMath.clampX(2000, 1080, 140))
        assertEquals(300, BubbleMath.clampX(300, 1080, 140))
    }
    @Test fun clampYKeepsMargins() {
        assertEquals(60, BubbleMath.clampY(-10, 2400, 140, 60))
        assertEquals(2400 - 140 - 60, BubbleMath.clampY(5000, 2400, 140, 60))
    }
    @Test fun tinyScreenDoesNotCrash() = assertEquals(60, BubbleMath.clampY(10, 100, 140, 60))
    @Test fun overTargetUsesCenterDistance() {
        assertTrue(BubbleMath.overTarget(540f, 2200f, 540f, 2250f, 100f))
        assertFalse(BubbleMath.overTarget(100f, 500f, 540f, 2250f, 100f))
    }
}
