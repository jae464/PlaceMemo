package com.jae464.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jae464.domain.model.SortBy
import com.jae464.domain.model.post.Category
import com.jae464.domain.repository.CategoryRepository
import com.jae464.domain.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FeedCategoryViewModel @Inject constructor(
    private val repository: MemoRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val selectedCategory = MutableStateFlow<Category?>(null)
    private val sortBy = MutableStateFlow(SortBy.DESC)

    @OptIn(ExperimentalCoroutinesApi::class)
    val memos = selectedCategory.flatMapLatest { category ->
        sortBy.flatMapLatest { sortBy ->
            if (category == null) {
                repository.getAllMemoWithPage(sortBy)

            } else {
                repository.getMemoByCategoryWithPage(category.id, sortBy)
            }
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

    fun setSortBy(sortBy: SortBy) {
        this.sortBy.value = sortBy
    }

}