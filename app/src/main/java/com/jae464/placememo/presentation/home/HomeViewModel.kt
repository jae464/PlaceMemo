package com.jae464.placememo.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

): ViewModel() {
    private val _locationSource: MutableLiveData<FusedLocationSource> by lazy { MutableLiveData<FusedLocationSource>() }
    val locationSource: LiveData<FusedLocationSource> = _locationSource

    fun moveToUserLocation() {

    }
}