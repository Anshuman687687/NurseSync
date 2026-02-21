package exceptionallybad.nursesync.domain.repository

import exceptionallybad.nursesync.domain.model.Patient
import kotlinx.coroutines.flow.Flow

interface PatientRepository {
    fun getPatients(wardId: String): Flow<List<Patient>>
    suspend fun getPatientById(id: String): Result<Patient>
    suspend fun searchPatients(query: String): List<Patient>
}
