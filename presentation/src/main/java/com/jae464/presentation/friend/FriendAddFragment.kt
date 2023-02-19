package com.jae464.presentation.friend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jae464.presentation.base.BaseFragment
import com.jae464.presentation.databinding.FragmentFriendAddBinding
import dagger.hilt.android.AndroidEntryPoint
import com.jae464.presentation.R
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