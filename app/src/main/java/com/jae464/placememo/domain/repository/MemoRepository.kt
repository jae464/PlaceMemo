package com.jae464.placememo.domain.repository

import android.graphics.Bitmap
import com.jae464.placememo.domain.model.post.Memo

interface MemoRepository {
    suspend fun getMemo(id: Long): Memo
    suspend fun saveMemo(memo: Memo)
    suspend fun getAllMemo(): List<Memo>
    suspend fun saveImage(imageList: List<Bitmap>, memoId: Long)
}