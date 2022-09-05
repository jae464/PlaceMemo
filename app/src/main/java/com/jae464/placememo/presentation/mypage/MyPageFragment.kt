package com.jae464.placememo.presentation.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jae464.placememo.LoginActivity
import com.jae464.placememo.R

class MyPageFragment : Fragment(R.layout.fragment_my_page) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }
}