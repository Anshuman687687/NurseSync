package exceptionallybad.nursesync.domain.model

import kotlinx.datetime.Instant

data class Patient(
    val id: String,
    val name: String,
    val bedNumber: String,
    val wardId: String,
    val age: Int,
    val gender: String,
    val admissionReason: String,
    val admissionDate: Instant,
    val diagnosis: String,
    val status: String,
)
