package com.jae464.placememo.data.model

data class UserEntity(
    var uid: String,
    var email: String,
    var nickname: String,
    var imageUrl: String? = null,
)
