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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val memoRepository: com.jae464.domain.repository.MemoRepository,
    private val addressRepository: com.jae464.domain.repository.AddressRepository
) : ViewModel() {
//    private val _memoList: MutableLiveData<List<com.jae464.domain.model.post.Memo>> by lazy { MutableLiveData<List<com.jae464.domain.model.post.Memo>>() }
//    val memoList: LiveData<List<com.jae464.domain.model.post.Memo>> = _memoList

    private val _currentAddress: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val currentAddress: LiveData<String> = _currentAddress

    private val memoList = memoRepository.getAllMemo().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    val filteredMemoList = MutableStateFlow<List<Memo>>(emptyList())

    fun getAddressName(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val region = addressRepository.getAddress(longitude, latitude)
            _currentAddress.postValue(region?.toAddressFormat())
        }
    }

    fun getMemoByCategory(category: Category) {
        viewModelScope.launch {
            memoList.collectLatest {
                if (category == Category.ALL) {
                    filteredMemoList.value = it
                } else {
                    filteredMemoList.value = it.filter { memo ->
                        memo.category == category
                    }
                }
            }
        }
    }

    fun getMemoImagePathList(memoId: Int): List<String> {
        return memoRepository.getImagePathList(memoId)
    }

}