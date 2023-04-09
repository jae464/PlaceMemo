package com.jae464.presentation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.jae464.presentation.R
import com.jae464.presentation.databinding.DialogAddCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryAddDialog(
    private val onClickAddButton: (String) -> Unit,
    private val onClickCancelButton: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogAddCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_category, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.btnAddCategory.setOnClickListener {
            val categoryName = binding.etCategoryName.text.toString()
            onClickAddButton(categoryName)
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            onClickCancelButton()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onClickCancelButton()
    }


}