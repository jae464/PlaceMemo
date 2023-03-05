package com.jae464.data.repository.folder

import android.util.Log
import com.jae464.data.db.FolderDao
import com.jae464.data.db.MemoDao
import com.jae464.data.mapper.folderEntityToFolder
import com.jae464.data.model.FolderEntity
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
        Log.d("FolderRepositoryImpl", "try get all folder")
//        return flow {
//            folderDao.getAllFolder().collect {
//                val folderList: MutableList<Folder> = mutableListOf()
//                it.forEach { folderEntity ->
//                    folderList.add(folderEntityToFolder(folderEntity))
//                }
//                emit(folderList)
//            }
//        }
        return folderDao.getAllFolder().map { list ->
            Log.d("FolderRepositoryImpl", list.toString())
            list.map {
                Log.d("FolderRepositoryImpl", it.toString())
                folderEntityToFolder(it)
            }
        }
    }

    override fun getFolderByName(folderName: String): Flow<Folder?> {
        return folderDao.getFolderByName(folderName).map {
//            Log.d("FolderRepositoryImpl", it.toString())
            folderEntityToFolder(it)
        }.catch {
            emit(Folder(0,"",0))
        }
//        return flow {
//            folderDao.getFolderByName(folderName).collect {
//                emit(folderEntityToFolder(it))
//            }
//        }
    }

    override suspend fun isExistFolderName(folderName: String): Boolean {
        return folderDao.isExistFolderName(folderName)
    }
}