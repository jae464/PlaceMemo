package com.jae464.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val geoService: com.jae464.data.api.GeoService =
        com.jae464.data.api.RetrofitClient.initGeoService()
    private fun initGeoService(): com.jae464.data.api.GeoService =
        Retrofit.Builder()
            .baseUrl(com.jae464.data.api.GeoApi.Companion.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(com.jae464.data.api.GeoService::class.java)
}