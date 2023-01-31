package com.jae464.placememo.presentation.mypage
import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jae464.placememo.presentation.login.LoginActivity
import com.jae464.placememo.MainActivity
import com.jae464.placememo.R
import com.jae464.placememo.data.manager.ImageManager
import com.jae464.placememo.databinding.FragmentMyPageBinding
import com.jae464.placememo.domain.model.login.User
import com.jae464.placememo.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val viewModel: MyPageViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->

        if (isGranted) {
            loadImage()
        }
        else {
            // TODO requestPermission 예외처리
        }
    }

    private val getImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (firebaseUser == null) return@registerForActivityResult
        val image = it.data?.data ?: return@registerForActivityResult

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            val bitmap =
                ImageManager.resizeBitmapFromUri(image, requireContext())
                    ?: return@registerForActivityResult

            println("변형된 bitmap 사이즈 : ${bitmap.density}")
            viewModel.updateUserProfileImage(bitmap)
            binding.userProfileImageView.setImageBitmap(bitmap)

        } else {
            val bitmap = MediaStore.Images.Media.getBitmap(
                requireContext().contentResolver,
                image ?: return@registerForActivityResult
            )

        }
    }

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

        binding.friendLinearLayout?.setOnClickListener {
            findNavController().navigate(R.id.friend_list)
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

        binding.editProfileButton?.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    private fun initObserver() {
        viewModel.user.observe(viewLifecycleOwner) {
            it ?: return@observe
            val imageUrl = it.imageUrl ?: return@observe

            Glide.with(requireContext() /* context */)
                .load(imageUrl)
                .into(binding.userProfileImageView)

        }
    }

    private fun loadImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        getImageLauncher.launch(intent)
    }

}