package exceptionallybad.nursesync.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment

import exceptionallybad.nursesync.feature.auth.ui.LoginScreen

import exceptionallybad.nursesync.feature.dashboard.ui.DashboardScreen
import exceptionallybad.nursesync.feature.discharge.ui.DischargeSummaryScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard,
        modifier = modifier,
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard) {
                        popUpTo<Screen.Login> { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.Dashboard> {
            DashboardScreen()
        }
        composable<Screen.VoiceLog> {
            PlaceholderScreen("Voice Log Screen")
        }
        composable<Screen.Tasks> {
            PlaceholderScreen("Tasks Screen")
        }
        composable<Screen.Handoff> {
            PlaceholderScreen("Handoff Screen")
        }
        composable<Screen.Chat> {
            PlaceholderScreen("AI Chat Screen")
        }
        composable<Screen.DischargeSummary> {
            DischargeSummaryScreen(
                onBackClick = { navController.popBackStack() },
                onShareClick = { /* Handle share */ }
            )
        }
        composable<Screen.Settings> {
            PlaceholderScreen("Settings Screen")
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(name)
    }
}
