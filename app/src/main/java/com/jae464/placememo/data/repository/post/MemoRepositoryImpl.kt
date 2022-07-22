package com.jae464.placememo.data.repository.post

import com.jae464.placememo.data.db.MemoDao
import com.jae464.placememo.data.model.MemoEntity
import com.jae464.placememo.data.repository.post.local.MemoLocalDataSource
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.domain.repository.MemoRepository
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoLocalDataSource: MemoLocalDataSource
): MemoRepository {
    override suspend fun getMemo(id: Long): Memo = memoLocalDataSource.getMemo(id)

    override suspend fun saveMemo(memo: Memo) = memoLocalDataSource.saveMemo(memo)
}