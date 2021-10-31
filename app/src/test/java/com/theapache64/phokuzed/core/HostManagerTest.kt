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
            ${HostManager.COMMENT_END}
        """.trimIndent()

        // Let's begin the test
        val hostManager = HostManager(inputHostFileContent)
        val actualHostFileContent = hostManager.apply(setOf(domainToBlock))
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
            0.0.0.0 facebook.com
            :: facebook.com
            ${HostManager.COMMENT_END}
        """.trimIndent()

        val expectedHostFileContent = """
            ## This is a comment
            127.0.0.1	localhost
            255.255.255.255 broadcasthost
            ::1             localhost
            
            
            
            ${HostManager.COMMENT_BEGIN}
            0.0.0.0 instagram.com
            :: instagram.com
            ${HostManager.COMMENT_END}
        """.trimIndent()

        // Let's begin the test
        val hostManager = HostManager(inputHostFileContent)
        val newDomains = hostManager.getDomains().toMutableSet().apply {
            remove("facebook.com")
        }

        val actualHostFileContent = hostManager.apply(newDomains)
        expectedHostFileContent.should.equal(actualHostFileContent)
    }


    @Test
    fun `Parse domain names`() {
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
            
            0.0.0.0 telegram.org
            1.2.3.4 mywebsite.com
        """.trimIndent()

        val expectedResult = setOf(
            "instagram.com",
            "facebook.com"
        )

        val actualResult = HostManager(inputHostFileContent).getDomains()
        expectedResult.should.equal(actualResult)
    }

    @Test
    fun `Removes the begin and end comment when there's no domain`(){
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
        val actualHostFileContent = hostManager.apply(emptySet())
        expectedHostFileContent.should.equal(actualHostFileContent)
    }
}