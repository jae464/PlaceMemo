package com.jae464.presentation.feed

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.database.collection.LLRBNode
import com.jae464.domain.model.SortBy
import com.jae464.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import com.jae464.presentation.R
import com.jae464.presentation.common.FeedSettingDialog
import com.jae464.presentation.common.FeedSettingDialogListener
import com.jae464.presentation.databinding.FragmentFeedCategoryBinding
import com.jae464.presentation.model.ViewType
import com.jae464.presentation.post.PostFragmentArgs
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedCategoryFragment :
    BaseFragment<FragmentFeedCategoryBinding>(R.layout.fragment_feed_category) {

    private var listAdapter: FeedListAdapter? = null
    private val viewModel: FeedCategoryViewModel by viewModels()
    private val folderId by lazy {
        arguments?.getLong(FOLDER_ID_KEY, -1L)
    }
    private var feedSettingDialog: FeedSettingDialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = FeedListAdapter(requireContext(), this::goToDetailPage, LIST_VIEW_TYPE)
        binding.feedRecyclerView.adapter = listAdapter
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        initAppbar()
        initDialog()
        initObserver()
        initListener()
        // Firebase 메모 불러오기 테스트
        // viewModel.getAllMemoByUser(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    private fun initDialog() {
        feedSettingDialog = FeedSettingDialog().apply {
            setFeedSettingDialogListener(object: FeedSettingDialogListener {
                override fun onSortChanged(sortBy: SortBy) {
                    Log.d(TAG, sortBy.toString())
                    viewModel.setSortBy(sortBy)
                }

                override fun onViewChanged(viewType: ViewType) {
                    Log.d(TAG, viewType.toString())
                    listAdapter = FeedListAdapter(
                        requireContext(),
                        this@FeedCategoryFragment::goToDetailPage,
                        viewType.ordinal
                    )
                    binding.feedRecyclerView.adapter = listAdapter
                    binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    viewLifecycleOwner.lifecycleScope.launch {
                        listAdapter?.submitData(viewModel.memos.value)
                    }
                }

            })
        }
    }

    private fun initAppbar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbarFeedCategory.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbarFeedCategory.title = arguments?.getString(FOLDER_NAME_KEY, "")
        binding.appbarFeedCategory.visibility = View.VISIBLE
        if (folderId == null) {
            binding.toolbarFeedCategory.visibility = View.GONE
        }
    }

    private fun initObserver() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collectLatest { categories ->
                    Log.d(TAG, categories.toString())
                    binding.chipCategory.removeAllViews()
                    addDefaultChip()
                    categories.forEach { category ->
                        binding.chipCategory.addView(
                            Chip(
                                requireContext(),
                                null,
                                com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
                            ).apply {
                                text = category.name
                                isCheckable = true
                                checkedIcon = null
                                chipStrokeColor = ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.blue_200
                                    )
                                )
                                chipStrokeWidth = 4f
                                setChipBackgroundColorResource(R.color.bg_chip)
                                setOnClickListener {
                                    viewModel.setCategoryFilter(category)
                                }
                            }
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.memos.collectLatest {
                    listAdapter?.submitData(it)
                }
            }
        }
    }

    private fun initListener() {
        binding.ivSettingFeed.setOnClickListener {
            feedSettingDialog?.show(requireActivity().supportFragmentManager,"Feed-Setting-Dialog")
        }
    }

    private fun goToDetailPage(memoId: Int) {
        if (folderId == null) {
            val action = FeedFragmentDirections.actionFeedToDetailMemo(memoId)
            findNavController().navigate(
                action
            )
        }
        else {
            val action = FeedCategoryFragmentDirections.actionFeedCategoryFragmentToDetailMemo(memoId)
            findNavController().navigate(
                action
            )
        }
    }

    private fun addDefaultChip() {
        binding.chipCategory.addView(
            Chip(
                requireContext(),
                null,
                com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
            ).apply {
                text = "전체"
                isCheckable = true
                checkedIcon = null
                chipStrokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.blue_200
                    )
                )
                chipStrokeWidth = 4f
                setChipBackgroundColorResource(R.color.bg_chip)
                setOnClickListener {
                    viewModel.setCategoryFilter(null)
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    companion object {
        private const val TAG = "FeedCategoryFragment"
        private const val CARD_VIEW_TYPE = 0
        private const val LIST_VIEW_TYPE = 1

        const val FOLDER_ID_KEY = "folderId"
        const val FOLDER_NAME_KEY = "folderName"
        const val MEMO_COUNT_KEY = "memoCount"
    }
}