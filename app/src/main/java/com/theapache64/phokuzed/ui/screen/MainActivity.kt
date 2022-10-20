package com.theapache64.phokuzed.ui.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.fragment.app.FragmentActivity
import com.theapache64.phokuzed.ui.theme.PhokuzedTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Splash still slow? check release build also
        setContent {
            PhokuzedTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}
