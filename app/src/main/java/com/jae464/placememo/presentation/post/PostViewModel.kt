package com.jae464.placememo.presentation.post

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.placememo.data.api.response.RegionResponse
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.domain.repository.AddressRepository
import com.jae464.placememo.domain.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: MemoRepository,
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _imageList = MutableLiveData<List<Bitmap>>()
    val imageList: LiveData<List<Bitmap>> get() = _imageList

    private val _address = MutableLiveData<RegionResponse?>()
    val address: LiveData<RegionResponse?> get() = _address

    private val memoId = MutableLiveData<Long>()

    fun saveMemo(id: Long, title: String, content: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val region = _address.value

            val memo = Memo(
                id, title, content, latitude, longitude,
                region?.area1?.name ?: "",
                region?.area2?.name ?: "",
                region?.area3?.name ?: ""
            )
            println("저장 전 내용 확인합니다.")
            println("$id $title $content $latitude $longitude")
            println(imageList)
            memoId.value = repository.saveMemo(memo)
            saveImage(memoId.value!!)
        }
    }

    fun setImageList(image: Bitmap) {
        val addedImageList = (_imageList.value ?: emptyList()) + listOf(image)
        _imageList.postValue(addedImageList)
    }

    fun saveImage(memoId: Long) {
        val saveImageList = (_imageList.value ?: emptyList())
        if (saveImageList.isEmpty()) return
        repository.saveImage(saveImageList, memoId)
    }

    fun getAddress(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val address = addressRepository.getAddress(longitude, latitude)
            _address.postValue(address)
        }
    }

    fun getAddressName(region: RegionResponse?): String {
        return addressRepository.addressToString(region)
    }
}