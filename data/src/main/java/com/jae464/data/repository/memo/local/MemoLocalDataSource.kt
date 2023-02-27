package com.jae464.data.repository.memo.local

import com.jae464.data.model.FolderWithMemos
import com.jae464.data.model.MemoEntity

interface MemoLocalDataSource {
    suspend fun getMemo(id: Long): MemoEntity
    suspend fun getAllMemo(): List<MemoEntity>
    suspend fun saveMemo(memo: MemoEntity): Long
    suspend fun updateMemo(memo: MemoEntity)
    suspend fun getMemoByCategory(category: Int): List<MemoEntity>
    suspend fun getMemoByTitle(title: String): List<MemoEntity>
    suspend fun getMemoByContent(content: String): List<MemoEntity>
    suspend fun deleteMemo(id: Long)
    suspend fun saveMemoImages(memoId: Long, imagePathList: List<String>)
    suspend fun getFoldersWithMemos(): List<FolderWithMemos>
    fun getImagePathList(memoId: Long): List<String>
}