package com.theapache64.phokuzed.core

import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

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
        Shell.rootAccess()
    }

    suspend fun remountSystemPartition(mountType: MountType): Boolean =
        withContext(Dispatchers.IO) {
            val result = Shell.su(
                "mount -o ${mountType.option},remount /system"
            ).exec()

            result.isSuccess.also { success ->
                if (!success) {
                    val error = result.err.joinToString(separator = "\n")
                    Timber.e("remountSystemPartition: Remounting failed. Caused by '$error'")
                }
            }
        }
}