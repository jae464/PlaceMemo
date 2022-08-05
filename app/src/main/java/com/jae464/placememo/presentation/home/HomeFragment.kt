package com.jae464.placememo.presentation.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.jae464.placememo.R
import com.jae464.placememo.data.manager.ImageManager
import com.jae464.placememo.presentation.base.BaseFragment
import com.jae464.placememo.databinding.FragmentHomeBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback, Overlay.OnClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var naverMap: NaverMap
    private lateinit var currentMarker: Marker
    private lateinit var viewPagerAdapter: HomeViewPagerAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("onViewCreated")
        binding.viewModel = viewModel
        binding.mapView.getMapAsync(this) // map 객체 가져오기
        // 모든 메모 가져오기 테스트
        viewModel.getAllMemo()
        println(viewModel.memoList.value)
    }

    override fun onMapReady(map: NaverMap) {
        println("onMapReady")
        naverMap = map
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0
        currentMarker = Marker()
        setUserLocation()
        initListener()
        initObserver()
        naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(37.4140919,126.8803569)))
    }

    private fun initListener() {

        binding.currentLocationButton.setOnClickListener {
            println("clicked")
            naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(37.4140919,126.8803569)))
        }

        // 일반 클릭 리스너. 마커 클릭시 실행되지 않는다.
        naverMap.setOnMapClickListener { pointF, latLng ->
            println("클린된 좌표 : ${latLng.latitude}, ${latLng.longitude} ")
            if(viewModel.memo.value != null) {
                viewModel.resetMemo()
                return@setOnMapClickListener
            }
            currentMarker.map = null
            currentMarker.position = LatLng(latLng.latitude, latLng.longitude)
            currentMarker.map = naverMap
            binding.postButton.visibility = View.VISIBLE
            viewModel.resetMemo()
        }

        currentMarker.setOnClickListener {
//            viewModel.toggleMapClick()
            currentMarker.map = null
            binding.postButton.visibility = View.GONE
            true
        }

        binding.postButton.setOnClickListener {
            // todo 업로드 페이지로 이동 (좌표 같이 넘겨줌)
            val bundle = Bundle().apply {
                putDouble("latitude", currentMarker.position.latitude)
                putDouble("longitude", currentMarker.position.longitude)
            }
            currentMarker.map = null
            findNavController().navigate(
                R.id.action_home_to_post,
                bundle
            )
        }
    }

    private fun initObserver() {

        viewModel.memoList.observe(viewLifecycleOwner) {
            println("memoList Observer Activated")
           it.forEach { memo ->
                println(memo.title)
                val marker = Marker()
                marker.position = LatLng(memo.latitude, memo.longitude)
                marker.map = naverMap
                marker.icon = MarkerIcons.RED
                marker.setOnClickListener {
                    viewModel.getMemo(memo.id)
                    binding.bottomPostView.visibility = View.VISIBLE
                    binding.postButton.visibility = View.GONE
                    currentMarker.map = null
                    println("Selected Marker Listener")
                    true
                }
            }
        }

        viewModel.memo.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.bottomPostView.visibility = View.GONE
                return@observe
            }
            binding.bottomPostView.visibility = View.VISIBLE
            val imageList = ImageManager.loadMemoImage(it.id)
            viewPagerAdapter = HomeViewPagerAdapter(imageList ?: emptyList())
            binding.thumbnailViewPager.adapter = viewPagerAdapter
//            imageList?.let { bitmapList ->
//                bitmapList.forEach { bitmap ->
////                    binding.previewImageView.setImageBitmap(bitmap)
//                    viewPagerAdapter.sub
//                }
//            }
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(it.latitude, it.longitude))
                .animate(CameraAnimation.Easing)
            naverMap.moveCamera(cameraUpdate)
        }
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
            binding.currentLocationButton.visibility = View.VISIBLE
        }
    }

    private fun showPermissionSnackBar(layout: View) {
        Snackbar.make(layout, "위치 권한 설정이 필요합니다.", Snackbar.LENGTH_INDEFINITE).apply {
            setAction("권한 설정") {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.show()
        }
    }

    override fun onClick(p0: Overlay): Boolean {
        TODO("Not yet implemented")
    }

}


