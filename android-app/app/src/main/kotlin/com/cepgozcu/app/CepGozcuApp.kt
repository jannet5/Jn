package com.cepgozcu.app

import android.app.Application
import com.cepgozcu.app.connection.ConnectionRepository
import com.cepgozcu.app.data.local.AppDatabase
import com.cepgozcu.app.notifications.NotificationHelper
import com.cepgozcu.app.pairing.PairingRepository
import com.cepgozcu.app.security.CredentialStore

/**
 * App-wide manual "service locator" — deliberately no Hilt/Dagger given the project's scope.
 * Everything here is a plain singleton built once in [onCreate] and read by ViewModel factories
 * via `(application as CepGozcuApp)`.
 */
class CepGozcuApp : Application() {

    lateinit var credentialStore: CredentialStore
        private set
    lateinit var connectionRepository: ConnectionRepository
        private set
    lateinit var pairingRepository: PairingRepository
        private set
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        credentialStore = CredentialStore(this)
        connectionRepository = ConnectionRepository(credentialStore)
        pairingRepository = PairingRepository()
        database = AppDatabase.getInstance(this)
        NotificationHelper.ensureChannels(this)

        if (credentialStore.loadSession() != null) {
            connectionRepository.startFromStoredSessionIfAny()
        }
    }
}
