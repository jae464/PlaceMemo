package com.jae464.placememo.presentation.post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jae464.placememo.R
import com.jae464.placememo.presentation.base.BaseFragment
import com.jae464.placememo.databinding.FragmentPostBinding
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(R.layout.fragment_post) {

    private val viewModel: PostViewModel by viewModels()
    private lateinit var location: LatLng
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")
        println("$latitude, $longitude")
    }
}