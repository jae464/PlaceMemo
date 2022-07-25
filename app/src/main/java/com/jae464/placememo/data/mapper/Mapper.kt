package com.jae464.placememo.data.mapper

import com.jae464.placememo.data.model.MemoEntity
import com.jae464.placememo.domain.model.post.Memo
import java.util.*

fun memoToMemoEntity(memo: Memo): MemoEntity {
    return MemoEntity(
        memo.title,
        memo.content,
        Date(),
        memo.imageUrlList,
        memo.latitude,
        memo.longitude
    )
}

fun memoEntityToMemo(memoEntity: MemoEntity): Memo {
    return Memo(
        memoEntity.title,
        memoEntity.content,
        memoEntity.imageUrlList,
        memoEntity.latitude,
        memoEntity.longitude
    )
}