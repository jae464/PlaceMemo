package com.jae464.data.repository.login

import android.util.Log
import com.jae464.data.mapper.userEntityToUser
import com.jae464.data.mapper.userToUserEntity
import com.jae464.data.model.toUser
import com.jae464.data.repository.login.remote.LoginRemoteDataSource
import com.jae464.domain.model.login.User
import com.jae464.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource
): LoginRepository {
    override suspend fun setUserInfo(user: User) {
        loginRemoteDataSource.setUserInfo(userToUserEntity(user))
    }

    override fun doLogout() {
        TODO("Not yet implemented")
    }

    override suspend fun getUserInfoByUid(uid: String): User? {
        return loginRemoteDataSource.getUserInfoByUid(uid)?.toUser()
    }

    override suspend fun getUserInfoByNickname(nickname: String): User? {
        val user = loginRemoteDataSource.getUserInfoByNickname(nickname)?.toUser()
        Log.d("LoginRepositoryImpl", user.toString())
        return user
    }

    override suspend fun checkNicknameAvailable(nickname: String): Boolean {
        return loginRemoteDataSource.checkNicknameAvailable(nickname)
    }

    override suspend fun updateUserProfileImage(uid: String, imagePath: String) {
        TODO("Not yet implemented")
    }

//    override suspend fun updateUserProfileImage(uid: String, image: Bitmap) {
//        loginRemoteDataSource.updateUserProfileImage(uid, image)
//    }
}