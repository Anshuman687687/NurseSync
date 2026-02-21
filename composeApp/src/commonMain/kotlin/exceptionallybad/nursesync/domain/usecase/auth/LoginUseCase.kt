package exceptionallybad.nursesync.domain.usecase.auth

import exceptionallybad.nursesync.domain.model.NurseProfile
import exceptionallybad.nursesync.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(employeeId: String, pin: String): Result<NurseProfile> =
        authRepository.login(employeeId, pin)
}
