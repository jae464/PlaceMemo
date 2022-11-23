package com.jae464.placememo.presentation.mypage
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.jae464.placememo.presentation.login.LoginActivity
import com.jae464.placememo.MainActivity
import com.jae464.placememo.R
import com.jae464.placememo.databinding.FragmentMyPageBinding
import com.jae464.placememo.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val viewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initListener()
        initView()
        initObserver()
        initAppBar()
    }

    private fun initView() {
        Log.d("MyPageFragment", firebaseUser?.email.toString())
        if (firebaseUser == null) {
            binding.loginViewGroup.visibility = View.GONE
            binding.logoutViewGroup.visibility = View.VISIBLE
        }
        else {
            binding.logoutViewGroup.visibility = View.GONE
            binding.loginViewGroup.visibility = View.VISIBLE
            binding.userProfileImageView.clipToOutline = true
            viewModel.getUser(firebaseUser.uid)
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

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.myPageToolBar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.myPageToolBar.setOnMenuItemClickListener {
            Log.d("MyPageFragment", it.toString())
            when (it.itemId) {
                R.id.setting -> {
                    findNavController().navigate(R.id.settings)
                }
            }
            true
        }

    }

    private fun initObserver() {

    }
}