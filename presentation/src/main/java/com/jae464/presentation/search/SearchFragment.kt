package com.jae464.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.jae464.presentation.base.BaseFragment
import com.jae464.presentation.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import com.jae464.presentation.R
import com.jae464.presentation.feed.FeedListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val TAG = "SearchFragment"
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var feedListAdapter: FeedListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        feedListAdapter = FeedListAdapter(requireContext(), this::goToDetailPage)
        binding.memoRecyclerView.adapter = feedListAdapter

        initAppBar()
        initListener()
        initObserver()
    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
    }

    private fun initListener() {
        binding.searchEditText.setOnEditorActionListener { textView, i, keyEvent ->
            Log.d(TAG, textView.text.toString())

            viewModel.getMemoByTitle(textView.text.toString())


            ViewCompat.getWindowInsetsController(binding.root)?.hide(
                WindowInsetsCompat.Type.ime()
            )

            true
        }
    }

    private fun initObserver() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.memoList.collectLatest {
                    // TODO Recycler View Update
                    Log.d(TAG, it.toString())
                }
            }
        }

    }

    private fun goToDetailPage(memoId: Int) {
        val action = SearchFragmentDirections.actionSearchToDetailMemo(memoId)
        findNavController().navigate(
            action
        )
    }
}