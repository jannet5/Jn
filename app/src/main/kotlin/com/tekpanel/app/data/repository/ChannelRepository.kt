package com.tekpanel.app.data.repository

import com.tekpanel.app.data.catalog.InstalledAppDetector
import com.tekpanel.app.data.catalog.SourceApp
import com.tekpanel.app.data.prefs.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** A catalog entry as shown in the channel settings sheet: only installed apps are ever listed (CAP-02). */
data class ChannelUiEntry(val app: SourceApp, val enabled: Boolean)

class ChannelRepository(
    private val installedAppDetector: InstalledAppDetector,
    private val preferences: AppPreferences,
) {

    fun observeInstalledChannels(): Flow<List<ChannelUiEntry>> {
        val installed = installedAppDetector.installedApps()
        return preferences.enabledChannelIds.map { enabledIds ->
            installed.map { app -> ChannelUiEntry(app, enabled = app.id in enabledIds) }
        }
    }

    suspend fun setChannelEnabled(channelId: String, enabled: Boolean) =
        preferences.setChannelEnabled(channelId, enabled)

    suspend fun setAllInstalledEnabled(enabled: Boolean) {
        val installedIds = installedAppDetector.installedApps().map { it.id }
        preferences.setAllChannelsEnabled(installedIds, enabled)
    }

    fun installedChannelIds(): Set<String> = installedAppDetector.installedApps().map { it.id }.toSet()
}
