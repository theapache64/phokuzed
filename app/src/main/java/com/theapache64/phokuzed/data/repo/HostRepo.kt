package com.theapache64.phokuzed.data.repo

import android.content.SharedPreferences
import timber.log.Timber
import java.io.DataOutputStream
import java.io.File
import javax.inject.Inject

interface HostRepo {
    fun getBlockList(): Set<String>
    fun writeHostFileContent(content: String)
    fun readHostFileContent(): String

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


    override fun readHostFileContent(): String {
        return File("/etc/hosts").readText()
    }


    override fun writeHostFileContent(content: String) {
        //File("/etc/hosts").writeText(content)
        Timber.d("writeHostFileContent: Content is '$content'")
        Runtime.getRuntime().exec("su").let { process ->
            val os = DataOutputStream(process.outputStream)
            /*os.bufferedWriter().use {
                it.write()
                it.write("exit\n")
            }*/
            os.writeBytes("echo \"$content\" >/etc/hosts\n")
            os.writeBytes("exit\n")
            os.flush()

            process.waitFor()
            process.exitValue()
                .also { Timber.d("writeHostFileContent: The exit value is $it") } == 1
        }
    }


}