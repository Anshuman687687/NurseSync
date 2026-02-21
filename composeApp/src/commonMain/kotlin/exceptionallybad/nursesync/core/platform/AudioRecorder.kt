package exceptionallybad.nursesync.core.platform

import kotlinx.coroutines.flow.Flow

sealed interface AudioState {
    data object Idle : AudioState
    data object Recording : AudioState
    data class Error(val message: String) : AudioState
}

expect class AudioRecorder {
    fun startRecording(outputPath: String)
    fun stopRecording(): String?
    val audioState: Flow<AudioState>
}
