package com.tekpanel.app.di

import com.tekpanel.app.capture.CaptureCoordinator
import com.tekpanel.app.capture.DuplicateGuard
import com.tekpanel.app.capture.LiveActionRegistry
import com.tekpanel.app.capture.MessageFilterEngine
import com.tekpanel.app.capture.PromotionalHeuristics
import com.tekpanel.app.data.apps.AppIconLoader
import com.tekpanel.app.data.apps.AppInventory
import com.tekpanel.app.data.local.TekPanelDatabase
import com.tekpanel.app.data.media.MediaLoader
import com.tekpanel.app.data.media.MediaStore
import com.tekpanel.app.data.prefs.AppPreferences
import com.tekpanel.app.data.repository.ChannelRepository
import com.tekpanel.app.data.repository.InboxRepository
import com.tekpanel.app.diagnostics.DiagnosticsRecorder
import com.tekpanel.app.domain.usecase.EraseAllDataUseCase
import com.tekpanel.app.export.DataExporter
import com.tekpanel.app.ui.channels.ChannelPickerViewModel
import com.tekpanel.app.ui.diagnostics.DiagnosticsViewModel
import com.tekpanel.app.ui.main.MainViewModel
import com.tekpanel.app.util.IntentLauncher
import com.tekpanel.app.util.NotificationAccessChecker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { TekPanelDatabase.build(get()) }
    single { get<TekPanelDatabase>().inboxMessageDao() }

    single { AppPreferences(get()) }
    single { AppInventory(get()) }
    single { AppIconLoader(get()) }
    single { MediaStore(get()) }
    single { MediaLoader() }
    single { NotificationAccessChecker(get()) }

    single { InboxRepository(get(), get()) }
    single { ChannelRepository(get(), get(), get()) }

    single { DiagnosticsRecorder() }
    single { LiveActionRegistry() }
    single { DuplicateGuard() }
    single { PromotionalHeuristics() }
    single { MessageFilterEngine(ownPackage = androidContext().packageName, heuristics = get()) }
    single { CaptureCoordinator(get(), get(), get(), get(), get(), get(), get()) }
    single { DataExporter(get()) }
    single { IntentLauncher(get(), get()) }
    single { EraseAllDataUseCase(get(), get(), get(), get(), get()) }

    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ChannelPickerViewModel(get()) }
    viewModel { DiagnosticsViewModel(get(), get(), get()) }
}
