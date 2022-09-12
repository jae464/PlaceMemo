package com.jae464.placememo.data.mapper

import com.jae464.placememo.data.model.MemoEntity
import com.jae464.placememo.data.model.Region
import com.jae464.placememo.data.model.UserEntity
import com.jae464.placememo.domain.model.login.User
import com.jae464.placememo.domain.model.post.Memo
import java.util.*

fun memoToMemoEntity(memo: Memo): MemoEntity {
    return MemoEntity(
        memo.title,
        memo.content,
        Date(),
        memo.latitude,
        memo.longitude,
        memo.category,
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
        memoEntity.category,
        memoEntity.region?.area1.toString(),
        memoEntity.region?.area2.toString(),
        memoEntity.region?.area3.toString()
    )
}

fun userToUserEntity(user: User): UserEntity {
    return UserEntity(
        user.uid,
        user.email
    )
}

fun userEntityToUser(userEntity: UserEntity?): User? {
    if (userEntity == null) return null
    return User(
        userEntity.uid,
        userEntity.email
    )
}