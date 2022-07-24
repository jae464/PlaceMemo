package com.jae464.placememo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jae464.placememo.domain.model.post.Memo
import java.security.Timestamp
import java.util.*

@Entity(tableName = "memo")
data class MemoEntity(
    val _title: String,
    val _content: String,
    val _createdAt: Date,
    val _imageUrlList: List<String>,
    val _latitude: Double,
    val _longitude: Double,
    @PrimaryKey(autoGenerate = true) val _id: Long,
) : Memo {
    override val title: String
        get() = _title
    override val content: String
        get() = _content
    override val imageUrlList: List<String>
        get() = _imageUrlList
    override val latitude: Double
        get() = _latitude
    override val longitude: Double
        get() = _longitude

}