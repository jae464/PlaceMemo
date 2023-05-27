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
import com.jae464.presentation.databinding.DialogAddBinding

class AddDialog: DialogFragment() {
    private lateinit var binding: DialogAddBinding
    private var addDialogListener: AddDialogListener? = null
    private var title: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.tvTitle.text = title
        binding.btnAdd.setOnClickListener {
            addDialogListener?.onConfirmClick(binding.etName.text.toString())
            binding.etName.text.clear()
        }
        binding.btnCancel.setOnClickListener {
            addDialogListener?.onCancelClick()
            binding.etName.text.clear()
            binding.tvErrorMessage.text = ""
        }
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setErrorMessage(message: String) {
        binding.tvErrorMessage.text = message
    }

    fun addDialogListener(listener: AddDialogListener) {
        this.addDialogListener = listener
    }

    fun closeDialog() {
        dismiss()
    }
}

interface AddDialogListener {
    fun onConfirmClick(name: String)
    fun onCancelClick()
}

