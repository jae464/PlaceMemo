package com.jae464.presentation.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.jae464.presentation.R

abstract class BaseMapFragment<T: ViewDataBinding>(@LayoutRes val layoutRes: Int): Fragment(), OnMapReadyCallback {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    private lateinit var  locationManager: LocationManager
    lateinit var map: GoogleMap
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("BaseFragment onCreate")
        // 권한 요청하기
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted: Boolean ->
            if (isGranted) {
                setUserLocation()
            }
            else {
                showPermissionSnackBar(binding.root)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        (childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment)
            .getMapAsync(this)
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return binding.root
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMapToolbarEnabled = false
    }

    fun setUserLocation() {
        if(ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한 설정 필요
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5f, object: LocationListener {
                override fun onLocationChanged(location: Location) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude),16f), 1000, null)
                }

            })
            return
        }
    }

    private fun showPermissionSnackBar(layout: View) {
        Snackbar.make(layout, "위치 권한 설정이 필요합니다.", Snackbar.LENGTH_INDEFINITE).apply {
            setAction("권한 설정") {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.show()
        }
    }

    fun getLocation(): Location? {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }

    fun setCurrentLocation() {
        val locationListener = LocationListener {location ->
            Log.d("BaseMapFragment", "Location Changed to $location")
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude),16f), 1000, null)
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5f, locationListener)
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}