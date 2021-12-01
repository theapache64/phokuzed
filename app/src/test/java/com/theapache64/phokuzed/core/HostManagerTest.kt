package com.theapache64.phokuzed.core

import com.github.theapache64.expekt.should
import org.junit.Test

class HostManagerTest {
    @Test
    fun `Rules are inserted at correct position`() {
        // Test data
        val inputHostFileContent = """
            ## This is a comment
            127.0.0.1	localhost
            255.255.255.255 broadcasthost
            ::1             localhost
            
        """.trimIndent()

        val domainToBlock = "instagram.com"

        val expectedHostFileContent = """
            ## This is a comment
            127.0.0.1	localhost
            255.255.255.255 broadcasthost
            ::1             localhost
            ${HostManager.COMMENT_BEGIN}
            ${HostManager.UNKNOWN_IP_V4} $domainToBlock
            ${HostManager.UNKNOWN_IP_V6} $domainToBlock
            ${HostManager.UNKNOWN_IP_V4} www.$domainToBlock
            ${HostManager.UNKNOWN_IP_V6} www.$domainToBlock
            ${HostManager.COMMENT_END}
        """.trimIndent()

        // Let's begin the test
        val hostManager = HostManager(inputHostFileContent)
        val actualHostFileContent = hostManager.applyBlockList(setOf(domainToBlock))
        expectedHostFileContent.should.equal(actualHostFileContent)
    }

    @Test
    fun `Rules are removed from correct position`() {
        // Test data
        val inputHostFileContent = """
            ## This is a comment
            127.0.0.1	localhost
            255.255.255.255 broadcasthost
            ::1             localhost
            ${HostManager.COMMENT_BEGIN}
            0.0.0.0 instagram.com
            :: instagram.com
            0.0.0.0 www.instagram.com
            :: www.instagram.com
            0.0.0.0 facebook.com
            :: facebook.com
            0.0.0.0 www.facebook.com
            :: www.facebook.com
            ${HostManager.COMMENT_END}
            # some comment
        """.trimIndent()

        val expectedHostFileContent = """
            ## This is a comment
            127.0.0.1	localhost
            255.255.255.255 broadcasthost
            ::1             localhost
            # some comment
            ${HostManager.COMMENT_BEGIN}
            0.0.0.0 instagram.com
            :: instagram.com
            0.0.0.0 www.instagram.com
            :: www.instagram.com
            ${HostManager.COMMENT_END}
        """.trimIndent()

        // Let's begin the test
        val hostManager = HostManager(inputHostFileContent)
        val actualHostFileContent = hostManager.applyBlockList(setOf("instagram.com"))
        expectedHostFileContent.should.equal(actualHostFileContent)
    }

    @Test
    fun `Rules are removed from an empty host file`() {

        val inputHostFileContent = """
            ${HostManager.COMMENT_BEGIN}
            0.0.0.0 instagram.com
            :: instagram.com
            ${HostManager.COMMENT_END}
        """.trimIndent()

        // Then remove the rules
        val emptyHostFileContent = HostManager(inputHostFileContent).run {
            clearRules()
        }

        emptyHostFileContent.should.equal("")
    }

    @Test
    fun `Removes the begin and end comment when there's no domain`() {
        // Test data
        val inputHostFileContent = """
            ## This is a comment
            127.0.0.1	localhost
            255.255.255.255 broadcasthost
            ::1             localhost
            ${HostManager.COMMENT_BEGIN}
            0.0.0.0 instagram.com
            :: instagram.com
            0.0.0.0 facebook.com
            :: facebook.com
            ${HostManager.COMMENT_END}
        """.trimIndent()

        val expectedHostFileContent = """
            ## This is a comment
            127.0.0.1	localhost
            255.255.255.255 broadcasthost
            ::1             localhost
        """.trimIndent()

        // Let's begin the test
        val hostManager = HostManager(inputHostFileContent)
        val actualHostFileContent = hostManager.clearRules()
        actualHostFileContent.should.equal(expectedHostFileContent)
    }
}
