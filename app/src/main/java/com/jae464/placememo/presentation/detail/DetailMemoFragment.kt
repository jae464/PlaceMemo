package com.jae464.placememo.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentDetailMemoBinding
import com.jae464.placememo.presentation.base.BaseFragment

class DetailMemoFragment: BaseFragment<FragmentDetailMemoBinding>(R.layout.fragment_detail_memo) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DetailMemoFragment", "view created")
    }
}