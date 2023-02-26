package com.jae464.data.repository.login.remote

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jae464.data.model.UserEntity
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : LoginRemoteDataSource {

    private val storage = Firebase.storage
    val TAG = "LoginRemoteDataSourceImpl"

    override suspend fun getUserInfoByUid(uid: String): UserEntity? {
        var userInfo: UserEntity? = null
        val userRef = firestore.collection("users")
            .document(uid)

        userRef.get()
            .addOnSuccessListener {
                if (it.data != null) {
                    userInfo = UserEntity(
                        it.data?.get("uid").toString(),
                        it.data?.get("email").toString(),
                        it.data?.get("nickname").toString(),
                        it.data?.get("imageUrl").toString()
                    )
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "유저 정보를 가져오는데 실패했습니다.")
            }
            .await()

        Log.d("LoginRemoteDataSourceImpl", "$userInfo")
        return userInfo
    }

    override suspend fun getUserInfoByNickname(nickname: String): UserEntity? {
        val userRef = firestore.collection("users")
        val query = userRef.whereEqualTo("nickname", nickname)
        var userEntity: UserEntity? = null

        query.get()
            .addOnSuccessListener {
                // 가입할 때 닉네임 중복 확인하므로 하나여야만 한다.
                if (it.documents.size != 1) {
                    Log.e(TAG, "발견된 닉네임이 없거나 2개 이상입니다.")
                    return@addOnSuccessListener
                }

                userEntity = it.documents[0].toObject<UserEntity>()
            }
            .addOnFailureListener {
                Log.e(TAG, "닉네임으로 유저 정보를 가져오는데 실패했습니다.")
            }
            .await()

        return userEntity
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

    override suspend fun updateUserProfileImage(uid: String, image: Bitmap) {
        val userDoc = firestore.collection("users")
            .document(uid)

        val profilesRef = storage.reference.child("profiles")
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val data = baos.toByteArray()
        val profileRef = profilesRef.child("${uid}.jpg")
        val uploadTask = profileRef.putBytes(data)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            profileRef.downloadUrl
        }.addOnSuccessListener {
            userDoc.update("imageUrl", it.toString())
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener { e -> Log.e(TAG, "Error", e) }
        }.addOnFailureListener {
            Log.e(TAG, "Fire Storage 에 이미지 업로드를 실패했습니다.")
        }

    }

}