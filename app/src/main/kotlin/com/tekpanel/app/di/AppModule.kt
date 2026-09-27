package com.tekpanel.app.di

import com.tekpanel.app.capture.CaptureCoordinator
import com.tekpanel.app.capture.DuplicateGuard
import com.tekpanel.app.capture.LiveIntentRegistry
import com.tekpanel.app.capture.MessageFilterEngine
import com.tekpanel.app.capture.PromotionalHeuristics
import com.tekpanel.app.data.catalog.InstalledAppDetector
import com.tekpanel.app.data.local.TekPanelDatabase
import com.tekpanel.app.data.prefs.AppPreferences
import com.tekpanel.app.data.repository.ChannelRepository
import com.tekpanel.app.data.repository.InboxRepository
import com.tekpanel.app.diagnostics.DiagnosticsRecorder
import com.tekpanel.app.domain.usecase.EraseAllDataUseCase
import com.tekpanel.app.export.DataExporter
import com.tekpanel.app.ui.channels.ChannelSettingsViewModel
import com.tekpanel.app.ui.diagnostics.DiagnosticsViewModel
import com.tekpanel.app.ui.inbox.InboxViewModel
import com.tekpanel.app.util.IntentLauncher
import com.tekpanel.app.util.NotificationAccessChecker
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { TekPanelDatabase.build(get()) }
    single { get<TekPanelDatabase>().inboxMessageDao() }

    single { AppPreferences(get()) }
    single { InstalledAppDetector(get()) }
    single { NotificationAccessChecker(get()) }

    single { InboxRepository(get()) }
    single { ChannelRepository(get(), get()) }

    single { DiagnosticsRecorder() }
    single { LiveIntentRegistry() }
    single { DuplicateGuard() }
    single { PromotionalHeuristics() }
    single { MessageFilterEngine(get()) }
    single { CaptureCoordinator(get(), get(), get(), get(), get(), get()) }
    single { DataExporter(get()) }
    single { IntentLauncher(get(), get()) }
    single { EraseAllDataUseCase(get(), get(), get(), get(), get()) }

    viewModel { InboxViewModel(get(), get(), get(), get()) }
    viewModel { ChannelSettingsViewModel(get(), get(), get(), get()) }
    viewModel { DiagnosticsViewModel(get(), get(), get()) }
}
