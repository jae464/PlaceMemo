package com.jae464.domain.repository

import com.jae464.domain.model.feed.Folder

interface FolderRepository {
    suspend fun createFolder(folder: Folder)
    suspend fun deleteFolder(id: Long)
    suspend fun getFolderByName(folderName: String)
}