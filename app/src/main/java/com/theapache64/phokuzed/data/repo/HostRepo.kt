package com.theapache64.phokuzed.data.repo

import com.theapache64.phokuzed.core.RootUtils
import com.theapache64.phokuzed.util.isSuccessOrLog
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

interface HostRepo {

    suspend fun updateHostFileContent(content: String): Boolean
    suspend fun getHostFileContent(): String
}

class HostRepoImpl @Inject constructor() : HostRepo {
    companion object {
        const val PATH_HOSTS = "/system/etc/hosts"
    }

    override suspend fun getHostFileContent(): String {
        // TODO : Use su here
        return File(PATH_HOSTS).readText()
    }

    override suspend fun updateHostFileContent(content: String): Boolean =
        withContext(Dispatchers.IO) {
            RootUtils.remountSystemPartition {
                val result = Shell.su(
                    "echo \"$content\" >$PATH_HOSTS"
                ).exec()

                result.isSuccessOrLog(
                    msg = "writeHostFileContent: Failed to modify hosts file"
                )
            }
        }
}
