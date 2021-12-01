package com.theapache64.phokuzed.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.theapache64.phokuzed.data.local.entity.Subdomain

@Dao
interface SubdomainDao {
    @Query("SELECT * FROM subdomains WHERE main_domain = :mainDomain")
    suspend fun getSubdomain(mainDomain: String): Subdomain

    @Query("SELECT * FROM subdomains WHERE main_domain IN (:domains)")
    suspend fun getSubdomains(domains: Set<String>): List<Subdomain>

    @Insert
    suspend fun insertAll(subdomains: List<Subdomain>)

    @Query("DELETE FROM subdomains")
    suspend fun nukeTable()
}
