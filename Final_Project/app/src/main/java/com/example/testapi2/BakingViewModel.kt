package com.example.testapi2

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BakingViewModel : ViewModel() {
    private val geminiClient = GeminiClient()
    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState

    fun sendPrompt(bitmap: Bitmap, prompt: String, mode: String = "student") {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val actualPrompt = when (mode) {
                    "study" -> "Answer this study question clearly:\n$prompt"
                    else -> "Analyze student info and respond:\n$prompt"
                }
                val result = geminiClient.generateContent(bitmap, actualPrompt)
                _uiState.value = UiState.Success(result.text)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
