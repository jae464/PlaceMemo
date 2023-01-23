package com.jae464.placememo.presentation.friend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentFriendListBinding
import com.jae464.placememo.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendListFragment : BaseFragment<FragmentFriendListBinding>(R.layout.fragment_friend_list) {

    val TAG = "FreindListFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppBar()

    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.friendListToolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.friendListToolbar.inflateMenu(R.menu.friend_list_toolbar_menu)
    }
}