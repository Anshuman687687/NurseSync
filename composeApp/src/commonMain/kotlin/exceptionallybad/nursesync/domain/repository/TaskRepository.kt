package exceptionallybad.nursesync.domain.repository

import exceptionallybad.nursesync.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun createTask(task: Task): Result<Task>
    suspend fun completeTask(taskId: String): Result<Unit>
    fun getPendingTasks(shiftId: String): Flow<List<Task>>
    fun getOverdueTasks(): Flow<List<Task>>
}
