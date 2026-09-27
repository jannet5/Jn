package com.tekpanel.app

import android.app.Application
import com.tekpanel.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TekPanelApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TekPanelApp)
            modules(appModule)
        }
    }
}
