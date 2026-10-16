package com.jn.yazikart // test paketi

import com.jn.yazikart.render.Snap // ortaya yapışma
import org.junit.Assert.assertEquals // eşitlik
import org.junit.Assert.assertFalse // yanlışlık
import org.junit.Assert.assertTrue // doğruluk
import org.junit.Test // test işareti

// Mıknatıslı ortalama hesabının testleri
class SnapTest {
    @Test
    fun yakinsaOrtayaYapisir() { // merkez ortaya 20 px uzakta, eşik 1000*0.025=25 px
        val (x, ok) = Snap.snap(raw = 0.1f, centerPx = 480f, targetPx = 500f, sizePx = 1000f) // yazı biraz solda
        assertTrue(ok) // yapıştı
        assertEquals(0.12f, x, 1e-6f) // 20 px = 0.02 oran sağa kaydırıldı, merkez tam ortada
    }

    @Test
    fun uzaksaParmakNeredeyseOrada() { // 100 px uzakta
        val (x, ok) = Snap.snap(raw = -0.3f, centerPx = 400f, targetPx = 500f, sizePx = 1000f) // uzak
        assertFalse(ok) // yapışmadı
        assertEquals(-0.3f, x, 0f) // kayma değişmedi
    }
}
