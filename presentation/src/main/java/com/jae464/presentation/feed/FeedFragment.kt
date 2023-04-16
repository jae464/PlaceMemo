package com.jae464.presentation.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jae464.presentation.R
import com.jae464.presentation.base.BaseFragment
import com.jae464.presentation.databinding.FragmentFeedBinding


class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private val TAG = "FeedFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
        initAppBar()
        initListener()
    }

    private fun initTabLayout() {
        val adapter = FeedStateAdapter(this)
        binding.viewPager.adapter = adapter
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, tab?.position.toString())
                when(tab?.position) {
                    FOLDER_PAGE -> {

                    }
                    CATEGORY_PAGE -> {
                        // VIEW MODE INFLATE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab , position ->
            tab.text = tabTitleArray[position]
        }.attach()

    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbarFeed.setupWithNavController(findNavController(), appBarConfiguration)
    }

    private fun initListener() {

    }

    companion object {
        private const val FOLDER_PAGE = 0
        private const val CATEGORY_PAGE = 1

        private val tabTitleArray = arrayOf("폴더별", "카테고리별")
    }

}