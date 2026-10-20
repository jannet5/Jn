package com.sende.app.di

import com.sende.app.capture.CaptureCoordinator
import com.sende.app.capture.DuplicateGuard
import com.sende.app.capture.LiveActionRegistry
import com.sende.app.capture.MessageFilterEngine
import com.sende.app.capture.PromotionalHeuristics
import com.sende.app.data.apps.AppIconLoader
import com.sende.app.data.apps.AppInventory
import com.sende.app.data.local.SendeDatabase
import com.sende.app.data.media.MediaLoader
import com.sende.app.data.media.MediaStore
import com.sende.app.data.prefs.AppPreferences
import com.sende.app.data.repository.ChannelRepository
import com.sende.app.data.repository.InboxRepository
import com.sende.app.diagnostics.DiagnosticsRecorder
import com.sende.app.domain.usecase.EraseAllDataUseCase
import com.sende.app.export.DataExporter
import com.sende.app.monetization.Dist
import com.sende.app.monetization.SupportViewModel
import com.sende.app.ui.channels.ChannelPickerViewModel
import com.sende.app.ui.diagnostics.DiagnosticsViewModel
import com.sende.app.ui.main.MainViewModel
import com.sende.app.util.IntentLauncher
import com.sende.app.util.ListenerHealth
import com.sende.app.util.NotificationAccessChecker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { SendeDatabase.build(get()) }
    single { get<SendeDatabase>().inboxMessageDao() }

    single { AppPreferences(get()) }
    single { AppInventory(get()) }
    single { AppIconLoader(get()) }
    single { MediaStore(get()) }
    single { MediaLoader() }
    single { NotificationAccessChecker(get()) }
    single { Dist.purchaseGateway(androidContext()) }

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

    viewModel {
        val context = androidContext()
        MainViewModel(get(), get(), get(), get(), get(), get(), get(), listenerHealthRestart = { ListenerHealth.restart(context) })
    }
    viewModel { ChannelPickerViewModel(get()) }
    viewModel { SupportViewModel(get(), get(), androidContext()) }
    viewModel { DiagnosticsViewModel(get(), get(), get()) }
}
