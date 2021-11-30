package com.theapache64.phokuzed.ui.screen.splash

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.theapache64.phokuzed.BuildConfig
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.data.local.entity.Subdomain
import com.theapache64.phokuzed.data.map.map
import com.theapache64.phokuzed.data.remote.Config
import com.theapache64.phokuzed.data.repo.ConfigRepo
import com.theapache64.phokuzed.data.repo.RootRepo
import com.theapache64.phokuzed.data.repo.SubdomainRepo
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
    private val configRepo: ConfigRepo,
    private val rootRepo: RootRepo,
    private val subdomainRepo: SubdomainRepo
) : BaseViewModel<SplashViewState, SplashInteractor, SplashViewAction>(
    defaultViewState = SplashViewState.Loading(R.string.splash_loading_init)
),
    DefaultLifecycleObserver {

    init {
        Timber.d("SplashViewModel: created")
    }

    companion object {
        const val versionName = "v${BuildConfig.VERSION_NAME}"
        const val URL_PLAY_STORE =
            "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
    }

    fun init() {
        viewModelScope.launch {
            configRepo.getRemoteConfig()
                .collect {
                    when (it) {
                        is Resource.Idle -> {
                            // do nothing
                        }
                        is Resource.Loading -> {
                            emitViewState(SplashViewState.Loading(R.string.splash_loading_config))
                        }

                        is Resource.Success -> {
                            val config = it.data
                            onConfigLoaded(config)
                        }
                        is Resource.Error -> {
                            emitViewState(SplashViewState.Error(it.errorData))
                        }
                    }
                }
        }
    }

    private fun onConfigLoaded(config: Config) {
        // Saving new config
        configRepo.saveRemoteConfig(config)

        viewModelScope.launch {
            subdomainRepo.getRemoteSubdomains()
                .collect { response ->
                    when (response) {
                        is Resource.Idle -> {
                            // do nothing
                        }
                        is Resource.Loading -> {
                            emitViewState(SplashViewState.Loading(R.string.splash_loading_subdomains))
                        }
                        is Resource.Success -> {
                            val newSubdomains = response.data.map { it.map() }
                            onSubdomainsLoaded(newSubdomains)
                        }
                        is Resource.Error -> {
                            emitViewState(SplashViewState.Error(response.errorData))
                        }
                    }.exhaustive()
                }
        }
    }

    private fun onSubdomainsLoaded(newSubdomains: List<Subdomain>) {
        viewModelScope.launch {
            subdomainRepo.nukeLocalSubdomains()
            subdomainRepo.addAll(newSubdomains)
            performRootCheckAndGoToMainOrThrowErrorState()
        }
    }

    private fun performRootCheckAndGoToMainOrThrowErrorState(onRootAccess: () -> Unit) {
        viewModelScope.launch {
            if (rootRepo.isRooted()) {
                Timber.d("performRootCheck: yey got root")
                onRootAccess()
            } else {
                Timber.e("performRootCheck: uh ho no root")
                emitViewState(SplashViewState.NoRootAccess)
            }
        }
    }

    private fun performVersionCheck(
        config: Config,
        onValidVersion: () -> Unit
    ) {
        // Version check
        val currentVersionCode = BuildConfig.VERSION_CODE
        val mandatoryVersionCode = config.mandatoryVersionCode

        if (currentVersionCode < mandatoryVersionCode) {
            // need to update
            emitViewAction(SplashViewAction.ShowUpdateDialog)
        } else {
            onValidVersion()
        }
    }

    override fun onInteraction(interactor: SplashInteractor) {
        // no external interaction
        when (interactor) {
            SplashInteractor.UpdateClick -> {
                emitViewAction(SplashViewAction.OpenUrl(URL_PLAY_STORE))
            }
            SplashInteractor.RetryRootCheckClick -> {
                performRootCheckAndGoToMainOrThrowErrorState()
            }
        }.exhaustive()
    }

    var resumeCount = 0

    override fun onStart(owner: LifecycleOwner) {
        resumeCount++
        if (resumeCount > 1) {
            // After every resume, do version check
            viewModelScope.launch {
                val config = configRepo.getLocalConfig()
                performVersionCheck(config) {
                    performRootCheckAndGoToMainOrThrowErrorState()
                }
            }
        }
    }

    // TODO: Suggest better name if you have any :P
    private fun performRootCheckAndGoToMainOrThrowErrorState() {
        performRootCheckAndGoToMainOrThrowErrorState {
            emitViewAction(SplashViewAction.GoToMain)
        }
    }
}
