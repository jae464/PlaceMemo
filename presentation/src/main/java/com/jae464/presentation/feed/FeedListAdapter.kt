package com.jae464.presentation.feed

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jae464.presentation.databinding.ItemMemoListViewBinding
import com.jae464.presentation.databinding.ItemMemoPreviewBinding
import com.jae464.presentation.home.HomeViewPagerAdapter
import com.jae464.presentation.regionToString
import java.io.File

class FeedListAdapter(private val context: Context, private val onClick: (Long) -> (Unit), private val viewType: Int = 0)
    : ListAdapter<com.jae464.domain.model.post.Memo, FeedListAdapter.FeedViewHolder>(diff) {

    private val TAG = "FeedListAdapter"
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
            abstract fun bind(memo: com.jae464.domain.model.post.Memo)
        }


    inner class FeedGridViewHolder(
        private val binding: ItemMemoPreviewBinding
    ): FeedViewHolder(binding) {

        override fun bind(memo: com.jae464.domain.model.post.Memo) {
            println("FeedListAdapter")
            binding.memo = memo
            binding.locationTextView.text = regionToString(memo.area1, memo.area2, memo.area3)
            binding.memoCardView.setOnClickListener {
                onClick(memo.id)
            }

            val imageUriList = memo.imageUriList ?: emptyList()
            val imagePathList = imageUriList.map {uri ->
                val dirPath = "${context.filesDir}/images"
                val filePath = "$dirPath/${memo.id}/${uri.substringAfterLast("/")}"
                filePath
            }

            Log.d(TAG, imagePathList.toString())

            if (imagePathList.isNotEmpty()) {
                val viewPagerAdapter = HomeViewPagerAdapter(imagePathList)
                binding.thumbnailViewPager.adapter = viewPagerAdapter
                binding.dotIndicator.attachTo(binding.thumbnailViewPager)
            }
        }
    }

    inner class FeedListViewHolder(
        private val binding: ItemMemoListViewBinding,
    ): FeedViewHolder(binding) {

        override fun bind(memo: com.jae464.domain.model.post.Memo) {
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

        private val diff = object: DiffUtil.ItemCallback<com.jae464.domain.model.post.Memo>() {
            override fun areItemsTheSame(oldItem: com.jae464.domain.model.post.Memo, newItem: com.jae464.domain.model.post.Memo): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: com.jae464.domain.model.post.Memo, newItem: com.jae464.domain.model.post.Memo): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}