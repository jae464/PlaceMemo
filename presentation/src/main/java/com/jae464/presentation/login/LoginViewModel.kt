package com.jae464.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: com.jae464.domain.repository.LoginRepository
): ViewModel() {
    private val _user = MutableLiveData<com.jae464.domain.model.login.User?>()
    val user: LiveData<com.jae464.domain.model.login.User?> get() = _user

    private val _nicknameCheck = MutableLiveData<Boolean>()
    val nicknameCheck: LiveData<Boolean> get() = _nicknameCheck

    fun getUserInfo(uid: String) {
        viewModelScope.launch {
            val userInfo = loginRepository.getUserInfoByUid(uid)
            Log.d("LoginViewModel", userInfo.toString())
            _user.postValue(userInfo)
        }
    }

    fun setUserInfo(user: com.jae464.domain.model.login.User) {
        viewModelScope.launch {
            loginRepository.setUserInfo(user)
            Log.d("LoginViewModel", "setUser 완료")
        }
    }

    fun checkNicknameAvailable(nickname: String) {
        viewModelScope.launch {
            _nicknameCheck.postValue(loginRepository.checkNicknameAvailable(nickname))
        }
    }
}