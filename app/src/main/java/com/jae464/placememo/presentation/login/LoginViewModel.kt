package com.jae464.placememo.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.placememo.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {
    fun getUserInfo(uid: String) {

        viewModelScope.launch {
            val result = loginRepository.getUserInfo(uid)
            Log.d("LoginViewModel", result.toString())
        }

    }
}