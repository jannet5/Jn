package com.sende.app.domain.usecase

import com.sende.app.capture.DuplicateGuard
import com.sende.app.capture.LiveActionRegistry
import com.sende.app.data.prefs.AppPreferences
import com.sende.app.data.repository.InboxRepository
import com.sende.app.diagnostics.DiagnosticsRecorder

/**
 * CAP-17's "Tüm yerel veriyi sil": a distinct, stronger action from CAP-13's "Ekranı
 * tertemiz yap". This wipes every message, every channel/onboarding preference, and every
 * in-memory cache tied to them, leaving the app in its first-run state.
 */
class EraseAllDataUseCase(
    private val inboxRepository: InboxRepository,
    private val appPreferences: AppPreferences,
    private val duplicateGuard: DuplicateGuard,
    private val liveActionRegistry: LiveActionRegistry,
    private val diagnosticsRecorder: DiagnosticsRecorder,
) {
    suspend operator fun invoke() {
        inboxRepository.eraseAllMessages()
        appPreferences.clearAll()
        duplicateGuard.clear()
        liveActionRegistry.clear()
        diagnosticsRecorder.reset()
    }
}
