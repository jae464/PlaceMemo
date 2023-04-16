package com.jae464.data.repository.memo.local

import androidx.paging.PagingData
import com.jae464.data.model.FolderWithMemos
import com.jae464.data.model.MemoEntity
import kotlinx.coroutines.flow.Flow

interface MemoLocalDataSource {
    fun getMemo(id: Int): Flow<MemoEntity>
    fun getAllMemo(): Flow<List<MemoEntity>>
    fun getAllMemoWithPage(): Flow<PagingData<MemoEntity>>
    suspend fun saveMemo(memo: MemoEntity)
    suspend fun updateMemo(memo: MemoEntity)
    fun getMemoByCategory(categoryId: Long): Flow<List<MemoEntity>>
    fun getMemoByCategoryWithPage(categoryId: Long): Flow<PagingData<MemoEntity>>
    fun getMemoByTitle(title: String): Flow<List<MemoEntity>>
    fun getMemoByContent(content: String): Flow<List<MemoEntity>>
    suspend fun deleteMemo(id: Int)
    suspend fun saveMemoImages(imagePathList: List<String>)
    suspend fun getFoldersWithMemos(): List<FolderWithMemos>
    fun getImagePathList(memoId: Int): List<String>
}