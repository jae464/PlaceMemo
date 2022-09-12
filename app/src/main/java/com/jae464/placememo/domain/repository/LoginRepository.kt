package com.jae464.placememo.domain.repository

import com.jae464.placememo.domain.model.login.User

interface LoginRepository {
    suspend fun setUserInfo(user: User)
    fun doLogout()
    suspend fun getUserInfo(uid: String): User?
}