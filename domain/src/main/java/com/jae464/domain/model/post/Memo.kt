package com.jae464.domain.model.post

data class Memo (
    val id: Int,
    val title: String,
    val content: String,
    val latitude: Double,
    val longitude: Double,
    val area1: String = "",
    val area2: String = "",
    val area3: String = "",
    val imageUriList: List<String>? = null,
    val viewType: Int = 0,
) {
}
