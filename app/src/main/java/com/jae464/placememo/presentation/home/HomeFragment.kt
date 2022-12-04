package com.jae464.placememo.presentation.home

import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.jae464.placememo.R
import com.jae464.placememo.data.api.RetrofitClient
import com.jae464.placememo.data.api.response.GeoResponse
import com.jae464.placememo.data.api.response.RegionResponse
import com.jae464.placememo.data.manager.ImageManager
import com.jae464.placememo.databinding.FragmentHomeBinding
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.presentation.base.BaseMapFragment
import com.jae464.placememo.presentation.login.LoginActivity
import com.jae464.placememo.presentation.markerIconList
import com.jae464.placememo.presentation.regionToString
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class HomeFragment : BaseMapFragment<FragmentHomeBinding>(R.layout.fragment_home),
    GoogleMap.OnMarkerClickListener {

    private val TAG: String = "HomeFragment"

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mapFragment: SupportMapFragment
    private var currentMarker: Marker? = null
    private lateinit var viewPagerAdapter: HomeViewPagerAdapter
    private var currentMemoId = -1L
    private var user = FirebaseAuth.getInstance().currentUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"onViewCreated")
        binding.viewModel = viewModel
        binding.memoPreview.memoCardView.visibility = View.INVISIBLE
    }

    override fun onMapReady(map: GoogleMap) {
        Log.d(TAG, "onMapReady")
        super.onMapReady(map)
        map.apply {
            setMinZoomPreference(6.0f)
            setMaxZoomPreference(16.0f)
        }
        setUserLocation()
        val seoul = LatLng(37.554891, 126.970814)
        mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment = mapFragment.also {
            val mapOptions = GoogleMapOptions().useViewLifecycleInFragment(true)
            SupportMapFragment.newInstance(mapOptions)
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 36F))
        initObserver()
        initListener()
        initAppBar()
        viewModel.getAllMemo()
    }

    private fun initAppBar() {
        val appBarConfiguration =
            AppBarConfiguration(findNavController().graph, binding.drawerLayout)
        binding.homeToolBar.setupWithNavController(findNavController(), appBarConfiguration)

        if (user == null) {
            binding.drawerNavigationView.inflateMenu(R.menu.drawer_menu_not_login)
        } else {
            binding.drawerNavigationView.inflateMenu(R.menu.drawer_menu)
        }
    }

    private fun initListener() {
        map.setOnMapClickListener { location ->

            // 현재 보여지고 있는 메모가 있는 경우 해당 메모를 지운다.
            if (binding.memoPreview.memoCardView.visibility == View.VISIBLE) {
                binding.memoPreview.memoCardView.visibility = View.GONE
                binding.currentLocationButton.visibility = View.VISIBLE
                return@setOnMapClickListener
            }

            displayCurrentMarker(location)
        }

        map.setOnMarkerClickListener(this)

        binding.postButton.setOnClickListener {
            currentMarker ?: return@setOnClickListener
            val bundle = Bundle().apply {
                putDouble("latitude", currentMarker!!.position.latitude)
                putDouble("longitude", currentMarker!!.position.longitude)
            }

            findNavController().navigate(
                R.id.action_home_to_post,
                bundle
            )
        }

        binding.currentLocationButton.setOnClickListener {
            val currentLocation = getLocation() ?: return@setOnClickListener
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude, currentLocation.longitude), 16f))
        }

        // TODO 메모 프리뷰를 클릭하면, 해당 메모의 디테일 페이지로 이동한다.
        binding.memoPreview.memoCardView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeToDetailMemo(currentMemoId)
            findNavController().navigate(
                action
            )
        }

        binding.chipGroupType.setOnCheckedStateChangeListener { group, checkedIds ->
            // 모든 칩이 체크 해제되어 있는 경우 전체에 다시 체크함
            Log.d(TAG, checkedIds.toString())
            if (checkedIds.isEmpty()) {
                binding.chipTypeAll.isChecked = true
                return@setOnCheckedStateChangeListener
            }

            if (checkedIds.size > 1 && checkedIds.contains(R.id.chip_type_all)) {
                binding.chipTypeAll.isChecked = false
                return@setOnCheckedStateChangeListener
            }
        }

        binding.chipTypeAll.setOnClickListener {
            clearChipGroup()
            binding.chipTypeAll.isChecked = true
        }

        binding.drawerNavigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.login -> {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    //
    private fun initObserver() {
        viewModel.memoList.observe(viewLifecycleOwner) {
            it.forEach { memo ->
                Log.d(TAG, memo.title)
                val resourceId = markerIconList[memo.category] ?: R.drawable.marker
//                val icon = ImageManager.changeColor(0, resourceId, requireContext())
                val memoMarker = map.addMarker(
                    MarkerOptions()
                        .position(LatLng(memo.latitude, memo.longitude))
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .icon(BitmapDescriptorFactory.fromResource(resourceId))
                )
                memoMarker?.tag = memo
            }
        }

        viewModel.currentAddress.observe(viewLifecycleOwner) {
            currentMarker?.snippet = it
            currentMarker?.showInfoWindow()
        }


    }

    private fun clearChipGroup() {
        binding.chipTypeFood.isChecked = false
        binding.chipTypeCafe.isChecked = false
        binding.chipTypeHotel.isChecked = false
        binding.chipTypeOther.isChecked = false
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        if (marker.tag == "current") {
            marker.remove()
            binding.postButton.visibility = View.GONE
            return false
        }

        val memo = marker.tag as Memo
        currentMemoId = memo.id
        val cameraUpdate =
            CameraUpdateFactory.newLatLngZoom(LatLng(memo.latitude, memo.longitude), 16F)

        map.animateCamera(cameraUpdate, 200, null)

        displayMemoPreview(memo)

        return true
    }

    private fun displayCurrentMarker(location: LatLng) {
        currentMarker?.remove()
        currentMarker = map.addMarker(
            MarkerOptions()
                .position(LatLng(location.latitude, location.longitude))
                .title("주소")
        )

        currentMarker?.tag = "current"
        val cameraUpdate =
            CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 16F)

        map.animateCamera(cameraUpdate, 500, null)
        println("주소 요청 전")
        viewModel.getAddressName(location.latitude, location.longitude)
        println("주소 요청 후")
        binding.postButton.visibility = View.VISIBLE
    }

    private fun displayMemoPreview(memo: Memo) {
        currentMarker?.remove()

        val imageList = ImageManager.loadMemoImage(memo.id)

        binding.memoPreview.memo = memo
        binding.memoPreview.locationTextView.text = regionToString(memo.area1, memo.area2, memo.area3)

        viewPagerAdapter = HomeViewPagerAdapter(imageList ?: emptyList())
        binding.memoPreview.thumbnailViewPager.adapter = viewPagerAdapter
        binding.memoPreview.dotIndicator.attachTo(binding.memoPreview.thumbnailViewPager)

        binding.memoPreview.memoCardView.visibility = View.VISIBLE
        binding.currentLocationButton.visibility = View.GONE
        binding.postButton.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        map.clear()
    }
}


