package exceptionallybad.nursesync.domain.repository

import exceptionallybad.nursesync.domain.model.MedicationInfo
import exceptionallybad.nursesync.domain.model.Prescription
import kotlinx.coroutines.flow.Flow

interface PrescriptionRepository {
    fun getPrescriptionsForPatient(patientId: String): Flow<List<Prescription>>
    suspend fun getPrescriptionById(id: String): Result<Prescription>
    suspend fun uploadPrescription(patientId: String, imageData: ByteArray): Result<Prescription>
    suspend fun matchAgainstPrescription(
        patientId: String,
        medication: MedicationInfo,
    ): Result<PrescriptionMatchResult>
}

data class PrescriptionMatchResult(
    val matched: Boolean,
    val prescription: Prescription?,
    val discrepancy: String?,
)
