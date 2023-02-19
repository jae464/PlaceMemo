package com.jae464.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: com.jae464.domain.repository.MemoRepository
) : ViewModel() {

    val TAG = "SearchViewModel"
    private val _memoList = MutableLiveData<List<com.jae464.domain.model.post.Memo>>()
    val memoList: LiveData<List<com.jae464.domain.model.post.Memo>> get() = _memoList

    fun getMemoByTitle(title: String) {
        viewModelScope.launch {

            _memoList.postValue(repository.getMemoByTitle(title))

        }
    }
}