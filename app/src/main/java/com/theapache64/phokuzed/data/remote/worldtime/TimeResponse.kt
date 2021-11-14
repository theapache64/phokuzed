package com.theapache64.phokuzed.data.remote.worldtime

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimeResponse(
    @Json(name = "abbreviation")
    val abbreviation: String, // IST
    @Json(name = "client_ip")
    val clientIp: String, // 111.92.76.246
    @Json(name = "datetime")
    val datetime: String, // 2021-10-30T03:11:34.870411+05:30
    @Json(name = "day_of_week")
    val dayOfWeek: Int, // 6
    @Json(name = "day_of_year")
    val dayOfYear: Int, // 303
    @Json(name = "dst")
    val dst: Boolean, // false
    @Json(name = "dst_from")
    val dstFrom: Any?, // null
    @Json(name = "dst_offset")
    val dstOffset: Int, // 0
    @Json(name = "dst_until")
    val dstUntil: Any?, // null
    @Json(name = "raw_offset")
    val rawOffset: Int, // 19800
    @Json(name = "timezone")
    val timezone: String, // Asia/Kolkata
    @Json(name = "unixtime")
    val unixtime: Long, // 1635543694
    @Json(name = "utc_datetime")
    val utcDatetime: String, // 2021-10-29T21:41:34.870411+00:00
    @Json(name = "utc_offset")
    val utcOffset: String, // +05:30
    @Json(name = "week_number")
    val weekNumber: Int // 43
)