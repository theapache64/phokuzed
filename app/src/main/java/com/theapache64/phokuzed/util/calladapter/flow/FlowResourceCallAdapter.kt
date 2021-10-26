/*
 * Copyright 2021 Boil (https://github.com/theapache64/boil)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.theapache64.phokuzed.util.calladapter.flow

import com.theapache64.phokuzed.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.awaitResponse
import java.lang.reflect.Type

/**
 * To convert retrofit response to Flow<Resource<T>>.
 * Inspired from FlowCallAdapterFactory
 */
class FlowResourceCallAdapter<R>(
    private val responseType: Type,
    private val isSelfExceptionHandling: Boolean
) : CallAdapter<R, Flow<Resource<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>) = flow<Resource<R>> {

        // Firing loading resource
        emit(Resource.Loading())

        val resp = call.awaitResponse()

        if (resp.isSuccessful) {
            resp.body()?.let { data ->
                // Success
                emit(Resource.Success(data, null))
            } ?: kotlin.run {
                // Error
                emit(Resource.Error("Response can't be null"))
            }
        } else {
            // Error
            val errorBody = resp.message()
            emit(Resource.Error(errorBody))
        }

    }.catch { error: Throwable ->
        error.printStackTrace()
        if (isSelfExceptionHandling) {
            emit(Resource.Error(error.message ?: "Something went wrong"))
        } else {
            throw error
        }
    }
}
