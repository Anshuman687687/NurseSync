package exceptionallybad.nursesync.domain.model

data class NurseProfile(
    val id: String,
    val name: String,
    val employeeId: String,
    val role: String,
    val ward: String,
    val languagePreference: String,
)
