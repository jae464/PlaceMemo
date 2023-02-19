package com.jae464.presentation.friend

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import com.jae464.presentation.R
import com.jae464.presentation.base.BaseFragment
import com.jae464.presentation.databinding.FragmentFriendListBinding

@AndroidEntryPoint
class FriendListFragment : BaseFragment<FragmentFriendListBinding>(R.layout.fragment_friend_list) {

    val TAG = "FreindListFragment"

    private var friendListAdapter = FriendListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppBar()

        binding.friendListRecyclerView.adapter = friendListAdapter
        friendListAdapter.submitList(userList)
    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.friendListToolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.friendListToolbar.inflateMenu(R.menu.friend_list_toolbar_menu)

        binding.friendListToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.add_friend -> {
                    findNavController().navigate(R.id.action_friend_list_to_friend_add)
                }
            }
            true
        }
    }
}