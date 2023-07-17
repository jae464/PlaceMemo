package com.jae464.presentation.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.jae464.domain.model.SortBy
import com.jae464.presentation.R
import com.jae464.presentation.databinding.DialogFeedSettingBinding
import com.jae464.presentation.model.ViewType

class FeedSettingDialog : DialogFragment() {
    private lateinit var binding: DialogFeedSettingBinding
    private var feedSettingDialogListener: FeedSettingDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_feed_setting, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initListener()
    }

    private fun initListener() {
        binding.btnSortDesc.setOnClickListener {
            feedSettingDialogListener?.onSortChanged(SortBy.DESC)
        }
        binding.btnSortAsc.setOnClickListener {
            feedSettingDialogListener?.onSortChanged(SortBy.ASC)
        }
        binding.btnViewList.setOnClickListener {
            feedSettingDialogListener?.onViewChanged(ViewType.LIST_VIEW_TYPE)
        }
        binding.btnViewCard.setOnClickListener {
            feedSettingDialogListener?.onViewChanged(ViewType.CARD_VIEW_TYPE)
        }
    }

    fun setFeedSettingDialogListener(listener: FeedSettingDialogListener) {
        this.feedSettingDialogListener = listener
    }
}

interface FeedSettingDialogListener {
    fun onSortChanged(sortBy: SortBy)
    fun onViewChanged(viewType: ViewType)
}