package com.jae464.presentation.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.map
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.jae464.domain.model.post.Category
import com.jae464.domain.model.post.Memo
import com.jae464.presentation.R
import com.jae464.presentation.base.BaseMapFragment
import com.jae464.presentation.databinding.FragmentHomeBinding
import com.jae464.presentation.databinding.ItemMemoMarkerBinding
import com.jae464.presentation.login.LoginActivity
import com.jae464.presentation.markerIconList
import com.jae464.presentation.regionToString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.io.FileOutputStream


@AndroidEntryPoint
class HomeFragment : BaseMapFragment<FragmentHomeBinding>(R.layout.fragment_home),
    GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private val TAG: String = "HomeFragment"

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mapFragment: SupportMapFragment
    private var currentMarker: Marker? = null
    private lateinit var viewPagerAdapter: HomeViewPagerAdapter
    private var currentMemoId = -1
    private var user = FirebaseAuth.getInstance().currentUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
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

        setCurrentLocation()

        mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment = mapFragment.also {
            val mapOptions = GoogleMapOptions().useViewLifecycleInFragment(true)
            SupportMapFragment.newInstance(mapOptions)
        }

        initObserver()
        initListener()
        initAppBar()
        viewModel.getAllMemo()
    }

    private fun initAppBar() {
        val appBarConfiguration =
            AppBarConfiguration(findNavController().graph, binding.drawerLayout)
        binding.homeToolBar.setupWithNavController(findNavController(), appBarConfiguration)
        setLogo()
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
        map.setOnInfoWindowClickListener(this)

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
            setCurrentLocation()
        }

        // TODO 메모 프리뷰를 클릭하면, 해당 메모의 디테일 페이지로 이동한다.
        binding.memoPreview.memoCardView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeToDetailMemo(currentMemoId)
            findNavController().navigate(
                action
            )
        }

        binding.drawerNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.login -> {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.memoList.collectLatest { memos ->
                    viewModel.filteredMemoList.value = memos
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredMemoList.collectLatest { memoList ->
                    map.clear()
                    memoList.map { memo ->
                        Log.d(TAG, memo.toString())

                        // TODO 썸네일 가져오는 로직 추후 수정 필요
                        val imagePathList = memo.imageUriList ?: emptyList()
                        val thumbnailImage = if (imagePathList.isEmpty()) null else imagePathList[0]

                        val thumbnailImageBitmap = thumbnailImage.let { path ->
                            BitmapFactory.decodeFile("${context?.filesDir}/images/${path?.substringAfterLast("/")}.jpg")
                        }

                        // TODO Glide 로 이미지 비동기로 로드하는거 필요
                        val thumbnailMarkerView =
                            ItemMemoMarkerBinding.inflate(LayoutInflater.from(context), null, false)
                                .apply {
                                    if (thumbnailImageBitmap == null) {
                                        cvThumbnail.setImageResource(R.drawable.ic_sample_profile)
                                    } else {
                                        cvThumbnail.setImageBitmap(thumbnailImageBitmap)
                                    }
                                }

                        val thumbnailBitmap = createDrawableFromView(requireContext(), thumbnailMarkerView.root)

                        Log.d(TAG, memo.title)

                        val memoMarker = map.addMarker(
                            MarkerOptions()
                                .position(LatLng(memo.latitude, memo.longitude))
                                .icon(BitmapDescriptorFactory.fromBitmap((thumbnailBitmap!!)))

                        )
                        memoMarker?.tag = memo
                    }

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collectLatest {categories ->
                    binding.chipGroupType.removeAllViews()
                    addDefaultChip()
                    categories.forEach {category ->
                        binding.chipGroupType.addView(
                            Chip(
                                requireContext(),
                                null,
                                com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
                            ).apply {
                                text = category.name
                                isCheckable = true
                                checkedIcon = null
                                chipStrokeColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue_200))
                                chipStrokeWidth = 4f
                                setChipBackgroundColorResource(R.color.bg_chip)
                                setOnClickListener {
                                    Log.d(TAG, category.name)
                                    viewModel.getMemoByCategory(category)
                                }
                            }
                        )
                    }
                }
            }
        }



        viewModel.currentAddress.observe(viewLifecycleOwner) {
            currentMarker?.snippet = it
            currentMarker?.showInfoWindow()
        }


    }

    private fun clearChipGroup() {
//        binding.chipTypeFood.isChecked = false
//        binding.chipTypeCafe.isChecked = false
//        binding.chipTypeHotel.isChecked = false
//        binding.chipTypeOther.isChecked = false
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
        viewModel.getAddressName(location.latitude, location.longitude)
        binding.postButton.visibility = View.VISIBLE
    }

    private fun displayMemoPreview(memo: Memo) {
        currentMarker?.remove()

        val imageList = memo.imageUriList?.map {
            "${context?.filesDir}/images/${it.substringAfterLast("/")}.jpg"
        } ?: emptyList()


        binding.memoPreview.memo = memo
        binding.memoPreview.locationTextView.text =
            regionToString(memo.area1, memo.area2, memo.area3)

        viewPagerAdapter = HomeViewPagerAdapter(imageList)
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

    override fun onInfoWindowClick(p0: Marker) {
        currentMarker ?: return

        val bundle = Bundle().apply {
            putDouble("latitude", currentMarker!!.position.latitude)
            putDouble("longitude", currentMarker!!.position.longitude)
        }

        findNavController().navigate(
            R.id.action_home_to_post,
            bundle
        )
    }

    private fun createDrawableFromView(context: Context, view: View): Bitmap? {
        val displayMetrics = DisplayMetrics()

        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.buildDrawingCache()
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun setLogo() {
        Glide.with(requireContext())
            .load(R.drawable.logo)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d(TAG, "Image Load Error")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.homeToolBar.logo = resource
                    }
                    return true
                }
            })
            .override(64, 64)
            .submit()
    }

    private fun addDefaultChip() {
        binding.chipGroupType.addView(
            Chip(
                requireContext(),
                null,
                com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
            ).apply {
                text = "전체"
                isCheckable = true
                checkedIcon = null
                chipStrokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.blue_200
                    )
                )
                chipStrokeWidth = 4f
                setChipBackgroundColorResource(R.color.bg_chip)
                setOnClickListener {
                    viewModel.getAllMemo()
                }
            }
        )
    }
}


