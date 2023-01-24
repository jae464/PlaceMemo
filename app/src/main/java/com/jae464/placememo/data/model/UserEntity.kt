package com.jae464.placememo.data.model

import com.jae464.placememo.domain.model.login.User


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
