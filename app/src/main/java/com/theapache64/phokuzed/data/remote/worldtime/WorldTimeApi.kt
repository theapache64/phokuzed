package com.theapache64.phokuzed.data.remote.worldtime

import com.theapache64.phokuzed.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface WorldTimeApi {
    @GET("timezone/Asia/Kolkata")
    suspend fun getTime(): TimeResponse
}