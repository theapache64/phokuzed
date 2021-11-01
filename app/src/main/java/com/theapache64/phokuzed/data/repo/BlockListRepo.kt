package com.theapache64.phokuzed.data.repo

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface BlockListRepo {
    suspend fun getBlockList(): Set<String>
    fun saveBlockList(blockList: Set<String>)
}

class BlockListRepoImpl @Inject constructor(
    private val sharedPref: SharedPreferences
) : BlockListRepo {

    companion object {
        private const val KEY_BLOCK_LIST = "block_list"

        private val defaultBlockList by lazy {
            setOf(
                "facebook.com",
                "instagram.com"
            )
        }
    }

    override suspend fun getBlockList(): Set<String> = withContext(Dispatchers.IO) {
        val blockList = sharedPref.getStringSet(KEY_BLOCK_LIST, null)
        if (blockList == null) {
            // first time call
            saveBlockList(defaultBlockList)
            defaultBlockList
        } else {
            blockList
        }
    }

    override fun saveBlockList(blockList: Set<String>) {
        sharedPref.edit {
            putStringSet(KEY_BLOCK_LIST, blockList)
        }
    }
}