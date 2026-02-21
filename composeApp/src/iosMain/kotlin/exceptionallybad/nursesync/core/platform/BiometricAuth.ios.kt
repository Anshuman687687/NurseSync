package exceptionallybad.nursesync.core.platform

actual class BiometricAuth {
    actual suspend fun isAvailable(): Boolean = false
    actual suspend fun authenticate(
        promptTitle: String,
        promptSubtitle: String?,
    ): Result<Unit> = Result.failure(Exception("Not implemented"))
}
