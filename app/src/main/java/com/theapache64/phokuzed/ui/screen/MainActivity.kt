package com.theapache64.phokuzed.ui.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.theapache64.phokuzed.core.MountType
import com.theapache64.phokuzed.core.RootUtils
import com.theapache64.phokuzed.core.RootUtils.remountSystemPartition
import com.theapache64.phokuzed.ui.theme.PhokuzedTheme
import com.topjohnwu.superuser.Shell
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.util.*

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Splash still slow? check release build also
        setContent {
            val navController = rememberNavController()
            PhokuzedTheme {
                Surface {
                    AppNavigation(navController)
                }
            }
        }

        Thread {
            /*val hostFile = File("/system/etc/hosts").canonicalFile
            remountSystemPartition(hostFile, MountType.READ_WRITE)
            Shell.su("echo \"# ${Date()}\" >> /system/etc/hosts").exec().also {
                Timber.d("onCreate: ${it.err} : ${it.out}")
            }*/

        }.start()

    }
}
