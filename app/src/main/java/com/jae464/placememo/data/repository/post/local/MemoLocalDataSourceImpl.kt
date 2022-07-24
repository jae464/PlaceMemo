package com.jae464.placememo.data.repository.post.local

import com.jae464.placememo.data.db.MemoDao
import com.jae464.placememo.data.model.MemoEntity
import com.jae464.placememo.domain.model.post.Memo
import javax.inject.Inject

class MemoLocalDataSourceImpl @Inject constructor(
    private val memoDao: MemoDao
): MemoLocalDataSource {
    override suspend fun getMemo(id: Long): MemoEntity {
        return memoDao.getMemo(id)
    }

    override suspend fun saveMemo(memo: MemoEntity) {
        memoDao.insertMemo(memo)
    }

}