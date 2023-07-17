package com.jae464.presentation.feed

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jae464.domain.model.post.Memo
import com.jae464.presentation.databinding.ItemMemoListViewBinding
import com.jae464.presentation.databinding.ItemMemoPreviewBinding
import com.jae464.presentation.home.HomeViewPagerAdapter
import com.jae464.presentation.regionToString
import java.io.File

class FeedListAdapter(
    private val context: Context,
    private val onClick: (Int) -> (Unit),
    private var viewType: Int = VIEW_TYPE_CARD
) : PagingDataAdapter<Memo, FeedListAdapter.FeedViewHolder>(diff) {

    private val TAG = "FeedListAdapter"

    sealed class FeedViewHolder(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(memo: Memo)
    }

    inner class FeedGridViewHolder(
        private val binding: ItemMemoPreviewBinding
    ) : FeedViewHolder(binding) {

        override fun bind(memo: Memo) {
            binding.memo = memo
            binding.locationTextView.text = regionToString(memo.area1, memo.area2, memo.area3)
            binding.memoCardView.setOnClickListener {
                onClick(memo.id)
            }

            val imageUriList = memo.imageUriList ?: emptyList()
            val imagePathList = imageUriList.map { uri ->
                val dirPath = "${context.filesDir}/images"
                val filePath = "$dirPath/${uri.substringAfterLast("/")}.jpg"
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
    ) : FeedViewHolder(binding) {

        override fun bind(memo: Memo) {
            binding.memo = memo
            binding.memoLocation.text = regionToString(memo.area1, memo.area2, memo.area3)
            binding.memoListView.setOnClickListener {
                onClick(memo.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return when (this.viewType) {
            VIEW_TYPE_CARD -> {
                val binding = ItemMemoPreviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FeedGridViewHolder(binding)
            }

            VIEW_TYPE_LIST -> {
                val binding = ItemMemoListViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FeedListViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(item)
        }
    }

    companion object {

        const val VIEW_TYPE_LIST = 0
        const val VIEW_TYPE_CARD = 1

        private val diff = object : DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}