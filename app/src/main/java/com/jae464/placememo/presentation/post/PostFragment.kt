package com.jae464.placememo.presentation.post

import android.Manifest
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jae464.placememo.R
import com.jae464.placememo.data.model.MemoEntity
import com.jae464.placememo.presentation.base.BaseFragment
import com.jae464.placememo.databinding.FragmentPostBinding
import com.jae464.placememo.domain.model.post.Memo
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(R.layout.fragment_post) {

    private val viewModel: PostViewModel by viewModels()
    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        isGranted ->
        if (isGranted) {
            loadImage()
        } else {

        }
    }
    private val getImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val imageList = it.data?.data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, imageList ?: return@registerForActivityResult))
            viewModel.setImageList(bitmap)
            binding.sampleImageView.setImageBitmap(bitmap)
        }
        else {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageList ?: return@registerForActivityResult)
            viewModel.setImageList(bitmap)
            binding.sampleImageView.setImageBitmap(bitmap)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        latitude = arguments?.getDouble("latitude")!!
        longitude = arguments?.getDouble("longitude")!!
        println("$latitude, $longitude")
        initAppBar()
        initListener()
    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.postToolBar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.postToolBar.title = "게시글 업로드"
        binding.postToolBar.inflateMenu(R.menu.post_toolbar_menu)
        binding.postToolBar.setOnMenuItemClickListener {
            // todo 게시글 저장
            when(it.itemId) {
                R.id.save -> {
                    Toast.makeText(requireContext(),"저장버튼클릭", Toast.LENGTH_SHORT).show()
                    val title = binding.titleEditText.text.toString()
                    val content = binding.contentEditText.text.toString()
                    viewModel.saveMemo(0,title,content,latitude,longitude)
                    // 업로드 후 메인페이지로 이동
                    findNavController().popBackStack()
                }
            }
            true
        }

    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    private fun initListener() {
        binding.addImageButton.setOnClickListener {
            requestPermission()
        }
    }

    private fun loadImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        getImageLauncher.launch(intent)
    }

}