package exceptionallybad.nursesync.data.mapper

import exceptionallybad.nursesync.database.PatientEntity
import exceptionallybad.nursesync.domain.model.Patient
import kotlinx.datetime.Instant

fun PatientEntity.toDomain(): Patient = Patient(
    id = id,
    name = name,
    bedNumber = bedNumber,
    wardId = wardId,
    age = age.toInt(),
    gender = gender,
    admissionReason = admissionReason,
    admissionDate = Instant.fromEpochMilliseconds(0), // Placeholder as it's not in the entity yet
    diagnosis = "", // Placeholder
    status = status,
)
