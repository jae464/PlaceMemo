package com.jae464.placememo.data.repository

import com.jae464.placememo.data.db.MemoDao
import com.jae464.placememo.data.model.MemoEntity
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.domain.repository.MemoRepository
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDao: MemoDao
): MemoRepository {
    override suspend fun getMemo(id: Long): MemoEntity = memoDao.getMemo(id)


    override suspend fun saveMemo(memo: MemoEntity) = memoDao.insertMemo(memo)

}