package exceptionallybad.nursesync.domain.model

import exceptionallybad.nursesync.domain.model.enums.ClinicalAction
import exceptionallybad.nursesync.domain.model.enums.LogStatus
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class StructuredLogData(
    val action: ClinicalAction,
    val medication: MedicationInfo? = null,
    val vitals: VitalsInfo? = null,
    val notes: String? = null,
)

@Serializable
data class MedicationInfo(
    val drugName: String,
    val dosage: String,
    val route: String,
    val administeredAt: Instant,
)

@Serializable
data class VitalsInfo(
    val temperature: Float? = null,
    val bloodPressureSystolic: Int? = null,
    val bloodPressureDiastolic: Int? = null,
    val heartRate: Int? = null,
    val respiratoryRate: Int? = null,
    val oxygenSaturation: Float? = null,
    val painScore: Int? = null,
)

data class LogEntry(
    val id: String,
    val shiftId: String,
    val patientId: String,
    val timestamp: Instant,
    val rawTranscript: String,
    val editedTranscript: String?,
    val audioFilePath: String?,
    val structuredData: StructuredLogData,
    val confidenceScore: Float,
    val flaggedForReview: Boolean,
    val status: LogStatus,
)
