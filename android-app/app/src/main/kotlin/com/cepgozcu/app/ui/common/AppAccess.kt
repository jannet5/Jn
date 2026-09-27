package com.cepgozcu.app.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.cepgozcu.app.CepGozcuApp

/** Reads the manually-built app singletons from anywhere in the Compose tree. */
@Composable
fun localApp(): CepGozcuApp = LocalContext.current.applicationContext as CepGozcuApp
