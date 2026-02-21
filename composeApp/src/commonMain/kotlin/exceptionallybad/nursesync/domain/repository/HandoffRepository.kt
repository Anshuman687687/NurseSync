package exceptionallybad.nursesync.domain.repository

import exceptionallybad.nursesync.domain.model.HandoffSummary

interface HandoffRepository {
    suspend fun generateHandoff(shiftId: String): Result<HandoffSummary>
    suspend fun getHandoffForShift(shiftId: String): Result<HandoffSummary?>
}
