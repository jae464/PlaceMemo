package com.jae464.data.model

import com.jae464.domain.model.login.User

data class UserEntity(
    var uid: String = "",
    var email: String = "",
    var nickname: String = "",
    var imageUrl: String? = null,
)

internal fun UserEntity.toUser(): User {
    return User(
        uid, email, nickname, imageUrl
    )
}

internal fun User.toUserEntity(): UserEntity {
    return UserEntity(
        uid, email, nickname, imageUrl
    )
}
