package com.jae464.data.repository.memo.local

import androidx.paging.PagingData
import com.jae464.data.model.FolderWithMemos
import com.jae464.data.model.MemoEntity
import com.jae464.domain.model.SortBy
import kotlinx.coroutines.flow.Flow

interface MemoLocalDataSource {
    fun getMemo(id: Int): Flow<MemoEntity>
    fun getAllMemo(): Flow<List<MemoEntity>>
    fun getAllMemoWithPage(sortBy: SortBy): Flow<PagingData<MemoEntity>>

    suspend fun saveMemo(memo: MemoEntity): Long
    suspend fun updateMemo(memo: MemoEntity)

    fun getMemoByCategory(categoryId: Long): Flow<List<MemoEntity>>
    fun getMemoByCategoryWithPage(categoryId: Long, sortBy: SortBy): Flow<PagingData<MemoEntity>>
    fun getMemoByTitle(title: String): Flow<List<MemoEntity>>
    fun getMemoByContent(content: String): Flow<List<MemoEntity>>

    fun getAllMemoByFolder(folderId: Long, sortBy: SortBy): Flow<PagingData<MemoEntity>>
    fun getAllMemoByFolderWithCategory(folderId: Long, categoryId: Long, sortBy: SortBy): Flow<PagingData<MemoEntity>>

    suspend fun deleteMemo(id: Int)
    suspend fun saveMemoImages(memoId: Long, imagePathList: List<String>)
    fun updateMemoImages(memoId: Long, imagePathList: List<String>)
    suspend fun getFolderWithMemos(folderId: Long): List<FolderWithMemos>
    fun getImagePathList(memoId: Int): List<String>
}