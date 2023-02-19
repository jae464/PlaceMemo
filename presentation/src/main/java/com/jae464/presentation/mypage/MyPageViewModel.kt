package com.jae464.presentation.mypage

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val loginRepository: com.jae464.domain.repository.LoginRepository
): ViewModel() {
    private val _user = MutableLiveData<com.jae464.domain.model.login.User?>()
    val user: LiveData<com.jae464.domain.model.login.User?> get() = _user

    fun getUser(uid: String) {
        viewModelScope.launch {
            val user = loginRepository.getUserInfoByUid(uid)
            Log.d("MyPageViewModel", user.toString())
            _user.postValue(user)
        }
    }

    // image 만 필요함
    fun updateUserProfileImage(image: Bitmap) {
        viewModelScope.launch {
//            loginRepository.updateUserProfileImage(user.value!!.uid, image)
        }
    }

}