package com.theapache64.phokuzed.di.module

import com.github.theapache64.retrosheet.RetrosheetInterceptor
import com.squareup.moshi.Moshi
import com.theapache64.phokuzed.core.Retrosheet
import com.theapache64.phokuzed.data.remote.Api
import com.theapache64.phokuzed.util.calladapter.flow.FlowResourceCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://docs.google.com/spreadsheets/d/1aouhKq50Z3RkW0ztMhl2_3Fx5rJjD19aaxrzK62J-O4/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .build()
    }

    @Provides
    fun provideApi(retrofit : Retrofit) : Api {
        return retrofit.create(Api::class.java)
    }
}