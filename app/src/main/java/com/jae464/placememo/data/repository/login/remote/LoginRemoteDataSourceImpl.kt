package com.jae464.placememo.data.repository.login.remote

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jae464.placememo.data.model.UserEntity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait

class LoginRemoteDataSourceImpl : LoginRemoteDataSource {
    private val firestore = Firebase.firestore

    override suspend fun getUserInfoWithUid(uid: String): UserEntity? {
        var userInfo: UserEntity? = null
        val userRef = firestore.collection("users")
            .document(uid)

        userRef.get()
            .addOnSuccessListener {
                Log.d("LoginRemoteDataSourceImpl", it.data.toString())
                if (it.data != null) {
                    userInfo = UserEntity(it.data?.get("uid").toString(),
                    it.data?.get("email").toString())
                }
            }
            .addOnFailureListener {

            }
            .await()

        Log.d("LoginRemoteDataSourceImpl", "$userInfo")
        return userInfo

    }

    override suspend fun setUserInfo(user: UserEntity) {
        val userDoc = firestore.collection("users")
            .document(user.uid)
        userDoc.set(user)
    }
}