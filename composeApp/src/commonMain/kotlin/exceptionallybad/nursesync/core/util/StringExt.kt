package exceptionallybad.nursesync.core.util

fun String.truncate(maxLength: Int): String {
    return if (this.length <= maxLength) this
    else "${this.take(maxLength - 3)}..."
}

fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { word ->
        word.lowercase().replaceFirstChar { it.uppercase() }
    }
}

fun String?.orEmpty(): String = this ?: ""

fun String?.isNotNullOrBlank(): Boolean = !this.isNullOrBlank()

fun Float.toConfidenceLabel(): String = when {
    this >= 0.85f -> "High"
    this >= 0.70f -> "Medium"
    else -> "Low"
}
