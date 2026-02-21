package exceptionallybad.nursesync.core.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

actual class SpeechToText {
    actual fun isAvailable(): Boolean = false
    actual suspend fun startListening(language: String): Flow<SttResult> = emptyFlow()
    actual fun stopListening() {}
    actual suspend fun transcribeFile(
        audioPath: String,
        language: String,
    ): SttResult = SttResult("", 0f, "", false)
}
