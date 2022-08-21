package com.jae464.placememo.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.domain.repository.AddressRepository
import com.jae464.placememo.domain.repository.MemoRepository
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MemoRepository,
    private val addressRepository: AddressRepository
): ViewModel() {

    private val _memoList: MutableLiveData<List<Memo>> by lazy { MutableLiveData<List<Memo>>() }
    val memoList: LiveData<List<Memo>> = _memoList

    private val _currentAddress: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val currentAddress: LiveData<String> = _currentAddress

    private val _memoAddress = MutableLiveData<String>()
    val memoAddress: LiveData<String> get() = _memoAddress
    fun getAllMemo() {
        viewModelScope.launch {
            _memoList.postValue(repository.getAllMemo())
        }
    }

    fun getAddressName(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val addressName = addressRepository.getAddress(longitude, latitude)
            _currentAddress.postValue(addressRepository.addressToString(addressName))
        }
    }

    fun getMemoAddressName(memo: Memo) {
        viewModelScope.launch {
            val addressName = addressRepository.getAddress(memo.longitude, memo.latitude)
            _memoAddress.postValue(addressRepository.addressToString(addressName))
        }
    }
}