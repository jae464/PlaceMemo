package com.jae464.placememo.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.jae464.placememo.R

abstract class BaseMapFragment<T: ViewDataBinding>(@LayoutRes val layoutRes: Int): Fragment(), OnMapReadyCallback {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    lateinit var map: GoogleMap

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
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}