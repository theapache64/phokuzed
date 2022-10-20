package com.theapache64.phokuzed.core

import com.theapache64.phokuzed.data.repo.SubdomainRepo
import dagger.Reusable
import javax.inject.Inject

@Reusable
class HostExtender @Inject constructor(
    private val subdomainRepo: SubdomainRepo,
) {
    suspend fun getExtendedHosts(blockList: Set<String>): Set<String> {
        val subDomains = subdomainRepo.getLocalSubdomains(blockList)

        // blockList + subDomains
        return blockList + subDomains.flatMap { it.subDomains }
    }
}
