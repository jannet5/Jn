package com.cepgozcu.app.ui.common

/** Shared shape for every screen's ViewModel state — covers every case the task asks each screen to render explicitly. */
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Content<T>(val data: T) : UiState<T>()
    data object Empty : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
    data object Offline : UiState<Nothing>()
    data object Unauthorized : UiState<Nothing>()
}
