package com.jae464.presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.jae464.presentation.R
import com.jae464.presentation.base.BaseFragment
import com.jae464.presentation.databinding.FragmentFeedBinding


class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private val tabTitleArray = arrayOf(
        "폴더별",
        "카테고리별"
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabLayout()
        setAppBar()
    }

    private fun setTabLayout() {
        val adapter = FeedStateAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab , position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    private fun setAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbarFeed.setupWithNavController(findNavController(), appBarConfiguration)
    }


}