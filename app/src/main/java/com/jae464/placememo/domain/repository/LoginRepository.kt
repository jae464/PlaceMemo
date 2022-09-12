package com.jae464.placememo.domain.repository

interface LoginRepository {
    fun set()
    fun doLogout()
    suspend fun getUserInfo(uid: String)
}