package com.jae464.placememo.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

): ViewModel() {
    private val _locationSource: MutableLiveData<FusedLocationSource> by lazy { MutableLiveData<FusedLocationSource>() }
    val locationSource: LiveData<FusedLocationSource> = _locationSource

    // 클릭된 좌표
    private val _clickedLocation: MutableLiveData<LatLng> by lazy { MutableLiveData<LatLng>() }
    val clickedLocation: LiveData<LatLng> = _clickedLocation

    private val _isMapClicked = MutableLiveData(false)
    val isMapClicked: LiveData<Boolean> = _isMapClicked

    fun setMapCliekd() {
        _isMapClicked.value = true
    }

    fun setMapUnclicked() {
        _isMapClicked.value = false
    }
}