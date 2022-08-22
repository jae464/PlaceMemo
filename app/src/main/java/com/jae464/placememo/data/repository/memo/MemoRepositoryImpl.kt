package com.jae464.placememo.data.repository.memo

import android.graphics.Bitmap
import com.jae464.placememo.data.manager.ImageManager
import com.jae464.placememo.data.mapper.memoEntityToMemo
import com.jae464.placememo.data.mapper.memoToMemoEntity
import com.jae464.placememo.data.repository.memo.local.MemoLocalDataSource
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.domain.repository.MemoRepository
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoLocalDataSource: MemoLocalDataSource
): MemoRepository {
    override suspend fun getMemo(id: Long): Memo = memoEntityToMemo(memoLocalDataSource.getMemo(id))

    override suspend fun saveMemo(memo: Memo): Long {
        return memoLocalDataSource.saveMemo(memoToMemoEntity(memo))
    }

    override suspend fun getAllMemo(): List<Memo> {
        val memoList = mutableListOf<Memo>()
        memoLocalDataSource.getAllMemo().forEach {
            memoList.add(memoEntityToMemo(it))
        }
        return memoList
    }

    override fun saveImage(imageList: List<Bitmap>, memoId: Long) {
        ImageManager.saveImage(imageList, memoId)
    }
}