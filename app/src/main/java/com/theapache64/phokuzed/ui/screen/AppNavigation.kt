package com.theapache64.phokuzed.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.theapache64.phokuzed.ui.screen.blocklist.BlockListScreen
import com.theapache64.phokuzed.ui.screen.blocklist.BlockListViewModel
import com.theapache64.phokuzed.ui.screen.dashboard.DashboardScreen
import com.theapache64.phokuzed.ui.screen.splash.SplashScreen

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

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
                onEditBlockListClicked = { shouldEnableRemove ->
                    navController.navigate(
                        Screen.BlockList.createRoute(
                            shouldEnableRemove = shouldEnableRemove
                        )
                    )
                }
            )
        }

        // Blocklist
        composable(
            Screen.BlockList.route,
            arguments = listOf(
                navArgument(BlockListViewModel.ARG_SHOULD_ENABLE_REMOVE) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            BlockListScreen(onBackPressed = { navController.popBackStack() })
        }
    }
}