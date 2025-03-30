package com.example.testapi2

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig

data class GeminiResponse(val text: String)

class GeminiClient {

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash", // ✅ Cập nhật model mới
        apiKey = BuildConfig.apiKey,
        generationConfig = generationConfig {
            temperature = 0.7f
        }
    )

    suspend fun generateContent(bitmap: Bitmap, prompt: String): GeminiResponse {
        val response = model.generateContent(
            content {
                image(bitmap)
                text(prompt)
            }
        )
        val output = response.text ?: "No response from Gemini"
        return GeminiResponse(text = output)
    }
}