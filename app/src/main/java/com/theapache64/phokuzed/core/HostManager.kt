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

    fun removeRules(): String {
        return if (hostFileContent.contains(COMMENT_BEGIN) && hostFileContent.contains(COMMENT_END)) {
            val startIndex =
                hostFileContent.indexOf(COMMENT_BEGIN) - 1 // -1 because we added \n at the start
            val endIndex =
                hostFileContent.indexOf(COMMENT_END) + COMMENT_END.length + 1 // +1 because we added \n at the end

            hostFileContent.removeRange(startIndex, endIndex)
        } else {
            hostFileContent
        }
    }

    fun apply(domainsToBlock: Set<String>): String {
        if (domainsToBlock.isEmpty()) {
            error("domainSet can't be empty")
        }

        // TODO: Generate possible subdomains like api.domain.com

        // first remove the existing p-rules
        val freshHostFile = removeRules()
        return if (domainsToBlock.isEmpty()) {
            freshHostFile
        } else {
            val newHostContentBuilder = StringBuilder(freshHostFile)
                .append("\n$COMMENT_BEGIN\n")

            for (domain in domainsToBlock) {
                // sanitize domain
                newHostContentBuilder
                    .append("${getV4BlockLine(domain)}\n")
                    .append("${getV6BlockLine(domain)}\n")
                    // the api.domain also
                    .append("${getV4BlockLine("api.$domain")}\n")
                    .append("${getV6BlockLine("api.$domain")}\n")

            }

            newHostContentBuilder
                .append("$COMMENT_END\n")
                .trim()
                .toString()
        }
    }

    private fun getV6BlockLine(domain: String) = "$UNKNOWN_IP_V6 $domain"
    private fun getV4BlockLine(domain: String) = "$UNKNOWN_IP_V4 $domain"

}