package com.jae464.data.repository.folder

import com.jae464.data.db.MemoDao
import com.jae464.data.model.FolderEntity
import com.jae464.data.model.toFolderEntity
import com.jae464.domain.model.feed.Folder
import com.jae464.domain.repository.FolderRepository
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val memoDao: MemoDao
): FolderRepository {
    override suspend fun createFolder(folder: Folder) {
        memoDao.insertFolder(folder.toFolderEntity())
    }
}