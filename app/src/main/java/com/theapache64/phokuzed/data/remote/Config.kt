package com.theapache64.phokuzed.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Config(
    @Json(name = "mandatory_version_code")
    val mandatoryVersionCode: Int
)