package com.theapache64.phokuzed.data.map

import com.theapache64.phokuzed.data.local.entity.Subdomain
import com.theapache64.phokuzed.data.remote.RemoteSubdomain

fun RemoteSubdomain.map(): Subdomain {
    return Subdomain(
        mainDomain,
        subDomains.split("\n").toSet()
    )
}
