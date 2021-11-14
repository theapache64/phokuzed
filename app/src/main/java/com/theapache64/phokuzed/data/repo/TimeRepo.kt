package com.theapache64.phokuzed.data.repo

import android.content.SharedPreferences
import androidx.core.content.edit
import com.theapache64.phokuzed.data.remote.worldtime.WorldTimeApi
import javax.inject.Inject

interface TimeRepo {
    suspend fun getCurrentTimeInSeconds(): Long
    fun saveTargetSeconds(targetSeconds: Long)
    fun getTargetSeconds(): Long?
    fun clearTargetSeconds()
}

class TimeRepoImpl @Inject constructor(
    private val worldTimeApi: WorldTimeApi,
    private val sharedPref: SharedPreferences
) : TimeRepo {
    companion object {
        private const val KEY_TARGET_SECONDS = "target_seconds"
    }

    override suspend fun getCurrentTimeInSeconds(): Long {
        return worldTimeApi.getTime().unixtime
    }

    override fun saveTargetSeconds(targetSeconds: Long) {
        sharedPref.edit {
            putLong(KEY_TARGET_SECONDS, targetSeconds)
        }
    }

    override fun getTargetSeconds(): Long? {
        return sharedPref.getLong(KEY_TARGET_SECONDS, -1).let { targetSeconds ->
            if (targetSeconds == -1L) {
                null
            } else {
                targetSeconds
            }
        }
    }

    override fun clearTargetSeconds() {
        sharedPref.edit {
            remove(KEY_TARGET_SECONDS)
        }
    }
}