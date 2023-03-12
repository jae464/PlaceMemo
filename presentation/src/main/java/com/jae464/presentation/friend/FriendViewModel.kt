package com.jae464.presentation.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val loginRepository: com.jae464.domain.repository.LoginRepository
) : ViewModel() {

    fun getUserByNickname(nickname: String) {
        viewModelScope.launch {
            val user = loginRepository.getUserInfoByNickname(nickname)
        }
    }
}