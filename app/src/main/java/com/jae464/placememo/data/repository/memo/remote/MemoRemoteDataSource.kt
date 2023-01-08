package com.jae464.placememo.data.repository.memo.remote

import android.graphics.Bitmap
import android.net.Uri
import com.jae464.placememo.data.dto.MemoDTO
import com.jae464.placememo.data.model.MemoEntity

interface MemoRemoteDataSource {
    suspend fun getMemo(id: Long)
    suspend fun getAllMemoByUser(uid: String): List<MemoDTO>

    suspend fun insertMemo(memo: MemoDTO)
    suspend fun updateMemo(userId: String, memo: MemoDTO)
    suspend fun deleteMemo(userId: String, memoId: Long)

    suspend fun saveImageOnRemote(imageList: List<Bitmap>, imageUriList: List<String>)
}