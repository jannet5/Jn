package com.tekpanel.app.data.catalog

import android.content.Context
import android.content.pm.PackageManager

/**
 * Detects which catalog entries are actually installed on this device (spec CAP-02, 3.5).
 *
 * Only packages declared in the manifest's <queries> block are visible to
 * [PackageManager.getPackageInfo] starting on Android 11 (API 30); since [SourceAppCatalog]
 * and the manifest queries list are kept in sync, this never needs QUERY_ALL_PACKAGES.
 */
class InstalledAppDetector(private val context: Context) {

    fun installedApps(): List<SourceApp> {
        val packageManager = context.packageManager
        return SourceAppCatalog.ALL.filter { app -> app.packageNames.any { isInstalled(packageManager, it) } }
    }

    fun isChannelInstalled(app: SourceApp): Boolean {
        val packageManager = context.packageManager
        return app.packageNames.any { isInstalled(packageManager, it) }
    }

    private fun isInstalled(packageManager: PackageManager, packageName: String): Boolean =
        try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
}
