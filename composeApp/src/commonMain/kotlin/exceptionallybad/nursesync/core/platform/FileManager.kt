package exceptionallybad.nursesync.core.platform

expect class FileManager {
    fun getAudioDirectory(): String
    fun createAudioFile(): String
    fun deleteFile(path: String): Boolean
    fun fileExists(path: String): Boolean
    fun getFileSize(path: String): Long
}
