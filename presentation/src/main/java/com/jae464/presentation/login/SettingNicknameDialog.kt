package com.jae464.presentation.login

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.jae464.presentation.databinding.DialogSettingNicknameBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.ClassCastException
import com.jae464.presentation.R

@AndroidEntryPoint
class SettingNicknameDialog : DialogFragment() {

    internal lateinit var listener: NoticeDialogListener
    private lateinit var binding: DialogSettingNicknameBinding
    private val viewModel: LoginViewModel by viewModels()

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, nickname: String)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_setting_nickname, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initListener()
        initObserver()
    }

    private fun initListener() {
        binding.checkButton.setOnClickListener {
            println("sendButton click")
            println(binding.nicknameEditText.text)
            viewModel.checkNicknameAvailable(binding.nicknameEditText.text.toString())
        }

        binding.sendButton.setOnClickListener {
            listener.onDialogPositiveClick(this, binding.nicknameEditText.text.toString())
            dismiss()
        }
    }

    private fun initObserver() {
        viewModel.nicknameCheck.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            if (it) {
                binding.nicknameCheckTextView.text = "사용 가능한 닉네임입니다."
            }
            else {
                binding.nicknameCheckTextView.text = "이미 존재하는 닉네임입니다."
            }

        }
    }

}