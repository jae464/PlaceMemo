package com.jae464.placememo.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.domain.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMemoViewModel @Inject constructor(
    private val memoRepository: MemoRepository
): ViewModel() {

    private val TAG = "DetailMemoViewModel"
    private val _memo = MutableLiveData<Memo>()
    val memo: LiveData<Memo> get() = _memo

    private val _isDone = MutableLiveData<Boolean>()
    val isDone: LiveData<Boolean> get() = _isDone

    fun getMemo(id: Long) {
        viewModelScope.launch {
           _memo.postValue(memoRepository.getMemo(id))
        }
    }

    fun deleteMemo(id: Long) {
        viewModelScope.launch {
            memoRepository.deleteMemo(id)
        }
    }

    fun deleteMemoOnRemote(userId: String?, memoId: Long) {
        if (userId == null) {
            _isDone.postValue(true)
            return
        }

        viewModelScope.launch {
            memoRepository.deleteMemoOnRemote(userId, memoId)
            _isDone.postValue(true)
        }
    }

}
