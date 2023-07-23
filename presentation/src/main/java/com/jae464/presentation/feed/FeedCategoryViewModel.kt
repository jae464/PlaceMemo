package com.jae464.presentation.feed

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jae464.domain.model.SortBy
import com.jae464.domain.model.post.Category
import com.jae464.domain.model.post.Memo
import com.jae464.domain.repository.CategoryRepository
import com.jae464.domain.repository.MemoRepository
import com.jae464.presentation.model.ViewType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class FeedCategoryViewModel @Inject constructor(
    private val repository: MemoRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val selectedCategory = MutableStateFlow<Category?>(null)
    private val sortBy = MutableStateFlow(SortBy.DESC)
    private val folderId = MutableStateFlow(-1L)
    private val _viewType = MutableStateFlow(ViewType.CARD_VIEW_TYPE)
    val viewType = _viewType.asStateFlow()

    init {
        folderId.value = savedStateHandle.get<Long>(FeedCategoryFragment.FOLDER_ID_KEY) ?: -1L
    }

    val memos = selectedCategory.flatMapLatest { selectedCategory ->
        sortBy.flatMapLatest { sortBy ->
            folderId.flatMapLatest { folderId ->
                if (folderId == -1L) { // 모든 메모를 보여주는 경우
                    if (selectedCategory == null) {
                        repository.getAllMemoWithPage(sortBy)
                    } else {
                        repository.getMemoByCategoryWithPage(selectedCategory.id, sortBy)
                    }
                } else { // Folder ID 가 있어서, 해당 폴더의 메모만 보여주는 경우
                    if (selectedCategory == null) {
                        repository.getAllMemoByFolder(folderId, sortBy)
                    } else {
                        repository.getAllMemoByFolderWithCategory(
                            folderId,
                            selectedCategory.id,
                            sortBy
                        )
                    }

                }
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

    fun setViewType(viewType: ViewType) {
        this._viewType.value = viewType
    }

    companion object {
        private const val TAG = "FeedCategoryViewModel"
    }

}