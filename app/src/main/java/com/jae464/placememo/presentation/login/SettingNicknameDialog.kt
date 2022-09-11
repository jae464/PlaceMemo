package com.jae464.placememo.presentation.login

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.jae464.placememo.R

class SettingNicknameDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            builder.setView(inflater.inflate(R.layout.dialog_setting_nickname, null))
//                .setPositiveButton("저장"
//                ) { dialog, id ->
//
//                }
            builder.create()
        } ?: throw IllegalStateException("no")
    }

}