package com.jae464.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.jae464.data.mapper.intToCategory
import com.jae464.domain.model.post.Memo
import java.util.*

@Entity(tableName = "memo")
data class MemoEntity(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "category")
    val category: Int,
    @Embedded
    val region: Region? = null,
    @ColumnInfo(name = "image_path_list")
    val imagePathList: List<String>?,
    @ColumnInfo(name = "folder_id")
    val folderId: Long = 0,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "memo_id")
    val id: Long = 0L,
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
        imageUriList = imagePathList
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
        imagePathList = imageUriList,
        id = id
    )
}