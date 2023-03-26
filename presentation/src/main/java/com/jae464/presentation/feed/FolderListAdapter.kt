package com.jae464.presentation.feed

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jae464.domain.model.feed.Folder
import com.jae464.presentation.R
import com.jae464.presentation.databinding.ItemFolderBinding

class FolderListAdapter(private val context: Context, private val supportFragmentManager: FragmentManager): ListAdapter<Folder, FolderListAdapter.FolderViewHolder>(diff) {

    private val TAG = "FolderListAdapter"

    inner class FolderViewHolder(
        private val binding: ItemFolderBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(folder: Folder) {
            binding.folder = folder
            binding.ivFolderEdit.setOnClickListener {
                val popupMenu = PopupMenu(context, binding.ivFolderEdit)
                popupMenu.menuInflater.inflate(R.menu.folder_menu, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener {menuItem ->
                    when (menuItem.itemId) {
                        R.id.edit -> {
                            Log.d(TAG, menuItem.itemId.toString())
                            // TODO 폴더 이름 수정 다이얼로그 띄우기
                            true
                        }
                        R.id.delete -> {
                            Log.d(TAG, menuItem.itemId.toString())
                            // TODO 폴더 삭제 다이얼로그 띄우기
                            DeleteFolderDialog(folder.id).show(supportFragmentManager, "delete_folder")

                            true
                        }
                        else -> false
                    }
                }

                popupMenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val diff = object: DiffUtil.ItemCallback<Folder>() {
            override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}