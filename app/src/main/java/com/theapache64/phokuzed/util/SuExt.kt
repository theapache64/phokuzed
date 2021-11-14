package com.theapache64.phokuzed.util

import com.topjohnwu.superuser.Shell
import timber.log.Timber

fun Shell.Result.isSuccessOrLog(
    msg: String = "Failed"
): Boolean {
    return isSuccess.also { success ->
        if (!success) {
            val error = err.joinToString(separator = "\n")
            Timber.e("$msg. Caused by '$error'")
        }
    }
}
