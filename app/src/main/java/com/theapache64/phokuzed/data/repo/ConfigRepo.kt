package com.theapache64.phokuzed.data.repo

import com.theapache64.phokuzed.data.remote.Config
import com.theapache64.phokuzed.util.Resource
import kotlinx.coroutines.flow.Flow

interface ConfigRepo {
    fun getRemoteConfig(): Flow<Resource<Config>>
    suspend fun getLocalConfig(): Config
    fun saveRemoteConfig(config: Config)
}