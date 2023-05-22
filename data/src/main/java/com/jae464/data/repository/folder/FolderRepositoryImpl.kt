package com.jae464.data.repository.folder

import android.util.Log
import com.jae464.data.db.FolderDao
import com.jae464.data.mapper.folderEntityToFolder
import com.jae464.data.model.toFolderEntity
import com.jae464.domain.model.feed.Folder
import com.jae464.domain.repository.FolderRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val folderDao: FolderDao
): FolderRepository {
    override suspend fun createFolder(folder: Folder) {
        folderDao.insertFolder(folder.toFolderEntity())
    }

    override suspend fun deleteFolder(id: Long) {
        folderDao.deleteFolder(folderId = id)
    }

    override fun getAllFolder(): Flow<List<Folder>> {
        return folderDao.getAllFolder().map { list ->
            list.map {
                val memoCount = folderDao.getMemoCountByFolder(it.folderId)
                Log.d("FolderRepositoryImpl", "memoCount : $memoCount")
                folderEntityToFolder(it, memoCount)
            }
        }
    }

    override fun getFolderByName(folderName: String): Flow<Folder?> {
        return folderDao.getFolderByName(folderName).map {
            folderEntityToFolder(it)
        }
    }

    override suspend fun isExistFolderName(folderName: String): Boolean {
        return folderDao.isExistFolderName(folderName)
    }

    override suspend fun getMemoCountByFolder(folderId: Long): Int {
        return folderDao.getMemoCountByFolder(folderId)
    }

    override suspend fun getFolderSize(): Int {
        return folderDao.getFolderSize()
    }

    override suspend fun updateFolders(folders: List<Folder>) {
        folderDao.updateFolders(folders = folders.map {
            it.toFolderEntity()
        })
    }
}