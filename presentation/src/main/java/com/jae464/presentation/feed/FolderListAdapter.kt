package com.jae464.presentation.feed

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jae464.domain.model.feed.Folder
import com.jae464.presentation.R
import com.jae464.presentation.common.ConfirmDialog
import com.jae464.presentation.common.ConfirmDialogListener
import com.jae464.presentation.databinding.ItemFolderBinding


class FolderListAdapter(
    private val context: Context,
    private val supportFragmentManager: FragmentManager,
    private val onFolderUpdate: (List<Folder>) -> Unit,
    private val onFolderDelete: (Folder) -> Unit,
) : ListAdapter<Folder, FolderListAdapter.FolderViewHolder>(diff),
    FolderItemTouchHelperCallback.OnFolderMoveListener {

    private val TAG = "FolderListAdapter"
    private var folderList = mutableListOf<Folder>() // Room 에 Folder 순서 업데이트를 위한 폴더 리스트
    private var deleteDialog: ConfirmDialog? = null

    inner class FolderViewHolder(
        private val binding: ItemFolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(folder: Folder) {
            binding.folder = folder
            binding.ivFolderEdit.setOnClickListener {
                val popupMenu = PopupMenu(context, binding.ivFolderEdit)

                popupMenu.menuInflater.inflate(R.menu.folder_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.edit -> {
                            Log.d(TAG, menuItem.itemId.toString())
                            // TODO 폴더 이름 수정 다이얼로그 띄우기
                            true
                        }

                        R.id.delete -> {
                            deleteDialog?.setConfirmDialogListener(object: ConfirmDialogListener {
                                override fun onConfirmClick() {
                                    onFolderDelete(folder)
                                    deleteDialog?.dismiss()
                                }

                                override fun onCancelClick() {
                                    deleteDialog?.dismiss()
                                }

                            })
                            deleteDialog?.show(supportFragmentManager, "delete-folder")


                            true
                        }

                        else -> false
                    }
                }
                if (!folder.isDefault) {
                    val setDefaultMenu = popupMenu.menu.add(Menu.NONE, Menu.NONE, 0, "기본 폴더로 지정")
                    setDefaultMenu.setOnMenuItemClickListener {
                        folderList.forEachIndexed { index, item->
                            if (item.isDefault) {
                                folderList[index] = item.copy(isDefault = false)
                            }
                            if (folder.id == item.id) {
                                folderList[index] = item.copy(isDefault = true)
                            }
                        }
                        onFolderUpdate(folderList)
                        true
                    }
                }

                popupMenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        folderList = currentList.toMutableList()
        initDialog()
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private fun initDialog() {
        deleteDialog = ConfirmDialog().apply {
            setTitle("정말 삭제하시겠습니까?")
            setConfirmText("삭제")
        }
    }

    companion object {

        private val diff = object : DiffUtil.ItemCallback<Folder>() {
            override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    override fun onFolderMove(fromPosition: Int, toPosition: Int) {
        val folder = folderList[fromPosition]
        folderList.removeAt(fromPosition)
        folderList.add(toPosition, folder)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onFolderMoveFinished() {
        onFolderUpdate(folderList)
    }


}