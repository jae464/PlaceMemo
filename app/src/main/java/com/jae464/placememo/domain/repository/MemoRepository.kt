package com.jae464.placememo.domain.repository

import com.jae464.placememo.domain.model.post.Memo

interface MemoRepository {
    suspend fun getMemo(id: Long): Memo
    suspend fun saveMemo(memo: Memo)
}