package exceptionallybad.nursesync.core.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

inline fun <T, R> Flow<T>.mapResult(crossinline transform: (T) -> R): Flow<Result<R>> =
    this.map { data -> Result.success(transform(data)) }
        .catch { e -> emit(Result.failure(e)) }

inline fun <T> Flow<T>.catchToResult(): Flow<Result<T>> =
    this.map { Result.success(it) }
        .catch { e -> emit(Result.failure(e)) }
