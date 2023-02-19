package com.jae464.data.api.response

import com.google.gson.annotations.SerializedName
import com.jae464.domain.model.post.Region

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

fun RegionResponse.toRegion(): Region {
    return Region(
        this.area1.name,
        this.area2.name,
        this.area3.name
    )
}

