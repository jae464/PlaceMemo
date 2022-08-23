package com.jae464.placememo.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentDetailMemoBinding
import com.jae464.placememo.presentation.base.BaseFragment

class DetailMemoFragment: BaseFragment<FragmentDetailMemoBinding>(R.layout.fragment_detail_memo) {

    private val args: DetailMemoFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DetailMemoFragment", "view created")
        initAppBar()
    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.detailToolBar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.detailToolBar.title = args.memoId.toString()
        binding.detailToolBar.inflateMenu(R.menu.detail_toolbar_menu)
    }
}