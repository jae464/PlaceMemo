package com.jae464.presentation.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jae464.domain.model.feed.Folder
import com.jae464.domain.repository.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: com.jae464.domain.repository.MemoRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {

    val viewType = MutableStateFlow("card")
    @OptIn(ExperimentalCoroutinesApi::class)
    val memoList = viewType
        .filter { it.isNotEmpty() }
        .flatMapLatest { viewType ->
            Log.d("FeedViewModel", "GetAllMemoWithPage")
            repository.getAllMemoWithPage().cachedIn(viewModelScope)
        }

//    val memoList = repository.getAllMemoWithPage().cachedIn(viewModelScope)
//        .stateIn(viewModelScope, SharingStarted.Eagerly, PagingData.empty())

    val folderList = folderRepository.getAllFolder()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


    fun getAllMemoByUser(uid: String) {
        viewModelScope.launch {
            val remoteMemo = repository.getAllMemoByUserOnRemote(uid)
            Log.d("FeedViewModel", "remote memo : $remoteMemo")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("FeedViewModel", "onCleared")
    }
}