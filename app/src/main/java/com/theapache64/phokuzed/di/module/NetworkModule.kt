package com.theapache64.phokuzed.di.module

import com.github.theapache64.retrosheet.RetrosheetInterceptor
import com.squareup.moshi.Moshi
import com.theapache64.phokuzed.core.Retrosheet
import com.theapache64.phokuzed.data.remote.Api
import com.theapache64.phokuzed.data.remote.worldtime.WorldTimeApi
import com.theapache64.phokuzed.util.calladapter.flow.FlowResourceCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideRetrosheetInterceptor(): RetrosheetInterceptor {
        return RetrosheetInterceptor.Builder()
            .setLogging(true) // TODO: Remove
            .addSheet(
                Retrosheet.TABLE_CONFIG,
                "key", "value"
            )
            .addSheet(
                Retrosheet.TABLE_SUBDOMAINS,
                "main_domain", "sub_domains"
            )
            .build()
    }

    @Provides
    fun provideOkHttpClient(retrosheetInterceptor: RetrosheetInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retrosheetInterceptor)
            .build()
    }

    @Provides
    fun getMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    fun provideApi(okHttpClient: OkHttpClient, moshi: Moshi): Api {
        return Retrofit.Builder()
            .baseUrl("https://docs.google.com/spreadsheets/d/1aouhKq50Z3RkW0ztMhl2_3Fx5rJjD19aaxrzK62J-O4/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .build()
            .create(Api::class.java)
    }

    @OptIn(ExperimentalTime::class)
    @Provides
    fun provideWorldTimeApi(moshi: Moshi): WorldTimeApi {
        // TODO: Refactor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .readTimeout(kotlin.time.Duration.seconds(60).inWholeSeconds, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            // .client(okHttpClient)
            .baseUrl("http://worldtimeapi.org/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .build()
            .create(WorldTimeApi::class.java)
    }
}
