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

    fun clearRules(): String {
        return if (hostFileContent.contains(COMMENT_BEGIN) && hostFileContent.contains(COMMENT_END)) {
            var startIndex =
                hostFileContent.indexOf(COMMENT_BEGIN) - 1 // -1 because we added \n at the start
            val endIndex =
                hostFileContent.indexOf(COMMENT_END) + COMMENT_END.length

            if (startIndex < 0) {
                startIndex = 0
            }

            hostFileContent.removeRange(startIndex, endIndex)
        } else {
            hostFileContent
        }
    }

    fun applyBlockList(domainsToBlock: Set<String>): String {
        if (domainsToBlock.isEmpty()) {
            error("domainSet can't be empty")
        }

        // TODO: Generate possible subdomains like api.domain.com
        // first remove the existing p-rules
        val freshHostFileContent = clearRules()
        val optionalNewLine =
            if (
                freshHostFileContent.isBlank() || // file is blank
                freshHostFileContent.lastOrNull() == '\n' // ends with new line
            ) {
                ""
            } else {
                "\n"
            }

        return if (domainsToBlock.isEmpty()) {
            freshHostFileContent
        } else {
            val newHostContentBuilder = StringBuilder(freshHostFileContent)
                .append("$optionalNewLine$COMMENT_BEGIN\n")

            for (domain in domainsToBlock) {
                // sanitize domain
                newHostContentBuilder
                    .append("${getV4BlockLine(domain)}\n")
                    .append("${getV6BlockLine(domain)}\n")
                // TODO: We might need to do this!
                /*.append("${getV4BlockLine("api.$domain")}\n")
                .append("${getV6BlockLine("api.$domain")}\n")*/
            }

            newHostContentBuilder
                .append(COMMENT_END)
                .toString()
        }
    }

    private fun getV6BlockLine(domain: String) = "$UNKNOWN_IP_V6 $domain"
    private fun getV4BlockLine(domain: String) = "$UNKNOWN_IP_V4 $domain"
}
