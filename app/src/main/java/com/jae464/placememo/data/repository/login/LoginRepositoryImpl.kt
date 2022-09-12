package com.jae464.placememo.data.repository.login

import com.jae464.placememo.data.repository.login.remote.LoginRemoteDataSource
import com.jae464.placememo.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource
): LoginRepository {
    override fun set() {
        TODO("Not yet implemented")
    }

    override fun doLogout() {
        TODO("Not yet implemented")
    }

    override suspend fun getUserInfo(uid: String) {
        loginRemoteDataSource.getUserInfoWithUid(uid)
    }
}