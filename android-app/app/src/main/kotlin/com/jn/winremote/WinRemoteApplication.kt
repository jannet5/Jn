package com.jn.winremote

import android.app.Application
import com.jn.winremote.data.SecureStore
import com.jn.winremote.repository.PairingClient
import com.jn.winremote.repository.WinRemoteRepository

/**
 * Minimal hand-rolled service locator (no DI framework — the app is a
 * single module with a handful of collaborators). Everything here is a
 * process-wide singleton so the WebSocket connection survives navigation
 * between screens.
 */
class WinRemoteApplication : Application() {

    lateinit var secureStore: SecureStore
        private set
    lateinit var repository: WinRemoteRepository
        private set
    lateinit var pairingClient: PairingClient
        private set

    override fun onCreate() {
        super.onCreate()
        secureStore = SecureStore(this)
        repository = WinRemoteRepository(secureStore)
        pairingClient = PairingClient()
        repository.connectToActiveDevice()
    }
}
