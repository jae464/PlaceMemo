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
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jae464.domain.model.post.Category
import com.jae464.presentation.base.BaseFragment
import com.jae464.presentation.databinding.FragmentPostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates
import com.jae464.presentation.R
import com.jae464.presentation.common.AddDialog
import com.jae464.presentation.common.AddDialogListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(R.layout.fragment_post) {

    private val TAG = "PostFragment"
    private val args: PostFragmentArgs by navArgs()
    private val viewModel: PostViewModel by viewModels()
    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()
    private val imageAdapter = ImageListAdapter()
    private lateinit var category: Category
    private var folderId = 0L

    private var imagePathList = mutableListOf<String>()
    private var imageViewList = mutableListOf<Bitmap?>()

    private var addCategoryDialog: AddDialog? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadImage()
        } else {
            // TODO requestPermission 예외처리
            Log.e(TAG, "이미지를 불러오는데 실패했습니다.")
        }
    }
    private val getImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val image = it.data?.data ?: return@registerForActivityResult
        imagePathList.add(image.toString())
        setImageView(image)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        binding.imageRecyclerView.adapter = imageAdapter
        binding.postViewModel = viewModel

        initAppBar()
        initData()
        initDialog()
        initListener()
        initObserver()
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

                    if (args.memoId == -1) {
                        viewModel.saveMemo(
                            id = 0,
                            title = title,
                            content = content,
                            latitude = latitude,
                            longitude = longitude,
                            category = category,
                            folderId = folderId,
                            imageUriList = imagePathList
                        )
                    } else {
                        viewModel.updateMemo(
                            title = title,
                            content = content,
                            category = category,
                            folderId = folderId,
                            imageUriList = imagePathList
                        )
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
            -1 -> {
                if (arguments?.getDouble("latitude") == null
                    || arguments?.getDouble("longitude") == null
                ) {
                    findNavController().popBackStack()
                }

                latitude = arguments?.getDouble("latitude")!!
                longitude = arguments?.getDouble("longitude")!!
                viewModel.getAddress(latitude, longitude)
            }
        }
    }

    private fun initDialog() {
        addCategoryDialog = AddDialog().apply {
            setTitle("새로운 카테고리를 입력하세요.")
            addDialogListener(object : AddDialogListener {
                override fun onConfirmClick(name: String) {
                    viewModel.addCategory(name)
                }

                override fun onCancelClick() {
                    binding.categorySpinner.setSelection(0)
                    dismiss()
                }

            })
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.memo.collectLatest { memo ->
                    Log.d(TAG, "memo collect")
                    memo ?: return@collectLatest
                    binding.titleEditText.setText(memo.title)
                    binding.contentEditText.setText(memo.content)
                    binding.locationTextView.text = "${memo.area1} ${memo.area2} ${memo.area3}"
                    memo.imageUriList?.forEach {imageUri ->
                        val dirPath = File(requireContext().filesDir, "images")
                        val filePath = File("${dirPath}/${imageUri}.jpg")
                        setImageView(filePath.toUri())
                        imagePathList.add(imageUri)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collectLatest { categories ->
                    initSpinner(categories)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.folders.collectLatest { folders ->
                    Log.d(TAG, folders.toString())
                    val folderNames = folders.map { folder -> folder.name }.toMutableList()
                    val folderAdapter =
                        ArrayAdapter(requireContext(), R.layout.item_spinner, folderNames)
                    binding.spinnerFolder.adapter = folderAdapter
                    binding.spinnerFolder.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                folderId = folders[p2].id
                                Log.d(TAG, "현재 선택된 폴더 ID : $folderId")
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                        }

                }
            }
        }

        viewModel.isDone.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().popBackStack()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect {addCategoryEvent ->
                    when(addCategoryEvent) {
                        is AddCategoryEvent.ExistCategoryName -> {
                            addCategoryDialog?.setErrorMessage("이미 존재하는 카테고리입니다.")
                        }
                        is AddCategoryEvent.AddCategoryCompleted -> {
                            addCategoryDialog?.dismiss()
                        }
                        is AddCategoryEvent.EmptyCategoryName -> {
                            addCategoryDialog?.setErrorMessage("카테고리 명을 입력해주세요.")
                        }
                    }
                }
            }
        }
    }

    private fun initSpinner(categories: List<Category>) {

        val categoryNames = categories.map { category -> category.name }.toMutableList().apply {
            add("새로운 카테고리")
        }

        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, categoryNames)
        binding.categorySpinner.adapter = categoryAdapter
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p0 != null) {
                        if (p2 < categories.size) {
                            category = categories[p2]
                        } else {
                            addCategoryDialog?.show(
                                requireActivity().supportFragmentManager, "add-category"
                            )
                        }
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
        getImageLauncher.launch(intent)
    }

    private fun setImageView(imageUri: Uri) {
        Glide.with(requireContext())
            .asBitmap()
            .load(imageUri)
            .listener(object : RequestListener<Bitmap> {
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
            .override(512, 512)
            .submit()
    }
}