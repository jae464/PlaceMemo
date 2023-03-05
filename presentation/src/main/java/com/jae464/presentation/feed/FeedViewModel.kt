package com.jae464.presentation.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.domain.model.feed.Folder
import com.jae464.domain.repository.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: com.jae464.domain.repository.MemoRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val _memoList: MutableLiveData<List<com.jae464.domain.model.post.Memo>> by lazy { MutableLiveData<List<com.jae464.domain.model.post.Memo>>() }
    val memoList: LiveData<List<com.jae464.domain.model.post.Memo>> = _memoList

    private val _memo = MutableLiveData<com.jae464.domain.model.post.Memo?>()
    val memo: LiveData<com.jae464.domain.model.post.Memo?> get() = _memo

    val folderList = folderRepository.getAllFolder().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun getAllMemo() {
        viewModelScope.launch {
            _memoList.postValue(repository.getAllMemo())
        }
    }

    fun getAllMemoByUser(uid: String) {
        viewModelScope.launch {
            val remoteMemo = repository.getAllMemoByUserOnRemote(uid)
            Log.d("FeedViewModel", "remote memo : $remoteMemo")
        }
    }

    fun clearMemo() {
        _memoList.postValue(emptyList())
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("FeedViewModel", "onCleared")
    }
}