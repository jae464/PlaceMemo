package com.jae464.placememo.data.repository.address

import com.jae464.placememo.data.api.GeoService
import com.jae464.placememo.data.api.response.GeoResponse
import com.jae464.placememo.data.api.response.RegionResponse
import com.jae464.placememo.data.api.response.ResultResponse
import com.jae464.placememo.data.model.Region
import com.jae464.placememo.domain.repository.AddressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val geoService: GeoService
): AddressRepository {
    override suspend fun getAddress(latitude: Double, longitude: Double): RegionResponse?{
        return withContext(Dispatchers.IO) {
            val call = geoService.getAddress("$latitude,$longitude","json")
            val response = call.awaitResponse()
            if(!response.isSuccessful) {
                null
            }
            else response.body()?.results?.get(0)?.region
        }
    }

    override fun addressToString(region: RegionResponse?): String {
        if (region == null) return ""
        return "${region.area1.name} ${region.area2.name} ${region.area3.name}"
    }
}