package exceptionallybad.nursesync.core.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

actual class AudioRecorder {
    actual fun startRecording(outputPath: String) {}
    actual fun stopRecording(): String? = null
    actual val audioState: Flow<AudioState> = emptyFlow()
}
