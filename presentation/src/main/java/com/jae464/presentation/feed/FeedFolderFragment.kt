package com.jae464.presentation.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jae464.presentation.base.BaseFragment
import com.jae464.presentation.R
import com.jae464.presentation.databinding.FragmentFeedFolderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFolderFragment :
    BaseFragment<FragmentFeedFolderBinding>(R.layout.fragment_feed_folder) {

    private lateinit var folderListAdapter: FolderListAdapter
    private val viewModel: FeedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        folderListAdapter = FolderListAdapter(requireContext(), parentFragmentManager)
        initView()
        initListener()
        initObserver()
    }

    private fun initListener() {
        binding.fabAddFolder.setOnClickListener {
            AddFolderDialog().show(activity?.supportFragmentManager!!, "add_folder")
        }
    }

    private fun initView() {
        binding.rcvFolderList.adapter = folderListAdapter
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.folderList.collectLatest { folder ->
                    Log.d("FeedFolderFragment", folder.toString())
                    folderListAdapter.submitList(folder)
                }
            }
        }

    }

}