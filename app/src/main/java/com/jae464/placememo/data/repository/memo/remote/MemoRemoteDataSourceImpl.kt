package com.jae464.placememo.data.repository.memo.remote

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jae464.placememo.data.dto.MemoDTO
import com.jae464.placememo.data.model.MemoEntity
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class MemoRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MemoRemoteDataSource {

    private val TAG = "MemoRemoteDataSourceImpl"
    private val storage = Firebase.storage

    override suspend fun getMemo(id: Long) {

    }

    override suspend fun getAllMemoByUser(uid: String): List<MemoDTO> {
        val result = mutableListOf<MemoDTO>()

        Log.d(TAG, uid)
        val memoDoc = firestore.collection("memos")
            .whereEqualTo("userId", uid)


        memoDoc.get().addOnSuccessListener {
            Log.d(TAG, "총 메모 개수 : ${it.documents.size}")
//            Log.d(TAG, it.documents.toString())
            it.documents.forEach {

            }

        }.await()

        return result

    }

    override suspend fun insertMemo(memo: MemoDTO) {
        val memoDoc = firestore.collection("memos")
            .document()
        memoDoc.set(memo)
    }

    override suspend fun saveImageOnRemote(imageList: List<Bitmap>, imageUriList: List<String>) {
        val imagesRef = storage.reference.child("images")
        for (index in imageUriList.indices) {

            val baos = ByteArrayOutputStream()
            val image = imageList[index]
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            val data = baos.toByteArray()
            val imageRef = imagesRef.child(imageUriList[index])
            val uploadTask = imageRef.putBytes(data)

            uploadTask.addOnSuccessListener {
                Log.d(TAG, it.toString())
            }.addOnFailureListener {
                Log.e(TAG, "Fire Storage 에 이미지 업로드를 실패했습니다.")
            }

        }
    }
}