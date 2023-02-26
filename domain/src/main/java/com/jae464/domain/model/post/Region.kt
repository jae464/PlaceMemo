package com.jae464.domain.model.post

data class Region(
    val area1: String,
    val area2: String,
    val area3: String
)

fun Region.toAddressFormat(): String {
    return "$area1 $area2 $area3"
}