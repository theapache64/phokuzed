package com.theapache64.phokuzed.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.theapache64.phokuzed.ui.screen.dashboard.DashboardScreen
import com.theapache64.phokuzed.ui.screen.splash.SplashScreen
import com.theapache64.phokuzed.ui.theme.PhokuzedTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            PhokuzedTheme {
                Surface {
                    NavHost(navController = navController, startDestination = Screen.Splash.route) {

                        // Splash
                        composable(Screen.Splash.route) {
                            SplashScreen(
                                onSplashFinished = {
                                    navController.popBackStack() // Remove splash from stack
                                    navController.navigate(Screen.Dashboard.route) // Move to dashboard
                                }
                            )
                        }

                        // Dashboard
                        composable(Screen.Dashboard.route) {
                            DashboardScreen()
                        }
                    }
                }
            }
        }
    }
}
