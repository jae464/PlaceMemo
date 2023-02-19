package com.jae464.data.mapper

import com.jae464.data.model.MemoEntity
import com.jae464.data.model.Region
import com.jae464.data.model.UserEntity
import com.jae464.domain.model.login.User
import com.jae464.domain.model.post.Category
import com.jae464.domain.model.post.Memo
import java.util.*

fun memoToMemoEntity(memo: Memo): MemoEntity {
    return MemoEntity(
        memo.title,
        memo.content,
        Date(),
        memo.latitude,
        memo.longitude,
        memo.category.ordinal,
        Region(memo.area1, memo.area2, memo.area3),
        memo.id
    )
}

fun memoEntityToMemo(memoEntity: MemoEntity): Memo {
    return Memo(
        memoEntity.id,
        memoEntity.title,
        memoEntity.content,
        memoEntity.latitude,
        memoEntity.longitude,
        intToCategory(memoEntity.category),
        memoEntity.region?.area1.toString(),
        memoEntity.region?.area2.toString(),
        memoEntity.region?.area3.toString()
    )
}

fun userToUserEntity(user: User): UserEntity {
    return UserEntity(
        user.uid,
        user.email,
        user.nickname,
        user.imageUrl
    )
}

fun userEntityToUser(userEntity: UserEntity?): User? {
    if (userEntity == null) return null
    return User(
        userEntity.uid,
        userEntity.email,
        userEntity.nickname,
        userEntity.imageUrl
    )
}

fun categoryToInt(category: Category): Int {
    return when (category) {
        Category.RESTAURANT -> 0
        Category.TOURIST -> 1
        Category.CAFE -> 2
        Category.HOTEL -> 3
        Category.OTHER -> 4
    }
}

fun intToCategory(index: Int): Category {
    return when (index) {
        0 -> Category.RESTAURANT
        1 -> Category.TOURIST
        2 -> Category.CAFE
        3 -> Category.HOTEL
        4 -> Category.OTHER
        else -> Category.OTHER
    }
}