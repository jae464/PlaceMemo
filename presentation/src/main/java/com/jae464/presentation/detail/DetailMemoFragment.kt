package com.jae464.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.jae464.presentation.base.BaseMapFragment
import com.jae464.presentation.databinding.FragmentDetailMemoBinding
import dagger.hilt.android.AndroidEntryPoint
import com.jae464.presentation.home.HomeViewPagerAdapter
import com.jae464.presentation.regionToString
import com.jae464.presentation.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMemoFragment: BaseMapFragment<FragmentDetailMemoBinding>(R.layout.fragment_detail_memo) {

    private val args: DetailMemoFragmentArgs by navArgs()
    private val viewModel: DetailMemoViewModel by viewModels()
    private lateinit var viewPagerAdapter: HomeViewPagerAdapter
    private lateinit var mapFragment: SupportMapFragment
    val TAG = "DetailMemoFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DetailMemoFragment", "view created")
        binding.viewModel = viewModel
        initAppBar()
//        initView()
        initObserver()
        initListener()
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.memo.collectLatest { memo ->
                    Log.d(TAG, memo.toString())
                    if (memo != null) {
                        binding.memo = memo
                        binding.memoLocation.text = "${memo.area1} ${memo.area2} ${memo.area3}"
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    memo.latitude,
                                    memo.longitude
                                ), 36F
                            )
                        )

                        map.addMarker(
                            MarkerOptions()
                                .position(LatLng(memo.latitude, memo.longitude))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        )

                        // ViewPager 초기화
                        initView(memo.imageUriList ?: emptyList())
                    }
                }
            }
        }

        viewModel.isDone.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun initView(imageUriList: List<String>) {
        if (imageUriList.isEmpty()) {
            binding.chipTypeViewMode.visibility = View.INVISIBLE
            return
        }

        val imagePathList = imageUriList.map {uri ->
            "${context?.filesDir}/images/${uri.substringAfterLast("/")}.jpg"
        }

        viewPagerAdapter = HomeViewPagerAdapter(imagePathList)
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