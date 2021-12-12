package com.theapache64.phokuzed.ui.screen

import com.theapache64.phokuzed.ui.screen.blocklist.BlockListViewModel
import com.theapache64.phokuzed.ui.screen.blocklist.Mode

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Dashboard : Screen("dashboard")
    object BlockList : Screen("block_list/{${BlockListViewModel.KEY_ARG_MODE}}") {
        fun createRoute(mode: Mode): String {
            return "block_list/$mode"
        }
    }
}
