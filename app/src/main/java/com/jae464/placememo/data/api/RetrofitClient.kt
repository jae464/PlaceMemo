package com.jae464.placememo.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    val geoService: GeoService = initGeoService()
    private fun initGeoService(): GeoService =
        Retrofit.Builder()
            .baseUrl(GeoApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoService::class.java)
}