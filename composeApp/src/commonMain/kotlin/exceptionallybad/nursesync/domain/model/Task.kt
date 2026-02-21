package exceptionallybad.nursesync.domain.model

import exceptionallybad.nursesync.domain.model.enums.TaskPriority
import kotlinx.datetime.Instant

data class Task(
    val id: String,
    val patientId: String,
    val shiftId: String,
    val description: String,
    val priority: TaskPriority,
    val dueBy: Instant?,
    val completed: Boolean,
    val sourceLogId: String?,
)
