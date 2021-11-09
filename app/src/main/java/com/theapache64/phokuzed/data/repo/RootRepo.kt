package com.theapache64.phokuzed.data.repo

import com.theapache64.phokuzed.core.RootUtils
import javax.inject.Inject
import javax.inject.Singleton

interface RootRepo {
    suspend fun isRooted(): Boolean
}

@Singleton
class RootRepoImpl @Inject constructor() : RootRepo {
    override suspend fun isRooted(): Boolean {
        return RootUtils.hasRootAccess()
    }
}