package com.theapache64.phokuzed.di.module

import com.theapache64.phokuzed.data.repo.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun bindConfigRepo(repoImpl: ConfigRepoImpl): ConfigRepo
    @Binds
    abstract fun bindTimeRepo(repoImpl: TimeRepoImpl): TimeRepo
    @Binds
    abstract fun bindHostRepo(repoImpl: HostRepoImpl): HostRepo
    @Binds
    abstract fun bindBlockListRepo(repoImpl: BlockListRepoImpl): BlockListRepo
    @Binds
    abstract fun bindRootRepo(repoImpl: RootRepoImpl): RootRepo
}
