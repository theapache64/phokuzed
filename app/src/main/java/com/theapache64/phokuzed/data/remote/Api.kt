package com.theapache64.phokuzed.data.remote

import com.github.theapache64.retrosheet.annotations.KeyValue
import com.theapache64.phokuzed.core.Retrosheet
import com.theapache64.phokuzed.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface Api {

    @KeyValue
    @GET(Retrosheet.TABLE_CONFIG)
    fun getConfig(): Flow<Resource<Config>>
}
