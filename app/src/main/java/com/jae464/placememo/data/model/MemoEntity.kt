package com.jae464.placememo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "memo")
data class MemoEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val content: String,
    val createdAt: Date,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double
)
