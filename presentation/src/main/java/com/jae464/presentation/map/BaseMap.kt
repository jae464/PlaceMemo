package com.jae464.presentation.map

import com.google.type.LatLng

abstract class BaseMap(
    val onMapReadyListener: OnMapReadyListener
) {
    abstract fun setCurrentLocation(lat: Double, lng: Double)
    abstract fun setOnMapClickListener(onMapClick: (Double, Double) -> Unit)
    abstract fun setCurrentMarker(lat: Double, lng: Double)

}
