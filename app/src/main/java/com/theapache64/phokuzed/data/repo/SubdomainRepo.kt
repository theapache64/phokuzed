package com.theapache64.phokuzed.data.repo

import com.theapache64.phokuzed.data.local.dao.SubdomainDao
import com.theapache64.phokuzed.data.local.entity.Subdomain
import com.theapache64.phokuzed.data.remote.Api
import javax.inject.Inject

class SubdomainRepo @Inject constructor(
    private val api: Api,
    private val subdomainDao: SubdomainDao
) {
    fun getRemoteSubdomains() = api.getSubdomains()
    suspend fun nukeLocalSubdomains() = subdomainDao.nukeTable()
    suspend fun addAll(subdomains: List<Subdomain>) = subdomainDao.insertAll(subdomains)
}
