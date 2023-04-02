package com.jae464.presentation.post

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.jae464.domain.model.post.Category
import com.jae464.domain.model.post.Memo
import com.jae464.domain.model.post.Region
import com.jae464.domain.repository.AddressRepository
import com.jae464.domain.repository.CategoryRepository
import com.jae464.domain.repository.LoginRepository
import com.jae464.domain.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    private val addressmemoRepository: AddressRepository,
    private val loginmemoRepository: LoginRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val TAG = "PostViewModel"

    private val _address = MutableLiveData<Region?>()
    val address: LiveData<Region?> get() = _address

    private var user = FirebaseAuth.getInstance().currentUser

    val memo = MutableStateFlow<Memo?>(null)

    val categorys = categoryRepository.getAllCategory().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )


    init {
        Log.d(TAG, savedStateHandle.get<Int>("memoId").toString())

        // 새 메모가 아닌 경우 navigation argument 로 메모 아이디를 넘겨받은 후 메모 업데이트
        if (savedStateHandle.get<Int>("memoId") != -1) {
            viewModelScope.launch {
                memoRepository.getMemo(savedStateHandle.get<Int>("memoId") ?: 0).collectLatest {
                    memo.value = it
                }
            }
        }
    }

    private val _isDone = MutableLiveData<Boolean>()
    val isDone: LiveData<Boolean> get() = _isDone

    @SuppressLint("SimpleDateFormat")
    fun saveMemo(
        id: Int,
        title: String,
        content: String,
        latitude: Double,
        longitude: Double,
        category: Category,
        imageUriList: List<String>
    ) {
        viewModelScope.launch {
            val region = _address.value

            val memo = Memo(
                id, title, content, latitude, longitude,
                category,
                region?.area1 ?: "",
                region?.area2 ?: "",
                region?.area3 ?: "",
                imageUriList
            )

            memoRepository.saveMemo(memo)
            saveImage(imageUriList)

//            val newMemo = memo.copy(id = memoId)
//
//            // 로그인이 되어있을 시, 파이어베이스 스토어에 저장
//            if (user != null) {
//                Log.d(TAG, user.toString())
//                memoRepository.saveMemoOnRemote(user?.uid.toString(), newMemo)
//            }

            _isDone.postValue(true)

        }
    }

    fun updateMemo(title: String, content: String, category: Category, imageUriList: List<String>) {
        val beforeMemo = memo.value ?: return
        val newMemo = Memo(
            beforeMemo.id, title, content, beforeMemo.latitude, beforeMemo.longitude,
            category, beforeMemo.area1, beforeMemo.area2, beforeMemo.area3, imageUriList
        )

        Log.d(TAG, newMemo.toString())

        viewModelScope.launch {
            // Local Room update
            memoRepository.updateMemo(newMemo)
            saveImage(imageUriList)

            // Remote Firebase update
            if (user != null) {
                memoRepository.updateMemoOnRemote(user?.uid.toString(), newMemo)
                // TODO Image Remote Update
            }

            _isDone.postValue(true)
        }
    }

    private fun saveImage(imageUriList: List<String>) {

        if (imageUriList.isEmpty()) return
        viewModelScope.launch {
            Log.d(TAG, "이미지 저장 시작")
            memoRepository.saveImages(imageUriList)
            Log.d(TAG, "이미지 저장 완료")
        }

        // 네트워크 연결되어 있을 시 FireBase Store 에 저장
        saveImageOnRemote(imageUriList)
    }

    fun getAddress(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val address = addressmemoRepository.getAddress(longitude, latitude)
            _address.postValue(address)
        }
    }

    fun getAddressName(region: Region?): String {
        return addressmemoRepository.addressToString(region)
    }

    fun getMemo(memoId: Int) {
//        viewModelScope.launch {
//            val memo = memoRepository.getMemo(memoId)
//            _memo.postValue(memo)
//
////            memo.imageUriList?.forEach {
////                imagePathList.add(it)
////            }
//        }
    }

    private fun saveImageOnRemote(saveImageList: List<String>) {
        viewModelScope.launch {
//            Log.d(TAG, imagePathList.toString())
//            memoRepository.saveImageOnRemote(saveImageList, imageFileNameList)
        }
    }


}