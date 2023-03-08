package com.jae464.data.repository.memo.local

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jae464.data.db.MemoDao
import com.jae464.data.manager.ImageManager
import com.jae464.data.model.FolderEntity
import com.jae464.data.model.FolderWithMemos
import com.jae464.data.model.MemoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MemoLocalDataSourceImpl @Inject constructor(
    private val memoDao: MemoDao,
    private val imageManager: ImageManager
): MemoLocalDataSource {
    private val TAG = "MemoLocalDataSourceImpl"
    override suspend fun getMemo(id: Long): MemoEntity {
        return memoDao.getMemo(id)
    }

    override suspend fun getAllMemo(): Flow<PagingData<MemoEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                memoDao.getAllMemo()
            }
        ).flow
    }

    override suspend fun saveMemo(memo: MemoEntity): Long {
        return memoDao.insertMemo(memo)
    }

    override suspend fun updateMemo(memo: MemoEntity) {
        memoDao.updateMemo(memo)
    }

    override suspend fun getMemoByCategory(category: Int): Flow<PagingData<MemoEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                memoDao.getMemoByCategory(category)
            }
        ).flow
    }

    override suspend fun getMemoByTitle(title: String): Flow<PagingData<MemoEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                memoDao.getMemoByTitle(title)
            }
        ).flow
    }

    override suspend fun getMemoByContent(content: String): Flow<PagingData<MemoEntity>> {
        // TODO getMemoByContent 로 수정 필요
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                memoDao.getMemoByTitle(content)
            }
        ).flow
    }

    override suspend fun deleteMemo(id: Long) {
        memoDao.deleteMemo(id)
    }

    override suspend fun saveMemoImages(memoId: Long, imagePathList: List<String>) {
        imagePathList.forEach {imagePath ->
            imageManager.saveImage(memoId, imagePath)
        }
    }

    override suspend fun getFoldersWithMemos(): List<FolderWithMemos> {
        return memoDao.getFoldersWithMemos()
    }

    override fun getImagePathList(memoId: Long): List<String> {
        return imageManager.getImagePathList(memoId)

    }

}