package com.jae464.placememo.presentation.base

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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.jae464.placememo.R

abstract class BaseMapFragment<T: ViewDataBinding>(@LayoutRes val layoutRes: Int): Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, LocationListener {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    lateinit var map: GoogleMap
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("HomeFragment onCreate")
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.apply {
            setMinZoomPreference(6.0f)
            setMaxZoomPreference(16.0f)
        }
        map.setOnMyLocationButtonClickListener(this)
        setUserLocation()
    }

    private fun setUserLocation() {
        if(ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한 설정 필요
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        else {
            // 사용자 위치 이동 버튼 활성화
            map.uiSettings.isMyLocationButtonEnabled = true
            map.isMyLocationEnabled = true
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

    override fun onMyLocationButtonClick(): Boolean {
        println("on my location Button click")
        val currentLocation = getLocation()
        Log.d("BaseMapFragment", "${currentLocation?.latitude.toString()} ${currentLocation?.longitude.toString()}")

//        var locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            println("location매니저에 위치정보를 요청합니다.")
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5f, this)
//        } else {
//            Toast.makeText(context, "GPS를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show()
//        }

        return false
    }

    private fun getLocation(): Location? {
        var locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5f, this)
        var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        println("${location?.latitude} ${location?.longitude}")
        return location
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onLocationChanged(p0: Location) {
        println("Location Changed to $p0")
    }
}