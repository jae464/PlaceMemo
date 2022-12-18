package com.jae464.placememo.domain.model.login

data class User (
    val uid: String,
    val email: String,
    val nickname: String,
    val image: String? = null,
)