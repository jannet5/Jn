package com.notibox.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.compose.runtime.*
import com.notibox.app.data.Prefs
import com.notibox.app.ui.*

class MainActivity : AppCompatActivity() {
    private var unlocked by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = Prefs(this)
        unlocked = !prefs.appLock
        setContent {
            NotiTheme {
                if (unlocked) App() else LockScreen { promptUnlock() }
            }
        }
        if (!unlocked) promptUnlock()
    }

    override fun onStop() { super.onStop(); if (Prefs(this).appLock) unlocked = false }
    override fun onStart() { super.onStart(); if (!unlocked) promptUnlock() }

    private fun promptUnlock() {
        val p = BiometricPrompt(this, ContextCompat.getMainExecutor(this), object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(r: BiometricPrompt.AuthenticationResult) { unlocked = true }
        })
        p.authenticate(BiometricPrompt.PromptInfo.Builder().setTitle("NotiBox")
            .setAllowedAuthenticators(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK or
                androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL).build())
    }
}
