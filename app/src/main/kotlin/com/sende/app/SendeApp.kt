package com.sende.app

import android.app.Application
import com.sende.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SendeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SendeApp)
            modules(appModule)
        }
    }
}
