package com.jae464.data.repository.login.remote

import android.graphics.Bitmap
import com.jae464.data.model.UserEntity

/**
 * FirebaseAuth 로그인, FireStore에 유저 정보 저장 등
 * */
interface LoginRemoteDataSource {
    suspend fun getUserInfoByUid(uid: String): UserEntity?
    suspend fun getUserInfoByNickname(nickname: String): UserEntity?
    suspend fun setUserInfo(user: UserEntity)
    suspend fun checkNicknameAvailable(nickname: String): Boolean
    suspend fun updateUserProfileImage(uid: String, image: Bitmap)
}