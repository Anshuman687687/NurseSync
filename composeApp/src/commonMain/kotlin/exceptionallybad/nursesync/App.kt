package exceptionallybad.nursesync

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import nursesync.composeapp.generated.resources.Res
import nursesync.composeapp.generated.resources.compose_multiplatform

import exceptionallybad.nursesync.app.theme.NurseSyncTheme

import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import exceptionallybad.nursesync.app.navigation.BottomNavBar
import exceptionallybad.nursesync.app.navigation.NavGraph
import exceptionallybad.nursesync.app.navigation.Screen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
@Preview
fun App() {
    NurseSyncTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        
        val showBottomBar = currentDestination?.hierarchy?.any { 
            it.hasRoute(Screen.Dashboard::class) || 
            it.hasRoute(Screen.VoiceLog::class) || 
            it.hasRoute(Screen.Tasks::class) || 
            it.hasRoute(Screen.Handoff::class) || 
            it.hasRoute(Screen.Chat::class) || 
            it.hasRoute(Screen.Settings::class)
        } == true

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomNavBar(navController = navController)
                }
            }
        ) { padding ->
            NavGraph(
                navController = navController,
                modifier = Modifier.padding(padding)
            )
        }
    }
}