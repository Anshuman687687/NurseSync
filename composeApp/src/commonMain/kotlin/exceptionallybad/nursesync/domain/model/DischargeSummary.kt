package exceptionallybad.nursesync.domain.model

import kotlinx.datetime.Instant

data class DischargeSummary(
    val id: String,
    val patientId: String,
    val generatedAt: Instant,
    val admissionSummary: String,
    val treatmentTimeline: List<LogEntry>,
    val medicationsAtDischarge: List<Prescription>,
    val followUpInstructions: String,
    val nurseNotes: String,
)
