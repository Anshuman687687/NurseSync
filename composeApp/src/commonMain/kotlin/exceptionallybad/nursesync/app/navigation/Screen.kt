package exceptionallybad.nursesync.app.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Login : Screen
    
    @Serializable
    data object RoleSelect : Screen
    
    @Serializable
    data object Dashboard : Screen
    
    @Serializable
    data object VoiceLog : Screen
    
    @Serializable
    data object Tasks : Screen
    
    @Serializable
    data object Handoff : Screen
    
    @Serializable
    data object Chat : Screen
    
    @Serializable
    data object Settings : Screen
}
