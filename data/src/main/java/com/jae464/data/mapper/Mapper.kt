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

fun folderEntityToFolder(folderEntity: FolderEntity, memoCount: Int = 0): Folder {
    return Folder(
        id = folderEntity.folderId,
        name = folderEntity.folderName,
        order = folderEntity.folderOrder,
        isDefault = folderEntity.isDefault,
        memoCount = memoCount
    )
}