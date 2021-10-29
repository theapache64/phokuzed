package com.theapache64.phokuzed.core


data class Host(
    val ipType: IpType,
    val ip: String,
    val domain: String
) {
    enum class IpType {
        v4, v6
    }
}

class HostManager(
    private val hostFileContent: String
) {


    companion object {
        const val COMMENT_BEGIN = "# PHOKUZED RULES BEING HERE"
        const val COMMENT_END = "# PHOKUZED RULES END HERE"
        const val UNKNOWN_IP_V4 = "0.0.0.0"
        const val UNKNOWN_IP_V6 = "::"
    }

    /**
     * To block list of domains.
     *
     * @param domainsToBlock
     * @return blocked host file content
     */
    fun block(domainsToBlock: List<String>): String {
        var hostBuilder = StringBuilder(hostFileContent)

        if (hostFileContent.contains(COMMENT_BEGIN) && hostFileContent.contains(COMMENT_END)) {
            // file already contains phokuzed rule, so we are removing the end sign, so that
            // it can reuse the normal method.
            val endIndex = hostBuilder.indexOf(COMMENT_END)
            hostBuilder = hostBuilder.delete(
                endIndex, endIndex + COMMENT_END.length
            )
        } else {
            // current host file doesn't have phokuzed rules. so it needs the start sign
            hostBuilder.append("\n\n$COMMENT_BEGIN\n")
        }

        for (domain in domainsToBlock) {
            hostBuilder.append("${getV4BlockLine(domain)}\n")
                .append("${getV6BlockLine(domain)}\n")
        }
        hostBuilder.append("$COMMENT_END\n")

        // last, we'll append the end sign
        return hostBuilder.trim().toString()
    }

    private fun getV6BlockLine(domain: String) = "$UNKNOWN_IP_V6 $domain"

    private fun getV4BlockLine(domain: String) = "$UNKNOWN_IP_V4 $domain"

    fun unblock(domainsToBlock: List<String>): String {
        if (!hostFileContent.contains(COMMENT_BEGIN) || !hostFileContent.contains(COMMENT_END)) {
            return hostFileContent
        }

        val startIndex = hostFileContent.indexOf(COMMENT_BEGIN)
        val endIndex = hostFileContent.indexOf(COMMENT_END)
        val phokuzedArea = hostFileContent.substring(startIndex, endIndex)

        val newPhokuzedArea = phokuzedArea
            .split("\n")
            .map { blockLine -> blockLine.trim() }
            .filter { blockLine ->
                for (domain in domainsToBlock) {
                    val v4BlockLine = getV4BlockLine(domain)
                    val v6BlockLine = getV6BlockLine(domain)
                    if (blockLine != v4BlockLine && blockLine != v6BlockLine) {
                        return@filter true
                    }
                }
                false
            }.joinToString(separator = "\n")

        val unPhokuzedHostFileContent = hostFileContent.removeRange(startIndex, endIndex)
        val phokuzedHostFileContent = StringBuilder(unPhokuzedHostFileContent)
            .insert(startIndex, newPhokuzedArea)

        return phokuzedHostFileContent.trim().toString()
    }

}