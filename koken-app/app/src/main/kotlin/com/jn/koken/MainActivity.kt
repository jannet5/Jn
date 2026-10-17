package com.jn.koken

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.jn.koken.ui.KokenApp
import com.jn.koken.ui.KokenTheme

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Başka uygulamada metin seçip "Köken" denirse o sözcüğü hemen ara
        if (savedInstanceState == null) {
            intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()?.let { vm.submit(it) }
        }
        setContent { KokenTheme { KokenApp(vm) } }
    }
}
