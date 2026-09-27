package com.tekpanel.app.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tekpanel.app.data.catalog.SourceAppCatalog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "tekpanel_prefs")

/**
 * Channel on/off state and small UI/onboarding flags (spec CAP-03, CAP-08).
 *
 * Only preferences live here; message content always lives in Room (see
 * data_extraction_rules.xml, which excludes the Room DB but includes this DataStore file
 * from backups since none of these values are sensitive message content).
 */
class AppPreferences(context: Context) {

    private val dataStore = context.dataStore

    private object Keys {
        val ENABLED_CHANNEL_IDS = stringSetPreferencesKey("enabled_channel_ids")
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
        val DIAGNOSTICS_UNLOCKED = booleanPreferencesKey("diagnostics_unlocked")
    }

    /** Defaults to every installed channel being enabled the first time it's seen (CAP-03). */
    val enabledChannelIds: Flow<Set<String>> = dataStore.data.map { prefs ->
        prefs[Keys.ENABLED_CHANNEL_IDS] ?: SourceAppCatalog.ALL.map { it.id }.toSet()
    }

    val onboardingComplete: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_COMPLETE] ?: false
    }

    val diagnosticsUnlocked: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.DIAGNOSTICS_UNLOCKED] ?: false
    }

    suspend fun isChannelEnabledNow(channelId: String): Boolean = channelId in enabledChannelIds.first()

    suspend fun setChannelEnabled(channelId: String, enabled: Boolean) {
        dataStore.edit { prefs ->
            val current = prefs[Keys.ENABLED_CHANNEL_IDS] ?: SourceAppCatalog.ALL.map { it.id }.toSet()
            prefs[Keys.ENABLED_CHANNEL_IDS] = if (enabled) current + channelId else current - channelId
        }
    }

    suspend fun setAllChannelsEnabled(channelIds: Collection<String>, enabled: Boolean) {
        dataStore.edit { prefs ->
            val current = prefs[Keys.ENABLED_CHANNEL_IDS] ?: SourceAppCatalog.ALL.map { it.id }.toSet()
            prefs[Keys.ENABLED_CHANNEL_IDS] = if (enabled) current + channelIds else current - channelIds
        }
    }

    suspend fun setOnboardingComplete(complete: Boolean) {
        dataStore.edit { prefs -> prefs[Keys.ONBOARDING_COMPLETE] = complete }
    }

    suspend fun setDiagnosticsUnlocked(unlocked: Boolean) {
        dataStore.edit { prefs -> prefs[Keys.DIAGNOSTICS_UNLOCKED] = unlocked }
    }

    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
