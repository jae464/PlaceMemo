package com.jae464.placememo.presentation.search

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
class SearchViewModel @Inject constructor(
    private val repository: MemoRepository
) : ViewModel() {

    val TAG = "SearchViewModel"
    private val _memoList = MutableLiveData<List<Memo>>()
    val memoList: LiveData<List<Memo>> get() = _memoList

    fun getMemoByTitle(title: String) {
        viewModelScope.launch {

            _memoList.postValue(repository.getMemoByTitle(title))

        }
    }
}