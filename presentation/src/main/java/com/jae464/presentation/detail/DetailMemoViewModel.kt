package com.jae464.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMemoViewModel @Inject constructor(
    private val memoRepository: com.jae464.domain.repository.MemoRepository
): ViewModel() {

    private val TAG = "DetailMemoViewModel"
    private val _memo = MutableLiveData<com.jae464.domain.model.post.Memo>()
    val memo: LiveData<com.jae464.domain.model.post.Memo> get() = _memo

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

    fun getMemoImagePathList(memoId: Long): List<String> {
        return memoRepository.getImagePathList(memoId)
    }

}
