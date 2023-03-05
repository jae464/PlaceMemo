package com.jae464.domain.repository

import com.jae464.domain.model.feed.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    suspend fun createFolder(folder: Folder)

    suspend fun deleteFolder(id: Long)

    fun getAllFolder(): Flow<List<Folder>>

    fun getFolderByName(folderName: String): Flow<Folder?>

    suspend fun isExistFolderName(folderName: String): Boolean

    suspend fun getMemoCountByFolder(folderId: Long): Int
}