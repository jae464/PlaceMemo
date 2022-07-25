package com.jae464.placememo.data.repository.post

import com.jae464.placememo.data.mapper.memoEntityToMemo
import com.jae464.placememo.data.mapper.memoToMemoEntity
import com.jae464.placememo.data.model.MemoEntity
import com.jae464.placememo.data.repository.post.local.MemoLocalDataSource
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.domain.repository.MemoRepository
import java.security.Timestamp
import java.util.*
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoLocalDataSource: MemoLocalDataSource
): MemoRepository {
    override suspend fun getMemo(id: Long): Memo = memoEntityToMemo(memoLocalDataSource.getMemo(id))

    // todo Memo -> MemoEntity로 변환하여 saveMemo
    override suspend fun saveMemo(memo: Memo) {
        memoLocalDataSource.saveMemo(memoToMemoEntity(memo))
    }

    override suspend fun getAllMemo(): List<Memo> {
        val memoList = mutableListOf<Memo>()
        memoLocalDataSource.getAllMemo().forEach {
            memoList.add(memoEntityToMemo(it))
        }
        return memoList
    }
}