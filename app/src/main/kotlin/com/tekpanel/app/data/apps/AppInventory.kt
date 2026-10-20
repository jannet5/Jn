package com.tekpanel.app.data.apps

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** A launchable app on this phone. [label] is what the user sees; the package name never is. */
data class InstalledApp(val packageName: String, val label: String)

/**
 * Lists every app that has a launcher icon, which is exactly what a person calls "an app on my
 * phone". Uses a `<queries><intent>` MAIN/LAUNCHER declaration instead of the restricted
 * QUERY_ALL_PACKAGES permission, so the Play listing needs no special permission declaration.
 */
class AppInventory(private val context: Context) {

    @Volatile private var cached: List<InstalledApp> = emptyList()

    /** Latest known list without touching the package manager; empty until [refresh] ran once. */
    fun snapshot(): List<InstalledApp> = cached

    suspend fun refresh(): List<InstalledApp> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val launcher = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolved = if (Build.VERSION.SDK_INT >= 33) {
            packageManager.queryIntentActivities(launcher, PackageManager.ResolveInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(launcher, 0)
        }
        val apps = resolved
            .asSequence()
            .map { it.activityInfo.packageName to it }
            .filter { (pkg, _) -> pkg != context.packageName }
            .distinctBy { (pkg, _) -> pkg }
            .map { (pkg, info) ->
                val label = runCatching { info.loadLabel(packageManager).toString() }.getOrNull()
                InstalledApp(pkg, label?.takeIf { it.isNotBlank() } ?: FALLBACK_LABEL)
            }
            .toList()
        cached = apps
        apps
    }

    /** Human label for [packageName], or the generic fallback; never the raw package id. */
    fun labelOf(packageName: String): String =
        cached.firstOrNull { it.packageName == packageName }?.label ?: FALLBACK_LABEL

    fun isInstalled(packageName: String): Boolean = cached.any { it.packageName == packageName }

    fun launchIntent(packageName: String): Intent? =
        context.packageManager.getLaunchIntentForPackage(packageName)?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    companion object {
        const val FALLBACK_LABEL = "Uygulama"
    }
}
