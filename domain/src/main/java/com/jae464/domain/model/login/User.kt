package com.jae464.domain.model.login

data class User (
    val uid: String,
    val email: String,
    val nickname: String,
    val imageUrl: String? = null,
)