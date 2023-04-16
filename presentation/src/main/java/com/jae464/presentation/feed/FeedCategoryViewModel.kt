package com.jae464.presentation.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.jae464.domain.model.feed.Folder
import com.jae464.domain.model.post.Category
import com.jae464.domain.model.post.Memo
import com.jae464.domain.repository.CategoryRepository
import com.jae464.domain.repository.FolderRepository
import com.jae464.domain.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedCategoryViewModel @Inject constructor(
    private val repository: MemoRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val viewType = MutableStateFlow("card")

    val memoList = repository.getAllMemoWithPage().cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Eagerly, PagingData.empty())

    private val selectedCategory = MutableStateFlow<Category?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val memos = selectedCategory.flatMapLatest { category ->
        if (category == null) {
            repository.getAllMemoWithPage()
        } else {
            repository.getMemoByCategoryWithPage(category.id)
        }
    }
        .cachedIn(viewModelScope)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            PagingData.empty()
        )

    val categories = categoryRepository.getAllCategory()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun setCategoryFilter(category: Category?) {
         selectedCategory.value = category
    }

}