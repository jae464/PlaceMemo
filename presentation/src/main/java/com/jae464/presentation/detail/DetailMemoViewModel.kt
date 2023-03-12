package com.jae464.presentation.detail

import android.util.Log
import androidx.lifecycle.*
import com.jae464.domain.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMemoViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val TAG = "DetailMemoViewModel"
//    private val _memo = MutableLiveData<com.jae464.domain.model.post.Memo>()
//    val memo: LiveData<com.jae464.domain.model.post.Memo> get() = _memo

    private val _isDone = MutableLiveData<Boolean>()
    val isDone: LiveData<Boolean> get() = _isDone

    val memo = memoRepository.getMemo(savedStateHandle.get<Int>("memoId") ?: -1)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        Log.d(TAG, savedStateHandle.get<Int>("memoId").toString())
    }
    fun getMemo(id: Int) {
        viewModelScope.launch {
//           _memo.postValue(memoRepository.getMemo(id))
        }
    }

    fun deleteMemo(id: Int) {
        viewModelScope.launch {
            memoRepository.deleteMemo(id)
        }
    }

    fun deleteMemoOnRemote(userId: String?, memoId: Int) {
        if (userId == null) {
            _isDone.postValue(true)
            return
        }

        viewModelScope.launch {
            memoRepository.deleteMemoOnRemote(userId, memoId)
            _isDone.postValue(true)
        }
    }

    fun getMemoImagePathList(memoId: Int): List<String> {
        return memoRepository.getImagePathList(memoId)
    }

}
