package com.jae464.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.domain.model.post.toAddressFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: com.jae464.domain.repository.MemoRepository,
    private val addressRepository: com.jae464.domain.repository.AddressRepository
): ViewModel() {
    private val _memoList: MutableLiveData<List<com.jae464.domain.model.post.Memo>> by lazy { MutableLiveData<List<com.jae464.domain.model.post.Memo>>() }
    val memoList: LiveData<List<com.jae464.domain.model.post.Memo>> = _memoList

    private val _currentAddress: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val currentAddress: LiveData<String> = _currentAddress

    fun getAllMemo() {
        viewModelScope.launch {
            _memoList.postValue(repository.getAllMemo())
        }
    }

    fun getAddressName(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val region = addressRepository.getAddress(longitude, latitude)
            _currentAddress.postValue(region?.toAddressFormat())
        }
    }

    fun getMemoByCategory(category: com.jae464.domain.model.post.Category) {
        viewModelScope.launch {
            val temp = repository.getMemoByCategory(category.ordinal)
            _memoList.postValue(temp)
        }
    }

    fun getMemoImagePathList(memoId: Long): List<String> {
        return repository.getImagePathList(memoId)
    }

}