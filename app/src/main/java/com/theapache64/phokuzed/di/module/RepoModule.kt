package com.theapache64.phokuzed.di.module

import com.theapache64.phokuzed.data.repo.ConfigRepo
import com.theapache64.phokuzed.data.repo.ConfigRepoImpl
import com.theapache64.phokuzed.data.repo.TimeRepo
import com.theapache64.phokuzed.data.repo.TimeRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds abstract fun bindConfigRepo(repoImpl: ConfigRepoImpl): ConfigRepo
    @Binds abstract fun bindTimeRepo(repoImpl: TimeRepoImpl): TimeRepo
}