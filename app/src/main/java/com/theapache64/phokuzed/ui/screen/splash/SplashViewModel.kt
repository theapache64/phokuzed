package com.theapache64.phokuzed.ui.screen.splash

import androidx.lifecycle.*
import com.theapache64.phokuzed.BuildConfig
import com.theapache64.phokuzed.data.remote.Config
import com.theapache64.phokuzed.data.repo.ConfigRepoImpl
import com.theapache64.phokuzed.ui.base.BaseViewModel
import com.theapache64.phokuzed.util.Resource
import com.theapache64.phokuzed.util.exhaustive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val configRepoImpl: ConfigRepoImpl
) : BaseViewModel<SplashViewState, SplashInteractor, SplashViewAction>(
    defaultViewState = SplashViewState.ConfigLoading
), DefaultLifecycleObserver {

    init {
        Timber.d("SplashViewModel: created")
    }

    companion object {
        const val versionName = "v${BuildConfig.VERSION_NAME}"
        const val URL_PLAY_STORE =
            "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
    }

    init {
        viewModelScope.launch {
            configRepoImpl.getRemoteConfig().collect {
                when (it) {
                    is Resource.Idle -> {
                        // do nothing
                    }
                    is Resource.Loading -> {
                        emitViewState(SplashViewState.ConfigLoading)
                    }

                    is Resource.Success -> {
                        val config = it.data
                        // Saving new config
                        configRepoImpl.saveRemoteConfig(config)
                        performVersionCheck(config)
                    }
                    is Resource.Error -> {
                        emitViewState(SplashViewState.ConfigError(it.errorData))
                    }
                }
            }
        }
    }

    private fun performVersionCheck(config: Config) {
        // Version check
        val currentVersionCode = BuildConfig.VERSION_CODE
        val mandatoryVersionCode = config.mandatoryVersionCode

        if (currentVersionCode < mandatoryVersionCode) {
            // need to update
            emitViewAction(SplashViewAction.NeedUpdate)
        } else {
            emitViewAction(SplashViewAction.GoToMain)
        }
    }

    override fun onInteraction(interactor: SplashInteractor) {
        // no external interaction
        when (interactor) {
            SplashInteractor.UpdateClick -> {
                emitViewAction(SplashViewAction.OpenUrl(URL_PLAY_STORE))
            }
        }.exhaustive
    }

    var resumeCount = 0

    override fun onResume(owner: LifecycleOwner) {
        Timber.d("onResume: Hit it ")
        resumeCount++
        if (resumeCount > 1) {
            // After every resume, do version check
            viewModelScope.launch {
                val config = configRepoImpl.getLocalConfig()
                performVersionCheck(config)
            }
        }
    }
}