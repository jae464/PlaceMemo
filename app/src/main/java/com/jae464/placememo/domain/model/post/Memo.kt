package com.jae464.placememo.domain.model.post

import android.graphics.Bitmap
import android.net.Uri
import java.util.*

data class Memo (
    val id: Long,
    val title: String,
    val content: String,
    val latitude: Double,
    val longitude: Double,
    val category: Category,
    val area1: String = "",
    val area2: String = "",
    val area3: String = "",
    val imageUriList: List<String>? = null,
    val viewType: Int = 0,
)
