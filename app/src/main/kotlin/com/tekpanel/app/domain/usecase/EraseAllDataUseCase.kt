package com.tekpanel.app.domain.usecase

import com.tekpanel.app.capture.DuplicateGuard
import com.tekpanel.app.capture.LiveIntentRegistry
import com.tekpanel.app.data.prefs.AppPreferences
import com.tekpanel.app.data.repository.InboxRepository
import com.tekpanel.app.diagnostics.DiagnosticsRecorder

/**
 * CAP-17's "Tüm yerel veriyi sil": a distinct, stronger action from CAP-13's "Ekranı
 * tertemiz yap". This wipes every message, every channel/onboarding preference, and every
 * in-memory cache tied to them, leaving the app in its first-run state.
 */
class EraseAllDataUseCase(
    private val inboxRepository: InboxRepository,
    private val appPreferences: AppPreferences,
    private val duplicateGuard: DuplicateGuard,
    private val liveIntentRegistry: LiveIntentRegistry,
    private val diagnosticsRecorder: DiagnosticsRecorder,
) {
    suspend operator fun invoke() {
        inboxRepository.eraseAllMessages()
        appPreferences.clearAll()
        duplicateGuard.clear()
        liveIntentRegistry.clear()
        diagnosticsRecorder.reset()
    }
}
