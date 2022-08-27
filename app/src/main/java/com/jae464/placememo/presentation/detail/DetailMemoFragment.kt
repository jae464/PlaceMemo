package com.jae464.placememo.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentDetailMemoBinding
import com.jae464.placememo.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMemoFragment: BaseFragment<FragmentDetailMemoBinding>(R.layout.fragment_detail_memo) {

    private val args: DetailMemoFragmentArgs by navArgs()
    private val viewModel: DetailMemoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DetailMemoFragment", "view created")
        binding.viewModel = viewModel
        viewModel.getMemo(args.memoId)
        initAppBar()
        initObserver()
    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.detailToolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.detailToolbar.title = args.memoId.toString()
        binding.detailToolbar.inflateMenu(R.menu.detail_toolbar_menu)
    }

    private fun initObserver() {
        viewModel.memo.observe(viewLifecycleOwner) {

        }
    }
}