package exceptionallybad.nursesync.domain.repository

import exceptionallybad.nursesync.domain.model.LogEntry
import kotlinx.coroutines.flow.Flow

interface LogRepository {
    suspend fun saveLog(entry: LogEntry): Result<LogEntry>
    suspend fun updateLog(entry: LogEntry): Result<LogEntry>
    suspend fun deleteLog(id: String): Result<Unit>
    fun getLogsForShift(shiftId: String): Flow<List<LogEntry>>
    fun getLogsForPatient(patientId: String): Flow<List<LogEntry>>
    fun getFlaggedLogs(): Flow<List<LogEntry>>
}
