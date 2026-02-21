package exceptionallybad.nursesync.core.network

object ApiRoutes {
    private const val BASE_URL = "https://api.nursesync.example.com"
    const val LOGIN = "$BASE_URL/auth/login"
    const val REFRESH_TOKEN = "$BASE_URL/auth/refresh"
    const val DASHBOARD = "$BASE_URL/dashboard"
    const val LOGS = "$BASE_URL/logs"
    const val TASKS = "$BASE_URL/tasks"
    const val HANDOFFS = "$BASE_URL/handoffs"
    const val PRESCRIPTIONS = "$BASE_URL/prescriptions"
    const val CHAT = "$BASE_URL/chat"
}
