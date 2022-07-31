package com.jae464.placememo.presentation.post

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.domain.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: MemoRepository
) : ViewModel() {

    private val _imageList = MutableLiveData<Bitmap>()
    val imageList: LiveData<Bitmap> get() = _imageList

    fun saveMemo(id: Long, title: String, content: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val memo = Memo(id, title, content, imageList.value, latitude, longitude)
            println("저장 전 내용 확인합니다.")
            println("$id $title $content $latitude $longitude")
            println(imageList)
            repository.saveMemo(memo)
        }
    }

    fun setImageList(image: Bitmap) {
        _imageList.postValue(image)
    }
}