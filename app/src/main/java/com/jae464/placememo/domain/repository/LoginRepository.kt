package com.jae464.placememo.domain.repository

import android.graphics.Bitmap
import com.jae464.placememo.domain.model.login.User

interface LoginRepository {
    suspend fun setUserInfo(user: User)
    fun doLogout()
    suspend fun getUserInfo(uid: String): User?
    suspend fun checkNicknameAvailable(nickname: String): Boolean
    suspend fun updateUserProfileImage(uid: String, image: Bitmap)
}