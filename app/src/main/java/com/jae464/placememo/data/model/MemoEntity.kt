package com.jae464.placememo.data.model

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.jae464.placememo.data.mapper.categoryToInt
import com.jae464.placememo.data.mapper.intToCategory
import com.jae464.placememo.domain.model.post.Memo
import java.security.Timestamp
import java.util.*

@Entity(tableName = "memo")
data class MemoEntity(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("createdAt")
    val createdAt: Date,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("category")
    val category: Int,
    @Embedded
    val region: Region? = null,
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true) val id: Long=0,
)

data class Region(
    val area1: String = "",
    val area2: String = "",
    val area3: String = "",
)

internal fun MemoEntity.toMemo(): Memo {
    return Memo(
        id = id,
        title = title,
        content = content,
        latitude = latitude,
        longitude = longitude,
        category = intToCategory(category),
        area1 = region?.area1 ?: "",
        area2 = region?.area2 ?: "",
        area3 = region?.area3 ?: "",
    )
}

internal fun Memo.toMemoEntity(): MemoEntity {
    return MemoEntity(
        title = title,
        content = content,
        Date(),
        latitude = latitude,
        longitude = longitude,
        category = category.ordinal,
        region = Region(area1, area2, area3),
        id = id
    )
}