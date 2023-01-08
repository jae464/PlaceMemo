package com.jae464.placememo.presentation.detail

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.jae464.placememo.R
import com.jae464.placememo.data.manager.ImageManager
import com.jae464.placememo.databinding.FragmentDetailMemoBinding
import com.jae464.placememo.presentation.base.BaseFragment
import com.jae464.placememo.presentation.base.BaseMapFragment
import com.jae464.placememo.presentation.home.HomeViewPagerAdapter
import com.jae464.placememo.presentation.markerIconList
import com.jae464.placememo.presentation.regionToString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMemoFragment: BaseMapFragment<FragmentDetailMemoBinding>(R.layout.fragment_detail_memo) {

    private val args: DetailMemoFragmentArgs by navArgs()
    private val viewModel: DetailMemoViewModel by viewModels()
    private lateinit var viewPagerAdapter: HomeViewPagerAdapter
    private lateinit var mapFragment: SupportMapFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DetailMemoFragment", "view created")
        binding.viewModel = viewModel
        initAppBar()
        initView()
        initObserver()
        initListener()
        viewModel.getMemo(args.memoId)
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
        binding.detailToolbar.inflateMenu(R.menu.detail_toolbar_menu)
        binding.detailToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.delete -> {
                    // TODO 삭제 다이얼로드 표시
                    showDeleteDialog()
                }
                R.id.edit -> {
                    val action = DetailMemoFragmentDirections.actionDetailMemoToPost(args.memoId)
                    findNavController().navigate(
                        action
                    )
                }
            }
            true
        }
    }

    private fun initObserver() {
        viewModel.memo.observe(viewLifecycleOwner) { memo ->
            binding.memoLocation.text = regionToString(memo.area1, memo.area2, memo.area3)
            map.clear()
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(memo.latitude, memo.longitude), 36F))
            val resourceId = markerIconList[memo.category.ordinal] ?: R.drawable.marker
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(memo.latitude, memo.longitude))
                    .icon(BitmapDescriptorFactory.fromResource(resourceId))
            )
        }

        viewModel.isDone.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun initView() {
        val imageList = ImageManager.loadMemoImage(memoId = args.memoId)
        // imageList 가 없을 경우 chip 제거
        if (imageList == null) {
            binding.chipTypeViewMode.visibility = View.INVISIBLE
            return
        }
        viewPagerAdapter = HomeViewPagerAdapter(imageList)
        binding.memoPhotoViewpager.adapter = viewPagerAdapter
        binding.dotIndicator.attachTo(binding.memoPhotoViewpager)

    }

    private fun initListener() {
        binding.chipTypeViewMode.setOnCheckedStateChangeListener { group, checkedIds ->
            when(checkedIds[0]) {
                R.id.chip_type_map -> {
                    binding.mapContainer.visibility = View.VISIBLE
                    binding.memoPhotoViewpager.visibility = View.INVISIBLE
                    binding.dotIndicator.visibility = View.INVISIBLE
                }
                R.id.chip_type_photo -> {
                    binding.memoPhotoViewpager.visibility = View.VISIBLE
                    binding.dotIndicator.visibility = View.VISIBLE
                    binding.mapContainer.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Title")
            .setMessage("정말 삭제하시겠습니까?")
            .setPositiveButton("삭제") { p0, p1 ->
                viewModel.deleteMemo(args.memoId)
                viewModel.deleteMemoOnRemote(FirebaseAuth.getInstance().uid, args.memoId)
                Snackbar.make(binding.root, "메모 삭제가 완료되었습니다.", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소") { p0, p1 ->

            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("DetailMemoFragment", "onDestroyView")
    }
}