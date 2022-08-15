package com.jae464.placememo.presentation.home

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jae464.placememo.R
import com.jae464.placememo.data.manager.ImageManager
import com.jae464.placememo.databinding.FragmentHomeBinding
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.presentation.base.BaseMapFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseMapFragment<FragmentHomeBinding>(R.layout.fragment_home), GoogleMap.OnMarkerClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mapFragment: SupportMapFragment
    private var currentMarker: Marker? = null
    private lateinit var viewPagerAdapter: HomeViewPagerAdapter
    private lateinit var geocoder: Geocoder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("onViewCreated")
        geocoder = Geocoder(context)
        binding.viewModel = viewModel
        binding.memoPreview.memoCardView.visibility = View.INVISIBLE
        viewModel.getAllMemo()
        println(viewModel.memoList.value)
    }

    override fun onMapReady(map: GoogleMap) {
        println("onMapReady")
        super.onMapReady(map)
        val seoul = LatLng(37.554891, 126.970814)
        mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment = mapFragment.also {
            val mapOptions = GoogleMapOptions().useViewLifecycleInFragment(true)
            SupportMapFragment.newInstance(mapOptions)
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15F))
        initListener()
        initObserver()
    }

    private fun initListener() {
        map.setOnMapClickListener { it ->
            // 현재 보여지고 있는 메모가 있는 경우 해당 메모를 지운다.
            if (binding.memoPreview.memoCardView.visibility == View.VISIBLE) {
                binding.memoPreview.memoCardView.visibility = View.GONE
                return@setOnMapClickListener
            }
            currentMarker?.remove()
            currentMarker = map.addMarker(
                MarkerOptions()
                    .position(LatLng(it.latitude, it.longitude))
            )
            val addressList = geocoder.getFromLocation(it.latitude, it.longitude, 5)
            println("Location List")
            println(addressList)
            currentMarker?.tag = "current"
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 15F)
            map.animateCamera(cameraUpdate, 500, null)
            binding.postButton.visibility = View.VISIBLE
        }

        map.setOnMarkerClickListener(this)

        binding.postButton.setOnClickListener {
            // todo 업로드 페이지로 이동 (좌표 같이 넘겨줌)
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
    }
//
    private fun initObserver() {
        viewModel.memoList.observe(viewLifecycleOwner) {
            println("memoList Observer Activated")
           it.forEach { memo ->
                println(memo.title)
                val memoMarker = map.addMarker(
                    MarkerOptions()
                        .position(LatLng(memo.latitude, memo.longitude))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )
               memoMarker?.tag = memo
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker.tag == "current") {
            marker.remove()
            binding.postButton.visibility = View.GONE
            return false
        }
        currentMarker?.remove()
        val memo = marker.tag as Memo
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(memo.latitude, memo.longitude), 15F)
        val imageList = ImageManager.loadMemoImage(memo.id)
        map.animateCamera(cameraUpdate, 200, null)
        binding.memoPreview.memoCardView.visibility = View.VISIBLE
        binding.postButton.visibility = View.GONE
        binding.memoPreview.titleTextView.text = memo.title
        binding.memoPreview.contentTextView.text = memo.content
        viewPagerAdapter = HomeViewPagerAdapter(imageList ?: emptyList())
        binding.memoPreview.thumbnailViewPager.adapter = viewPagerAdapter
        Log.d("HomeFragment onMarkerClick", memo.title)
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        map.clear()
    }


}


