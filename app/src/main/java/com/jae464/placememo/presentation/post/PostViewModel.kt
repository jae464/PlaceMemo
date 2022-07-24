package com.jae464.placememo.presentation.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.domain.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: MemoRepository
) : ViewModel() {
    fun saveMemo(title: String, content: String, imageUrlList: List<String>,
                 latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repository.saveMemo(title, content, imageUrlList, latitude, longitude)
        }
    }
}