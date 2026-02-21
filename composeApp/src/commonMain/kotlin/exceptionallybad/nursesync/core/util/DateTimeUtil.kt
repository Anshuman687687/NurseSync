package exceptionallybad.nursesync.core.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateTimeUtil {
    private val systemTimeZone = TimeZone.currentSystemDefault()

    fun Instant.toLocalDateTime(): LocalDateTime =
        this.toLocalDateTime(systemTimeZone)

    fun Instant.formatTime(): String {
        val dateTime = this.toLocalDateTime()
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')
        return "$hour:$minute"
    }

    fun Instant.formatDate(): String {
        val dateTime = this.toLocalDateTime()
        val day = dateTime.dayOfMonth.toString().padStart(2, '0')
        val month = dateTime.monthNumber.toString().padStart(2, '0')
        val year = dateTime.year
        return "$day/$month/$year"
    }

    fun Instant.formatDateTime(): String {
        return "${this.formatDate()} ${this.formatTime()}"
    }

    fun Instant.formatRelative(): String {
        val now = Instant.fromEpochMilliseconds(System.currentTimeMillis())
        val diffMs = now.toEpochMilliseconds() - this.toEpochMilliseconds()
        val diffMins = diffMs / 60_000
        val diffHours = diffMs / 3_600_000
        val diffDays = diffMs / 86_400_000

        return when {
            diffMins < 1 -> "Just now"
            diffMins < 60 -> "$diffMins min ago"
            diffHours < 24 -> "$diffHours hr ago"
            diffDays < 7 -> "$diffDays days ago"
            else -> this.formatDate()
        }
    }
}
