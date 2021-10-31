package com.theapache64.phokuzed.ui.screen.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import app.cash.turbine.test
import com.github.theapache64.expekt.should
import com.theapache64.phokuzed.data.remote.Config
import com.theapache64.phokuzed.data.repo.ConfigRepo
import com.theapache64.phokuzed.test.MainCoroutineRule
import com.theapache64.phokuzed.util.Resource
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SplashViewModelTest {

    @Test
    fun `Shows update dialog for older version`() = runBlockingTest {
        // Fake repo
        val fakeConfigRepo = mock<ConfigRepo> {
            on { getRemoteConfig() } doReturn flow {
                val fakeConfig = Config(mandatoryVersionCode = Int.MAX_VALUE)
                emit(Resource.Success(fakeConfig))
            }
        }

        val viewModel = SplashViewModel(fakeConfigRepo)
        viewModel.viewAction.test {
            viewModel.init()
            awaitItem().action.should.be.instanceof(SplashViewAction.ShowUpdateDialog::class.java)
        }
    }

    @Test
    fun `Goes to main for newer versions`() = runBlockingTest {
        // Fake repo
        val fakeConfigRepo = mock<ConfigRepo> {
            on { getRemoteConfig() } doReturn flow {
                val fakeConfig = Config(mandatoryVersionCode = 1)
                emit(Resource.Success(fakeConfig))
            }
        }
        val viewModel = SplashViewModel(fakeConfigRepo)
        viewModel.viewAction.test {
            viewModel.init()
            awaitItem().action.should.be.instanceof(SplashViewAction.GoToMain::class.java)
        }
    }

    @Test
    fun `Version check happens on every onResume except the first`() = runBlockingTest {
        // Fake repo
        val fakeConfigRepo = mock<ConfigRepo> {
            onBlocking { getLocalConfig() } doReturn Config(mandatoryVersionCode = Int.MAX_VALUE)
        }

        val viewModel = SplashViewModel(fakeConfigRepo)
        viewModel.viewAction.test {
            viewModel.onResume(mock())
            viewModel.onResume(mock())
            awaitItem().action.should.be.instanceof(SplashViewAction.ShowUpdateDialog::class.java)
        }
    }

    @Test
    fun `Clicking update launches a URL`() = runBlockingTest {
        val viewModel = SplashViewModel(mock())
        viewModel.viewAction.test {
            viewModel.onInteraction(SplashInteractor.UpdateClick)
            awaitItem().action.should.instanceof(SplashViewAction.OpenUrl::class.java)
        }
    }

}