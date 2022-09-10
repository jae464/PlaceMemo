package com.jae464.placememo.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.jae464.placememo.presentation.login.LoginActivity
import com.jae464.placememo.MainActivity
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentMyPageBinding
import com.jae464.placememo.presentation.base.BaseFragment

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        val user = FirebaseAuth.getInstance().currentUser
        // 로그인 상태가 아닐 때 로그인 화면으로 이동
        if (user == null) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        binding.userEmailTextView.text = user?.email
    }

    private fun initListener() {
        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(context, "로그아웃 완료", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }
}