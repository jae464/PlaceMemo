package com.jae464.placememo.data.api

import com.jae464.placememo.BuildConfig
import com.jae464.placememo.data.api.response.GeoResponse
import com.jae464.placememo.data.api.response.RegionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GeoService {
    @GET("map-reversegeocode/v2/gc")
    @Headers(
        "X-NCP-APIGW-API-KEY-ID: ${GeoApi.API_KEY_ID}",
        "X-NCP-APIGW-API-KEY: ${GeoApi.API_KEY_SECRET}"
    )
    fun getAddress(
        @Query("coords") coords: String,
        @Query("output") output: String
    ): Call<GeoResponse>
}

class GeoApi {
    companion object {
        const val BASE_URL = "https://naveropenapi.apigw.ntruss.com/"
        const val API_KEY_ID = BuildConfig.naver_api_key_id
        const val API_KEY_SECRET = BuildConfig.naver_api_key_secret
    }
}
