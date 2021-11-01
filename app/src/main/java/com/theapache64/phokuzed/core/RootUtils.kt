package com.theapache64.phokuzed.core

import com.theapache64.phokuzed.util.isSuccessOrLog
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.internal.MainShell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.internal.closeQuietly

/**
 * This class is an enum to define mount type.
 *
 * @author Bruce BUJON (bruce.bujon(at)gmail(dot)com)
 */
enum class MountType(
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

    suspend fun remountSystemPartition(mountType: MountType): Unit =
        withContext(Dispatchers.IO) {
            val result = Shell.su(
                "mount -o ${mountType.option},remount /system"
            ).exec()

            result.isSuccessOrLog(msg = "remountSystemPartition: Remounting failed")
        }


}