package com.jae464.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "folder_memo",
    foreignKeys = [
        ForeignKey(
            entity = MemoEntity::class,
            parentColumns = ["memo_id"],
            childColumns = ["memo_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["folder_id"],
            childColumns = ["folder_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["memo_id", "folder_id"]
)
data class FolderMemoXRef(
    @ColumnInfo(name = "memo_id", index = true) val memoId: Long,
    @ColumnInfo(name = "folder_id", index = true) val folderId: Long
)