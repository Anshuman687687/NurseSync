package exceptionallybad.nursesync.core.platform

actual class PermissionHandler {
    actual suspend fun requestMicrophonePermission(): Boolean = false
    actual suspend fun requestSpeechRecognitionPermission(): Boolean = false
    actual suspend fun hasMicrophonePermission(): Boolean = false
    actual suspend fun hasSpeechRecognitionPermission(): Boolean = false
    actual suspend fun requestAllPermissions(): Boolean = false
}
