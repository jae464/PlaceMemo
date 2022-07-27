package com.jae464.placememo.domain.model.post

import java.util.*

data class Memo (
    val id: Long,
    val title: String,
    val content: String,
    val imageUrlList: List<String>,
    val latitude: Double,
    val longitude: Double
)