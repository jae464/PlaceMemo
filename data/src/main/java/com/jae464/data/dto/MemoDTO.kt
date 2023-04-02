package com.jae464.data.dto

import com.jae464.data.model.Region
import com.jae464.domain.model.post.Category
import com.jae464.domain.model.post.Memo
import java.util.*

data class MemoDTO(
    val memoId: Int = -1,
    val userId: String = "", // Firebase Store 에 저장된 user의 uid
    val title: String = "",
    val content: String = "",
    val createdAt: Date = Date(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val category: String = "",
    val region: Region? = null,
    val imageUrlList: List<String>? = emptyList(),
)

fun MemoDTO.toMemo(): Memo {
    return Memo(
        this.memoId,
        this.title,
        this.content,
        this.latitude,
        this.longitude,
        Category(0,"기타"), // 임시. 추후 수정 필요
        this.region?.area1.toString(),
        this.region?.area2.toString(),
        this.region?.area3.toString(),
        this.imageUrlList
    )
}

fun Memo.toMemoDTO(userId: String): MemoDTO {
    return MemoDTO(
        this.id,
        userId,
        this.title,
        this.content,
        Date(),
        this.latitude,
        this.longitude,
        this.category.name,
        Region(this.area1, this.area2, this.area3),
        this.imageUriList
    )
}