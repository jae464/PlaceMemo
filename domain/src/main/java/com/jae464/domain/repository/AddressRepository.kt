package com.jae464.domain.repository

import com.jae464.domain.model.post.Region
interface AddressRepository {
    suspend fun getAddress(latitude: Double, longitude: Double): Region?
    fun addressToString(region: Region?): String
}