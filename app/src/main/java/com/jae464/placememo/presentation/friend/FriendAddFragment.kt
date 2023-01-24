package com.jae464.placememo.presentation.friend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentFriendAddBinding
import com.jae464.placememo.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendAddFragment : BaseFragment<FragmentFriendAddBinding>(R.layout.fragment_friend_add) {

    private val viewModel: FriendViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAppBar()

        // TEST
        viewModel.getUserByNickname("하프문")
    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.friendAddToolbar.setupWithNavController(findNavController(), appBarConfiguration)

    }

}