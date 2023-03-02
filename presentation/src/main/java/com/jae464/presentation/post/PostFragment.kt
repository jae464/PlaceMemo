package com.jae464.presentation.post

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jae464.presentation.base.BaseFragment
import com.jae464.presentation.databinding.FragmentPostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates
import com.jae464.presentation.R
import com.jae464.presentation.indexToCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(R.layout.fragment_post) {

    private val TAG = "PostFragment"
    private val args: PostFragmentArgs by navArgs()
    private val viewModel: PostViewModel by viewModels()
    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()
    private val imageAdapter = ImageListAdapter()
    private var category = com.jae464.domain.model.post.Category.OTHER

    private var imagePathList = mutableListOf<String>()
    private var imageViewList = mutableListOf<Bitmap?>()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadImage()
        } else {
            // TODO requestPermission 예외처리
        }
    }
    private val getImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val image = it.data?.data ?: return@registerForActivityResult
        imagePathList.add(image.toString())
        setImageView(image)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(tag, arguments.toString())
        if (arguments?.getDouble("latitude", 0.0) == 0.0) {
            println("latitude 정보가 없습니다.")
        }
        binding.imageRecyclerView.adapter = imageAdapter
        binding.postViewModel = viewModel

        initAppBar()
        initData()
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

                    val title = binding.titleEditText.text.toString()
                    val content = binding.contentEditText.text.toString()

                    if (args.memoId == -1L) {
                        viewModel.saveMemo(0, title, content, latitude, longitude, category, imagePathList)
                    }
                    else {
                        viewModel.updateMemo(title, content, category, imagePathList)
                    }
                }
            }
            true
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun initData() {
        println("argument memoId : ${args.memoId}")
        when (args.memoId) {
            // 새 메모 업로드
            -1L -> {
                latitude = arguments?.getDouble("latitude")!!
                longitude = arguments?.getDouble("longitude")!!
                viewModel.getAddress(latitude, longitude)
            }
            // 기존 메모 수정
            else -> {
                viewModel.getMemo(args.memoId)
            }
        }

    }

    private fun initListener() {
        binding.addImageButton.setOnClickListener {
            requestPermission()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initObserver() {

        viewModel.address.observe(viewLifecycleOwner) {
            binding.locationTextView.text = viewModel.getAddressName(it)
        }

        // 메모 수정인 경우 해당 memoId 의 메모를 가져와서 세팅해줌
        viewModel.memo.observe(viewLifecycleOwner) { it ->
            binding.titleEditText.setText(it.title)
            binding.contentEditText.setText(it.content)
            binding.locationTextView.text = "${it.area1} ${it.area2} ${it.area3}"
        }

        viewModel.isDone.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().popBackStack()
            }
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
                        category = indexToCategory(p2)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    return
                }
            }
    }

    private fun loadImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getImageLauncher.launch(intent)
    }

    private fun setImageView(imageUri: Uri) {
        Glide.with(requireContext())
            .asBitmap()
            .load(imageUri)
            .listener(object: RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d(TAG, "Image Load Error")
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    CoroutineScope(Dispatchers.Main).launch {
                        val newImageViewList = imageViewList + listOf(resource)
                        imageAdapter.submitList(newImageViewList)
                        imageViewList = newImageViewList.toMutableList()
                    }
                    return true
                }
            })
            .override(512,512)
            .submit()
    }
}