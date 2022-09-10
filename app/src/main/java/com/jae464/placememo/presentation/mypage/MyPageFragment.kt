package com.jae464.placememo.presentation.mypage
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.jae464.placememo.presentation.login.LoginActivity
import com.jae464.placememo.MainActivity
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentMyPageBinding
import com.jae464.placememo.presentation.base.BaseFragment

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val user = FirebaseAuth.getInstance().currentUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initView()

    }

    private fun initView() {
        Log.d("MyPageFragment", user?.email.toString())
        if (user == null) {
            binding.loginViewGroup.visibility = View.GONE
            binding.logoutViewGroup.visibility = View.VISIBLE
        }
        else {
            binding.logoutViewGroup.visibility = View.GONE
            binding.loginViewGroup.visibility = View.VISIBLE
            binding.nickNameTextView.text = user.email
        }
    }

    private fun initListener() {
        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(context, "로그아웃 완료", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}