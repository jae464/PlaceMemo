package com.jae464.placememo.presentation.mypage

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.placememo.domain.model.login.User
import com.jae464.placememo.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun getUser(uid: String) {
        viewModelScope.launch {
            val user = loginRepository.getUserInfo(uid)
            Log.d("MyPageViewModel", user.toString())
            _user.postValue(user)
        }
    }

    // image 만 필요함
    fun updateUserProfileImage(image: Bitmap) {
        viewModelScope.launch {
            loginRepository.updateUserProfileImage(user.value!!.uid, image)
        }
    }

}