package com.jae464.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    val id: Long = 0L,

    @ColumnInfo(name = "category_name")
    val name: String

)
