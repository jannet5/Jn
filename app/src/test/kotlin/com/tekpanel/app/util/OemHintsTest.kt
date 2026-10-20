package com.tekpanel.app.util

import com.google.common.truth.Truth.assertThat
import com.tekpanel.app.R
import org.junit.Test

class OemHintsTest {
    @Test fun `picks the hint of the phone maker regardless of case`() {
        assertThat(OemHints.hintFor("Xiaomi")).isEqualTo(R.string.oem_hint_xiaomi)
        assertThat(OemHints.hintFor("POCO")).isEqualTo(R.string.oem_hint_xiaomi)
        assertThat(OemHints.hintFor("samsung")).isEqualTo(R.string.oem_hint_samsung)
        assertThat(OemHints.hintFor("HONOR")).isEqualTo(R.string.oem_hint_huawei)
        assertThat(OemHints.hintFor("realme")).isEqualTo(R.string.oem_hint_oppo)
        assertThat(OemHints.hintFor("vivo")).isEqualTo(R.string.oem_hint_vivo)
    }

    @Test fun `unknown or missing makers get no hint`() {
        assertThat(OemHints.hintFor("Google")).isNull()
        assertThat(OemHints.hintFor(null)).isNull()
        assertThat(OemHints.hintFor("")).isNull()
    }
}
