package com.jae464.domain.repository

import com.jae464.domain.model.login.User

interface LoginRepository {
    // Login
    suspend fun setUserInfo(user: User)
    fun doLogout()

    // Get User Info
    suspend fun getUserInfoByUid(uid: String): User?
    suspend fun getUserInfoByNickname(nickname: String): User?

    suspend fun checkNicknameAvailable(nickname: String): Boolean
//    suspend fun updateUserProfileImage(uid: String, image: Bitmap)
}