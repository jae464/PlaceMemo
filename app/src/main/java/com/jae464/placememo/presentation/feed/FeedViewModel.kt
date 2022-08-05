package com.jae464.placememo.presentation.feed

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
class FeedViewModel @Inject constructor(
    private val repository: MemoRepository
): ViewModel() {

    private val _memoList: MutableLiveData<List<Memo>> by lazy { MutableLiveData<List<Memo>>() }
    val memoList: LiveData<List<Memo>> = _memoList


    private val _memo = MutableLiveData<Memo?>()
    val memo: LiveData<Memo?> get() = _memo

    fun getAllMemo() {
        viewModelScope.launch {
            _memoList.postValue(repository.getAllMemo())
        }
    }

    fun getMemo(id: Long) {
        viewModelScope.launch {
            _memo.postValue(repository.getMemo(id))
        }
    }

    fun resetMemo() {
        _memo.value = null
    }


}