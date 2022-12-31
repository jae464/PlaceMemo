package com.jae464.placememo.presentation.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentFeedBinding
import com.jae464.placememo.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private val TAG: String = "FeedFragment"
    private var feedListAdapter = FeedListAdapter(this::goToDetailPage)
    private var listAdapter = FeedListAdapter(this::goToDetailPage, 1)
    private val viewModel: FeedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.feedRecyclerView.adapter = feedListAdapter

        initObserver()
        initAppBar()
        initListener()
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

            if (it.isEmpty()) {
                binding.emptyMessageTextView.visibility = View.VISIBLE
                feedListAdapter.submitList(emptyList())
                listAdapter.submitList(emptyList())
                return@observe
            }
            binding.emptyMessageTextView.visibility = View.INVISIBLE
            feedListAdapter.submitList(it.toMutableList())
            listAdapter.submitList(it.toMutableList())

        }
    }

    private fun initListener() {
        binding.chipTypeFeedType.setOnCheckedStateChangeListener { group, checkedIds ->
            Log.d(TAG, checkedIds.toString())

            when (checkedIds[0]) {
                R.id.chip_type_card_view -> {
                    binding.feedRecyclerView.adapter = feedListAdapter
                    binding.feedRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
                    viewModel.getAllMemo()
                }
                R.id.chip_type_grid_view -> {
                    binding.feedRecyclerView.adapter = feedListAdapter
                    binding.feedRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                    viewModel.getAllMemo()
                }
                R.id.chip_type_list_view -> {
                    binding.feedRecyclerView.adapter = listAdapter
                    binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    viewModel.getAllMemo()

                }
            }
        }
    }

    private fun goToDetailPage(memoId: Long) {
        val action = FeedFragmentDirections.actionFeedToDetailMemo(memoId)
        findNavController().navigate(
            action
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
        feedListAdapter.clearData()
        listAdapter.clearData()
    }
}