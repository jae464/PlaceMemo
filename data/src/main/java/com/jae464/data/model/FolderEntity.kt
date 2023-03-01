package com.jae464.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jae464.domain.model.feed.Folder

@Entity(tableName = "folder")
data class FolderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "folder_id")
    val folderId: Long = 0L,
    @ColumnInfo(name = "folder_name")
    val folderName: String,
)

fun FolderEntity.toFolder(): Folder {
    return Folder(
        this.folderId,
        this.folderName,
        0
    )
}

fun Folder.toFolderEntity(): FolderEntity {
    return FolderEntity(
        this.id,
        this.name
    )
}