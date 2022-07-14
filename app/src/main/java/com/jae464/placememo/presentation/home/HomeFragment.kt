package com.jae464.placememo.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.jae464.placememo.R
import com.jae464.placememo.base.BaseFragment
import com.jae464.placememo.databinding.FragmentHomeBinding
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Overlay
import dagger.hilt.android.AndroidEntryPoint


class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback, Overlay.OnClickListener {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var naverMap: NaverMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("HomeFragment Created")
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
            if (isGranted) {
                println("권한 설정 완료")
                setUserLocation()
            }
            else {
                println("권한 설정이 필요합니다.")
                showPermissionToast(binding.root)
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mapView.getMapAsync(this) // map 객체 가져오기
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        println("MapReady")
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0
        setUserLocation()


    }

    override fun onClick(p0: Overlay): Boolean {
        TODO("Not yet implemented")
    }

    private fun setUserLocation() {
        if(ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("권한 설정 필요")
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        else {
            println("권한 설정 완료")
        }
    }

    private fun showPermissionToast(layout: View) {
        Toast.makeText(requireContext(),"권한설정이 필요합니다.",Toast.LENGTH_SHORT).show()
    }

}