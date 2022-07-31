package com.jae464.placememo.domain.model.post

import android.graphics.Bitmap
import android.net.Uri
import java.util.*

data class Memo (
    val id: Long,
    val title: String,
    val content: String,
    val imageUrlList: Bitmap?,
    val latitude: Double,
    val longitude: Double
)