package com.jae464.placememo.data.repository.memo.local

import com.jae464.placememo.data.db.MemoDao
import com.jae464.placememo.data.model.MemoEntity
import javax.inject.Inject

class MemoLocalDataSourceImpl @Inject constructor(
    private val memoDao: MemoDao
): MemoLocalDataSource {
    override suspend fun getMemo(id: Long): MemoEntity {
        return memoDao.getMemo(id)
    }

    override suspend fun getAllMemo(): List<MemoEntity> {
        return memoDao.getAllMemo()
    }

    override suspend fun saveMemo(memo: MemoEntity): Long {
        return memoDao.insertMemo(memo)
    }

    override suspend fun updateMemo(memo: MemoEntity) {
        memoDao.updateMemo(memo)
    }

    override suspend fun getMemoByCategory(category: Int): List<MemoEntity> {
        return memoDao.getMemoByCategory(category)
    }

    override suspend fun deleteMemo(id: Long) {
        memoDao.deleteMemo(id)
    }

}