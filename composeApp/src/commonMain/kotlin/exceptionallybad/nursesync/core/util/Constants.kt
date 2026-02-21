package exceptionallybad.nursesync.core.util

object Constants {
    const val MAX_AUDIO_DURATION_MS = 300_000L
    const val MIN_CONFIDENCE_THRESHOLD = 0.70f
    const val HIGH_CONFIDENCE_THRESHOLD = 0.85f
    const val SESSION_TIMEOUT_MS = 300_000L
    const val MAX_LOG_ENTRIES_PER_SHIFT = 500
    const val DATABASE_NAME = "nursesync_db"
    const val AUDIO_FILE_EXTENSION = ".m4a"
    const val SYNC_BATCH_SIZE = 50
}
