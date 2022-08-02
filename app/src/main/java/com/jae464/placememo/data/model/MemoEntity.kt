package com.jae464.placememo.data.model

import android.graphics.Bitmap
import android.net.Uri
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
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true) val id: Long=0,
)