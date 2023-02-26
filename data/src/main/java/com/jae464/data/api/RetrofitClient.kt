package com.jae464.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val geoService: GeoService =
        initGeoService()
    private fun initGeoService(): GeoService =
        Retrofit.Builder()
            .baseUrl(GeoApi.Companion.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoService::class.java)
}