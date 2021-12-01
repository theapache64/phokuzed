package com.theapache64.phokuzed.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * id, main_domain, sub_domains
 */
@Entity(tableName = "subdomains")
data class Subdomain(
    @ColumnInfo(name = "main_domain")
    val mainDomain: String,
    @ColumnInfo(name = "sub_domains")
    val subDomains: Set<String>
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}
