package com.jae464.placememo.data.repository.login.remote

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jae464.placememo.data.model.UserEntity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : LoginRemoteDataSource {

    private val storage = Firebase.storage

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
                        it.data?.get("nickname").toString(),
                        it.data?.get("imageUrl").toString()
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

    override suspend fun updateUserProfileImage(uid: String, image: Bitmap) {
        // TODO uid 에 해당되는 유저 가져오기
        val userDoc = firestore.collection("users")
            .document(uid)

        // TODO 해당 User 의 profileImage 속성 값에 프로필 이미지 링크 저장
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