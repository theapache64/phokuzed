package com.theapache64.phokuzed.ui.screen.splash

import androidx.lifecycle.viewModelScope
import com.theapache64.phokuzed.BuildConfig
import com.theapache64.phokuzed.data.repo.ConfigRepo
import com.theapache64.phokuzed.ui.base.BaseViewModel
import com.theapache64.phokuzed.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val configRepo: ConfigRepo
) : BaseViewModel<SplashViewState, SplashInteractor, SplashViewAction>(
    defaultViewState = SplashViewState.ConfigLoading
) {

    init {
        Timber.d("SplashViewModel: created")
    }

    companion object {
        const val versionName = "v${BuildConfig.VERSION_NAME}"
    }

    init {
        viewModelScope.launch {
            configRepo.getRemoteConfig().collect {
                when (it) {
                    is Resource.Idle -> {
                        // do nothing
                    }
                    is Resource.Loading -> {
                        //
                    }
                    is Resource.Error -> {
                        emitViewState(SplashViewState.ConfigError(it.errorData))
                    }
                    is Resource.Success -> {
                        configRepo.saveRemoteConfig(it.data)
                        emitViewAction(SplashViewAction.GoToMain)
                    }
                }
            }
        }
    }

    override fun onInteraction(interactor: SplashInteractor) {
        // no external interaction
    }
}