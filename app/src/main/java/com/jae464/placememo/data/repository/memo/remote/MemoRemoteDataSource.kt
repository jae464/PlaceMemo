package com.jae464.placememo.data.repository.memo.remote

import android.graphics.Bitmap
import android.net.Uri
import com.jae464.placememo.data.dto.MemoDTO
import com.jae464.placememo.data.model.MemoEntity

interface MemoRemoteDataSource {
    suspend fun getMemo(id: Long)
    suspend fun getAllMemoByUser(uid: String): List<MemoDTO>
    suspend fun insertMemo(memo: MemoDTO)
    suspend fun saveImageOnRemote(imageList: List<Bitmap>, imageUriList: List<String>)
}