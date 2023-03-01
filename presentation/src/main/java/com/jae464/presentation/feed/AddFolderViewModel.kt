package com.jae464.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.domain.model.feed.Folder
import com.jae464.domain.repository.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFolderViewModel @Inject constructor(
    private val folderRepository: FolderRepository
): ViewModel() {
    fun createFolder(folder: Folder) {
        viewModelScope.launch {
            folderRepository.createFolder(folder)
        }
    }
}