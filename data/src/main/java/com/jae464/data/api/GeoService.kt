package com.jae464.data.api

import com.jae464.data.BuildConfig
import com.jae464.data.api.response.GeoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GeoService {
    @GET("map-reversegeocode/v2/gc")
    @Headers(
        "X-NCP-APIGW-API-KEY-ID: ${GeoApi.Companion.API_KEY_ID}",
        "X-NCP-APIGW-API-KEY: ${GeoApi.Companion.API_KEY_SECRET}"
    )
    fun getAddress(
        @Query("coords") coords: String,
        @Query("output") output: String
    ): Call<GeoResponse>
}

class GeoApi {
    companion object {
        const val BASE_URL = "https://naveropenapi.apigw.ntruss.com/"
        const val API_KEY_ID = BuildConfig.NAVER_API_KEY
        const val API_KEY_SECRET = BuildConfig.NAVER_API_KEY_SECRET
    }
}
