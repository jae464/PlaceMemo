package com.jae464.presentation.feed
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.jae464.presentation.R
import com.jae464.presentation.databinding.DialogDeleteFolderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteFolderDialog(private val folderId: Long): DialogFragment() {

    private lateinit var binding: DialogDeleteFolderBinding
    private val viewModel: DeleteFolderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_delete_folder, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initListener()


    }

    private fun initListener() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteFolder(folderId)
            dismiss()
        }
    }
}