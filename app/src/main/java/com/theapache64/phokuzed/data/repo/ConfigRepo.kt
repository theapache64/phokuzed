package com.theapache64.phokuzed.data.repo

import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import com.theapache64.phokuzed.data.remote.Api
import com.theapache64.phokuzed.data.remote.Config
import com.theapache64.phokuzed.data.remote.ConfigJsonAdapter
import com.theapache64.phokuzed.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface ConfigRepo {
    fun getRemoteConfig(): Flow<Resource<Config>>
    suspend fun getLocalConfig(): Config
    fun saveRemoteConfig(config: Config)
}

@Singleton
class ConfigRepoImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val api: Api,
    private val moshi: Moshi
) : ConfigRepo {

    companion object {
        private const val KEY_CONFIG = "config"
    }

    private val configJsonAdapter by lazy {
        ConfigJsonAdapter(moshi)
    }

    override fun getRemoteConfig() = api.getConfig()

    override suspend fun getLocalConfig(): Config = withContext(Dispatchers.IO) {
        val configJson = sharedPreferences.getString(KEY_CONFIG, null)
            ?: error("No local config found. You should call getRemoteConfig first and saveRemoteConfig")
        return@withContext configJsonAdapter.fromJson(configJson)
            ?: error("Failed to parse configJson. Input: '$configJson'")
    }

    override fun saveRemoteConfig(config: Config) {
        sharedPreferences.edit {
            val configJson = configJsonAdapter.toJson(config)
            putString(KEY_CONFIG, configJson)
        }
    }
}
