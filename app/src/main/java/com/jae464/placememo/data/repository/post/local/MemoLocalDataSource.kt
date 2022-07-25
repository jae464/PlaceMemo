package com.jae464.placememo.data.repository.post.local

import com.jae464.placememo.data.model.MemoEntity
import com.jae464.placememo.domain.model.post.Memo

interface MemoLocalDataSource {
    suspend fun getMemo(id: Long): MemoEntity
    suspend fun getAllMemo(): List<MemoEntity>
    suspend fun saveMemo(memo: MemoEntity)
}