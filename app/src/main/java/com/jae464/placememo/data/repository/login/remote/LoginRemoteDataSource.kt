package com.jae464.placememo.data.repository.login.remote

import com.jae464.placememo.data.model.UserEntity

/**
 * FirebaseAuth 로그인, FireStore에 유저 정보 저장 등
 * */
interface LoginRemoteDataSource {
    suspend fun getUserInfoWithUid(uid: String)
    suspend fun setUserInfo(user: UserEntity)
}