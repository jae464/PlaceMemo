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
import com.jae464.presentation.databinding.FragmentFeedCategoryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedCategoryFragment :
    BaseFragment<FragmentFeedCategoryBinding>(R.layout.fragment_feed_category) {

    private val TAG: String = "FeedCategoryFragment"
    private var listAdapter: FeedListAdapter? = null
    private val viewModel: FeedCategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = FeedListAdapter(requireContext(), this::goToDetailPage, LIST_VIEW_TYPE)
        binding.feedRecyclerView.adapter = listAdapter
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        initView()
        initObserver()
        initListener()
        // Firebase 메모 불러오기 테스트
        // viewModel.getAllMemoByUser(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    private fun initView() {
        val sortItems = resources.getStringArray(R.array.sort_array)
        val sortSpinnerAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, sortItems)
        binding.spinnerSort.adapter = sortSpinnerAdapter
        binding.spinnerSort.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d(TAG, p0?.getItemAtPosition(p2).toString())

                when(p2) {
                    0 -> viewModel.setSortBy(SortBy.DESC)
                    1 -> viewModel.setSortBy(SortBy.ASC)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }

        }

        val viewModeItems = resources.getStringArray(R.array.view_mode_array)
        val viewModeSpinnerAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, viewModeItems)
        binding.spinnerViewMode.adapter = viewModeSpinnerAdapter
        binding.spinnerViewMode.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                listAdapter = FeedListAdapter(requireContext(), this@FeedCategoryFragment::goToDetailPage, p2)
                binding.feedRecyclerView.adapter = listAdapter
                binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                viewLifecycleOwner.lifecycleScope.launch {
                    listAdapter?.submitData(viewModel.memos.value)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }
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

    }

    private fun goToDetailPage(memoId: Int) {
        val action = FeedFragmentDirections.actionFeedToDetailMemo(memoId)
        findNavController().navigate(
            action
        )
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
        const val CARD_VIEW_TYPE = 0
        const val LIST_VIEW_TYPE = 1
    }
}