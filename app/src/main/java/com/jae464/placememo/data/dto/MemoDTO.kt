package com.jae464.placememo.data.dto

import com.jae464.placememo.data.model.Region
import java.util.*

data class MemoDTO(
    val memoId: String,
    val userId: String, // Firebase Store 에 저장된 user의 uid
    val title: String,
    val content: String,
    val createdAt: Date,
    val latitude: Double,
    val longitude: Double,
    val category: Int,
    val region: Region? = null,
    val imageUrlList: List<String>? = emptyList(),
)