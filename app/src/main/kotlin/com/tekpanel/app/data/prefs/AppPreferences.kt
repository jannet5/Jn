package com.tekpanel.app.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "tekpanel_prefs")

/**
 * Channel selection and small UI flags. Message content always lives in Room, never here
 * (data_extraction_rules.xml excludes the Room DB and media files from backups).
 *
 * A channel is simply an installed app's package name; the set below is "the apps the user
 * wants to hear from". It is empty until [seedEnabledPackages] ran once on first launch.
 */
class AppPreferences(context: Context) {

    private val dataStore = context.dataStore

    private object Keys {
        val ENABLED_PACKAGES = stringSetPreferencesKey("enabled_packages")
        val SEEDED = booleanPreferencesKey("channels_seeded")
        val DIAGNOSTICS_UNLOCKED = booleanPreferencesKey("diagnostics_unlocked")
        val ACCESS_HINT_DISMISSED = booleanPreferencesKey("access_hint_dismissed")
        val PAYWALL_DISMISSED_DAY = longPreferencesKey("paywall_dismissed_day")
    }

    val enabledPackages: Flow<Set<String>> = dataStore.data.map { it[Keys.ENABLED_PACKAGES] ?: emptySet() }

    val channelsSeeded: Flow<Boolean> = dataStore.data.map { it[Keys.SEEDED] ?: false }

    val diagnosticsUnlocked: Flow<Boolean> = dataStore.data.map { it[Keys.DIAGNOSTICS_UNLOCKED] ?: false }

    val accessHintDismissed: Flow<Boolean> = dataStore.data.map { it[Keys.ACCESS_HINT_DISMISSED] ?: false }

    /** Epoch day on which the user pressed "not now" on the support sheet; it stays quiet for that day. */
    val paywallDismissedDay: Flow<Long?> = dataStore.data.map { it[Keys.PAYWALL_DISMISSED_DAY] }

    suspend fun setPaywallDismissedDay(day: Long) {
        dataStore.edit { prefs -> prefs[Keys.PAYWALL_DISMISSED_DAY] = day }
    }

    suspend fun isChannelEnabledNow(packageName: String): Boolean = packageName in enabledPackages.first()

    suspend fun setChannelEnabled(packageName: String, enabled: Boolean) {
        dataStore.edit { prefs ->
            val current = prefs[Keys.ENABLED_PACKAGES] ?: emptySet()
            prefs[Keys.ENABLED_PACKAGES] = if (enabled) current + packageName else current - packageName
            prefs[Keys.SEEDED] = true
        }
    }

    suspend fun setChannelsEnabled(packageNames: Collection<String>, enabled: Boolean) {
        dataStore.edit { prefs ->
            val current = prefs[Keys.ENABLED_PACKAGES] ?: emptySet()
            prefs[Keys.ENABLED_PACKAGES] = if (enabled) current + packageNames else current - packageNames.toSet()
            prefs[Keys.SEEDED] = true
        }
    }

    /** First launch only: pre-selects [defaults]. A no-op once the user (or a previous seed) chose. */
    suspend fun seedEnabledPackages(defaults: Collection<String>) {
        dataStore.edit { prefs ->
            if (prefs[Keys.SEEDED] != true) {
                prefs[Keys.ENABLED_PACKAGES] = (prefs[Keys.ENABLED_PACKAGES] ?: emptySet()) + defaults
                prefs[Keys.SEEDED] = true
            }
        }
    }

    suspend fun setDiagnosticsUnlocked(unlocked: Boolean) {
        dataStore.edit { prefs -> prefs[Keys.DIAGNOSTICS_UNLOCKED] = unlocked }
    }

    suspend fun setAccessHintDismissed(dismissed: Boolean) {
        dataStore.edit { prefs -> prefs[Keys.ACCESS_HINT_DISMISSED] = dismissed }
    }

    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
