package com.theapache64.phokuzed.data.repo

import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import com.theapache64.phokuzed.data.remote.Api
import com.theapache64.phokuzed.data.remote.Config
import com.theapache64.phokuzed.data.remote.ConfigJsonAdapter
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ConfigRepo @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val api: Api,
    private val moshi: Moshi
) {
    companion object {
        private const val KEY_CONFIG = "config"
    }

    private val configJsonAdapter by lazy {
        ConfigJsonAdapter(moshi)
    }

    fun getRemoteConfig() = api.getConfig()

    fun getLocalConfig(): Config {
        val configJson = sharedPreferences.getString(KEY_CONFIG, null)
            ?: error("No local config found. You should call getRemoteConfig first and saveRemoteConfig")
        return configJsonAdapter.fromJson(configJson)
            ?: error("Failed to parse configJson. Input: '$configJson'")
    }

    fun saveRemoteConfig(config: Config) {
        sharedPreferences.edit {
            val configJson = configJsonAdapter.toJson(config)
            putString(KEY_CONFIG, configJson)
        }
    }
}
