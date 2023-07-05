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
import com.jae464.domain.model.SortBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

class MemoLocalDataSourceImpl @Inject constructor(
    private val memoDao: MemoDao,
    private val categoryDao: CategoryDao,
    private val imageManager: ImageManager
) : MemoLocalDataSource {

    private val TAG = "MemoLocalDataSourceImpl"

    override fun getMemo(id: Int): Flow<MemoEntity> {
        return memoDao.getMemo(id)
    }

    override fun getAllMemo(): Flow<List<MemoEntity>> {
        return memoDao.getAllMemo()
    }

    override fun getAllMemoWithPage(sortBy: SortBy): Flow<PagingData<MemoEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                when (sortBy) {
                    SortBy.DESC -> memoDao.getAllMemoWithPageSortByDesc()
                    SortBy.ASC -> memoDao.getAllMemoWithPageSortByAsc()
                }
            }
        ).flow
    }

    override suspend fun saveMemo(memo: MemoEntity): Long {
        return memoDao.insertMemo(memo)
    }

    override suspend fun updateMemo(memo: MemoEntity) {
        memoDao.updateMemo(memo)
    }

    override fun getMemoByCategory(categoryId: Long): Flow<List<MemoEntity>> {
        return memoDao.getMemoByCategory(categoryId)
    }

    override fun getMemoByCategoryWithPage(
        categoryId: Long,
        sortBy: SortBy
    ): Flow<PagingData<MemoEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                when (sortBy) {
                    SortBy.DESC -> memoDao.getMemoByCategoryWithPageSortByDesc(categoryId)
                    SortBy.ASC -> memoDao.getMemoByCategoryWithPageSortByAsc(categoryId)
                }

            }
        ).flow
    }

    override fun getMemoByTitle(title: String): Flow<List<MemoEntity>> {
        return memoDao.getMemoByTitle(title)
    }

    override fun getMemoByContent(content: String): Flow<List<MemoEntity>> {
        return memoDao.getMemoByTitle(content)
    }

    override fun getAllMemoByFolder(folderId: Long, sortBy: SortBy): Flow<PagingData<MemoEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                when (sortBy) {
                    SortBy.DESC -> memoDao.getAllMemoByFolderSortByDescWithPage(folderId)
                    SortBy.ASC -> memoDao.getAllMemoByFolderSortByAscWithPage(folderId)
                }
            }
        ).flow
    }

    override fun getAllMemoByFolderWithCategory(
        folderId: Long,
        categoryId: Long,
        sortBy: SortBy
    ): Flow<PagingData<MemoEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                when (sortBy) {
                    SortBy.DESC -> memoDao.getMemoByFolderWithCategorySortByDescWithPage(
                        folderId,
                        categoryId
                    )

                    SortBy.ASC -> memoDao.getMemoByFolderWithCategorySortByAscWithPage(
                        folderId,
                        categoryId
                    )
                }
            }
        ).flow
    }


    override suspend fun deleteMemo(id: Int) {
        memoDao.deleteMemo(id)
        imageManager.deleteAllImages(id.toLong())

    }

    override suspend fun saveMemoImages(memoId: Long, imagePathList: List<String>) {
        imagePathList.forEach { imagePath ->
            imageManager.saveImage(memoId, imagePath)
        }
    }

    override fun updateMemoImages(memoId: Long, imagePathList: List<String>) {
        // TODO 만약 메모 수정 시 삭제된 이미지가 없으면 해당 이미지 폴더에서 제거하기
        val currentImages = imageManager.getMemoImageFiles(memoId).map {
            it.path.substringAfterLast("${memoId}_")
        }
        Log.d(TAG, currentImages.toString())
        imagePathList.forEach { imagePath ->
            if (!currentImages.contains("${imagePath.substringAfterLast("/")}.jpg")) {
                Log.d(TAG, "${imagePath} 는 없는 이미지이므로 저장합니다.")
                imageManager.saveImage(memoId, imagePath)
            }
        }
    }

    override suspend fun getFolderWithMemos(folderId: Long): List<FolderWithMemos> {
        return memoDao.getFoldersWithMemos(folderId)
    }

    override fun getImagePathList(memoId: Int): List<String> {
        return emptyList()
    }

}