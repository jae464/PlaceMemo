package com.jae464.placememo.data.repository.address

import com.jae464.placememo.data.api.GeoService
import com.jae464.placememo.data.api.response.GeoResponse
import com.jae464.placememo.data.api.response.ResultResponse
import com.jae464.placememo.domain.repository.AddressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val geoService: GeoService
): AddressRepository {
    override suspend fun getAddress(latitude: Double, longitude: Double): String?{
        return withContext(Dispatchers.IO) {
            val call = geoService.getAddress("$latitude,$longitude","json")
            val response = call.awaitResponse()
            if(!response.isSuccessful) {
                null
            }
            else addressToString(response.body()?.results)
        }
    }

    override fun addressToString(results: List<ResultResponse>?): String {
        if (results == null) return ""
        val bestAddress = results[0]
        return "${bestAddress.region.area1.name} ${bestAddress.region.area2.name} ${bestAddress.region.area3.name}"
    }
}