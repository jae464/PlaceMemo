package com.jae464.placememo.data.repository.memo

import android.graphics.Bitmap
import com.jae464.placememo.data.dto.MemoDTO
import com.jae464.placememo.data.manager.ImageManager
import com.jae464.placememo.data.mapper.memoEntityToMemo
import com.jae464.placememo.data.mapper.memoToMemoEntity
import com.jae464.placememo.data.model.Region
import com.jae464.placememo.data.repository.memo.local.MemoLocalDataSource
import com.jae464.placememo.data.repository.memo.remote.MemoRemoteDataSource
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.domain.repository.MemoRepository
import java.util.*
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoLocalDataSource: MemoLocalDataSource,
    private val memoRemoteDataSource: MemoRemoteDataSource
): MemoRepository {
    override suspend fun getMemo(id: Long): Memo = memoEntityToMemo(memoLocalDataSource.getMemo(id))

    override suspend fun saveMemo(memo: Memo): Long {
        return memoLocalDataSource.saveMemo(memoToMemoEntity(memo))
    }

    override suspend fun saveMemoOnRemote(userId: String, memo: Memo) {
        return memoRemoteDataSource.insertMemo(MemoDTO(
            userId,
            memo.title,
            memo.content,
            Date(),
            memo.latitude,
            memo.longitude,
            memo.category,
            Region(memo.area1, memo.area1, memo.area3),
            imageUrlList = memo.imageUriList
        ))
    }

    override suspend fun getAllMemo(): List<Memo> {
        val memoList = mutableListOf<Memo>()
        memoLocalDataSource.getAllMemo().forEach {
            memoList.add(memoEntityToMemo(it))
        }
        return memoList
    }

    override suspend fun getMemoByCategory(category: Int): List<Memo> {
        val memoList = mutableListOf<Memo>()
        memoLocalDataSource.getMemoByCategory(category).forEach {
            memoList.add(memoEntityToMemo(it))
        }
        return memoList
    }

    override suspend fun deleteMemo(id: Long) {
        memoLocalDataSource.deleteMemo(id)
    }

    override fun saveImage(imageList: List<Bitmap>, memoId: Long) {
        ImageManager.saveImage(imageList, memoId)
    }

    override suspend fun saveImageOnRemote(imageList: List<Bitmap>, imageUriList: List<String>) {
        memoRemoteDataSource.saveImageOnRemote(imageList, imageUriList)
    }
}