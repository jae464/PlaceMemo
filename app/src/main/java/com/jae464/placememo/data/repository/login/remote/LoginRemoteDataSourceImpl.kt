package com.jae464.placememo.data.repository.login.remote

import android.content.ContentValues.TAG
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
                if (it.data != null) {
                    userInfo = UserEntity(
                        it.data?.get("uid").toString(),
                        it.data?.get("email").toString(),
                        it.data?.get("nickname").toString()
                    )
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

    override suspend fun checkNicknameAvailable(nickname: String): Boolean {
        val userRef = firestore.collection("users")
        val query = userRef.whereEqualTo("nickname", nickname)
        var result = true
        query.get()
            .addOnSuccessListener { documents ->
                Log.d(TAG, documents.isEmpty.toString())
                if (!documents.isEmpty) {
                    result = false
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "닉네임 조회에 실패했습니다.")
            }
            .await()
        return result
    }

}