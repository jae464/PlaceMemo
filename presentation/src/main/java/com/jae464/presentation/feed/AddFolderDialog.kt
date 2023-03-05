package com.jae464.presentation.feed

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jae464.domain.model.feed.Folder
import com.jae464.presentation.R
import com.jae464.presentation.databinding.DialogAddFolderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect {

                    when (it) {
                        is AddFolderEvent.ExistFolderName -> {
                            Toast.makeText(requireContext(), "이미 존재하는 폴더입니다.", Toast.LENGTH_SHORT).show()

                        }
                        is AddFolderEvent.NotExistFolderName -> {
                            viewModel.createFolder(Folder(name = binding.etFolderName.text.toString(), memoCount = 0))
                            dismiss()
                        }
                        else -> {

                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initListener()


    }

    private fun initListener() {
        binding.btnAdd.setOnClickListener {
            viewModel.checkFolderNameAvailable(binding.etFolderName.text.toString())
        }
    }
}