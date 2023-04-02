package com.jae464.data.repository.memo.local

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jae464.data.db.CategoryDao
import com.jae464.data.db.MemoDao
import com.jae464.data.manager.ImageManager
import com.jae464.data.model.FolderWithMemos
import com.jae464.data.model.MemoEntity
import com.jae464.data.model.toMemo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MemoLocalDataSourceImpl @Inject constructor(
    private val memoDao: MemoDao,
    private val categoryDao: CategoryDao,
    private val imageManager: ImageManager
): MemoLocalDataSource {

    private val TAG = "MemoLocalDataSourceImpl"

    override fun getMemo(id: Int): Flow<MemoEntity> {
        return memoDao.getMemo(id)
    }

    override fun getAllMemo(): Flow<List<MemoEntity>> {
        return memoDao.getAllMemo()
    }

    override fun getAllMemoWithPage(): Flow<PagingData<MemoEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                memoDao.getAllMemoWithPage()
            }
        ).flow
    }

    override suspend fun saveMemo(memo: MemoEntity) {
        return memoDao.insertMemo(memo)
    }

    override suspend fun updateMemo(memo: MemoEntity) {
        memoDao.updateMemo(memo)
    }

    override fun getMemoByCategory(categoryId: Long): Flow<List<MemoEntity>> {
        return memoDao.getMemoByCategory(categoryId)
    }

    override fun getMemoByTitle(title: String): Flow<List<MemoEntity>> {
        return memoDao.getMemoByTitle(title)
    }

    override fun getMemoByContent(content: String): Flow<List<MemoEntity>> {
        return memoDao.getMemoByTitle(content)
    }

    override suspend fun deleteMemo(id: Int) {
        memoDao.deleteMemo(id)
    }

    override suspend fun saveMemoImages(imagePathList: List<String>) {
        imagePathList.forEach {imagePath ->
            imageManager.saveImage(imagePath)
        }
    }

    override suspend fun getFoldersWithMemos(): List<FolderWithMemos> {
        return memoDao.getFoldersWithMemos()
    }

    override fun getImagePathList(memoId: Int): List<String> {
        return imageManager.getImagePathList(memoId)
    }

}