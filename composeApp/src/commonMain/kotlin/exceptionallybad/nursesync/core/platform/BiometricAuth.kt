package exceptionallybad.nursesync.core.platform

expect class BiometricAuth {
    suspend fun isAvailable(): Boolean
    suspend fun authenticate(promptTitle: String, promptSubtitle: String? = null): Result<Unit>
}
