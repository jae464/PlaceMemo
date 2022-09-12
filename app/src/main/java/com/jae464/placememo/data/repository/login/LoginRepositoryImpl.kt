package com.jae464.placememo.data.repository.login

import com.jae464.placememo.data.mapper.userEntityToUser
import com.jae464.placememo.data.mapper.userToUserEntity
import com.jae464.placememo.data.repository.login.remote.LoginRemoteDataSource
import com.jae464.placememo.domain.model.login.User
import com.jae464.placememo.domain.repository.LoginRepository
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

    override suspend fun getUserInfo(uid: String): User? {
        return userEntityToUser(loginRemoteDataSource.getUserInfoWithUid(uid))
    }

    override suspend fun checkNicknameAvailable(nickname: String): Boolean {
        return loginRemoteDataSource.checkNicknameAvailable(nickname)
    }
}