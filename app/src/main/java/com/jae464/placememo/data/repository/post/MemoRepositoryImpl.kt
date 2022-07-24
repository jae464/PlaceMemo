package com.jae464.placememo.data.repository.post

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
    override suspend fun getMemo(id: Long): Memo = memoLocalDataSource.getMemo(id)

    // todo Memo -> MemoEntity로 변환하여 saveMemo
    override suspend fun saveMemo(title: String, content: String, imageUrlList: List<String>,
    latitude: Double, longitude: Double) {
        val memoEntity = MemoEntity(
            title,
            content,
            Date(),
            imageUrlList,
            latitude,
            longitude,
            1L
        )
        memoLocalDataSource.saveMemo(memoEntity)
    }
}