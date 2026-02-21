package exceptionallybad.nursesync.domain.model

import exceptionallybad.nursesync.domain.model.enums.ShiftStatus
import kotlinx.datetime.Instant

data class Shift(
    val id: String,
    val nurseId: String,
    val wardId: String,
    val startTime: Instant,
    val endTime: Instant?,
    val status: ShiftStatus,
)
