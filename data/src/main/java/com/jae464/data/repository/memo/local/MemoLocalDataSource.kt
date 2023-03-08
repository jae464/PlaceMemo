package com.jae464.data.repository.memo.local

import androidx.paging.PagingData
import com.jae464.data.model.FolderWithMemos
import com.jae464.data.model.MemoEntity
import kotlinx.coroutines.flow.Flow

interface MemoLocalDataSource {
    suspend fun getMemo(id: Long): MemoEntity
    fun getAllMemo(): Flow<PagingData<MemoEntity>>
    suspend fun saveMemo(memo: MemoEntity): Long
    suspend fun updateMemo(memo: MemoEntity)
    fun getMemoByCategory(category: Int): Flow<PagingData<MemoEntity>>
    fun getMemoByTitle(title: String): Flow<PagingData<MemoEntity>>
    fun getMemoByContent(content: String): Flow<PagingData<MemoEntity>>
    suspend fun deleteMemo(id: Long)
    suspend fun saveMemoImages(memoId: Long, imagePathList: List<String>)
    suspend fun getFoldersWithMemos(): List<FolderWithMemos>
    fun getImagePathList(memoId: Long): List<String>
}