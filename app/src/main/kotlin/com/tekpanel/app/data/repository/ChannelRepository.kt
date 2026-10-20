package com.tekpanel.app.data.repository

import com.tekpanel.app.data.apps.AppInventory
import com.tekpanel.app.data.apps.InstalledApp
import com.tekpanel.app.data.catalog.KnownApps
import com.tekpanel.app.data.prefs.AppPreferences
import com.tekpanel.app.domain.model.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

/**
 * Joins what is installed (every app with a launcher icon), what the user picked, and how many
 * messages each app produced into the [Channel] list the UI renders.
 */
class ChannelRepository(
    private val inventory: AppInventory,
    private val preferences: AppPreferences,
    private val inboxRepository: InboxRepository,
) {

    private val installed = MutableStateFlow<List<InstalledApp>>(emptyList())

    /** Re-reads the installed apps and, on the very first run, pre-selects the usual chat apps. */
    suspend fun refresh() {
        val apps = inventory.refresh()
        installed.value = apps
        preferences.seedEnabledPackages(apps.map { it.packageName }.filter { it in KnownApps.DEFAULT_ON })
    }

    /** Every installed app as a channel, selected or not. */
    fun observeAllChannels(): Flow<List<Channel>> =
        combine(installed, preferences.enabledPackages, inboxRepository.observePackageCounts()) { apps, enabled, counts ->
            val byPackage = counts.associateBy { it.sourcePackage }
            apps.map { app ->
                val count = byPackage[app.packageName]
                Channel(
                    packageName = app.packageName,
                    label = app.label,
                    enabled = app.packageName in enabled,
                    totalMessages = count?.total ?: 0,
                    unreadMessages = count?.unread ?: 0,
                    lastMessageAt = count?.lastAt ?: 0L,
                    isPopular = app.packageName in KnownApps.POPULAR,
                )
            }
        }

    suspend fun setEnabled(packageName: String, enabled: Boolean) = preferences.setChannelEnabled(packageName, enabled)

    suspend fun setAllEnabled(packageNames: Collection<String>, enabled: Boolean) =
        preferences.setChannelsEnabled(packageNames, enabled)
}
