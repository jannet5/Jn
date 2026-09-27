package com.jn.winremote.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Mirrors PROTOCOL.md §5's rules 1, 3, 4 (rule 2 cannot be mirrored client-side). */
class CriticalProcessRulesTest {

    @Test
    fun `pids 0 through 8 are protected`() {
        for (pid in 0..8) {
            assertTrue("pid=$pid", CriticalProcessRules.isLikelyProtected(pid, "whatever.exe", "C:\\whatever.exe", null))
        }
    }

    @Test
    fun `pid 9 and above is not protected by rule 1 alone`() {
        assertFalse(CriticalProcessRules.isLikelyProtected(9, "chrome.exe", "C:\\chrome.exe", "DESKTOP\\jake"))
    }

    @Test
    fun `system service owners are protected regardless of pid or name`() {
        assertTrue(
            CriticalProcessRules.isLikelyProtected(9999, "myapp.exe", "C:\\myapp.exe", "NT AUTHORITY\\SYSTEM")
        )
        assertTrue(
            CriticalProcessRules.isLikelyProtected(9999, "myapp.exe", "C:\\myapp.exe", "NT AUTHORITY\\LOCAL SERVICE")
        )
        assertTrue(
            CriticalProcessRules.isLikelyProtected(9999, "myapp.exe", "C:\\myapp.exe", "NT AUTHORITY\\NETWORK SERVICE")
        )
    }

    @Test
    fun `owner match is case-insensitive`() {
        assertTrue(
            CriticalProcessRules.isLikelyProtected(9999, "myapp.exe", "C:\\myapp.exe", "nt authority\\system")
        )
    }

    @Test
    fun `ordinary user-owned process is not protected`() {
        assertFalse(
            CriticalProcessRules.isLikelyProtected(9999, "myapp.exe", "C:\\myapp.exe", "DESKTOP\\jake")
        )
    }

    @Test
    fun `hardcoded critical executable names are protected by exe basename`() {
        val criticalExeNames = listOf(
            "smss.exe", "csrss.exe", "wininit.exe", "winlogon.exe", "services.exe",
            "lsass.exe", "lsaiso.exe", "svchost.exe", "fontdrvhost.exe", "dwm.exe",
            "explorer.exe", "wudfhost.exe", "sihost.exe", "ctfmon.exe",
        )
        for (exeName in criticalExeNames) {
            assertTrue(
                exeName,
                CriticalProcessRules.isLikelyProtected(9999, exeName, "C:\\Windows\\System32\\$exeName", "DESKTOP\\jake"),
            )
        }
    }

    @Test
    fun `exe basename match is case-insensitive and path-agnostic`() {
        assertTrue(
            CriticalProcessRules.isLikelyProtected(9999, "Explorer.exe", "C:\\WINDOWS\\EXPLORER.EXE", "DESKTOP\\jake")
        )
    }

    @Test
    fun `pseudo-processes without a real exe are matched by name`() {
        assertTrue(CriticalProcessRules.isLikelyProtected(4, "System", "", null))
        assertTrue(CriticalProcessRules.isLikelyProtected(9999, "Registry", "", "DESKTOP\\jake"))
        assertTrue(CriticalProcessRules.isLikelyProtected(9999, "Memory Compression", "", "DESKTOP\\jake"))
    }

    @Test
    fun `ordinary application is not protected`() {
        assertFalse(
            CriticalProcessRules.isLikelyProtected(9999, "chrome.exe", "C:\\Program Files\\Google\\Chrome\\chrome.exe", "DESKTOP\\jake")
        )
        assertFalse(
            CriticalProcessRules.isLikelyProtected(9999, "notepad.exe", "C:\\Windows\\System32\\notepad.exe", "DESKTOP\\jake")
        )
    }
}
