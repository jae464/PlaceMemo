package com.jae464.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.jae464.domain.model.post.Category
import com.jae464.domain.model.post.Memo
import com.jae464.domain.model.post.toAddressFormat
import com.jae464.domain.repository.AddressRepository
import com.jae464.domain.repository.CategoryRepository
import com.jae464.domain.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    private val addressRepository: AddressRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _currentAddress: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val currentAddress: LiveData<String> = _currentAddress

    val filteredMemoList = MutableStateFlow<List<Memo>>(emptyList())

    val memoList = memoRepository.getAllMemo().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    val categories = categoryRepository.getAllCategory().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    fun getAllMemo() {
       filteredMemoList.value = memoList.value
    }

    fun getAddressName(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val region = addressRepository.getAddress(longitude, latitude)
            _currentAddress.postValue(region?.toAddressFormat())
        }
    }

    fun getMemoByCategory(category: Category) {
        filteredMemoList.value = memoList.value.filter { memo ->
            memo.category == category
        }
    }

}