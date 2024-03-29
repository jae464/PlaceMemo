package com.jae464.presentation.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.jae464.domain.model.feed.Folder
import com.jae464.presentation.base.BaseFragment
import com.jae464.presentation.R
import com.jae464.presentation.common.AddDialog
import com.jae464.presentation.common.AddDialogListener
import com.jae464.presentation.databinding.FragmentFeedFolderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFolderFragment :
    BaseFragment<FragmentFeedFolderBinding>(R.layout.fragment_feed_folder) {

    private lateinit var folderListAdapter: FolderListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper
    private val viewModel: FeedViewModel by viewModels()
    private var addDialog: AddDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        folderListAdapter = FolderListAdapter(
            requireContext(),
            parentFragmentManager,
            this::updateFolderOrder,
            this::deleteFolder,
            this::moveToFeed
        )
        val itemTouchHelperCallback = FolderItemTouchHelperCallback(folderListAdapter)
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        initDialog()
        initView()
        initListener()
        initObserver()
    }

    private fun initDialog() {
        addDialog = AddDialog().apply {
            setTitle("생성할 폴더 이름을 입력하세요.")
            addDialogListener(object : AddDialogListener {
                override fun onConfirmClick(name: String) {
                    viewModel.createFolder(Folder(name = name, memoCount = 0))
                }

                override fun onCancelClick() {
                    closeDialog()
                }

            })
        }
    }

    private fun initListener() {
        binding.fabAddFolder.setOnClickListener {
            addDialog?.show(requireActivity().supportFragmentManager, "add-dialog")
        }
    }

    private fun initView() {
        binding.rcvFolderList.adapter = folderListAdapter
        itemTouchHelper.attachToRecyclerView(binding.rcvFolderList)
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.folderList.collectLatest { folder ->
                    Log.d("FeedFolderFragment", folder.toString())
                    folderListAdapter.submitList(folder)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { addFolderEvent ->
                    when (addFolderEvent) {
                        is AddFolderEvent.ExistFolderName -> {
                            addDialog?.setErrorMessage("이미 존재하는 이름입니다.")
                        }

                        is AddFolderEvent.CreateFolderCompleted -> {
                            addDialog?.dismiss()
                        }

                        is AddFolderEvent.EmptyFolderName -> {
                            addDialog?.setErrorMessage("폴더명을 입력해주세요.")
                        }

                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun updateFolderOrder(folders: List<Folder>) {
        val updatedFolders = folders.mapIndexed { index, folder ->
            folder.copy(order = index)
        }
        viewModel.updateFolders(updatedFolders)
    }

    private fun deleteFolder(folder: Folder) {
        if (folder.isDefault) {
            Toast.makeText(requireContext(), "기본 폴더는 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.deleteFolder(folder.id)
    }

    private fun moveToFeed(folder: Folder) {
        val bundle = Bundle().apply {
            putLong(FeedCategoryFragment.FOLDER_ID_KEY, folder.id)
            putString(FeedCategoryFragment.FOLDER_NAME_KEY, folder.name)
            putInt(FeedCategoryFragment.MEMO_COUNT_KEY, folder.memoCount)
        }
        findNavController().navigate(R.id.action_feed_to_feedCategoryFragment, bundle)
    }

}