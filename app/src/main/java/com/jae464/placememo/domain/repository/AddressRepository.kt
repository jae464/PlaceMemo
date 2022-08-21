package com.jae464.placememo.domain.repository

import com.jae464.placememo.data.api.response.RegionResponse
import com.jae464.placememo.data.api.response.ResultResponse

interface AddressRepository {
    suspend fun getAddress(latitude: Double, longitude: Double): RegionResponse?
    fun addressToString(region: RegionResponse?): String
}