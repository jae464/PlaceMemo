package com.jae464.data.repository.memo

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.jae464.data.db.CategoryDao
import com.jae464.data.dto.toMemo
import com.jae464.data.dto.toMemoDTO
import com.jae464.data.model.toMemo
import com.jae464.data.model.toMemoEntity
import com.jae464.data.repository.memo.local.MemoLocalDataSource
import com.jae464.data.repository.memo.remote.MemoRemoteDataSource
import com.jae464.domain.model.SortBy
import com.jae464.domain.model.post.Category
import com.jae464.domain.model.post.Memo
import com.jae464.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoLocalDataSource: MemoLocalDataSource,
    private val memoRemoteDataSource: MemoRemoteDataSource,
    private val categoryDao: CategoryDao
) : MemoRepository {

    override fun getMemo(id: Int): Flow<Memo> {
        return memoLocalDataSource.getMemo(id).map { memoEntity ->
            val categoryEntity = categoryDao.getCategoryById(categoryId = memoEntity.categoryId)
            val category = Category(categoryEntity.id, categoryEntity.name)
            memoEntity.toMemo(category)
        }
    }

    override suspend fun saveMemo(memo: Memo): Long {
        return memoLocalDataSource.saveMemo(memo.toMemoEntity())
    }

    override suspend fun updateMemo(memo: Memo) {
        memoLocalDataSource.updateMemo(memo.toMemoEntity())
    }

    override suspend fun updateMemoOnRemote(userId: String, memo: Memo) {
        memoRemoteDataSource.updateMemo(userId, memo.toMemoDTO(userId))
    }

    override fun getAllMemo(): Flow<List<Memo>> {
        return memoLocalDataSource.getAllMemo().map { memoEntities ->
            memoEntities.map { memoEntity ->
                val categoryEntity = categoryDao.getCategoryById(categoryId = memoEntity.categoryId)
                val category = Category(categoryEntity.id, categoryEntity.name)
                memoEntity.toMemo(category)
            }
        }
    }

    override suspend fun saveMemoOnRemote(userId: String, memo: Memo) {
        return memoRemoteDataSource.insertMemo(memo.toMemoDTO(userId))
    }

    override fun getAllMemoWithPage(sortBy: SortBy): Flow<PagingData<Memo>> {
        return memoLocalDataSource.getAllMemoWithPage(sortBy).map { pagingData ->
            pagingData.map { memoEntity ->
                val categoryEntity = categoryDao.getCategoryById(categoryId = memoEntity.categoryId)
                val category = Category(categoryEntity.id, categoryEntity.name)
                memoEntity.toMemo(category)
            }
        }
    }

    override suspend fun getAllMemoByUserOnRemote(uid: String): List<Memo> {
        val memoList = mutableListOf<Memo>()
        memoRemoteDataSource.getAllMemoByUser(uid).forEach {
            memoList.add(it.toMemo())
        }
        return memoList
    }

    override fun getMemoByCategory(category: Long): Flow<List<Memo>> {
        return memoLocalDataSource.getMemoByCategory(category).map { memoEntities ->
            memoEntities.map { memoEntity ->
                val categoryEntity = categoryDao.getCategoryById(categoryId = memoEntity.categoryId)
                val category = Category(categoryEntity.id, categoryEntity.name)
                memoEntity.toMemo(category)
            }
        }
    }

    override fun getMemoByCategoryWithPage(categoryId: Long, sortBy: SortBy): Flow<PagingData<Memo>> {
        return memoLocalDataSource.getMemoByCategoryWithPage(categoryId, sortBy).map { pagingData ->
            pagingData.map { memoEntity ->
                val categoryEntity = categoryDao.getCategoryById(categoryId = memoEntity.categoryId)
                val category = Category(categoryEntity.id, categoryEntity.name)
                memoEntity.toMemo(category)
            }
        }
    }

    override fun getMemoByTitle(title: String): Flow<List<Memo>> {
        return memoLocalDataSource.getMemoByTitle(title).map {
            it.map { memoEntity -> memoEntity.toMemo() }
        }
    }

    override fun getMemoByContent(content: String): Flow<List<Memo>> {
        TODO("Not yet implemented")
    }

    override fun getAllMemoByFolder(folderId: Long, sortBy: SortBy): Flow<PagingData<Memo>> {
        return memoLocalDataSource.getAllMemoByFolder(folderId, sortBy).map { pagingData ->
            Log.d("MemoRepositoryImpl", pagingData.toString())
            pagingData.map { memoEntity ->
                val categoryEntity = categoryDao.getCategoryById(categoryId = memoEntity.categoryId)
                val category = Category(categoryEntity.id, categoryEntity.name)
                Log.d("MemoRepositoryImpl", memoEntity.toString())
                memoEntity.toMemo(category)
            }
        }
    }

    override fun getAllMemoByFolderWithCategory(
        folderId: Long,
        categoryId: Long,
        sortBy: SortBy
    ): Flow<PagingData<Memo>> {
        return memoLocalDataSource.getAllMemoByFolderWithCategory(folderId, categoryId, sortBy).map { pagingData ->
            pagingData.map { memoEntity ->
                val categoryEntity = categoryDao.getCategoryById(categoryId = memoEntity.categoryId)
                val category = Category(categoryEntity.id, categoryEntity.name)
                memoEntity.toMemo(category)
            }
        }
    }

    override suspend fun deleteMemo(id: Int) {
        memoLocalDataSource.deleteMemo(id)
    }

    override suspend fun deleteMemoOnRemote(userId: String, memoId: Int) {
        memoRemoteDataSource.deleteMemo(userId, memoId)
    }

    override suspend fun saveImages(memoId: Long, imagePathList: List<String>) {
        memoLocalDataSource.saveMemoImages(memoId, imagePathList)
    }

    override suspend fun saveImagesOnRemote(imagePathList: List<String>) {
        TODO("Not yet implemented")
    }

    override fun updateImages(memoId: Long, imagePathList: List<String>) {
        memoLocalDataSource.updateMemoImages(memoId, imagePathList)
    }

    override fun getImagePathList(memoId: Int): List<String> {
        return memoLocalDataSource.getImagePathList(memoId)
    }


//    override fun saveImage(imageList: List<Bitmap>, memoId: Int) {
//        ImageManager.saveImage(imageList, memoId)
//    }
//
//    override suspend fun saveImageOnRemote(imageList: List<Bitmap>, imageUriList: List<String>) {
//        memoRemoteDataSource.saveImageOnRemote(imageList, imageUriList)
//    }
}