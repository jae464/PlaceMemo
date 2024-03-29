package com.jae464.domain.repository

import androidx.paging.PagingData
import com.jae464.domain.model.SortBy
import com.jae464.domain.model.post.Memo
import kotlinx.coroutines.flow.Flow

interface MemoRepository {
    fun getMemo(id: Int): Flow<Memo>

    suspend fun saveMemo(memo: Memo): Long
    suspend fun saveMemoOnRemote(userId: String, memo: Memo)

    suspend fun updateMemo(memo: Memo)
    suspend fun updateMemoOnRemote(userId: String, memo: Memo)

    fun getAllMemo(): Flow<List<Memo>>
    fun getAllMemoWithPage(sortBy: SortBy): Flow<PagingData<Memo>>
    suspend fun getAllMemoByUserOnRemote(uid: String): List<Memo>

    fun getMemoByCategory(category: Long) : Flow<List<Memo>>
    fun getMemoByCategoryWithPage(categoryId: Long, sortBy: SortBy): Flow<PagingData<Memo>>
    fun getMemoByTitle(title: String): Flow<List<Memo>>
    fun getMemoByContent(content: String): Flow<List<Memo>>

    fun getAllMemoByFolder(folderId: Long, sortBy: SortBy): Flow<PagingData<Memo>>
    fun getAllMemoByFolderWithCategory(folderId: Long, categoryId: Long, sortBy: SortBy): Flow<PagingData<Memo>>

    suspend fun deleteMemo(id: Int)
    suspend fun deleteMemoOnRemote(userId: String, memoId: Int)

    suspend fun saveImages(memoId: Long, imagePathList: List<String>)
    suspend fun saveImagesOnRemote(imagePathList: List<String>)

    fun updateImages(memoId: Long, imagePathList: List<String>)

    fun getImagePathList(memoId: Int): List<String>

}