package com.jae464.presentation.map

import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jae464.domain.model.post.Memo

class CustomGoogleMap(listener: OnMapReadyListener)
    : BaseMap(listener), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    var currentMarker: Marker? = null
    var markers: List<Marker> = emptyList()

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        onMapReadyListener.onMapReady()
        googleMap.apply {
            setMinZoomPreference(6.0f)
            setMaxZoomPreference(16.0f)
        }
    }

    override fun setCurrentLocation(lat: Double, lng: Double) {
        val latLng = LatLng(lat, lng)
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, 16f), 1000, null
        )
    }

    override fun setOnMapClickListener(onMapClick: (Double, Double) -> (Unit)) {
        googleMap.setOnMapClickListener {
            onMapClick(it.latitude, it.longitude)
        }
    }

    override fun setCurrentMarker(lat: Double, lng: Double) {
        currentMarker?.remove()
        currentMarker = googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .title("주소")
        )
        currentMarker?.tag = "current"
        setCurrentLocation(lat, lng)
    }

    fun setOnInfoWindowClickListener(onInfoWindowClick: (Marker) -> (Unit)) {
        googleMap.setOnInfoWindowClickListener(onInfoWindowClick)
    }

    fun setOnMarkerClickListener(onMarkerClick: (Marker) -> (Boolean)) {
        googleMap.setOnMarkerClickListener { marker ->
            onMarkerClick(marker)
        }
    }

    fun setCurrentMarkerSnippet(snippet: String) {
        currentMarker?.snippet = snippet
    }

    fun showInfoWindow() {
        currentMarker?.showInfoWindow()
    }

    fun removeMarker(marker: Marker) {
        marker.remove()
    }

    fun addMemoMarker(memo: Memo, thumbnail: Bitmap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(memo.latitude, memo.longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(thumbnail))
        )
            ?.tag = memo
    }

    companion object {
        private const val TAG = "CustomGoogleMap"
    }


}