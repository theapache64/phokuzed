package com.theapache64.phokuzed.ui.screen

import com.theapache64.phokuzed.ui.screen.blocklist.BlockListViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Dashboard : Screen("dashboard")
    object BlockList : Screen("block_list/{${BlockListViewModel.ARG_SHOULD_ENABLE_REMOVE}}") {
        fun createRoute(shouldEnableRemove: Boolean): String {
            return "block_list/$shouldEnableRemove"
        }
    }
    // Wondering how we pass arguments using this strucure?
    // See here -> https://github.com/chrisbanes/tivi/blob/add9b7d65633596fcebe32df5ef9ae9b40b1846f/app/src/main/java/app/tivi/AppNavigation.kt#L70
}