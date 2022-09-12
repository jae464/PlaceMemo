package com.jae464.placememo.presentation.login

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
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun getUserInfo(uid: String) {
        viewModelScope.launch {
            val userInfo = loginRepository.getUserInfo(uid)
            Log.d("LoginViewModel", userInfo.toString())
            _user.postValue(userInfo)
        }
    }

    fun setUserInfo(user: User) {
        viewModelScope.launch {
            loginRepository.setUserInfo(user)
            Log.d("LoginViewModel", "setUser 완료")
        }
    }
}