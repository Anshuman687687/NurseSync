package exceptionallybad.nursesync.core.platform

import kotlinx.coroutines.flow.Flow

data class SttResult(
    val text: String,
    val confidence: Float,
    val language: String,
    val isFinal: Boolean,
)

expect class SpeechToText {
    fun isAvailable(): Boolean
    suspend fun startListening(language: String = "en-US"): Flow<SttResult>
    fun stopListening()
    suspend fun transcribeFile(audioPath: String, language: String = "en-US"): SttResult
}
