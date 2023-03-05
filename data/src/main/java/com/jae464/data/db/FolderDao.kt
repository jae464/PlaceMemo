package com.jae464.data.db

import androidx.room.*
import com.jae464.data.model.FolderEntity
import com.jae464.data.model.FolderWithMemos
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: FolderEntity): Long

    @Query("DELETE FROM folder WHERE folder_id = :folderId")
    suspend fun deleteFolder(folderId: Long)

    @Query("SELECT * FROM folder")
    fun getAllFolder(): Flow<List<FolderEntity>>

    @Query("SELECT * FROM folder WHERE folder_name = :folderName")
    fun getFolderByName(folderName: String): Flow<FolderEntity>

    @Query(
        "SELECT EXISTS (SELECT * FROM folder WHERE folder_name = :folderName)"
    )
    suspend fun isExistFolderName(folderName: String): Boolean

//    @Query("SELECT COUNT() FROM folder WHERE folder_id = :folderId")
//    suspend fun getMemoCountByFolder(folderId: Long): Long

}