package com.jae464.placememo.data.repository.login.remote

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jae464.placememo.data.model.UserEntity

class LoginRemoteDataSourceImpl: LoginRemoteDataSource {
    private val firestore = Firebase.firestore

    override suspend fun getUserInfoWithUid(uid: String) {
        val userRef = firestore.collection("users")
            .document(uid)

        var userInfo: UserEntity? = null
        userRef.get()
            .addOnSuccessListener {
                Log.d("LoginRemoteDataSourceImpl", it.data.toString())
                userInfo?.uid = it.data?.get("uid").toString()
                userInfo?.email = it.data?.get("email").toString()
            }

        Log.d("LoginRemoteDataSourceImpl", userInfo.toString())
    }

    override suspend fun setUserInfo(user: UserEntity) {

    }
}