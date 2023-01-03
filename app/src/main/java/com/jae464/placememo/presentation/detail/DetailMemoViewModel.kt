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
            Log.d(TAG,"삭제 후 지연 함수")
            delay(1000L)
            Log.d(TAG, "삭제 후 지연 함수 완료")
            _isDone.postValue(true)
        }
    }

    fun deleteMemoOnRemote(uid: String, id: Long) {
        viewModelScope.launch {
            memoRepository.deleteMemoOnRemote(uid + "_" + id.toString())
        }
    }

}
