package com.theapache64.phokuzed.data.repo

import android.content.SharedPreferences
import com.theapache64.phokuzed.core.MountType
import com.theapache64.phokuzed.core.RootUtils
import com.theapache64.phokuzed.util.isSuccessOrLog
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject

interface HostRepo {
    fun getBlockList(): Set<String>
    suspend fun writeHostFileContent(content: String): Boolean
    suspend fun readHostFileContent(): String

}

class HostRepoImpl @Inject constructor(
    private val sharedPref: SharedPreferences,
) : HostRepo {
    companion object {
        private const val KEY_BLOCK_LIST = "block_list"
    }

    override fun getBlockList(): Set<String> {
        return sharedPref.getString(KEY_BLOCK_LIST, null)?.split(",")?.toSet() ?: emptySet()
    }


    override suspend fun readHostFileContent(): String {
        // TODO : Use su here
        return File("/etc/hosts").readText()
    }


    override suspend fun writeHostFileContent(content: String): Boolean =
        withContext(Dispatchers.IO) {
            Timber.d("writeHostFileContent: Content is '$content'")

            RootUtils.remountSystemPartition(MountType.READ_WRITE)
            val result = Shell.su(
                "echo \"$content\" >/system/etc/hosts"
            ).exec()

            result.isSuccessOrLog(
                msg = "writeHostFileContent: Failed to modify hosts file"
            )
        }
}