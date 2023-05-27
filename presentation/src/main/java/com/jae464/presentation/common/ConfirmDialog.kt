package com.jae464.presentation.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.jae464.presentation.R
import com.jae464.presentation.databinding.DialogConfirmationBinding

class ConfirmDialog: DialogFragment() {

    private lateinit var binding: DialogConfirmationBinding
    private var confirmDialogListener: ConfirmDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_confirmation, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.btnConfirm.setOnClickListener {
            confirmDialogListener?.onConfirmClick()
        }
        binding.btnCancel.setOnClickListener {
            confirmDialogListener?.onCancelClick()
        }
    }

    fun setConfirmDialogListener(listener: ConfirmDialogListener) {
        this.confirmDialogListener = listener
    }

}

interface ConfirmDialogListener {
    fun onConfirmClick()
    fun onCancelClick()
}