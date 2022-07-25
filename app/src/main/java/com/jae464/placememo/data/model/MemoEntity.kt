package com.jae464.placememo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
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
    @SerializedName("imageUrlList")
    val imageUrlList: List<String>,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true) val id: Long=0,
)