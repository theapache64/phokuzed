package com.theapache64.phokuzed.core

import com.theapache64.phokuzed.util.isSuccessOrLog
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.closeQuietly

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
        // remounting to read-write
        val readWriteResult = Shell.su(
            "mount -o ${MountType.READ_WRITE.option},remount /system"
        ).exec()

        val isReadWriteable =
            readWriteResult.isSuccessOrLog(msg = "remountSystemPartition: Remounting failed")
        if (isReadWriteable) {
            block()
            // remounting to read only
            val readResult = Shell.su(
                "mount -o ${MountType.READ_ONLY.option},remount /system"
            ).exec()

            readResult.isSuccessOrLog(msg = "Failed to mount back to read only")
        } else {
            false
        }
    }


}