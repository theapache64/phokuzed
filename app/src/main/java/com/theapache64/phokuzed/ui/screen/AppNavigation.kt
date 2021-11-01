package com.theapache64.phokuzed.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.theapache64.phokuzed.ui.screen.blocklist.BlockListScreen
import com.theapache64.phokuzed.ui.screen.dashboard.DashboardScreen
import com.theapache64.phokuzed.ui.screen.splash.SplashScreen

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.BlockList.route) {

        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    // Move to dashboard
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Dashboard
        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }

        // Blocklist
        composable(Screen.BlockList.route) {
            BlockListScreen(onBackPressed = { navController.popBackStack() })
        }
    }
}