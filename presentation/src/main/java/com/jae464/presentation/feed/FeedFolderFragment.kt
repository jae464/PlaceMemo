package com.jae464.presentation.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import com.jae464.presentation.base.BaseFragment
import com.jae464.presentation.R
import com.jae464.presentation.databinding.FragmentFeedFolderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFolderFragment :
    BaseFragment<FragmentFeedFolderBinding>(R.layout.fragment_feed_folder) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.fabAddFolder.setOnClickListener {
            AddFolderDialog().show(activity?.supportFragmentManager!!, "TEST")
        }
    }


}