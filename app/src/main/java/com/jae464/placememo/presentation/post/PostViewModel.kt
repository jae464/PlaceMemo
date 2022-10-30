package com.jae464.placememo.presentation.post

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jae464.placememo.data.api.response.RegionResponse
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.domain.repository.AddressRepository
import com.jae464.placememo.domain.repository.LoginRepository
import com.jae464.placememo.domain.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: MemoRepository,
    private val addressRepository: AddressRepository,
    private val loginRepository: LoginRepository
) : ViewModel() {

    val TAG = "PostViewModel"

    private val _imageList = MutableLiveData<List<Bitmap>>()
    val imageList: LiveData<List<Bitmap>> get() = _imageList

    private val _address = MutableLiveData<RegionResponse?>()
    val address: LiveData<RegionResponse?> get() = _address

    private var memoId: Long = -1
    private var user = FirebaseAuth.getInstance().currentUser

    private val imageFileNameList = mutableListOf<String>()

    @SuppressLint("SimpleDateFormat")
    fun saveMemo(id: Long, title: String, content: String, latitude: Double, longitude: Double, category: Int, imageUriList: List<Uri>) {
        viewModelScope.launch {
            val region = _address.value

            for (i in imageUriList.indices) {
                Log.d(TAG, i.toString())
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "${user?.uid}_${timeStamp}_${i}.jpg"
                imageFileNameList.add(imageFileName)
            }

            val memo = Memo(
                id, title, content, latitude, longitude,
                category,
                region?.area1?.name ?: "",
                region?.area2?.name ?: "",
                region?.area3?.name ?: "",
                imageFileNameList
            )

            println("저장 전 내용 확인합니다.")
            println("$id $title $content $latitude $longitude")
            println(imageList)
            memoId = repository.saveMemo(memo)
            saveImage(memoId)
            Log.d("PostViewModel", user.toString())
            println(user)

            // 로그인이 되어있을 시, 파이어베이스 스토어에 저장
            if (user != null) {
                Log.d(TAG, user.toString())
                repository.saveMemoOnRemote(user?.uid.toString(), memo)
            }
        }
    }

    fun setImageList(image: Bitmap) {
        val addedImageList = (_imageList.value ?: emptyList()) + listOf(image)
        _imageList.postValue(addedImageList)
    }

    fun saveImage(memoId: Long) {
        val saveImageList = (_imageList.value ?: emptyList())
        Log.d(TAG, "saveImage function")
        if (saveImageList.isEmpty()) return
        repository.saveImage(saveImageList, memoId)

        // 네트워크 연결되어 있을 시 FireBase Store 에 저장
        saveImageOnRemote(saveImageList)
    }

    fun getAddress(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val address = addressRepository.getAddress(longitude, latitude)
            _address.postValue(address)
        }
    }

    fun getAddressName(region: RegionResponse?): String {
        return addressRepository.addressToString(region)
    }

    private fun saveImageOnRemote(saveImageList: List<Bitmap>) {
        viewModelScope.launch {
            Log.d(TAG, imageFileNameList.toString())
            repository.saveImageOnRemote(saveImageList, imageFileNameList)
        }
    }
}