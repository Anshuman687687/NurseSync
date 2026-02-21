package exceptionallybad.nursesync.core.platform

actual class FileManager {
    actual fun getAudioDirectory(): String = ""
    actual fun createAudioFile(): String = ""
    actual fun deleteFile(path: String): Boolean = false
    actual fun fileExists(path: String): Boolean = false
    actual fun getFileSize(path: String): Long = 0L
}
