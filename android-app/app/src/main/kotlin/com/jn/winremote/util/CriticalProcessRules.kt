package com.jn.winremote.util

/**
 * Client-side mirror of PROTOCOL.md §5's critical-process rules, for a
 * pre-emptive UI hint (greying out "Sonlandır" / showing the "korumalı"
 * badge) *before* or *alongside* the server's authoritative `protected`
 * field on a `process_list` item.
 *
 * This is cosmetic only, exactly as §5 says: "the agent is the sole
 * enforcement point... the server re-checks on every kill_process
 * regardless of what the client sends." Nothing here ever skips or
 * replaces that server-side re-check.
 *
 * Rule 2 of §5 ("pid == agent's own pid") is intentionally NOT mirrored:
 * the protocol has no message that tells the client the agent's PID, so
 * this rule cannot be evaluated client-side.
 */
object CriticalProcessRules {

    private val protectedOwners = setOf(
        "nt authority\\system",
        "nt authority\\local service",
        "nt authority\\network service",
    )

    private val protectedNames = setOf(
        "system", "smss.exe", "csrss.exe", "wininit.exe", "winlogon.exe", "services.exe",
        "lsass.exe", "lsaiso.exe", "svchost.exe", "fontdrvhost.exe", "dwm.exe",
        "explorer.exe", "registry", "memory compression", "wudfhost.exe",
        "sihost.exe", "ctfmon.exe",
    )

    fun isLikelyProtected(pid: Int, name: String, exePath: String, owner: String?): Boolean {
        if (pid <= 8) return true
        if (owner != null && protectedOwners.contains(owner.trim().lowercase())) return true
        if (protectedNames.contains(name.trim().lowercase())) return true
        val exeBaseName = exePath.substringAfterLast('\\').substringAfterLast('/').trim().lowercase()
        if (exeBaseName.isNotEmpty() && protectedNames.contains(exeBaseName)) return true
        return false
    }
}
