package com.jae464.data.mapper

import com.jae464.data.model.FolderEntity
import com.jae464.data.model.MemoEntity
import com.jae464.data.model.Region
import com.jae464.data.model.UserEntity
import com.jae464.domain.model.feed.Folder
import com.jae464.domain.model.login.User
import com.jae464.domain.model.post.Category
import com.jae464.domain.model.post.Memo
import java.util.*

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

//fun categoryToInt(category: Category): Int {
//    return when (category) {
//        Category.RESTAURANT -> 0
//        Category.TOURIST -> 1
//        Category.CAFE -> 2
//        Category.HOTEL -> 3
//        Category.OTHER -> 4
//        Category.ALL -> 5
//    }
//}
//
//fun intToCategory(index: Int): Category {
//    return when (index) {
//        0 -> Category.RESTAURANT
//        1 -> Category.TOURIST
//        2 -> Category.CAFE
//        3 -> Category.HOTEL
//        4 -> Category.OTHER
//        else -> Category.OTHER
//    }
//}

fun folderEntityToFolder(folderEntity: FolderEntity, memoCount: Int = 0): Folder {
    return Folder(
        id = folderEntity.folderId,
        name = folderEntity.folderName,
        order = folderEntity.folderOrder,
        memoCount = memoCount
    )
}