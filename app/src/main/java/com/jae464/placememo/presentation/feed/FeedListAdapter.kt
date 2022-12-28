package com.jae464.placememo.presentation.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jae464.placememo.data.manager.ImageManager
import com.jae464.placememo.databinding.ItemMemoListViewBinding
import com.jae464.placememo.databinding.ItemMemoPreviewBinding
import com.jae464.placememo.domain.model.post.Memo
import com.jae464.placememo.presentation.home.HomeViewPagerAdapter
import com.jae464.placememo.presentation.regionToString

class FeedListAdapter(private val onClick: (Long) -> (Unit), private val viewType: Int = 0)
    : ListAdapter<Memo, FeedListAdapter.FeedViewHolder>(diff) {

    /**
    * ViewType 을 여러개 적용하기 위해
     * sealed class 인 FeedViewHolder를 만들고
     * 각각 FeedGridViewHolder와 FeedListViewHolder 는 FeedViewHolder 의
     * bind 함수를 override 하여 작성
    * */
    fun clearData() {
        notifyDataSetChanged()
    }

    sealed class FeedViewHolder(binding: ViewDataBinding):
        RecyclerView.ViewHolder(binding.root) {
            abstract fun bind(memo: Memo)
        }


    inner class FeedGridViewHolder(
        private val binding: ItemMemoPreviewBinding
    ): FeedViewHolder(binding) {

        override fun bind(memo: Memo) {
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
                binding.dotIndicator.attachTo(binding.thumbnailViewPager)
            }
        }
    }

    inner class FeedListViewHolder(
        private val binding: ItemMemoListViewBinding
    ): FeedViewHolder(binding) {

        override fun bind(memo: Memo) {
            Log.d("FeedListAdapter", "bind")
            binding.memo = memo
            binding.memoLocation.text = regionToString(memo.area1, memo.area2, memo.area3)
            binding.memoListView.setOnClickListener {
                onClick(memo.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        when (this.viewType) {
            0 -> {
                val binding = ItemMemoPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return FeedGridViewHolder(binding)
            }
            else -> {
                val binding = ItemMemoListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return FeedListViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
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