package exceptionallybad.nursesync.data.repository

import exceptionallybad.nursesync.domain.model.NurseProfile
import exceptionallybad.nursesync.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepositoryImpl : AuthRepository {
    private val _currentNurse = MutableStateFlow<NurseProfile?>(null)

    override suspend fun login(employeeId: String, pin: String): Result<NurseProfile> {
        // Mock login
        val profile = NurseProfile(
            id = "123",
            name = "Nurse Joy",
            employeeId = employeeId,
            role = "RN",
            ward = "ICU",
            languagePreference = "en",
        )
        _currentNurse.value = profile
        return Result.success(profile)
    }

    override suspend fun logout(): Result<Unit> {
        _currentNurse.value = null
        return Result.success(Unit)
    }

    override fun getCurrentNurse(): Flow<NurseProfile?> = _currentNurse.asStateFlow()

    override suspend fun refreshToken(): Result<Unit> {
        return Result.success(Unit)
    }
}
