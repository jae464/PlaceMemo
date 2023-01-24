package com.jae464.placememo.presentation.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.placememo.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    fun getUserByNickname(nickname: String) {
        viewModelScope.launch {
            val user = loginRepository.getUserInfoByNickname(nickname)

        }
    }
}