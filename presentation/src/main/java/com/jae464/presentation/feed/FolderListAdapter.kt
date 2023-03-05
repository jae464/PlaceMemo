package com.jae464.presentation.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jae464.domain.model.feed.Folder
import com.jae464.presentation.databinding.ItemFolderBinding

class FolderListAdapter: ListAdapter<Folder, FolderListAdapter.FolderViewHolder>(diff) {

    inner class FolderViewHolder(
        private val binding: ItemFolderBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(folder: Folder) {
            binding.folder = folder
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