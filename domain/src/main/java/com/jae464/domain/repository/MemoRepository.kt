package com.jae464.domain.repository

import androidx.paging.PagingData
import com.jae464.domain.model.post.Memo
import kotlinx.coroutines.flow.Flow

interface MemoRepository {
    suspend fun getMemo(id: Long): Memo

    suspend fun saveMemo(memo: Memo): Long
    suspend fun saveMemoOnRemote(userId: String, memo: Memo)

    suspend fun updateMemo(memo: Memo)
    suspend fun updateMemoOnRemote(userId: String, memo: Memo)

    fun getAllMemo(): Flow<PagingData<Memo>>
    suspend fun getAllMemoByUserOnRemote(uid: String): List<Memo>

    fun getMemoByCategory(category: Int) : Flow<PagingData<Memo>>
    fun getMemoByTitle(title: String): Flow<PagingData<Memo>>
    fun getMemoByContent(content: String): Flow<PagingData<Memo>>

    suspend fun deleteMemo(id: Long)
    suspend fun deleteMemoOnRemote(userId: String, memoId: Long)

    suspend fun saveImages(memoId: Long, imagePathList: List<String>)
    suspend fun saveImagesOnRemote(memoId: Long, imagePathList: List<String>)

    fun getImagePathList(memoId: Long): List<String>

}