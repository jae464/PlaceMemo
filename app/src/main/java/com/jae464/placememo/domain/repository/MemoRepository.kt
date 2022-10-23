package com.jae464.placememo.domain.repository

import android.graphics.Bitmap
import com.jae464.placememo.domain.model.post.Memo

interface MemoRepository {
    suspend fun getMemo(id: Long): Memo
    suspend fun saveMemo(memo: Memo): Long
    suspend fun saveMemoOnRemote(userId: String, memo: Memo)
    suspend fun getAllMemo(): List<Memo>
    suspend fun getMemoByCategory(category: Int) : List<Memo>
    fun saveImage(imageList: List<Bitmap>, memoId: Long)
    suspend fun saveImageOnRemote(imageList: List<Bitmap>, imageUriList: List<String>)
}