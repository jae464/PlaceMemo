package com.jae464.placememo.data.api.response

import com.google.gson.annotations.SerializedName

data class GeoResponse(
    @SerializedName("results")
    val results: List<ResultResponse>
)

data class ResultResponse(
    @SerializedName("name")
    val name: String,

    @SerializedName("region")
    val region: RegionResponse
)

data class RegionResponse(
    val area1: Area1,
    val area2: Area2,
    val area3: Area3
)

data class Area1(
    val name: String
)

data class Area2(
    val name: String
)

data class Area3(
    val name: String
)