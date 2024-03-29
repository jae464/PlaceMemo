package com.jae464.presentation.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.domain.model.feed.Folder
import com.jae464.domain.repository.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFolderViewModel @Inject constructor(
    private val folderRepository: FolderRepository
): ViewModel() {

    private val _event = MutableSharedFlow<AddFolderEvent>()
    val event = _event.asSharedFlow()

    fun createFolder(folder: Folder) {
        viewModelScope.launch {
            val newOrder = folderRepository.getFolderSize()
            folderRepository.createFolder(folder.copy(order = newOrder))
            _event.emit(AddFolderEvent.CreateFolderCompleted)
        }
    }

    fun checkFolderNameAvailable(folderName: String) {
        viewModelScope.launch {
            val isExist = folderRepository.isExistFolderName(folderName)
            Log.d("AddFolderViewModel", isExist.toString())
            if (isExist) {
                _event.emit(AddFolderEvent.ExistFolderName)
            }
            else {
                _event.emit(AddFolderEvent.NotExistFolderName)
            }
        }
    }
}


