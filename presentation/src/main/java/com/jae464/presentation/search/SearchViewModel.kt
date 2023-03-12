package com.jae464.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.domain.model.post.Memo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val memoRepository: com.jae464.domain.repository.MemoRepository
) : ViewModel() {

    val TAG = "SearchViewModel"

    val searchText = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val memoList = searchText
        .filter { it.isNotEmpty() }
        .flatMapLatest { title ->
            memoRepository.getMemoByTitle(title)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    fun getMemoByTitle(title: String) {
        searchText.value = title
    }
}