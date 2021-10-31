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
        const val COMMENT_BEGIN = "# PHOKUZED RULES BEGIN HERE"
        const val COMMENT_END = "# PHOKUZED RULES END HERE"
        const val UNKNOWN_IP_V4 = "0.0.0.0"
        const val UNKNOWN_IP_V6 = "::"
    }

    fun apply(domainsToBlock: Set<String>): String {
        // first remove the existing p-rules
        val freshHostFile =
            if (hostFileContent.contains(COMMENT_BEGIN) && hostFileContent.contains(COMMENT_END)) {
                val startIndex =
                    hostFileContent.indexOf(COMMENT_BEGIN) - 1 // -1 because we added \n at the start
                val endIndex =
                    hostFileContent.indexOf(COMMENT_END) + COMMENT_END.length + 1 // +1 because we added \n at the end

                hostFileContent.removeRange(startIndex, endIndex)
            } else {
                hostFileContent
            }

        return if (domainsToBlock.isEmpty()) {
            freshHostFile
        } else {
            val newHostContentBuilder = StringBuilder(freshHostFile)
                .append("\n$COMMENT_BEGIN\n")

            for (domain in domainsToBlock) {
                newHostContentBuilder
                    .append("${getV4BlockLine(domain)}\n")
                    .append("${getV6BlockLine(domain)}\n")
            }

            newHostContentBuilder
                .append("$COMMENT_END\n")
                .trim()
                .toString()
        }
    }

    private fun getV6BlockLine(domain: String) = "$UNKNOWN_IP_V6 $domain"
    private fun getV4BlockLine(domain: String) = "$UNKNOWN_IP_V4 $domain"

    fun getDomains(): Set<String> {
        return if (hostFileContent.contains(COMMENT_BEGIN) && hostFileContent.contains(COMMENT_END)) {
            val startIndex = hostFileContent.indexOf(COMMENT_BEGIN)
            val endIndex = hostFileContent.indexOf(COMMENT_END) + COMMENT_END.length

            // TODO: Replace this with regEx
            val pRules = hostFileContent.substring(startIndex, endIndex)
            pRules.split("\n")
                .filter { !it.startsWith("#") } // filtering out comments
                .map { it.split(" ")[1] }
                .toSet()
        } else {
            // return empty list if there's no start and end sign
            emptySet()
        }
    }

}