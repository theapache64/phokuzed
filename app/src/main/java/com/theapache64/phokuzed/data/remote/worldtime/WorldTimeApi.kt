package com.theapache64.phokuzed.data.remote.worldtime

import retrofit2.http.GET

interface WorldTimeApi {
    @GET("timezone/Asia/Kolkata")
    suspend fun getTime(): TimeResponse
}
