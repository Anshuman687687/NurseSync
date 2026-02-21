package exceptionallybad.nursesync.core.platform

expect class PermissionHandler {
    suspend fun requestMicrophonePermission(): Boolean
    suspend fun requestSpeechRecognitionPermission(): Boolean
    suspend fun hasMicrophonePermission(): Boolean
    suspend fun hasSpeechRecognitionPermission(): Boolean
    suspend fun requestAllPermissions(): Boolean
}
