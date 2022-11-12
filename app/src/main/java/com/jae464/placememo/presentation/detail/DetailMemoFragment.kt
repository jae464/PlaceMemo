package com.jae464.placememo.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentDetailMemoBinding
import com.jae464.placememo.presentation.base.BaseFragment
import com.jae464.placememo.presentation.base.BaseMapFragment
import com.jae464.placememo.presentation.markerIconList
import com.jae464.placememo.presentation.regionToString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMemoFragment: BaseMapFragment<FragmentDetailMemoBinding>(R.layout.fragment_detail_memo) {

    private val args: DetailMemoFragmentArgs by navArgs()
    private val viewModel: DetailMemoViewModel by viewModels()
    private lateinit var mapFragment: SupportMapFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DetailMemoFragment", "view created")
        binding.viewModel = viewModel
        viewModel.getMemo(args.memoId)
        initAppBar()
        initObserver()
    }

    override fun onMapReady(map: GoogleMap) {
        super.onMapReady(map)
        map.apply {
            setMinZoomPreference(6.0f)
            setMaxZoomPreference(16.0f)
        }
        mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment = mapFragment.also {
            val mapOptions = GoogleMapOptions().useViewLifecycleInFragment(true)
            SupportMapFragment.newInstance(mapOptions)
        }

    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.detailToolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.detailToolbar.title = args.memoId.toString()
        binding.detailToolbar.inflateMenu(R.menu.detail_toolbar_menu)
    }

    private fun initObserver() {
        viewModel.memo.observe(viewLifecycleOwner) { memo ->
            binding.memoLocation.text = regionToString(memo.area1, memo.area2, memo.area3)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(memo.latitude, memo.longitude), 36F))
            val resourceId = markerIconList[memo.category] ?: R.drawable.marker
//                val icon = ImageManager.changeColor(0, resourceId, requireContext())
            val memoMarker = map.addMarker(
                MarkerOptions()
                    .position(LatLng(memo.latitude, memo.longitude))
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .icon(BitmapDescriptorFactory.fromResource(resourceId))
            )
        }
    }
}