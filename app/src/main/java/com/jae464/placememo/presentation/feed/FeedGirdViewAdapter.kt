package com.jae464.placememo.presentation.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jae464.placememo.data.manager.ImageManager
import com.jae464.placememo.databinding.ItemMemoPreviewBinding
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.presentation.home.HomeViewPagerAdapter
import com.jae464.placememo.presentation.regionToString

class FeedGirdViewAdapter(private val onClick: (Long) -> (Unit)): ListAdapter<Memo, FeedGirdViewAdapter.ItemViewHolder>(diff) {

    inner class ItemViewHolder(
        private val binding: ItemMemoPreviewBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(memo: Memo) {
            println("FeedListAdapter")
            binding.memo = memo
            binding.locationTextView.text = regionToString(memo.area1, memo.area2, memo.area3)
            binding.memoCardView.setOnClickListener {
                onClick(memo.id)
            }
            val imageList = ImageManager.loadMemoImage(memo.id)
            if (imageList != null) {
                val viewPagerAdapter = HomeViewPagerAdapter(imageList)
                binding.thumbnailViewPager.adapter = viewPagerAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemMemoPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val diff = object: DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}