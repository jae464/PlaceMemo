package com.jae464.placememo.presentation.feed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentFeedBinding
import com.jae464.placememo.presentation.base.BaseFragment
import com.jae464.placememo.presentation.home.HomeViewModel
import com.jae464.placememo.presentation.home.HomeViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private val TAG: String = "FeedFragment"
    private val feedListAdapter = FeedListAdapter(this::goToDeatilPage)
    private val viewModel: FeedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.feedRecyclerView.adapter = feedListAdapter
        initObserver()
        initAppBar()
        viewModel.getAllMemo()
    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.postToolBar.setupWithNavController(findNavController(), appBarConfiguration)
    }

    private fun initObserver() {
        viewModel.memoList.observe(viewLifecycleOwner) {
            Log.d("FeedFragment","memoList Observer")
            println(it)
            feedListAdapter.submitList(it.toMutableList())
        }
    }

    private fun goToDeatilPage(memoId: Long) {
        val action = FeedFragmentDirections.actionFeedToDetailMemo(memoId)
        findNavController().navigate(
            action
        )
    }
}