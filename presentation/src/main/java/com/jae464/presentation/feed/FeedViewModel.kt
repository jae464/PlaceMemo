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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: com.jae464.domain.repository.MemoRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<AddFolderEvent>()
    val event = _event.asSharedFlow()

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

    fun updateFolders(folders: List<Folder>) {
        viewModelScope.launch {
            folderRepository.updateFolders(folders)
        }
    }

    fun createFolder(folder: Folder) {
        viewModelScope.launch {
            if (folder.name.isEmpty()) {
                _event.emit(AddFolderEvent.EmptyFolderName)
                return@launch
            }
            val isExist = folderRepository.isExistFolderName(folder.name)
            if (isExist) {
                _event.emit(AddFolderEvent.ExistFolderName)
                return@launch
            }
            val newOrder = folderRepository.getFolderSize()
            folderRepository.createFolder(folder.copy(order = newOrder))
            _event.emit(AddFolderEvent.CreateFolderCompleted)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("FeedViewModel", "onCleared")
    }
}

sealed class AddFolderEvent {
    data class CreateFolder(val folder: Folder): AddFolderEvent()
    data class CheckNameAvailable(val name: String): AddFolderEvent()
    object ExistFolderName: AddFolderEvent()
    object NotExistFolderName: AddFolderEvent()
    object CreateFolderCompleted: AddFolderEvent()
    object EmptyFolderName: AddFolderEvent()
}