package com.jae464.domain.model.feed

data class Folder(
    val id: Long = 0L,
    val name: String,
    val order: Int = -1,
    val isDefault: Boolean = false,
    val memoCount: Int
)
