package com.jae464.placememo.data.mapper

import com.jae464.placememo.data.model.MemoEntity
import com.jae464.placememo.data.model.Region
import com.jae464.placememo.domain.model.post.Memo
import java.util.*

fun memoToMemoEntity(memo: Memo): MemoEntity {
    return MemoEntity(
        memo.title,
        memo.content,
        Date(),
        memo.latitude,
        memo.longitude,
        Region(memo.area1, memo.area2, memo.area3)
    )
}

fun memoEntityToMemo(memoEntity: MemoEntity): Memo {
    return Memo(
        memoEntity.id,
        memoEntity.title,
        memoEntity.content,
        memoEntity.latitude,
        memoEntity.longitude,
        memoEntity.region?.area1.toString(),
        memoEntity.region?.area2.toString(),
        memoEntity.region?.area3.toString()
    )
}