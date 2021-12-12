package com.theapache64.phokuzed.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.theapache64.phokuzed.ui.screen.blocklist.BlockListScreen
import com.theapache64.phokuzed.ui.screen.blocklist.BlockListViewModel
import com.theapache64.phokuzed.ui.screen.blocklist.Mode
import com.theapache64.phokuzed.ui.screen.dashboard.DashboardScreen
import com.theapache64.phokuzed.ui.screen.splash.SplashScreen

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) { // TODO: Temp

        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    // Move to dashboard
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Dashboard
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onGoToBlockList = { mode ->
                    navController.navigate(Screen.BlockList.createRoute(mode))
                }
            )
        }

        // Blocklist
        composable(
            Screen.BlockList.route,
            arguments = listOf(
                navArgument(BlockListViewModel.KEY_ARG_MODE) {
                    // TODO: It works. but the API doesn't look good :(. Refactor later with something better maybe?
                    type = NavType.fromArgType(
                        "com.theapache64.phokuzed.ui.screen.blocklist.Mode",
                        "com.theapache64.phokuzed.ui.screen.blocklist"
                    )
                    defaultValue = Mode.ADD
                }
            )
        ) {
            BlockListScreen(onBackPressed = { navController.popBackStack() })
        }
    }
}
