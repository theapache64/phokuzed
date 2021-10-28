package com.theapache64.phokuzed.di.module

import com.theapache64.phokuzed.data.repo.ConfigRepo
import com.theapache64.phokuzed.data.repo.ConfigRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun bindRepoModule(repoImpl: ConfigRepoImpl): ConfigRepo
}