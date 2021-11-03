package com.theapache64.phokuzed.data.repo

import com.theapache64.phokuzed.core.RootUtils
import com.theapache64.phokuzed.util.isSuccessOrLog
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject

interface HostRepo {

    suspend fun updateHostFileContent(content: String): Boolean
    suspend fun getHostFileContent(): String

}

class HostRepoImpl @Inject constructor() : HostRepo {

    override suspend fun getHostFileContent(): String {
        // TODO : Use su here
        return File("/etc/hosts").readText()
    }


    override suspend fun updateHostFileContent(content: String): Boolean =
        withContext(Dispatchers.IO) {
            Timber.d("writeHostFileContent: Content is '$content'")

            RootUtils.remountSystemPartition {
                val result = Shell.su(
                    "echo \"$content\" >/system/etc/hosts"
                ).exec()

                result.isSuccessOrLog(
                    msg = "writeHostFileContent: Failed to modify hosts file"
                )
            }
        }
}