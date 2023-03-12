package com.jae464.data.repository.memo

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.jae464.data.dto.toMemo
import com.jae464.data.dto.toMemoDTO
import com.jae464.data.model.toMemo
import com.jae464.data.model.toMemoEntity
import com.jae464.data.repository.memo.local.MemoLocalDataSource
import com.jae464.data.repository.memo.remote.MemoRemoteDataSource
import com.jae464.domain.model.post.Memo
import com.jae464.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoLocalDataSource: MemoLocalDataSource,
    private val memoRemoteDataSource: MemoRemoteDataSource,
) : MemoRepository {

    override fun getMemo(id: Int): Flow<Memo> {
        Log.d("MemoRepositoryImpl", id.toString())
//        return flow {
//            memoLocalDataSource.getMemo(id).map {
//                Log.d("MemoRepositoryImpl", it.toString())
//                it.toMemo()
//            }
//        }
        return memoLocalDataSource.getMemo(id).map {
//            Log.d("MemoRepositoryImpl", it.toString())
            it.toMemo()
        }
    }

    override suspend fun saveMemo(memo: Memo) {
        return memoLocalDataSource.saveMemo(memo.toMemoEntity())
    }

    override suspend fun updateMemo(memo: Memo) {
        memoLocalDataSource.updateMemo(memo.toMemoEntity())
    }

    override suspend fun updateMemoOnRemote(userId: String, memo: Memo) {
        memoRemoteDataSource.updateMemo(userId, memo.toMemoDTO(userId))
    }

    override fun getAllMemo(): Flow<List<Memo>> {
        return memoLocalDataSource.getAllMemo().map { list ->
            list.map { it.toMemo() }
        }
    }

    override suspend fun saveMemoOnRemote(userId: String, memo: Memo) {
        return memoRemoteDataSource.insertMemo(memo.toMemoDTO(userId))
    }

    override fun getAllMemoWithPage(): Flow<PagingData<Memo>> {
        return memoLocalDataSource.getAllMemoWithPage().map { pagingData ->
            pagingData.map { memoEntity ->
                memoEntity.toMemo()
            }
        }

//        val memoList = mutableListOf<Memo>()
//        memoLocalDataSource.getAllMemo().forEach { memoEntity ->
//            memoList.add(memoEntity.toMemo())
//        }
//        return memoList
    }

    override suspend fun getAllMemoByUserOnRemote(uid: String): List<Memo> {
        val memoList = mutableListOf<Memo>()
        memoRemoteDataSource.getAllMemoByUser(uid).forEach {
            memoList.add(it.toMemo())
        }
        return memoList
    }

    override fun getMemoByCategory(category: Int): Flow<List<Memo>> {
        return flow {
            memoLocalDataSource.getMemoByCategory(category).map {
                it.map { memoEntity -> memoEntity.toMemo() }
            }
        }
//        val memoList = mutableListOf<Memo>()
//        memoLocalDataSource.getMemoByCategory(category).forEach {
////            memoList.add(memoEntityToMemo(it))
//            memoList.add(it.toMemo())
//        }
//        return memoList
    }

    override fun getMemoByTitle(title: String): Flow<List<Memo>> {
        return memoLocalDataSource.getMemoByTitle(title).map {
            it.map { memoEntity -> memoEntity.toMemo() }
        }
//        val memoList = mutableListOf<Memo>()
//        memoLocalDataSource.getMemoByTitle(title).forEach { memoEntity ->
//            memoList.add(memoEntity.toMemo())
//        }
//        return memoList
    }

    override fun getMemoByContent(content: String): Flow<List<Memo>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMemo(id: Int) {
        memoLocalDataSource.deleteMemo(id)
    }

    override suspend fun deleteMemoOnRemote(userId: String, memoId: Int) {
        memoRemoteDataSource.deleteMemo(userId, memoId)
    }

    override suspend fun saveImages(imagePathList: List<String>) {
        memoLocalDataSource.saveMemoImages(imagePathList)
    }

    override suspend fun saveImagesOnRemote(imagePathList: List<String>) {
        TODO("Not yet implemented")
    }

    override fun getImagePathList(memoId: Int): List<String> {
        return memoLocalDataSource.getImagePathList(memoId)
    }


//    override fun saveImage(imageList: List<Bitmap>, memoId: Int) {
//        ImageManager.saveImage(imageList, memoId)
//    }
//
//    override suspend fun saveImageOnRemote(imageList: List<Bitmap>, imageUriList: List<String>) {
//        memoRemoteDataSource.saveImageOnRemote(imageList, imageUriList)
//    }
}