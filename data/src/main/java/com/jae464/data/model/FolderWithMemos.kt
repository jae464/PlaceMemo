package com.jae464.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class FolderWithMemos(
    @Embedded val folder: FolderEntity,
    @Relation(
        parentColumn = "folder_id",
        entityColumn = "folder_id"
    )
    val memos: List<MemoEntity>
)
