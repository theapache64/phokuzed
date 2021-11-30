package com.theapache64.phokuzed.di.module

import android.content.Context
import androidx.room.Room
import com.theapache64.phokuzed.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "phokuzed.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideSubdomainDao(appDatabase: AppDatabase) = appDatabase.subdomainDao()
}
