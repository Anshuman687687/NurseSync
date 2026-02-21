package exceptionallybad.nursesync.domain.model

import kotlinx.datetime.Instant

data class HandoffSummary(
    val id: String,
    val outgoingShiftId: String,
    val incomingNurseId: String?,
    val generatedAt: Instant,
    val patientSummaries: List<PatientHandoffNote>,
    val pendingTasks: List<Task>,
    val audioSummaryPath: String?,
)

data class PatientHandoffNote(
    val patientId: String,
    val patientName: String,
    val bed: String,
    val summaryText: String,
    val pendingActions: List<String>,
    val alertFlags: List<String>,
)
