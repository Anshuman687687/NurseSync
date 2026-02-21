package exceptionallybad.nursesync.domain.repository

import exceptionallybad.nursesync.domain.model.NurseProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(employeeId: String, pin: String): Result<NurseProfile>
    suspend fun logout(): Result<Unit>
    fun getCurrentNurse(): Flow<NurseProfile?>
    suspend fun refreshToken(): Result<Unit>
}
