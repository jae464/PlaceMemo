package com.jae464.data.db

import androidx.room.*
import com.jae464.data.model.FolderEntity
import com.jae464.data.model.FolderWithMemos

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: FolderEntity): Long

    @Query("DELETE FROM folder WHERE folder_id = :folderId")
    suspend fun deleteFolder(folderId: Long)

    @Query("SELECT * FROM folder WHERE folder_name = :folderName")
    suspend fun getFolderByName(folderName: String): FolderEntity
}