package com.jae464.placememo.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentSearchBinding
import com.jae464.placememo.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val TAG = "SearchFragment"
    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

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
            //TODO 검색어랑 일치하는 메모 가져오기
            viewModel.getMemoByTitle(textView.text.toString())
            // TODO 메모 업데이트 후 EditText 에 Text 지우기
            true
        }
    }

    private fun initObserver() {
        viewModel.memoList.observe(viewLifecycleOwner) {
            Log.d(TAG, it.toString())
        }
    }
}