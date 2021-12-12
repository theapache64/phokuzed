package com.theapache64.phokuzed.core

import com.theapache64.phokuzed.data.repo.HostRepoImpl
import com.theapache64.phokuzed.util.isSuccessOrLog
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.closeQuietly
import timber.log.Timber
import java.io.File

/**
 * This class is an enum to define mount type.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
private enum class MountType(
    /**
     * Get related command line option.
     * @return The related command line option.
     */
    val option: String
) {
    /**
     * Mount as read only.
     */
    READ_ONLY("ro"),

    /**
     * Mount as read/write.
     */
    READ_WRITE("rw");
}

object RootUtils {

    suspend fun hasRootAccess(): Boolean = withContext(Dispatchers.IO) {
        val shell = Shell.getShell()
        val isRooted = shell.isRoot
        shell.closeQuietly()
        isRooted
    }

    suspend fun remountSystemPartition(
        block: () -> Unit
    ): Boolean = withContext(Dispatchers.IO) {

        val partition = findPartition(File(HostRepoImpl.PATH_HOSTS))

        // remounting to read-write
        val readWriteResult = Shell.su(
            "mount -o ${MountType.READ_WRITE.option},remount $partition" // TODO : Verify device compat
        ).exec()

        val isReadWriteable =
            readWriteResult.isSuccessOrLog(msg = "remountSystemPartition: Remounting failed")
        if (isReadWriteable) {
            block()
            // remounting to read only
            Shell.su(
                "mount -o ${MountType.READ_ONLY.option},remount $partition"
            ).exec()
            true
        } else {
            false
        }
    }

    /**
     * To find mount point for the given file
     */
    private fun findPartition(_file: File): String? {
        var file: File? = _file
        val result = Shell.su("cat /proc/mounts | cut -d ' ' -f2").exec()
        val mounts = result.out
        while (file != null) {
            val path = file.absolutePath
            for (mount in mounts) {
                if (path == mount) {
                    return mount.also { partition ->
                        Timber.d("findPartition: partition is $partition")
                    }
                }
            }
            file = file.parentFile
        }
        return null
    }
}
