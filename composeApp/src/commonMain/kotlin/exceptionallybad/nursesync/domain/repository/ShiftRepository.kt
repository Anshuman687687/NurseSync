package exceptionallybad.nursesync.domain.repository

import exceptionallybad.nursesync.domain.model.Shift
import kotlinx.coroutines.flow.Flow

interface ShiftRepository {
    suspend fun startShift(nurseId: String, wardId: String): Result<Shift>
    suspend fun endShift(shiftId: String): Result<Shift>
    fun getActiveShift(nurseId: String): Flow<Shift?>
    fun getShiftHistory(nurseId: String): Flow<List<Shift>>
}
