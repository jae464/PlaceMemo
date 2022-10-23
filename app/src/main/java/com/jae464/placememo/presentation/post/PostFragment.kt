package com.jae464.placememo.presentation.post

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jae464.placememo.R
import com.jae464.placememo.data.manager.ImageManager.resizeBitmapFromUri
import com.jae464.placememo.data.model.MemoEntity
import com.jae464.placememo.presentation.base.BaseFragment
import com.jae464.placememo.databinding.FragmentPostBinding
import com.jae464.placememo.domain.model.post.Memo
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(R.layout.fragment_post) {

    private val TAG = "PostFragment"
    private val viewModel: PostViewModel by viewModels()
    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()
    private val imageAdapter = ImageListAdapter()
    private var category = 0
    private var imageUrlList = mutableListOf<Uri>()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadImage()
        } else {

        }
    }
    private val getImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val image = it.data?.data
        imageUrlList.add(image!!)
        Log.d(TAG, image.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, image ?: return@registerForActivityResult))
            val bitmap =
                resizeBitmapFromUri(image ?: return@registerForActivityResult, requireContext())
                    ?: return@registerForActivityResult
            println("변형된 bitmap 사이즈 : ${bitmap.density}")
            viewModel.setImageList(bitmap)
        } else {
            val bitmap = MediaStore.Images.Media.getBitmap(
                requireContext().contentResolver,
                image ?: return@registerForActivityResult
            )
            viewModel.setImageList(bitmap)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        latitude = arguments?.getDouble("latitude")!!
        longitude = arguments?.getDouble("longitude")!!
        binding.imageRecyclerView.adapter = imageAdapter
        binding.postViewModel = viewModel
        viewModel.getAddress(latitude, longitude)
        println("$latitude, $longitude")
        initAppBar()
        initListener()
        initObserver()
        initSpinner()
    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.postToolBar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.postToolBar.title = "게시글 업로드"
        binding.postToolBar.inflateMenu(R.menu.post_toolbar_menu)
        binding.postToolBar.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.save -> {
                    Toast.makeText(requireContext(), "저장버튼클릭", Toast.LENGTH_SHORT).show()
                    val title = binding.titleEditText.text.toString()
                    val content = binding.contentEditText.text.toString()
                    viewModel.saveMemo(0, title, content, latitude, longitude, category, imageUrlList)
                    viewModel.saveImage(0)
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

    private fun initObserver() {
        viewModel.imageList.observe(viewLifecycleOwner) {
            imageAdapter.submitList(it.toMutableList())
        }

        viewModel.address.observe(viewLifecycleOwner) {
            binding.locationTextView.text = viewModel.getAddressName(it)
        }
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
        }
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p0 != null) {
                        Toast.makeText(
                            requireContext(),
                            p0.getItemAtPosition(p2).toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        category = p2
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    return
                }
            }
    }

    private fun loadImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        getImageLauncher.launch(intent)
    }

}