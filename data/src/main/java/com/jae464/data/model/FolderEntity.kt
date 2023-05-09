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
    @ColumnInfo(name = "folder_order")
    val folderOrder: Int,
    @ColumnInfo(name = "folder_default")
    val isDefault: Boolean

)

fun FolderEntity.toFolder(): Folder {
    return Folder(
        id = this.folderId,
        name = this.folderName,
        order = this.folderOrder,
        isDefault = this.isDefault,
        memoCount = 0
    )
}

fun Folder.toFolderEntity(): FolderEntity {
    return FolderEntity(
        folderId = this.id,
        folderName = this.name,
        folderOrder = this.order,
        isDefault = this.isDefault
    )
}