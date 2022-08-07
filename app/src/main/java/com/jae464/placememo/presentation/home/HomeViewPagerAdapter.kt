package com.jae464.placememo.presentation.home

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jae464.placememo.databinding.ItemMemoPreviewImageBinding

class HomeViewPagerAdapter(private val listData: List<Bitmap>): RecyclerView.Adapter<HomeViewPagerAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(
        private val binding: ItemMemoPreviewImageBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(bitmap: Bitmap) {
            binding.homePreviewImageView.setImageBitmap(bitmap)
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