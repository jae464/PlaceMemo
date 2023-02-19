package com.jae464.data.repository.address

import com.jae464.data.api.response.toRegion
import com.jae464.domain.model.post.Region
import com.jae464.domain.repository.AddressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val geoService: com.jae464.data.api.GeoService
): AddressRepository {
    override suspend fun getAddress(latitude: Double, longitude: Double): Region?{
        return withContext(Dispatchers.IO) {
            val call = geoService.getAddress("$latitude,$longitude","json")
            val response = call.awaitResponse()
            if(!response.isSuccessful) {
                null
            }
            else {
                response.body()?.results?.get(0)?.region?.toRegion()
            }
        }
    }

    override fun addressToString(region: Region?): String {
        if (region == null) return ""
        return "${region.area1} ${region.area2} ${region.area3}"
    }
}