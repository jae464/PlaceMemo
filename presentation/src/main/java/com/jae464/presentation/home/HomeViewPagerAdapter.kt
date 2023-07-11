package com.jae464.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jae464.presentation.databinding.ItemMemoPreviewImageBinding
import com.jae464.presentation.extension.setImage

class HomeViewPagerAdapter(private val listData: List<String>): RecyclerView.Adapter<HomeViewPagerAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(
        private val binding: ItemMemoPreviewImageBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(filePath: String) {
            binding.homePreviewImageView.setImage(filePath)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemMemoPreviewImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}