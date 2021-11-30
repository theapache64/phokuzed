package com.theapache64.phokuzed.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteSubdomain(
    @Json(name = "main_domain")
    val mainDomain: String, // twitter.com
    @Json(name = "sub_domains")
    val subDomains: String // api.twitter.com\nmobile.twitter.com
)
