package com.theapache64.phokuzed.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.theapache64.phokuzed.data.local.converter.StringSetConverter
import com.theapache64.phokuzed.data.local.dao.SubdomainDao
import com.theapache64.phokuzed.data.local.entity.Subdomain

@Database(
    entities = [Subdomain::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringSetConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subdomainDao(): SubdomainDao
}
