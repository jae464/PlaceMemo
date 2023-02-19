package com.jae464.presentation.post

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jae464.presentation.databinding.ItemPostImageBinding

class ImageListAdapter: ListAdapter<Bitmap, ImageListAdapter.ItemViewHolder>(diff) {
    inner class ItemViewHolder(
        private val binding: ItemPostImageBinding,
        ): RecyclerView.ViewHolder(binding.root) {
            fun bind(bitmap: Bitmap) {
                binding.memoImageView.setImageBitmap(bitmap)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemPostImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val diff = object: DiffUtil.ItemCallback<Bitmap>() {
            override fun areItemsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean {
                return false
            }
        }
    }
}