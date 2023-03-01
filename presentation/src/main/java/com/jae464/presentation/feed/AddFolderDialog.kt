package com.jae464.presentation.feed

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.jae464.domain.model.feed.Folder
import com.jae464.presentation.R
import com.jae464.presentation.databinding.DialogAddFolderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFolderDialog: DialogFragment() {

    private lateinit var binding: DialogAddFolderBinding
    private val viewModel: AddFolderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_folder, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initListener()
    }

    private fun initListener() {
        binding.btnAdd.setOnClickListener {
            viewModel.createFolder(Folder(name = binding.etFolderName.text.toString(), memoCount = 0))
            dismiss()
        }
    }
}